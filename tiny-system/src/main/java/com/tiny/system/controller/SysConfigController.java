package com.tiny.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.common.core.model.DeleteDTO;
import com.tiny.common.core.page.PageResult;
import com.tiny.core.web.ResponseResult;
import com.tiny.system.dto.SysConfigDTO;
import com.tiny.system.dto.SysConfigQueryDTO;
import com.tiny.system.dto.UpdateStatusDTO;
import com.tiny.system.service.SysConfigService;
import com.tiny.system.vo.SysConfigVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统参数配置控制器
 */
@Tag(name = "系统参数配置管理", description = "系统参数配置的增删改查等接口")
@RestController
@RequestMapping("/system/config")
@RequiredArgsConstructor
public class SysConfigController {

    private final SysConfigService configService;

    /**
     * 获取公开配置（无需登录）
     */
    @Operation(summary = "获取公开配置", description = "无需登录即可获取的系统公开配置")
    @GetMapping("/public")
    public ResponseResult<Map<String, Object>> getPublicConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("appName", configService.getConfigValue("sys.app.name"));
        config.put("appVersion", configService.getConfigValue("sys.app.version"));
        config.put("captchaEnabled", configService.getConfigBoolean("sys.captcha.enabled"));
        config.put("registerEnabled", configService.getConfigBoolean("sys.account.registerEnabled"));
        return ResponseResult.ok(config);
    }

    /**
     * 分页查询参数配置列表
     */
    @Operation(summary = "分页查询参数配置列表")
    @SaCheckPermission("system:config:list")
    @PostMapping("/page")
    public ResponseResult<PageResult<SysConfigVO>> page(@Valid @RequestBody SysConfigQueryDTO queryDTO) {
        PageResult<SysConfigVO> result = configService.page(queryDTO);
        return ResponseResult.ok(result);
    }

    /**
     * 查询所有参数配置列表
     */
    @Operation(summary = "查询所有参数配置列表")
    @SaCheckPermission("system:config:list")
    @GetMapping("/list")
    public ResponseResult<List<SysConfigVO>> list() {
        List<SysConfigVO> list = configService.listAll();
        return ResponseResult.ok(list);
    }

    /**
     * 根据分组查询参数配置列表
     */
    @Operation(summary = "根据分组查询参数配置列表")
    @SaCheckPermission("system:config:list")
    @GetMapping("/list/{configGroup}")
    public ResponseResult<List<SysConfigVO>> listByGroup(@Parameter(description = "参数分组") @PathVariable String configGroup) {
        List<SysConfigVO> list = configService.listByGroup(configGroup);
        return ResponseResult.ok(list);
    }

    /**
     * 查询参数配置详情
     */
    @Operation(summary = "查询参数配置详情")
    @SaCheckPermission("system:config:query")
    @GetMapping("/{configId}")
    public ResponseResult<SysConfigVO> getById(@Parameter(description = "参数配置ID") @PathVariable Long configId) {
        SysConfigVO config = configService.getDetail(configId);
        return ResponseResult.ok(config);
    }

    /**
     * 根据参数键名获取参数值
     */
    @Operation(summary = "根据参数键名获取参数值")
    @GetMapping("/value/{configKey}")
    public ResponseResult<String> getConfigValue(@Parameter(description = "参数键名") @PathVariable String configKey) {
        String value = configService.getConfigValue(configKey);
        return ResponseResult.ok(value);
    }

    /**
     * 新增参数配置
     */
    @Operation(summary = "新增参数配置")
    @OperationLog(module = "参数配置", type = OperationType.INSERT, desc = "新增参数配置")
    @SaCheckPermission("system:config:add")
    @PostMapping
    public ResponseResult<Void> add(@Valid @RequestBody SysConfigDTO dto) {
        configService.add(dto);
        return ResponseResult.ok();
    }

    /**
     * 修改参数配置
     */
    @Operation(summary = "修改参数配置")
    @OperationLog(module = "参数配置", type = OperationType.UPDATE, desc = "修改参数配置")
    @SaCheckPermission("system:config:edit")
    @PutMapping
    public ResponseResult<Void> update(@Valid @RequestBody SysConfigDTO dto) {
        configService.update(dto);
        return ResponseResult.ok();
    }

    /**
     * 删除参数配置
     */
    @Operation(summary = "删除参数配置")
    @OperationLog(module = "参数配置", type = OperationType.DELETE, desc = "删除参数配置")
    @SaCheckPermission("system:config:remove")
    @DeleteMapping("/{configId}")
    public ResponseResult<Void> delete(@Parameter(description = "参数配置ID") @PathVariable Long configId) {
        configService.delete(configId);
        return ResponseResult.ok();
    }

    /**
     * 批量删除参数配置
     */
    @Operation(summary = "批量删除参数配置")
    @OperationLog(module = "参数配置", type = OperationType.DELETE, desc = "批量删除参数配置")
    @SaCheckPermission("system:config:remove")
    @DeleteMapping("/batch")
    public ResponseResult<Void> deleteBatch(@Valid @RequestBody DeleteDTO dto) {
        configService.deleteBatch(dto.getIds());
        return ResponseResult.ok();
    }

    /**
     * 修改参数配置状态
     */
    @Operation(summary = "修改参数配置状态")
    @OperationLog(module = "参数配置", type = OperationType.UPDATE, desc = "修改参数配置状态")
    @SaCheckPermission("system:config:edit")
    @PutMapping("/status")
    public ResponseResult<Void> updateStatus(@Valid @RequestBody UpdateStatusDTO dto) {
        configService.updateStatus(dto.getId(), dto.getStatus());
        return ResponseResult.ok();
    }

    /**
     * 刷新参数配置缓存
     */
    @Operation(summary = "刷新参数配置缓存")
    @OperationLog(module = "参数配置", type = OperationType.OTHER, desc = "刷新参数配置缓存")
    @SaCheckPermission("system:config:refresh")
    @PostMapping("/refreshCache")
    public ResponseResult<Void> refreshCache() {
        configService.refreshCache();
        return ResponseResult.ok();
    }
}
