package com.tiny.security.log;

/**
 * 操作日志记录器接口
 * 由业务模块实现具体的日志持久化逻辑
 */
public interface OperationLogRecorder {

    /**
     * 记录操作日志
     *
     * @param logDTO 操作日志DTO
     */
    void record(OperationLogDTO logDTO);
}
