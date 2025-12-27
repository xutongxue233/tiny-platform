package com.tiny.storage.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.core.page.PageResult;
import com.tiny.core.web.ResponseResult;
import com.tiny.storage.dto.StorageConfigDTO;
import com.tiny.storage.dto.StorageConfigQueryDTO;
import com.tiny.storage.enums.StorageTypeEnum;
import com.tiny.storage.service.StorageConfigService;
import com.tiny.storage.vo.StorageConfigVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.enums.OperationType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 存储配置Controller
 */
@Tag(name = "存储配置管理")
@RestController
@RequestMapping("/storage/config")
@RequiredArgsConstructor
public class StorageConfigController {

    private final StorageConfigService storageConfigService;

    @Operation(summary = "分页查询存储配置")
    @SaCheckPermission("storage:config:list")
    @GetMapping("/page")
    public ResponseResult<PageResult<StorageConfigVO>> page(StorageConfigQueryDTO queryDTO) {
        return ResponseResult.ok(storageConfigService.page(queryDTO));
    }

    @Operation(summary = "查询所有存储配置")
    @SaCheckPermission("storage:config:list")
    @GetMapping("/list")
    public ResponseResult<List<StorageConfigVO>> list() {
        return ResponseResult.ok(storageConfigService.list());
    }

    @Operation(summary = "获取存储配置详情")
    @SaCheckPermission("storage:config:query")
    @GetMapping("/{configId}")
    public ResponseResult<StorageConfigVO> getById(@PathVariable Long configId) {
        return ResponseResult.ok(storageConfigService.getById(configId));
    }

    @Operation(summary = "新增存储配置")
    @SaCheckPermission("storage:config:add")
    @OperationLog(module = "存储配置", type = OperationType.INSERT, desc = "新增存储配置")
    @PostMapping
    public ResponseResult<Void> add(@Valid @RequestBody StorageConfigDTO dto) {
        storageConfigService.add(dto);
        return ResponseResult.ok();
    }

    @Operation(summary = "修改存储配置")
    @SaCheckPermission("storage:config:edit")
    @OperationLog(module = "存储配置", type = OperationType.UPDATE, desc = "修改存储配置")
    @PutMapping
    public ResponseResult<Void> update(@Valid @RequestBody StorageConfigDTO dto) {
        storageConfigService.update(dto);
        return ResponseResult.ok();
    }

    @Operation(summary = "删除存储配置")
    @SaCheckPermission("storage:config:delete")
    @OperationLog(module = "存储配置", type = OperationType.DELETE, desc = "删除存储配置")
    @DeleteMapping("/{configId}")
    public ResponseResult<Void> delete(@PathVariable Long configId) {
        storageConfigService.delete(configId);
        return ResponseResult.ok();
    }

    @Operation(summary = "设置默认存储配置")
    @SaCheckPermission("storage:config:edit")
    @OperationLog(module = "存储配置", type = OperationType.UPDATE, desc = "设置默认存储配置")
    @PutMapping("/default/{configId}")
    public ResponseResult<Void> setDefault(@PathVariable Long configId) {
        storageConfigService.setDefault(configId);
        return ResponseResult.ok();
    }

    @Operation(summary = "测试存储配置连接")
    @SaCheckPermission("storage:config:edit")
    @PostMapping("/test")
    public ResponseResult<Boolean> testConnection(@RequestBody StorageConfigDTO dto) {
        return ResponseResult.ok(storageConfigService.testConnection(dto));
    }

    @Operation(summary = "刷新存储服务")
    @SaCheckPermission("storage:config:edit")
    @OperationLog(module = "存储配置", type = OperationType.OTHER, desc = "刷新存储服务")
    @PutMapping("/refresh/{configId}")
    public ResponseResult<Void> refresh(@PathVariable Long configId) {
        storageConfigService.refreshStorage(configId);
        return ResponseResult.ok();
    }

    @Operation(summary = "获取存储类型列表")
    @GetMapping("/types")
    public ResponseResult<List<Map<String, String>>> getStorageTypes() {
        List<Map<String, String>> types = Arrays.stream(StorageTypeEnum.values())
                .map(type -> Map.of("code", type.getCode(), "desc", type.getDesc()))
                .collect(Collectors.toList());
        return ResponseResult.ok(types);
    }
}
