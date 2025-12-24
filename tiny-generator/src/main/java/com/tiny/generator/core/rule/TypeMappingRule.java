package com.tiny.generator.core.rule;

import com.tiny.generator.core.GenConstants;
import com.tiny.generator.core.context.GenContext;
import com.tiny.generator.entity.GenTable;
import com.tiny.generator.entity.GenTableColumn;
import com.tiny.generator.entity.GenTypeMapping;
import com.tiny.generator.mapper.GenTypeMappingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 类型映射规则
 */
@Component
@RequiredArgsConstructor
public class TypeMappingRule implements GenRule {

    private final GenTypeMappingMapper typeMappingMapper;

    /**
     * 默认类型映射
     */
    private static final List<TypeMappingItem> DEFAULT_MAPPINGS = List.of(
            new TypeMappingItem("bigint", "Long", null, GenConstants.HTML_NUMBER),
            new TypeMappingItem("int|integer", "Integer", null, GenConstants.HTML_NUMBER),
            new TypeMappingItem("tinyint\\(1\\)", "Boolean", null, GenConstants.HTML_RADIO),
            new TypeMappingItem("tinyint|smallint", "Integer", null, GenConstants.HTML_SELECT),
            new TypeMappingItem("decimal|numeric|double|float", "BigDecimal", "java.math.BigDecimal", GenConstants.HTML_NUMBER),
            new TypeMappingItem("varchar|char|text|longtext|mediumtext", "String", null, GenConstants.HTML_INPUT),
            new TypeMappingItem("datetime|timestamp", "LocalDateTime", "java.time.LocalDateTime", GenConstants.HTML_DATETIME),
            new TypeMappingItem("date", "LocalDate", "java.time.LocalDate", GenConstants.HTML_DATE),
            new TypeMappingItem("time", "LocalTime", "java.time.LocalTime", GenConstants.HTML_INPUT),
            new TypeMappingItem("blob|binary|varbinary", "byte[]", null, GenConstants.HTML_UPLOAD),
            new TypeMappingItem("json", "String", null, GenConstants.HTML_TEXTAREA)
    );

    @Override
    public String name() {
        return "TypeMapping";
    }

    @Override
    public int order() {
        return 20;
    }

    @Override
    public void apply(GenContext context) {
        GenTable table = context.getTable();
        if (table.getColumns() == null) {
            return;
        }

        // 从数据库加载自定义映射
        List<GenTypeMapping> customMappings = typeMappingMapper.selectList(null);

        for (GenTableColumn column : table.getColumns()) {
            // 如果已经设置了Java类型，跳过
            if (column.getJavaType() != null && !column.getJavaType().isEmpty()) {
                continue;
            }

            String dbType = column.getColumnType().toLowerCase();

            // 先尝试自定义映射
            boolean matched = false;
            for (GenTypeMapping mapping : customMappings) {
                if (Pattern.matches(mapping.getDbType().toLowerCase(), dbType)) {
                    column.setJavaType(mapping.getJavaType());
                    if (mapping.getJavaImport() != null && !mapping.getJavaImport().isEmpty()) {
                        context.addImport(mapping.getJavaImport());
                    }
                    if (column.getHtmlType() == null || column.getHtmlType().isEmpty()) {
                        column.setHtmlType(mapping.getDefaultHtmlType());
                    }
                    matched = true;
                    break;
                }
            }

            // 使用默认映射
            if (!matched) {
                for (TypeMappingItem item : DEFAULT_MAPPINGS) {
                    if (Pattern.matches(item.dbTypePattern, dbType)) {
                        column.setJavaType(item.javaType);
                        if (item.javaImport != null) {
                            context.addImport(item.javaImport);
                        }
                        if (column.getHtmlType() == null || column.getHtmlType().isEmpty()) {
                            column.setHtmlType(item.defaultHtmlType);
                        }
                        matched = true;
                        break;
                    }
                }
            }

            // 默认使用String
            if (!matched) {
                column.setJavaType("String");
                if (column.getHtmlType() == null || column.getHtmlType().isEmpty()) {
                    column.setHtmlType(GenConstants.HTML_INPUT);
                }
            }

            // 特殊处理：text类型使用textarea
            if (dbType.contains("text")) {
                column.setHtmlType(GenConstants.HTML_TEXTAREA);
            }
        }
    }

    /**
     * 类型映射项
     */
    private record TypeMappingItem(String dbTypePattern, String javaType, String javaImport, String defaultHtmlType) {
    }
}
