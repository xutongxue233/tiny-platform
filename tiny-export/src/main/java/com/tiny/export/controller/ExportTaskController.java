package com.tiny.export.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.common.core.dto.DeleteDTO;
import com.tiny.common.core.page.PageResult;
import com.tiny.core.web.ResponseResult;
import com.tiny.export.dto.ExportTaskQueryDTO;
import com.tiny.export.service.ExportTaskService;
import com.tiny.export.vo.ExportTaskVO;
import com.tiny.storage.factory.StorageFactory;
import com.tiny.storage.service.storage.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 导出任务管理Controller
 */
@Tag(name = "导出任务管理")
@RestController
@RequestMapping("/export/task")
@RequiredArgsConstructor
public class ExportTaskController {

    private final ExportTaskService exportTaskService;
    private final StorageFactory storageFactory;

    @Operation(summary = "分页查询导出任务")
    @SaCheckPermission("system:export:list")
    @PostMapping("/page")
    public ResponseResult<PageResult<ExportTaskVO>> page(@RequestBody ExportTaskQueryDTO queryDTO) {
        return ResponseResult.ok(exportTaskService.page(queryDTO));
    }

    @Operation(summary = "获取任务详情")
    @SaCheckPermission("system:export:query")
    @GetMapping("/{taskId}")
    public ResponseResult<ExportTaskVO> getDetail(@PathVariable Long taskId) {
        return ResponseResult.ok(exportTaskService.getTaskDetail(taskId));
    }

    @Operation(summary = "下载导出文件")
    @SaCheckPermission("system:export:download")
    @GetMapping("/download/{taskId}")
    public void download(@PathVariable Long taskId, HttpServletResponse response) throws Exception {
        ExportTaskVO task = exportTaskService.getTaskDetail(taskId);

        if (task.getFileUrl() == null) {
            throw new RuntimeException("文件不存在");
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(task.getFileName(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);

        // 从存储服务下载文件
        // fileUrl格式如: /api/file/export/xxx.xlsx，需要提取存储路径: export/xxx.xlsx
        StorageService storageService = storageFactory.getDefaultStorage();
        String fileUrl = task.getFileUrl();
        // 移除URL前缀，获取实际存储路径
        String filePath = fileUrl;
        if (fileUrl.contains("/api/file/")) {
            filePath = fileUrl.substring(fileUrl.indexOf("/api/file/") + "/api/file/".length());
        }

        try (InputStream inputStream = storageService.download(filePath);
             OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }
    }

    @Operation(summary = "删除导出任务")
    @OperationLog(module = "导出任务", type = OperationType.DELETE, desc = "删除导出任务")
    @SaCheckPermission("system:export:remove")
    @DeleteMapping("/{taskId}")
    public ResponseResult<Void> delete(@PathVariable Long taskId) {
        exportTaskService.deleteTask(taskId);
        return ResponseResult.ok();
    }

    @Operation(summary = "批量删除导出任务")
    @OperationLog(module = "导出任务", type = OperationType.DELETE, desc = "批量删除导出任务")
    @SaCheckPermission("system:export:remove")
    @DeleteMapping("/batch")
    public ResponseResult<Void> deleteBatch(@Valid @RequestBody DeleteDTO dto) {
        exportTaskService.deleteBatch(dto.getIds());
        return ResponseResult.ok();
    }

    @Operation(summary = "清理过期任务")
    @OperationLog(module = "导出任务", type = OperationType.DELETE, desc = "清理过期任务")
    @SaCheckPermission("system:export:clean")
    @DeleteMapping("/clean")
    public ResponseResult<Integer> cleanExpired() {
        int count = exportTaskService.cleanExpiredTasks();
        return ResponseResult.ok(count);
    }
}
