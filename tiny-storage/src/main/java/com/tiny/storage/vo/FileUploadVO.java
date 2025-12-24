package com.tiny.storage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件上传结果VO
 */
@Data
@Builder
@Schema(description = "文件上传结果")
public class FileUploadVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "原始文件名")
    private String originalFilename;

    @Schema(description = "存储文件名")
    private String storedFilename;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "文件大小（字节）")
    private Long fileSize;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "文件访问URL")
    private String url;

    @Schema(description = "存储类型")
    private String storageType;
}