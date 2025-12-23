package com.tiny.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.common.core.page.PageResult;
import com.tiny.system.dto.SysOperationLogQueryDTO;
import com.tiny.system.entity.SysOperationLog;
import com.tiny.system.vo.SysOperationLogVO;

import java.util.List;

/**
 * 操作日志Service
 */
public interface SysOperationLogService extends IService<SysOperationLog> {

    /**
     * 分页查询操作日志
     */
    PageResult<SysOperationLogVO> page(SysOperationLogQueryDTO queryDTO);

    /**
     * 查询操作日志详情
     */
    SysOperationLogVO getDetail(Long operationLogId);

    /**
     * 记录操作日志
     */
    void recordOperationLog(SysOperationLog operationLog);

    /**
     * 批量删除操作日志
     */
    void deleteBatch(List<Long> operationLogIds);

    /**
     * 清空操作日志
     */
    void clean();
}
