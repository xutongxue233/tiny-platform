package com.tiny.export.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.exception.BusinessException;
import com.tiny.export.dto.ExportTaskQueryDTO;
import com.tiny.export.entity.ExportTask;
import com.tiny.export.enums.ExportStatusEnum;
import com.tiny.export.enums.ExportTypeEnum;
import com.tiny.export.mapper.ExportTaskMapper;
import com.tiny.export.service.ExportTaskService;
import com.tiny.export.vo.ExportTaskVO;
import com.tiny.storage.factory.StorageFactory;
import com.tiny.storage.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 导出任务服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExportTaskServiceImpl extends ServiceImpl<ExportTaskMapper, ExportTask>
        implements ExportTaskService {

    private final StorageFactory storageFactory;

    @Override
    public PageResult<ExportTaskVO> page(ExportTaskQueryDTO queryDTO) {
        Page<ExportTask> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        LambdaQueryWrapper<ExportTask> wrapper = Wrappers.<ExportTask>lambdaQuery()
                .like(StrUtil.isNotBlank(queryDTO.getTaskName()), ExportTask::getTaskName, queryDTO.getTaskName())
                .eq(StrUtil.isNotBlank(queryDTO.getTaskType()), ExportTask::getTaskType, queryDTO.getTaskType())
                .eq(StrUtil.isNotBlank(queryDTO.getBusinessType()), ExportTask::getBusinessType, queryDTO.getBusinessType())
                .eq(StrUtil.isNotBlank(queryDTO.getStatus()), ExportTask::getStatus, queryDTO.getStatus())
                .ge(queryDTO.getStartTimeBegin() != null, ExportTask::getStartTime, queryDTO.getStartTimeBegin())
                .le(queryDTO.getStartTimeEnd() != null, ExportTask::getStartTime, queryDTO.getStartTimeEnd())
                .orderByDesc(ExportTask::getCreateTime);

        Page<ExportTask> result = this.page(page, wrapper);

        List<ExportTaskVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .toList();

        return PageResult.of(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public ExportTaskVO getTaskDetail(Long taskId) {
        ExportTask task = this.getById(taskId);
        if (task == null) {
            throw new BusinessException("任务不存在");
        }
        return convertToVO(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTask(Long taskId) {
        ExportTask task = this.getById(taskId);
        if (task == null) {
            throw new BusinessException("任务不存在");
        }

        // 删除关联文件
        if (task.getFileId() != null) {
            try {
                StorageService storageService = storageFactory.getDefaultStorage();
                if (StrUtil.isNotBlank(task.getFileUrl())) {
                    // 通过文件路径删除
                    storageService.delete(task.getFileUrl());
                }
            } catch (Exception e) {
                log.warn("删除任务关联文件失败: {}", e.getMessage());
            }
        }

        // 删除任务记录
        this.removeById(taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> taskIds) {
        if (taskIds == null || taskIds.isEmpty()) {
            return;
        }

        for (Long taskId : taskIds) {
            try {
                deleteTask(taskId);
            } catch (Exception e) {
                log.warn("删除任务失败, taskId: {}, error: {}", taskId, e.getMessage());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cleanExpiredTasks() {
        // 查询已过期的任务
        List<ExportTask> expiredTasks = this.list(
                Wrappers.<ExportTask>lambdaQuery()
                        .lt(ExportTask::getExpireTime, LocalDateTime.now())
                        .eq(ExportTask::getStatus, ExportStatusEnum.SUCCESS.getCode())
        );

        if (expiredTasks.isEmpty()) {
            return 0;
        }

        List<Long> taskIds = expiredTasks.stream()
                .map(ExportTask::getTaskId)
                .toList();

        deleteBatch(taskIds);

        log.info("已清理{}个过期导出任务", taskIds.size());
        return taskIds.size();
    }

    /**
     * 转换为VO
     */
    private ExportTaskVO convertToVO(ExportTask task) {
        ExportTaskVO vo = new ExportTaskVO();
        BeanUtil.copyProperties(task, vo);

        // 任务类型名称
        ExportTypeEnum typeEnum = ExportTypeEnum.getByCode(task.getTaskType());
        vo.setTaskTypeName(typeEnum != null ? typeEnum.getDesc() : task.getTaskType());

        // 状态名称
        ExportStatusEnum statusEnum = ExportStatusEnum.getByCode(task.getStatus());
        vo.setStatusName(statusEnum != null ? statusEnum.getDesc() : task.getStatus());

        // 业务类型名称(简单映射)
        vo.setBusinessTypeName(getBusinessTypeName(task.getBusinessType()));

        // 文件大小描述
        if (task.getFileSize() != null) {
            vo.setFileSizeDesc(formatFileSize(task.getFileSize()));
        }

        // 计算耗时
        if (task.getStartTime() != null && task.getEndTime() != null) {
            vo.setDuration(Duration.between(task.getStartTime(), task.getEndTime()).getSeconds());
        }

        return vo;
    }

    /**
     * 获取业务类型名称
     */
    private String getBusinessTypeName(String businessType) {
        if (businessType == null) {
            return "";
        }
        return switch (businessType) {
            case "sys_user" -> "用户管理";
            case "sys_dict_item" -> "字典数据";
            case "sys_role" -> "角色管理";
            case "sys_dept" -> "部门管理";
            case "sys_operation_log" -> "操作日志";
            case "sys_login_log" -> "登录日志";
            default -> businessType;
        };
    }

    /**
     * 格式化文件大小
     */
    private String formatFileSize(Long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
        }
    }
}
