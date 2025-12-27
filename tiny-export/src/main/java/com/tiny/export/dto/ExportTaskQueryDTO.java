package com.tiny.export.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 导出任务查询DTO
 */
@Data
@Schema(description = "导出任务查询参数")
public class ExportTaskQueryDTO {

    @Schema(description = "当前页码")
    private Integer current = 1;

    @Schema(description = "每页条数")
    private Integer size = 10;

    @Schema(description = "任务名称")
    private String taskName;

    @Schema(description = "任务类型(export/import)")
    private String taskType;

    @Schema(description = "业务类型")
    private String businessType;

    @Schema(description = "状态(0待处理 1处理中 2成功 3失败)")
    private String status;

    @Schema(description = "开始时间-起")
    private LocalDateTime startTimeBegin;

    @Schema(description = "开始时间-止")
    private LocalDateTime startTimeEnd;
}
