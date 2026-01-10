package com.tiny.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.core.web.ResponseResult;
import com.tiny.common.core.dto.DeleteDTO;
import com.tiny.common.core.page.PageResult;
import com.tiny.system.dto.SysOperationLogQueryDTO;
import com.tiny.system.service.SysOperationLogService;
import com.tiny.system.vo.SysOperationLogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志控制器
 */
@Tag(name = "操作日志", description = "操作日志查询等接口")
@RestController
@RequestMapping("/monitor/operationLog")
@RequiredArgsConstructor
public class SysOperationLogController {

    private final SysOperationLogService operationLogService;

    @Operation(summary = "分页查询操作日志")
    @SaCheckPermission("monitor:operationLog:list")
    @PostMapping("/page")
    public ResponseResult<PageResult<SysOperationLogVO>> page(@Valid @RequestBody SysOperationLogQueryDTO queryDTO) {
        PageResult<SysOperationLogVO> result = operationLogService.page(queryDTO);
        return ResponseResult.ok(result);
    }

    @Operation(summary = "查询操作日志详情")
    @SaCheckPermission("monitor:operationLog:query")
    @GetMapping("/{operationLogId}")
    public ResponseResult<SysOperationLogVO> getById(@Parameter(description = "操作日志ID") @PathVariable Long operationLogId) {
        SysOperationLogVO operationLog = operationLogService.getDetail(operationLogId);
        return ResponseResult.ok(operationLog);
    }

    @Operation(summary = "批量删除操作日志")
    @OperationLog(module = "操作日志", type = OperationType.DELETE, desc = "批量删除操作日志")
    @SaCheckPermission("monitor:operationLog:remove")
    @DeleteMapping("/batch")
    public ResponseResult<Void> deleteBatch(@Valid @RequestBody DeleteDTO dto) {
        operationLogService.deleteBatch(dto.getIds());
        return ResponseResult.ok();
    }

    @Operation(summary = "清空操作日志")
    @OperationLog(module = "操作日志", type = OperationType.DELETE, desc = "清空操作日志")
    @SaCheckPermission("monitor:operationLog:remove")
    @DeleteMapping("/clean")
    public ResponseResult<Void> clean() {
        operationLogService.clean();
        return ResponseResult.ok();
    }
}
