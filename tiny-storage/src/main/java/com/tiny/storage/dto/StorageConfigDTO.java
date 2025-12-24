package com.tiny.storage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 存储配置DTO
 */
@Data
@Schema(description = "存储配置")
public class StorageConfigDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "配置ID")
    private Long configId;

    @NotBlank(message = "配置名称不能为空")
    @Schema(description = "配置名称")
    private String configName;

    @NotBlank(message = "存储类型不能为空")
    @Schema(description = "存储类型")
    private String storageType;

    @Schema(description = "是否默认配置")
    private String isDefault;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "Endpoint")
    private String endpoint;

    @Schema(description = "存储桶名称")
    private String bucketName;

    @Schema(description = "Access Key ID")
    private String accessKeyId;

    @Schema(description = "Access Key Secret")
    private String accessKeySecret;

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
}
