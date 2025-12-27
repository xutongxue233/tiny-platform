package com.tiny.message.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.common.core.model.DeleteDTO;
import com.tiny.common.core.page.PageResult;
import com.tiny.core.web.ResponseResult;
import com.tiny.message.dto.MsgSendLogQueryDTO;
import com.tiny.message.service.MessageSendService;
import com.tiny.message.service.MsgSendLogService;
import com.tiny.message.vo.MsgSendLogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 发送记录控制器
 */
@Tag(name = "发送记录管理", description = "消息发送记录的查询、重试等接口")
@RestController
@RequestMapping("/message/log")
@RequiredArgsConstructor
public class MsgSendLogController {

    private final MsgSendLogService sendLogService;
    private final MessageSendService messageSendService;

    /**
     * 分页查询发送记录
     */
    @Operation(summary = "分页查询发送记录")
    @SaCheckPermission("message:log:list")
    @PostMapping("/page")
    public ResponseResult<PageResult<MsgSendLogVO>> page(@Valid @RequestBody MsgSendLogQueryDTO queryDTO) {
        PageResult<MsgSendLogVO> result = sendLogService.page(queryDTO);
        return ResponseResult.ok(result);
    }

    /**
     * 查询发送记录详情
     */
    @Operation(summary = "查询发送记录详情")
    @SaCheckPermission("message:log:query")
    @GetMapping("/{logId}")
    public ResponseResult<MsgSendLogVO> getById(@Parameter(description = "日志ID") @PathVariable Long logId) {
        MsgSendLogVO log = sendLogService.getDetail(logId);
        return ResponseResult.ok(log);
    }

    /**
     * 重试发送
     */
    @Operation(summary = "重试发送")
    @OperationLog(module = "发送记录", type = OperationType.UPDATE, desc = "重试发送")
    @SaCheckPermission("message:log:retry")
    @PostMapping("/retry/{logId}")
    public ResponseResult<Void> retry(@Parameter(description = "日志ID") @PathVariable Long logId) {
        messageSendService.retrySend(logId);
        return ResponseResult.ok();
    }

    /**
     * 删除发送记录
     */
    @Operation(summary = "删除发送记录")
    @OperationLog(module = "发送记录", type = OperationType.DELETE, desc = "删除发送记录")
    @SaCheckPermission("message:log:remove")
    @DeleteMapping("/{logId}")
    public ResponseResult<Void> delete(@Parameter(description = "日志ID") @PathVariable Long logId) {
        sendLogService.delete(logId);
        return ResponseResult.ok();
    }

    /**
     * 批量删除发送记录
     */
    @Operation(summary = "批量删除发送记录")
    @OperationLog(module = "发送记录", type = OperationType.DELETE, desc = "批量删除发送记录")
    @SaCheckPermission("message:log:remove")
    @DeleteMapping("/batch")
    public ResponseResult<Void> deleteBatch(@Valid @RequestBody DeleteDTO dto) {
        sendLogService.deleteBatch(dto.getIds());
        return ResponseResult.ok();
    }
}
