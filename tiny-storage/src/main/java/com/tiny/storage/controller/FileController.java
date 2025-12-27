package com.tiny.storage.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.io.IoUtil;
import com.tiny.common.core.page.PageResult;
import com.tiny.core.web.ResponseResult;
import com.tiny.storage.dto.FileRecordQueryDTO;
import com.tiny.storage.service.FileRecordService;
import com.tiny.storage.vo.FileRecordVO;
import com.tiny.storage.vo.FileUploadVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.enums.OperationType;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 文件管理Controller
 */
@Slf4j
@Tag(name = "文件管理")
@RestController
@RequestMapping("/storage/file")
@RequiredArgsConstructor
public class FileController {

    private final FileRecordService fileRecordService;

    @Operation(summary = "分页查询文件记录")
    @SaCheckPermission("storage:file:list")
    @GetMapping("/page")
    public ResponseResult<PageResult<FileRecordVO>> page(FileRecordQueryDTO queryDTO) {
        return ResponseResult.ok(fileRecordService.page(queryDTO));
    }

    @Operation(summary = "获取文件详情")
    @SaCheckPermission("storage:file:query")
    @GetMapping("/{fileId}")
    public ResponseResult<FileRecordVO> getById(@PathVariable Long fileId) {
        return ResponseResult.ok(fileRecordService.getById(fileId));
    }

    @Operation(summary = "上传文件")
    @SaCheckPermission("storage:file:upload")
    @OperationLog(module = "文件管理", type = OperationType.INSERT, desc = "上传文件")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult<FileUploadVO> upload(
            @Parameter(description = "文件") @RequestPart("file") MultipartFile file,
            @Parameter(description = "存储路径") @RequestParam(required = false) String path) {
        return ResponseResult.ok(fileRecordService.upload(file, path));
    }

    @Operation(summary = "使用指定配置上传文件")
    @SaCheckPermission("storage:file:upload")
    @OperationLog(module = "文件管理", type = OperationType.INSERT, desc = "使用配置上传文件")
    @PostMapping(value = "/upload/{configId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult<FileUploadVO> uploadWithConfig(
            @Parameter(description = "文件") @RequestPart("file") MultipartFile file,
            @Parameter(description = "存储配置ID") @PathVariable Long configId) {
        return ResponseResult.ok(fileRecordService.uploadWithConfig(file, configId));
    }

    @Operation(summary = "批量上传文件")
    @SaCheckPermission("storage:file:upload")
    @OperationLog(module = "文件管理", type = OperationType.INSERT, desc = "批量上传文件")
    @PostMapping(value = "/upload/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult<List<FileUploadVO>> uploadBatch(
            @Parameter(description = "文件列表") @RequestPart("files") MultipartFile[] files) {
        return ResponseResult.ok(fileRecordService.uploadBatch(files));
    }

    @Operation(summary = "下载文件")
    @SaCheckPermission("storage:file:download")
    @GetMapping("/download/{fileId}")
    public void download(@PathVariable Long fileId, HttpServletResponse response) {
        try {
            FileRecordVO record = fileRecordService.getById(fileId);
            InputStream inputStream = fileRecordService.download(fileId);

            // 设置响应头
            response.setContentType(record.getFileType());
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(record.getOriginalFilename(), StandardCharsets.UTF_8));

            // 写入响应流
            IoUtil.copy(inputStream, response.getOutputStream());
            IoUtil.close(inputStream);
        } catch (Exception e) {
            log.error("文件下载失败: {}", e.getMessage(), e);
            throw new RuntimeException("文件下载失败");
        }
    }

    @Operation(summary = "删除文件")
    @SaCheckPermission("storage:file:delete")
    @OperationLog(module = "文件管理", type = OperationType.DELETE, desc = "删除文件")
    @DeleteMapping("/{fileId}")
    public ResponseResult<Void> delete(@PathVariable Long fileId) {
        fileRecordService.delete(fileId);
        return ResponseResult.ok();
    }

    @Operation(summary = "批量删除文件")
    @SaCheckPermission("storage:file:delete")
    @OperationLog(module = "文件管理", type = OperationType.DELETE, desc = "批量删除文件")
    @DeleteMapping("/batch")
    public ResponseResult<Void> deleteBatch(@RequestBody List<Long> fileIds) {
        fileRecordService.deleteBatch(fileIds);
        return ResponseResult.ok();
    }

    @Operation(summary = "获取文件访问URL")
    @GetMapping("/url/{fileId}")
    public ResponseResult<String> getUrl(@PathVariable Long fileId) {
        return ResponseResult.ok(fileRecordService.getUrl(fileId));
    }

    @Operation(summary = "获取文件临时访问URL")
    @GetMapping("/temp-url/{fileId}")
    public ResponseResult<String> getTempUrl(
            @PathVariable Long fileId,
            @Parameter(description = "过期时间（秒）") @RequestParam(defaultValue = "3600") long expireTime) {
        return ResponseResult.ok(fileRecordService.getTempUrl(fileId, expireTime));
    }

    @Operation(summary = "预览文件（无需权限）")
    @GetMapping("/preview/{fileId}")
    public void preview(@PathVariable Long fileId, HttpServletResponse response) {
        try {
            FileRecordVO record = fileRecordService.getById(fileId);
            InputStream inputStream = fileRecordService.download(fileId);

            // 设置响应头（内联显示，不下载）
            response.setContentType(record.getFileType());
            response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(record.getOriginalFilename(), StandardCharsets.UTF_8));

            // 写入响应流
            IoUtil.copy(inputStream, response.getOutputStream());
            IoUtil.close(inputStream);
        } catch (Exception e) {
            log.error("文件预览失败: {}", e.getMessage(), e);
            throw new RuntimeException("文件预览失败");
        }
    }
}
