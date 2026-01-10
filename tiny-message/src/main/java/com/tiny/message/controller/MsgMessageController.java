package com.tiny.message.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.common.core.dto.DeleteDTO;
import com.tiny.common.core.page.PageResult;
import com.tiny.core.web.ResponseResult;
import com.tiny.message.dto.MsgMessageQueryDTO;
import com.tiny.message.dto.SendMessageDTO;
import com.tiny.message.service.MessageSendService;
import com.tiny.message.service.MsgMessageService;
import com.tiny.message.vo.MsgMessageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 消息管理控制器
 */
@Tag(name = "消息管理", description = "消息的发送、查询、撤回等接口")
@RestController
@RequestMapping("/message/list")
@RequiredArgsConstructor
public class MsgMessageController {

    private final MsgMessageService messageService;
    private final MessageSendService messageSendService;

    /**
     * 分页查询消息列表
     */
    @Operation(summary = "分页查询消息列表")
    @SaCheckPermission("message:list:list")
    @PostMapping("/page")
    public ResponseResult<PageResult<MsgMessageVO>> page(@Valid @RequestBody MsgMessageQueryDTO queryDTO) {
        PageResult<MsgMessageVO> result = messageService.page(queryDTO);
        return ResponseResult.ok(result);
    }

    /**
     * 查询消息详情
     */
    @Operation(summary = "查询消息详情")
    @SaCheckPermission("message:list:query")
    @GetMapping("/{messageId}")
    public ResponseResult<MsgMessageVO> getById(@Parameter(description = "消息ID") @PathVariable Long messageId) {
        MsgMessageVO message = messageService.getDetail(messageId);
        return ResponseResult.ok(message);
    }

    /**
     * 发送消息
     */
    @Operation(summary = "发送消息")
    @OperationLog(module = "消息管理", type = OperationType.INSERT, desc = "发送消息")
    @SaCheckPermission("message:list:send")
    @PostMapping("/send")
    public ResponseResult<Void> send(@Valid @RequestBody SendMessageDTO dto) {
        messageSendService.send(dto);
        return ResponseResult.ok();
    }

    /**
     * 撤回消息
     */
    @Operation(summary = "撤回消息")
    @OperationLog(module = "消息管理", type = OperationType.UPDATE, desc = "撤回消息")
    @SaCheckPermission("message:list:revoke")
    @PutMapping("/revoke/{messageId}")
    public ResponseResult<Void> revoke(@Parameter(description = "消息ID") @PathVariable Long messageId) {
        messageService.revoke(messageId);
        return ResponseResult.ok();
    }

    /**
     * 删除消息
     */
    @Operation(summary = "删除消息")
    @OperationLog(module = "消息管理", type = OperationType.DELETE, desc = "删除消息")
    @SaCheckPermission("message:list:remove")
    @DeleteMapping("/{messageId}")
    public ResponseResult<Void> delete(@Parameter(description = "消息ID") @PathVariable Long messageId) {
        messageService.delete(messageId);
        return ResponseResult.ok();
    }

    /**
     * 批量删除消息
     */
    @Operation(summary = "批量删除消息")
    @OperationLog(module = "消息管理", type = OperationType.DELETE, desc = "批量删除消息")
    @SaCheckPermission("message:list:remove")
    @DeleteMapping("/batch")
    public ResponseResult<Void> deleteBatch(@Valid @RequestBody DeleteDTO dto) {
        messageService.deleteBatch(dto.getIds());
        return ResponseResult.ok();
    }
}
