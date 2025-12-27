package com.tiny.generator.controller;

import com.tiny.core.web.ResponseResult;
import com.tiny.generator.entity.GenTable;
import com.tiny.generator.service.CodeGeneratorService;
import com.tiny.generator.service.GenTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 代码生成Controller
 */
@Tag(name = "代码生成")
@RestController
@RequestMapping("/gen")
@RequiredArgsConstructor
public class GenController {

    private final GenTableService genTableService;
private final CodeGeneratorService codeGeneratorService;

    @Operation(summary = "查询代码生成表列表")
    @GetMapping("/list")
    public ResponseResult<List<GenTable>> list(GenTable query) {
        List<GenTable> list = genTableService.list();
        return ResponseResult.ok(list);
    }

    @Operation(summary = "查询数据库表列表")
    @GetMapping("/db/list")
    public ResponseResult<List<GenTable>> dbTableList(@RequestParam(required = false) String tableName) {
        List<GenTable> list = genTableService.selectDbTableList(tableName);
        return ResponseResult.ok(list);
    }

    @Operation(summary = "获取表配置详情")
    @GetMapping("/{tableId}")
    public ResponseResult<GenTable> getInfo(@PathVariable Long tableId) {
        GenTable table = genTableService.getTableWithColumns(tableId);
        return ResponseResult.ok(table);
    }

    @Operation(summary = "导入表结构")
    @OperationLog(module = "代码生成", type = OperationType.IMPORT, desc = "导入表结构")
    @PostMapping("/import")
    public ResponseResult<Void> importTable(@RequestBody List<String> tableNames) {
        genTableService.importTable(tableNames);
        return ResponseResult.ok();
    }

    @Operation(summary = "更新表配置")
    @OperationLog(module = "代码生成", type = OperationType.UPDATE, desc = "更新表配置")
    @PutMapping
    public ResponseResult<Void> update(@RequestBody GenTable table) {
        genTableService.updateGenTable(table);
        return ResponseResult.ok();
    }

    @Operation(summary = "删除表配置")
    @OperationLog(module = "代码生成", type = OperationType.DELETE, desc = "删除表配置")
    @DeleteMapping("/{tableIds}")
    public ResponseResult<Void> delete(@PathVariable List<Long> tableIds) {
        genTableService.deleteByIds(tableIds);
        return ResponseResult.ok();
    }

    @Operation(summary = "同步数据库表结构")
    @OperationLog(module = "代码生成", type = OperationType.OTHER, desc = "同步数据库表结构")
    @PostMapping("/sync/{tableId}")
    public ResponseResult<Void> syncTable(@PathVariable Long tableId) {
        codeGeneratorService.syncTable(tableId);
        return ResponseResult.ok();
    }

    @Operation(summary = "重新生成表配置")
    @OperationLog(module = "代码生成", type = OperationType.OTHER, desc = "重新生成表配置")
    @PostMapping("/regenerate/{tableId}")
    public ResponseResult<Void> regenerateConfig(@PathVariable Long tableId) {
        genTableService.regenerateConfig(tableId);
        return ResponseResult.ok();
    }

    @Operation(summary = "批量重新生成表配置")
    @OperationLog(module = "代码生成", type = OperationType.OTHER, desc = "批量重新生成表配置")
    @PostMapping("/batchRegenerate")
    public ResponseResult<Void> batchRegenerateConfig(@RequestBody List<Long> tableIds) {
        genTableService.batchRegenerateConfig(tableIds);
        return ResponseResult.ok();
    }

    @Operation(summary = "批量同步数据库表结构")
    @OperationLog(module = "代码生成", type = OperationType.OTHER, desc = "批量同步数据库表结构")
    @PostMapping("/batchSync")
    public ResponseResult<Void> batchSyncTable(@RequestBody List<Long> tableIds) {
        for (Long tableId : tableIds) {
            codeGeneratorService.syncTable(tableId);
        }
        return ResponseResult.ok();
    }

    @Operation(summary = "预览代码")
    @GetMapping("/preview/{tableId}")
    public ResponseResult<Map<String, String>> preview(@PathVariable Long tableId) {
        Map<String, String> codes = codeGeneratorService.preview(tableId);
        return ResponseResult.ok(codes);
    }

    @Operation(summary = "生成代码下载")
    @GetMapping("/download/{tableId}")
    public void download(@PathVariable Long tableId, HttpServletResponse response) throws IOException {
        byte[] data = codeGeneratorService.generate(tableId);
        genZip(response, data);
    }

    @Operation(summary = "批量生成代码下载")
    @GetMapping("/batchDownload")
    public void batchDownload(@RequestParam Long[] tableIds, HttpServletResponse response) throws IOException {
        byte[] data = codeGeneratorService.batchGenerate(tableIds);
        genZip(response, data);
    }

    @Operation(summary = "生成代码到路径")
    @OperationLog(module = "代码生成", type = OperationType.OTHER, desc = "生成代码到路径")
    @PostMapping("/genToPath/{tableId}")
    public ResponseResult<Void> genToPath(@PathVariable Long tableId,
                                          @RequestParam(required = false) String path) {
        codeGeneratorService.generateToPath(tableId, path);
        return ResponseResult.ok();
    }

    /**
     * 生成ZIP文件
     */
    private void genZip(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"code.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        response.getOutputStream().write(data);
    }
}
