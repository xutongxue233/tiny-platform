package com.tiny.generator.core.rule;

import cn.hutool.core.util.StrUtil;
import com.tiny.generator.core.GenConstants;
import com.tiny.generator.core.context.GenContext;
import com.tiny.generator.entity.GenTable;
import com.tiny.generator.entity.GenTableColumn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 字段过滤规则
 */
@Component
public class FieldFilterRule implements GenRule {

    @Override
    public String name() {
        return "FieldFilter";
    }

    @Override
    public int order() {
        return 30;
    }

    @Override
    public void apply(GenContext context) {
        GenTable table = context.getTable();
        if (table.getColumns() == null) {
            return;
        }

        List<GenTableColumn> insertColumns = new ArrayList<>();
        List<GenTableColumn> editColumns = new ArrayList<>();
        List<GenTableColumn> listColumns = new ArrayList<>();
        List<GenTableColumn> queryColumns = new ArrayList<>();
        List<GenTableColumn> detailColumns = new ArrayList<>();
        List<GenTableColumn> exportColumns = new ArrayList<>();

        for (GenTableColumn column : table.getColumns()) {
            String columnName = column.getColumnName().toLowerCase();

            // 设置主键列
            if (column.isPk()) {
                table.setPkColumn(column);
            }

            // 基础实体字段特殊处理
            if (GenConstants.BASE_ENTITY_FIELDS.contains(columnName)) {
                // 基础字段不参与新增/编辑
                column.setIsInsert("0");
                column.setIsEdit("0");
                // 创建时间/更新时间可以显示在列表
                if ("create_time".equals(columnName) || "update_time".equals(columnName)) {
                    if (StrUtil.isBlank(column.getIsList())) {
                        column.setIsList("1");
                    }
                } else {
                    column.setIsList("0");
                }
                column.setIsQuery("0");
                column.setIsDetail("0");
                column.setIsExport("0");
                continue;
            }

            // 设置默认值
            setDefaultValues(column);

            // 分类收集
            if (column.isInsert()) {
                insertColumns.add(column);
            }
            if (column.isEdit()) {
                editColumns.add(column);
            }
            if (column.isList()) {
                listColumns.add(column);
            }
            if (column.isQuery()) {
                queryColumns.add(column);
            }
            if (column.isDetail()) {
                detailColumns.add(column);
            }
            if (column.isExport()) {
                exportColumns.add(column);
            }
        }

        // 设置模板变量
        context.setVariable("columns", table.getColumns());
        context.setVariable("pkColumn", table.getPkColumn());
        context.setVariable("insertColumns", insertColumns);
        context.setVariable("editColumns", editColumns);
        context.setVariable("listColumns", listColumns);
        context.setVariable("queryColumns", queryColumns);
        context.setVariable("detailColumns", detailColumns);
        context.setVariable("exportColumns", exportColumns);
    }

    /**
     * 设置字段默认值
     */
    private void setDefaultValues(GenTableColumn column) {
        String columnName = column.getColumnName().toLowerCase();

        // 默认都参与新增
        if (StrUtil.isBlank(column.getIsInsert())) {
            column.setIsInsert("1");
        }

        // 主键不参与编辑
        if (StrUtil.isBlank(column.getIsEdit())) {
            column.setIsEdit(column.isPk() ? "0" : "1");
        }

        // 默认都显示在列表
        if (StrUtil.isBlank(column.getIsList())) {
            column.setIsList("1");
        }

        // 默认不作为查询条件
        if (StrUtil.isBlank(column.getIsQuery())) {
            // 名称、标题、状态、类型字段默认作为查询条件
            if (columnName.contains("name") || columnName.contains("title")
                    || columnName.contains("status") || columnName.contains("type")) {
                column.setIsQuery("1");
            } else {
                column.setIsQuery("0");
            }
        }

        // 默认查询方式
        if (StrUtil.isBlank(column.getQueryType())) {
            if (columnName.contains("name") || columnName.contains("title")) {
                column.setQueryType(GenConstants.QUERY_LIKE);
            } else if (columnName.contains("time") || columnName.contains("date")) {
                column.setQueryType(GenConstants.QUERY_BETWEEN);
            } else {
                column.setQueryType(GenConstants.QUERY_EQ);
            }
        }

        // 默认显示在详情
        if (StrUtil.isBlank(column.getIsDetail())) {
            column.setIsDetail("1");
        }

        // 默认导出
        if (StrUtil.isBlank(column.getIsExport())) {
            column.setIsExport("1");
        }
    }
}
