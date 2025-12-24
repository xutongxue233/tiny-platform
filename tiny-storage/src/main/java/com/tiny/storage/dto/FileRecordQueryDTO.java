package com.tiny.storage.dto;

import com.tiny.common.core.page.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 文件记录查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "文件记录查询条件")
public class FileRecordQueryDTO extends PageQuery {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "原始文件名")
    private String originalFilename;

    @Schema(description = "存储类型")
    private String storageType;

    @Schema(description = "文件类型")
    private String fileType;
}
