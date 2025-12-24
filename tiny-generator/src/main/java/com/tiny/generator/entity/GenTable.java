package com.tiny.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tiny.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 代码生成表配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gen_table")
public class GenTable extends BaseEntity {

    /**
     * 表ID
     */
    @TableId(type = IdType.AUTO)
    private Long tableId;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表描述
     */
    private String tableComment;

    /**
     * 模块名
     */
    private String moduleName;

    /**
     * 包路径
     */
    private String packageName;

    /**
     * 实体类名
     */
    private String className;

    /**
     * 业务名
     */
    private String businessName;

    /**
     * 功能名称
     */
    private String functionName;

    /**
     * 作者
     */
    private String author;

    /**
     * 前端模块路径
     */
    private String feModulePath;

    /**
     * 生成类型: 1单表 2主从
     */
    private String feGenerateType;

    /**
     * 生成方式: 0zip 1路径
     */
    private String genType;

    /**
     * 生成路径
     */
    private String genPath;

    /**
     * 扩展选项JSON
     */
    private String options;

    /**
     * 表字段列表
     */
    @TableField(exist = false)
    private List<GenTableColumn> columns;

    /**
     * 主键列
     */
    @TableField(exist = false)
    private GenTableColumn pkColumn;
}
