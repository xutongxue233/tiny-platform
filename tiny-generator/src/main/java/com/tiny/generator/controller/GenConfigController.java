package com.tiny.generator.controller;

import com.tiny.core.web.ResponseResult;
import com.tiny.generator.entity.GenConfig;
import com.tiny.generator.service.GenConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.enums.OperationType;

import java.util.List;

/**
 * 代码生成配置Controller
 */
@Tag(name = "代码生成配置")
@RestController
@RequestMapping("/gen/config")
@RequiredArgsConstructor
public class GenConfigController {

    private final GenConfigService genConfigService;

    @Operation(summary = "获取所有配置")
    @GetMapping("/list")
    public ResponseResult<List<GenConfig>> list() {
        List<GenConfig> list = genConfigService.listAll();
        return ResponseResult.ok(list);
    }

    @Operation(summary = "获取单个配置")
    @GetMapping("/{configKey}")
    public ResponseResult<String> getConfig(@PathVariable String configKey) {
        String value = genConfigService.getConfigValue(configKey);
        return ResponseResult.ok(value);
    }

    @Operation(summary = "更新单个配置")
    @OperationLog(module = "生成配置", type = OperationType.UPDATE, desc = "更新单个配置")
    @PutMapping("/{configKey}")
    public ResponseResult<Void> updateConfig(@PathVariable String configKey,
                                             @RequestParam String configValue) {
        genConfigService.updateConfig(configKey, configValue);
        return ResponseResult.ok();
    }

    @Operation(summary = "批量更新配置")
    @OperationLog(module = "生成配置", type = OperationType.UPDATE, desc = "批量更新配置")
    @PutMapping("/batch")
    public ResponseResult<Void> batchUpdate(@RequestBody List<GenConfig> configs) {
        genConfigService.batchUpdateConfig(configs);
        return ResponseResult.ok();
    }
}
