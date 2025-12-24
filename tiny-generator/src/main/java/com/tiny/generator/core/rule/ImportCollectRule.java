package com.tiny.generator.core.rule;

import com.tiny.generator.core.context.GenContext;
import com.tiny.generator.entity.GenTable;
import com.tiny.generator.entity.GenTableColumn;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.TreeSet;

/**
 * 导包收集规则
 */
@Component
public class ImportCollectRule implements GenRule {

    @Override
    public String name() {
        return "ImportCollect";
    }

    @Override
    public int order() {
        return 40;
    }

    @Override
    public void apply(GenContext context) {
        GenTable table = context.getTable();
        if (table.getColumns() == null) {
            return;
        }

        Set<String> imports = new TreeSet<>(context.getImportPackages());

        for (GenTableColumn column : table.getColumns()) {
            String javaType = column.getJavaType();
            if (javaType == null) {
                continue;
            }

            // 根据Java类型添加导入
            switch (javaType) {
                case "BigDecimal" -> imports.add("java.math.BigDecimal");
                case "LocalDateTime" -> imports.add("java.time.LocalDateTime");
                case "LocalDate" -> imports.add("java.time.LocalDate");
                case "LocalTime" -> imports.add("java.time.LocalTime");
                case "Date" -> imports.add("java.util.Date");
                default -> {
                    // 其他类型不需要导入
                }
            }
        }

        context.setVariable("importPackages", imports);
    }
}
