package com.tiny.generator.core.rule;

import com.tiny.generator.core.context.GenContext;
import com.tiny.generator.entity.GenTable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 变量构建规则
 */
@Component
public class VariableBuildRule implements GenRule {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String name() {
        return "VariableBuild";
    }

    @Override
    public int order() {
        return 50;
    }

    @Override
    public void apply(GenContext context) {
        GenTable table = context.getTable();

        // 添加日期时间变量
        context.setVariable("datetime", LocalDateTime.now().format(DATE_FORMATTER));

        // 前端路径
        String feModulePath = table.getFeModulePath();
        if (feModulePath == null || feModulePath.isEmpty()) {
            feModulePath = "/" + table.getModuleName() + "/" + table.getBusinessName();
        }
        context.setVariable("feModulePath", feModulePath);

        // 权限前缀
        String permissionPrefix = table.getModuleName() + ":" + table.getBusinessName();
        context.setVariable("permissionPrefix", permissionPrefix);

        // API路径
        String apiPath = "/" + table.getBusinessName();
        context.setVariable("apiPath", apiPath);

        // 是否有必填字段
        boolean hasRequired = table.getColumns() != null && table.getColumns().stream()
                .anyMatch(col -> col.isRequired() && !col.isPk());
        context.setVariable("hasRequired", hasRequired);

        // 是否有字典字段
        boolean hasDict = table.getColumns() != null && table.getColumns().stream()
                .anyMatch(col -> col.getDictType() != null && !col.getDictType().isEmpty());
        context.setVariable("hasDict", hasDict);

        // 是否有日期字段
        boolean hasDate = table.getColumns() != null && table.getColumns().stream()
                .anyMatch(col -> "LocalDate".equals(col.getJavaType())
                        || "LocalDateTime".equals(col.getJavaType()));
        context.setVariable("hasDate", hasDate);

        // 是否有BigDecimal字段
        boolean hasBigDecimal = table.getColumns() != null && table.getColumns().stream()
                .anyMatch(col -> "BigDecimal".equals(col.getJavaType()));
        context.setVariable("hasBigDecimal", hasBigDecimal);
    }
}
