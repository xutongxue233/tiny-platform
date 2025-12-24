package com.tiny.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 代码生成字段配置
 */
@Data
@TableName("gen_table_column")
public class GenTableColumn implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 列ID
     */
    @TableId(type = IdType.AUTO)
    private Long columnId;

    /**
     * 表ID
     */
    private Long tableId;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列描述
     */
    private String columnComment;

    /**
     * 数据库类型
     */
    private String columnType;

    /**
     * Java类型
     */
    private String javaType;

    /**
     * Java字段名
     */
    private String javaField;

    /**
     * 是否主键: 0否 1是
     */
    private String isPk;

    /**
     * 是否自增: 0否 1是
     */
    private String isIncrement;

    /**
     * 是否必填: 0否 1是
     */
    private String isRequired;

    /**
     * 是否新增字段: 0否 1是
     */
    private String isInsert;

    /**
     * 是否编辑字段: 0否 1是
     */
    private String isEdit;

    /**
     * 是否列表显示: 0否 1是
     */
    private String isList;

    /**
     * 是否查询条件: 0否 1是
     */
    private String isQuery;

    /**
     * 是否详情显示: 0否 1是
     */
    private String isDetail;

    /**
     * 是否导出字段: 0否 1是
     */
    private String isExport;

    /**
     * 查询方式: EQ NE GT GTE LT LTE LIKE LIKE_LEFT LIKE_RIGHT BETWEEN IN
     */
    private String queryType;

    /**
     * 控件类型: input textarea number select radio checkbox datetime date time upload imageUpload editor treeSelect
     */
    private String htmlType;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 判断是否为主键
     */
    public boolean isPk() {
        return "1".equals(this.isPk);
    }

    /**
     * 判断是否自增
     */
    public boolean isIncrement() {
        return "1".equals(this.isIncrement);
    }

    /**
     * 判断是否必填
     */
    public boolean isRequired() {
        return "1".equals(this.isRequired);
    }

    /**
     * 判断是否新增字段
     */
    public boolean isInsert() {
        return "1".equals(this.isInsert);
    }

    /**
     * 判断是否编辑字段
     */
    public boolean isEdit() {
        return "1".equals(this.isEdit);
    }

    /**
     * 判断是否列表字段
     */
    public boolean isList() {
        return "1".equals(this.isList);
    }

    /**
     * 判断是否查询字段
     */
    public boolean isQuery() {
        return "1".equals(this.isQuery);
    }

    /**
     * 判断是否详情字段
     */
    public boolean isDetail() {
        return "1".equals(this.isDetail);
    }

    /**
     * 判断是否导出字段
     */
    public boolean isExport() {
        return "1".equals(this.isExport);
    }

    /**
     * 获取Java字段名首字母大写
     */
    public String getCapJavaField() {
        if (javaField == null || javaField.isEmpty()) {
            return "";
        }
        return javaField.substring(0, 1).toUpperCase() + javaField.substring(1);
    }
}
