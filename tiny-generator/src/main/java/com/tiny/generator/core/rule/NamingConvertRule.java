package com.tiny.generator.core.rule;

import cn.hutool.core.util.StrUtil;
import com.tiny.generator.config.GenConfigProvider;
import com.tiny.generator.core.context.GenContext;
import com.tiny.generator.entity.GenTable;
import com.tiny.generator.entity.GenTableColumn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 命名转换规则
 */
@Component
@RequiredArgsConstructor
public class NamingConvertRule implements GenRule {

    private final GenConfigProvider configProvider;

    @Override
    public String name() {
        return "NamingConvert";
    }

    @Override
    public int order() {
        return 10;
    }

    @Override
    public void apply(GenContext context) {
        GenTable table = context.getTable();

        // 如果没有设置类名，则根据表名生成
        if (StrUtil.isBlank(table.getClassName())) {
            String tableName = table.getTableName();
            // 去除前缀
            if (configProvider.isRemovePrefix()) {
                tableName = removePrefix(tableName);
            }
            // 转换为PascalCase
            String className = toPascalCase(tableName);
            table.setClassName(className);
        }

        // 如果没有设置业务名，则根据表名生成
        if (StrUtil.isBlank(table.getBusinessName())) {
            String tableName = table.getTableName();
            if (configProvider.isRemovePrefix()) {
                tableName = removePrefix(tableName);
            }
            // 业务名使用小写下划线转驼峰
            table.setBusinessName(toCamelCase(tableName));
        }

        // 如果没有设置包名，使用默认包名
        if (StrUtil.isBlank(table.getPackageName())) {
            table.setPackageName(configProvider.getPackageName());
        }

        // 如果没有设置作者，使用默认作者
        if (StrUtil.isBlank(table.getAuthor())) {
            table.setAuthor(configProvider.getAuthor());
        }

        // 如果没有设置模块名，从包名推断
        if (StrUtil.isBlank(table.getModuleName())) {
            String packageName = table.getPackageName();
            int lastDot = packageName.lastIndexOf('.');
            if (lastDot > 0) {
                table.setModuleName(packageName.substring(lastDot + 1));
            } else {
                table.setModuleName("system");
            }
        }

        // 转换列名为Java字段名
        if (table.getColumns() != null) {
            for (GenTableColumn column : table.getColumns()) {
                if (StrUtil.isBlank(column.getJavaField())) {
                    column.setJavaField(toCamelCase(column.getColumnName()));
                }
            }
        }

        // 设置模板变量
        context.setVariable("tableName", table.getTableName());
        context.setVariable("tableComment", table.getTableComment());
        context.setVariable("className", table.getClassName());
        context.setVariable("classname", StrUtil.lowerFirst(table.getClassName()));
        context.setVariable("moduleName", table.getModuleName());
        context.setVariable("businessName", table.getBusinessName());
        context.setVariable("packageName", table.getPackageName());
        context.setVariable("functionName", table.getFunctionName());
        context.setVariable("author", table.getAuthor());
    }

    /**
     * 去除表前缀
     */
    private String removePrefix(String tableName) {
        for (String prefix : configProvider.getTablePrefixes()) {
            if (tableName.toLowerCase().startsWith(prefix.toLowerCase())) {
                return tableName.substring(prefix.length());
            }
        }
        return tableName;
    }

    /**
     * 下划线转PascalCase
     */
    private String toPascalCase(String name) {
        return Arrays.stream(name.split("_"))
                .filter(StrUtil::isNotBlank)
                .map(s -> StrUtil.upperFirst(s.toLowerCase()))
                .collect(Collectors.joining());
    }

    /**
     * 下划线转camelCase
     */
    private String toCamelCase(String name) {
        String pascal = toPascalCase(name);
        return StrUtil.lowerFirst(pascal);
    }
}
