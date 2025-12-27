package com.tiny.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tiny.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 代码生成器配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gen_config")
public class GenConfig extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    @TableId(type = IdType.AUTO)
    private Long configId;

    /**
     * 配置键
     */
    private String configKey;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 配置描述
     */
    private String configDesc;

    // 配置键常量
    public static final String KEY_AUTHOR = "gen.author";
    public static final String KEY_PACKAGE_NAME = "gen.packageName";
    public static final String KEY_TABLE_PREFIXES = "gen.tablePrefixes";
    public static final String KEY_REMOVE_PREFIX = "gen.removePrefix";
    public static final String KEY_BACKEND_PATH = "gen.backendPath";
    public static final String KEY_FRONTEND_PATH = "gen.frontendPath";
}
