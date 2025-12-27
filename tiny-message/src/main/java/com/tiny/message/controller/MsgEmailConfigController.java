package com.tiny.message.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.core.web.ResponseResult;
import com.tiny.message.dto.MsgEmailConfigDTO;
import com.tiny.message.service.MsgEmailConfigService;
import com.tiny.message.vo.MsgEmailConfigVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 邮件配置控制器
 */
@Tag(name = "邮件配置管理", description = "邮件配置的增删改查等接口")
@RestController
@RequestMapping("/message/email")
@RequiredArgsConstructor
public class MsgEmailConfigController {

    private final MsgEmailConfigService emailConfigService;

    /**
     * 查询所有邮件配置
     */
    @Operation(summary = "查询所有邮件配置")
    @SaCheckPermission("message:email:list")
    @GetMapping("/list")
    public ResponseResult<List<MsgEmailConfigVO>> list() {
        List<MsgEmailConfigVO> list = emailConfigService.listAll();
        return ResponseResult.ok(list);
    }

    /**
     * 查询邮件配置详情
     */
    @Operation(summary = "查询邮件配置详情")
    @SaCheckPermission("message:email:query")
    @GetMapping("/{configId}")
    public ResponseResult<MsgEmailConfigVO> getById(@Parameter(description = "配置ID") @PathVariable Long configId) {
        MsgEmailConfigVO config = emailConfigService.getDetail(configId);
        return ResponseResult.ok(config);
    }

    /**
     * 新增邮件配置
     */
    @Operation(summary = "新增邮件配置")
    @OperationLog(module = "邮件配置", type = OperationType.INSERT, desc = "新增邮件配置", excludeParams = {"password"})
    @SaCheckPermission("message:email:add")
    @PostMapping
    public ResponseResult<Void> add(@Valid @RequestBody MsgEmailConfigDTO dto) {
        emailConfigService.add(dto);
        return ResponseResult.ok();
    }

    /**
     * 修改邮件配置
     */
    @Operation(summary = "修改邮件配置")
    @OperationLog(module = "邮件配置", type = OperationType.UPDATE, desc = "修改邮件配置", excludeParams = {"password"})
    @SaCheckPermission("message:email:edit")
    @PutMapping
    public ResponseResult<Void> update(@Valid @RequestBody MsgEmailConfigDTO dto) {
        emailConfigService.update(dto);
        return ResponseResult.ok();
    }

    /**
     * 删除邮件配置
     */
    @Operation(summary = "删除邮件配置")
    @OperationLog(module = "邮件配置", type = OperationType.DELETE, desc = "删除邮件配置")
    @SaCheckPermission("message:email:remove")
    @DeleteMapping("/{configId}")
    public ResponseResult<Void> delete(@Parameter(description = "配置ID") @PathVariable Long configId) {
        emailConfigService.delete(configId);
        return ResponseResult.ok();
    }

    /**
     * 设置为默认配置
     */
    @Operation(summary = "设置为默认配置")
    @OperationLog(module = "邮件配置", type = OperationType.UPDATE, desc = "设置默认邮件配置")
    @SaCheckPermission("message:email:edit")
    @PutMapping("/default/{configId}")
    public ResponseResult<Void> setDefault(@Parameter(description = "配置ID") @PathVariable Long configId) {
        emailConfigService.setDefault(configId);
        return ResponseResult.ok();
    }

    /**
     * 测试连接
     */
    @Operation(summary = "测试连接")
    @SaCheckPermission("message:email:test")
    @PostMapping("/test/{configId}")
    public ResponseResult<Boolean> testConnection(@Parameter(description = "配置ID") @PathVariable Long configId) {
        boolean result = emailConfigService.testConnection(configId);
        return ResponseResult.ok(result);
    }
}
