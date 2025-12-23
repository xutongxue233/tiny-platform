package com.tiny.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.common.core.page.PageResult;
import com.tiny.system.dto.SysOperationLogQueryDTO;
import com.tiny.system.entity.SysOperationLog;
import com.tiny.system.mapper.SysOperationLogMapper;
import com.tiny.system.service.SysOperationLogService;
import com.tiny.system.vo.SysOperationLogVO;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志Service实现类
 */
@Service
@RequiredArgsConstructor
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog> implements SysOperationLogService {

    @Override
    public PageResult<SysOperationLogVO> page(SysOperationLogQueryDTO queryDTO) {
        LambdaQueryWrapper<SysOperationLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getUsername()), SysOperationLog::getUsername, queryDTO.getUsername())
                .like(StrUtil.isNotBlank(queryDTO.getModuleName()), SysOperationLog::getModuleName, queryDTO.getModuleName())
                .eq(StrUtil.isNotBlank(queryDTO.getOperationType()), SysOperationLog::getOperationType, queryDTO.getOperationType())
                .eq(StrUtil.isNotBlank(queryDTO.getStatus()), SysOperationLog::getStatus, queryDTO.getStatus())
                .ge(queryDTO.getBeginTime() != null, SysOperationLog::getOperationTime, queryDTO.getBeginTime())
                .le(queryDTO.getEndTime() != null, SysOperationLog::getOperationTime, queryDTO.getEndTime())
                .orderByDesc(SysOperationLog::getOperationTime);

        Page<SysOperationLog> page = page(new Page<>(queryDTO.getCurrent(), queryDTO.getSize()), wrapper);
        return PageResult.of(page, SysOperationLog::toVO);
    }

    @Override
    public SysOperationLogVO getDetail(Long operationLogId) {
        SysOperationLog operationLog = getById(operationLogId);
        return operationLog != null ? operationLog.toVO() : null;
    }

    @Async
    @Override
    public void recordOperationLog(SysOperationLog operationLog) {
        save(operationLog);
    }

    @Override
    public void deleteBatch(List<Long> operationLogIds) {
        removeByIds(operationLogIds);
    }

    @Override
    public void clean() {
        remove(new LambdaQueryWrapper<>());
    }
}
