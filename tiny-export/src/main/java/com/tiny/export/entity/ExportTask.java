package com.tiny.export.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tiny.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 导出任务实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_export_task")
public class ExportTask extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId(type = IdType.AUTO)
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型(export/import)
     */
    private String taskType;

    /**
     * 业务类型(sys_user/sys_dict_item等)
     */
    private String businessType;

    /**
     * 状态(0待处理 1处理中 2成功 3失败)
     */
    private String status;

    /**
     * 文件ID(关联sys_file_record)
     */
    private Long fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件访问URL
     */
    private String fileUrl;

    /**
     * 文件大小(字节)
     */
    private Long fileSize;

    /**
     * 总记录数
     */
    private Integer totalCount;

    /**
     * 成功记录数
     */
    private Integer successCount;

    /**
     * 失败记录数
     */
    private Integer failCount;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 查询参数(JSON格式)
     */
    private String queryParams;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 文件过期时间
     */
    private LocalDateTime expireTime;
}
