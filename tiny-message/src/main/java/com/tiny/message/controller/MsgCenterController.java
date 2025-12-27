package com.tiny.message.controller;

import com.tiny.common.core.page.PageResult;
import com.tiny.core.web.ResponseResult;
import com.tiny.message.dto.UserMessageQueryDTO;
import com.tiny.message.service.MsgRecipientService;
import com.tiny.message.vo.UserMessageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;

/**
 * 用户消息中心控制器
 */
@Tag(name = "用户消息中心", description = "用户消息的查询、已读标记等接口")
@RestController
@RequestMapping("/message/center")
@RequiredArgsConstructor
public class MsgCenterController {

    private final MsgRecipientService recipientService;

    /**
     * 分页查询用户消息
     */
    @Operation(summary = "分页查询用户消息")
    @PostMapping("/page")
    public ResponseResult<PageResult<UserMessageVO>> page(@Valid @RequestBody UserMessageQueryDTO queryDTO) {
        PageResult<UserMessageVO> result = recipientService.pageUserMessages(queryDTO);
        return ResponseResult.ok(result);
    }

    /**
     * 获取未读消息数量
     */
    @Operation(summary = "获取未读消息数量")
    @GetMapping("/unread-count")
    public ResponseResult<Integer> getUnreadCount() {
        int count = recipientService.getUnreadCount();
        return ResponseResult.ok(count);
    }

    /**
     * 标记消息已读
     */
    @Operation(summary = "标记消息已读")
    @PostMapping("/read/{recipientId}")
    public ResponseResult<Void> markAsRead(@Parameter(description = "接收记录ID") @PathVariable Long recipientId) {
        recipientService.markAsRead(recipientId);
        return ResponseResult.ok();
    }

    /**
     * 标记所有消息已读
     */
    @Operation(summary = "标记所有消息已读")
    @PostMapping("/read-all")
    public ResponseResult<Void> markAllAsRead() {
        recipientService.markAllAsRead();
        return ResponseResult.ok();
    }

    /**
     * 删除消息
     */
    @Operation(summary = "删除消息")
    @OperationLog(module = "消息中心", type = OperationType.DELETE, desc = "删除消息")
    @DeleteMapping("/{recipientId}")
    public ResponseResult<Void> delete(@Parameter(description = "接收记录ID") @PathVariable Long recipientId) {
        recipientService.deleteByUser(recipientId);
        return ResponseResult.ok();
    }
}
