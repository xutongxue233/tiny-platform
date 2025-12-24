package com.tiny.storage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 存储配置VO
 */
@Data
@Schema(description = "存储配置信息")
public class StorageConfigVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "配置ID")
    private Long configId;

    @Schema(description = "配置名称")
    private String configName;

    @Schema(description = "存储类型")
    private String storageType;

    @Schema(description = "存储类型描述")
    private String storageTypeDesc;

    @Schema(description = "是否默认配置")
    private String isDefault;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "Endpoint")
    private String endpoint;

    @Schema(description = "存储桶名称")
    private String bucketName;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "区域")
    private String region;

    @Schema(description = "本地存储路径")
    private String localPath;

    @Schema(description = "本地存储URL前缀")
    private String localUrlPrefix;

    @Schema(description = "是否使用HTTPS")
    private String useHttps;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
