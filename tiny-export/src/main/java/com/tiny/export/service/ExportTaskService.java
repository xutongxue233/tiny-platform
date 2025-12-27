package com.tiny.export.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.common.core.page.PageResult;
import com.tiny.export.dto.ExportTaskQueryDTO;
import com.tiny.export.entity.ExportTask;
import com.tiny.export.vo.ExportTaskVO;

import java.util.List;

/**
 * 导出任务服务接口
 */
public interface ExportTaskService extends IService<ExportTask> {

    /**
     * 分页查询导出任务
     *
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    PageResult<ExportTaskVO> page(ExportTaskQueryDTO queryDTO);

    /**
     * 获取任务详情
     *
     * @param taskId 任务ID
     * @return 任务详情
     */
    ExportTaskVO getTaskDetail(Long taskId);

    /**
     * 删除任务
     *
     * @param taskId 任务ID
     */
    void deleteTask(Long taskId);

    /**
     * 批量删除任务
     *
     * @param taskIds 任务ID列表
     */
    void deleteBatch(List<Long> taskIds);

    /**
     * 清理过期任务
     *
     * @return 清理数量
     */
    int cleanExpiredTasks();
}
