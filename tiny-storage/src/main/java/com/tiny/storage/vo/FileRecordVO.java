package com.tiny.storage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件记录VO
 */
@Data
@Schema(description = "文件记录信息")
public class FileRecordVO implements Serializable {

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

    @Schema(description = "文件大小")
    private Long fileSize;

    @Schema(description = "文件大小描述")
    private String fileSizeDesc;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "文件扩展名")
    private String fileExt;

    @Schema(description = "存储类型")
    private String storageType;

    @Schema(description = "文件URL")
    private String fileUrl;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
