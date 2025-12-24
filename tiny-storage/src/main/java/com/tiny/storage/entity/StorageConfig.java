package com.tiny.storage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tiny.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 存储配置实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_storage_config")
public class StorageConfig extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    @TableId(type = IdType.AUTO)
    private Long configId;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 存储类型
     */
    private String storageType;

    /**
     * 是否默认配置
     */
    private String isDefault;

    /**
     * 状态
     */
    private String status;

    /**
     * Endpoint
     */
    private String endpoint;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * Access Key ID
     */
    private String accessKeyId;

    /**
     * Access Key Secret
     */
    private String accessKeySecret;

    /**
     * 域名
     */
    private String domain;

    /**
     * 区域
     */
    private String region;

    /**
     * 本地存储路径
     */
    private String localPath;

    /**
     * 本地存储URL前缀
     */
    private String localUrlPrefix;

    /**
     * 是否使用HTTPS
     */
    private String useHttps;
}