package com.tiny.storage.dto;

import com.tiny.common.core.page.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 存储配置查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "存储配置查询条件")
public class StorageConfigQueryDTO extends PageQuery {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "配置名称")
    private String configName;

    @Schema(description = "存储类型")
    private String storageType;

    @Schema(description = "状态")
    private String status;
}
