package com.tiny.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tiny.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 类型映射配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gen_type_mapping")
public class GenTypeMapping extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 映射ID
     */
    @TableId(type = IdType.AUTO)
    private Long mappingId;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * Java类型
     */
    private String javaType;

    /**
     * 需要导入的包
     */
    private String javaImport;

    /**
     * 默认控件类型
     */
    private String defaultHtmlType;

    /**
     * 排序
     */
    private Integer sortOrder;
}
