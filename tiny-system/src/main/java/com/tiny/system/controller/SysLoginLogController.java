package com.tiny.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.common.core.domain.ResponseResult;
import com.tiny.common.core.model.DeleteDTO;
import com.tiny.common.core.page.PageResult;
import com.tiny.system.dto.SysLoginLogQueryDTO;
import com.tiny.system.service.SysLoginLogService;
import com.tiny.system.vo.SysLoginLogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 登录日志控制器
 */
@Tag(name = "登录日志", description = "登录日志查询等接口")
@RestController
@RequestMapping("/monitor/loginLog")
@RequiredArgsConstructor
public class SysLoginLogController {

    private final SysLoginLogService loginLogService;

    @Operation(summary = "分页查询登录日志")
    @SaCheckPermission("monitor:loginLog:list")
    @PostMapping("/page")
    public ResponseResult<PageResult<SysLoginLogVO>> page(@RequestBody SysLoginLogQueryDTO queryDTO) {
        PageResult<SysLoginLogVO> result = loginLogService.page(queryDTO);
        return ResponseResult.ok(result);
    }

    @Operation(summary = "批量删除登录日志")
    @OperationLog(module = "登录日志", type = OperationType.DELETE, desc = "批量删除登录日志")
    @SaCheckPermission("monitor:loginLog:remove")
    @DeleteMapping("/batch")
    public ResponseResult<Void> deleteBatch(@Valid @RequestBody DeleteDTO dto) {
        loginLogService.deleteBatch(dto.getIds());
        return ResponseResult.ok();
    }

    @Operation(summary = "清空登录日志")
    @OperationLog(module = "登录日志", type = OperationType.DELETE, desc = "清空登录日志")
    @SaCheckPermission("monitor:loginLog:remove")
    @DeleteMapping("/clean")
    public ResponseResult<Void> clean() {
        loginLogService.clean();
        return ResponseResult.ok();
    }
}
