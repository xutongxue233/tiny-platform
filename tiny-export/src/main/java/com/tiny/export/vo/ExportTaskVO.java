package com.tiny.export.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 导出任务VO
 */
@Data
@Schema(description = "导出任务信息")
public class ExportTaskVO {

    @Schema(description = "任务ID")
    private Long taskId;

    @Schema(description = "任务名称")
    private String taskName;

    @Schema(description = "任务类型(export/import)")
    private String taskType;

    @Schema(description = "任务类型名称")
    private String taskTypeName;

    @Schema(description = "业务类型")
    private String businessType;

    @Schema(description = "业务类型名称")
    private String businessTypeName;

    @Schema(description = "状态(0待处理 1处理中 2成功 3失败)")
    private String status;

    @Schema(description = "状态名称")
    private String statusName;

    @Schema(description = "文件ID")
    private Long fileId;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "文件访问URL")
    private String fileUrl;

    @Schema(description = "文件大小(字节)")
    private Long fileSize;

    @Schema(description = "文件大小描述")
    private String fileSizeDesc;

    @Schema(description = "总记录数")
    private Integer totalCount;

    @Schema(description = "成功记录数")
    private Integer successCount;

    @Schema(description = "失败记录数")
    private Integer failCount;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "查询参数(JSON格式)")
    private String queryParams;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "文件过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "耗时(秒)")
    private Long duration;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "备注")
    private String remark;
}
