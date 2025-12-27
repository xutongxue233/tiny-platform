package com.tiny.message.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.common.core.model.DeleteDTO;
import com.tiny.common.core.page.PageResult;
import com.tiny.core.web.ResponseResult;
import com.tiny.message.dto.MsgTemplateDTO;
import com.tiny.message.dto.MsgTemplateQueryDTO;
import com.tiny.message.dto.UpdateStatusDTO;
import com.tiny.message.service.MsgTemplateService;
import com.tiny.message.vo.MsgTemplateVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 消息模板控制器
 */
@Tag(name = "消息模板管理", description = "消息模板的增删改查等接口")
@RestController
@RequestMapping("/message/template")
@RequiredArgsConstructor
public class MsgTemplateController {

    private final MsgTemplateService templateService;

    /**
     * 分页查询消息模板列表
     */
    @Operation(summary = "分页查询消息模板列表")
    @SaCheckPermission("message:template:list")
    @PostMapping("/page")
    public ResponseResult<PageResult<MsgTemplateVO>> page(@Valid @RequestBody MsgTemplateQueryDTO queryDTO) {
        PageResult<MsgTemplateVO> result = templateService.page(queryDTO);
        return ResponseResult.ok(result);
    }

    /**
     * 查询消息模板详情
     */
    @Operation(summary = "查询消息模板详情")
    @SaCheckPermission("message:template:query")
    @GetMapping("/{templateId}")
    public ResponseResult<MsgTemplateVO> getById(@Parameter(description = "模板ID") @PathVariable Long templateId) {
        MsgTemplateVO template = templateService.getDetail(templateId);
        return ResponseResult.ok(template);
    }

    /**
     * 新增消息模板
     */
    @Operation(summary = "新增消息模板")
    @OperationLog(module = "消息模板", type = OperationType.INSERT, desc = "新增消息模板")
    @SaCheckPermission("message:template:add")
    @PostMapping
    public ResponseResult<Void> add(@Valid @RequestBody MsgTemplateDTO dto) {
        templateService.add(dto);
        return ResponseResult.ok();
    }

    /**
     * 修改消息模板
     */
    @Operation(summary = "修改消息模板")
    @OperationLog(module = "消息模板", type = OperationType.UPDATE, desc = "修改消息模板")
    @SaCheckPermission("message:template:edit")
    @PutMapping
    public ResponseResult<Void> update(@Valid @RequestBody MsgTemplateDTO dto) {
        templateService.update(dto);
        return ResponseResult.ok();
    }

    /**
     * 删除消息模板
     */
    @Operation(summary = "删除消息模板")
    @OperationLog(module = "消息模板", type = OperationType.DELETE, desc = "删除消息模板")
    @SaCheckPermission("message:template:remove")
    @DeleteMapping("/{templateId}")
    public ResponseResult<Void> delete(@Parameter(description = "模板ID") @PathVariable Long templateId) {
        templateService.delete(templateId);
        return ResponseResult.ok();
    }

    /**
     * 批量删除消息模板
     */
    @Operation(summary = "批量删除消息模板")
    @OperationLog(module = "消息模板", type = OperationType.DELETE, desc = "批量删除消息模板")
    @SaCheckPermission("message:template:remove")
    @DeleteMapping("/batch")
    public ResponseResult<Void> deleteBatch(@Valid @RequestBody DeleteDTO dto) {
        templateService.deleteBatch(dto.getIds());
        return ResponseResult.ok();
    }

    /**
     * 修改消息模板状态
     */
    @Operation(summary = "修改消息模板状态")
    @OperationLog(module = "消息模板", type = OperationType.UPDATE, desc = "修改消息模板状态")
    @SaCheckPermission("message:template:edit")
    @PutMapping("/status")
    public ResponseResult<Void> updateStatus(@Valid @RequestBody UpdateStatusDTO dto) {
        templateService.updateStatus(dto.getId(), dto.getStatus());
        return ResponseResult.ok();
    }

    /**
     * 获取所有启用的模板列表（用于下拉选择）
     */
    @Operation(summary = "获取模板下拉列表")
    @SaCheckPermission("message:list:send")
    @GetMapping("/list")
    public ResponseResult<java.util.List<MsgTemplateVO>> listAll() {
        java.util.List<MsgTemplateVO> list = templateService.listAll();
        return ResponseResult.ok(list);
    }
}
