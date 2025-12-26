package com.tiny.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.common.core.model.DeleteDTO;
import com.tiny.common.core.page.PageResult;
import com.tiny.core.web.ResponseResult;
import com.tiny.system.dto.SysNoticeDTO;
import com.tiny.system.dto.SysNoticeQueryDTO;
import com.tiny.system.dto.UpdateStatusDTO;
import com.tiny.system.dto.UpdateTopDTO;
import com.tiny.system.service.SysNoticeService;
import com.tiny.system.vo.SysNoticeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 通知公告控制器
 */
@Tag(name = "通知公告管理", description = "通知公告的增删改查等接口")
@RestController
@RequestMapping("/info/notice")
@RequiredArgsConstructor
public class SysNoticeController {

    private final SysNoticeService noticeService;

    /**
     * 分页查询通知公告列表
     */
    @Operation(summary = "分页查询通知公告列表")
    @SaCheckPermission("info:notice:list")
    @PostMapping("/page")
    public ResponseResult<PageResult<SysNoticeVO>> page(@Valid @RequestBody SysNoticeQueryDTO queryDTO) {
        PageResult<SysNoticeVO> result = noticeService.page(queryDTO);
        return ResponseResult.ok(result);
    }

    /**
     * 查询通知公告详情
     */
    @Operation(summary = "查询通知公告详情")
    @SaCheckPermission("info:notice:query")
    @GetMapping("/{noticeId}")
    public ResponseResult<SysNoticeVO> getById(@Parameter(description = "公告ID") @PathVariable Long noticeId) {
        SysNoticeVO notice = noticeService.getDetail(noticeId);
        return ResponseResult.ok(notice);
    }

    /**
     * 新增通知公告
     */
    @Operation(summary = "新增通知公告")
    @OperationLog(module = "通知公告", type = OperationType.INSERT, desc = "新增通知公告")
    @SaCheckPermission("info:notice:add")
    @PostMapping
    public ResponseResult<Void> add(@Valid @RequestBody SysNoticeDTO dto) {
        noticeService.add(dto);
        return ResponseResult.ok();
    }

    /**
     * 修改通知公告
     */
    @Operation(summary = "修改通知公告")
    @OperationLog(module = "通知公告", type = OperationType.UPDATE, desc = "修改通知公告")
    @SaCheckPermission("info:notice:edit")
    @PutMapping
    public ResponseResult<Void> update(@Valid @RequestBody SysNoticeDTO dto) {
        noticeService.update(dto);
        return ResponseResult.ok();
    }

    /**
     * 删除通知公告
     */
    @Operation(summary = "删除通知公告")
    @OperationLog(module = "通知公告", type = OperationType.DELETE, desc = "删除通知公告")
    @SaCheckPermission("info:notice:remove")
    @DeleteMapping("/{noticeId}")
    public ResponseResult<Void> delete(@Parameter(description = "公告ID") @PathVariable Long noticeId) {
        noticeService.delete(noticeId);
        return ResponseResult.ok();
    }

    /**
     * 批量删除通知公告
     */
    @Operation(summary = "批量删除通知公告")
    @OperationLog(module = "通知公告", type = OperationType.DELETE, desc = "批量删除通知公告")
    @SaCheckPermission("info:notice:remove")
    @DeleteMapping("/batch")
    public ResponseResult<Void> deleteBatch(@Valid @RequestBody DeleteDTO dto) {
        noticeService.deleteBatch(dto.getIds());
        return ResponseResult.ok();
    }

    /**
     * 修改通知公告状态
     */
    @Operation(summary = "修改通知公告状态")
    @OperationLog(module = "通知公告", type = OperationType.UPDATE, desc = "修改通知公告状态")
    @SaCheckPermission("info:notice:edit")
    @PutMapping("/status")
    public ResponseResult<Void> updateStatus(@Valid @RequestBody UpdateStatusDTO dto) {
        noticeService.updateStatus(dto.getId(), dto.getStatus());
        return ResponseResult.ok();
    }

    /**
     * 修改通知公告置顶状态
     */
    @Operation(summary = "修改通知公告置顶状态")
    @OperationLog(module = "通知公告", type = OperationType.UPDATE, desc = "修改通知公告置顶状态")
    @SaCheckPermission("info:notice:edit")
    @PutMapping("/top")
    public ResponseResult<Void> updateTop(@Valid @RequestBody UpdateTopDTO dto) {
        noticeService.updateTop(dto.getId(), dto.getIsTop());
        return ResponseResult.ok();
    }

    /**
     * 标记公告已读
     */
    @Operation(summary = "标记公告已读")
    @PostMapping("/read/{noticeId}")
    public ResponseResult<Void> markAsRead(@Parameter(description = "公告ID") @PathVariable Long noticeId) {
        noticeService.markAsRead(noticeId);
        return ResponseResult.ok();
    }

    /**
     * 获取未读公告数量
     */
    @Operation(summary = "获取未读公告数量")
    @GetMapping("/unread-count")
    public ResponseResult<Integer> getUnreadCount() {
        int count = noticeService.getUnreadCount();
        return ResponseResult.ok(count);
    }

    /**
     * 全部标记已读
     */
    @Operation(summary = "全部标记已读")
    @PostMapping("/read-all")
    public ResponseResult<Void> markAllAsRead() {
        noticeService.markAllAsRead();
        return ResponseResult.ok();
    }
}
