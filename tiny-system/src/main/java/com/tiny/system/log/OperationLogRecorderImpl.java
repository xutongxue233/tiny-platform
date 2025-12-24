package com.tiny.system.log;

import com.tiny.security.log.OperationLogDTO;
import com.tiny.security.log.OperationLogRecorder;
import com.tiny.system.entity.SysOperationLog;
import com.tiny.system.service.SysOperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 操作日志记录器实现
 */
@Component
@RequiredArgsConstructor
public class OperationLogRecorderImpl implements OperationLogRecorder {

    private final SysOperationLogService operationLogService;

    @Async
    @Override
    public void record(OperationLogDTO logDTO) {
        SysOperationLog operationLog = convertToEntity(logDTO);
        operationLogService.save(operationLog);
    }

    /**
     * 将DTO转换为实体
     */
    private SysOperationLog convertToEntity(OperationLogDTO dto) {
        SysOperationLog entity = new SysOperationLog();
        entity.setUserId(dto.getUserId());
        entity.setUsername(dto.getUsername());
        entity.setModuleName(dto.getModule());
        entity.setOperationType(dto.getOperationType());
        entity.setOperationDesc(dto.getDescription());
        entity.setRequestMethod(dto.getRequestMethod());
        entity.setRequestUrl(dto.getRequestUrl());
        entity.setMethodName(dto.getMethod());
        entity.setRequestParam(dto.getRequestParams());
        entity.setResponseResult(dto.getResponseResult());
        entity.setStatus(dto.getStatus() != null ? String.valueOf(dto.getStatus()) : "0");
        entity.setErrorMsg(dto.getErrorMsg());
        entity.setIpAddr(dto.getIp());
        entity.setOperationLocation(dto.getLocation());
        entity.setBrowser(dto.getBrowser());
        entity.setOs(dto.getOs());
        entity.setExecutionTime(dto.getCostTime());
        entity.setOperationTime(LocalDateTime.now());
        return entity;
    }
}
