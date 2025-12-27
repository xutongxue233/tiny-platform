package com.tiny.export.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.core.web.ResponseResult;
import com.tiny.export.dto.user.SysUserExportDTO;
import com.tiny.export.dto.user.SysUserImportDTO;
import com.tiny.export.excel.handler.user.SysUserExportHandler;
import com.tiny.export.excel.listener.user.SysUserImportListener;
import com.tiny.export.service.ExportService;
import com.tiny.export.service.ImportService;
import com.tiny.export.vo.ImportResultVO;
import com.tiny.system.dto.SysUserQueryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 用户导入导出Controller
 */
@Tag(name = "用户导入导出")
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class SysUserExportController {

    private final ExportService exportService;
    private final ImportService importService;
    private final SysUserExportHandler userExportHandler;
    private final ApplicationContext applicationContext;

    @Operation(summary = "导出用户列表")
    @OperationLog(module = "用户管理", type = OperationType.EXPORT, desc = "导出用户列表")
    @SaCheckPermission("system:user:export")
    @PostMapping("/export")
    public void export(@RequestBody(required = false) SysUserQueryDTO queryDTO,
                       HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("用户数据", StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        exportService.exportSync(userExportHandler, queryDTO, response.getOutputStream());
    }

    @Operation(summary = "异步导出用户列表")
    @OperationLog(module = "用户管理", type = OperationType.EXPORT, desc = "异步导出用户列表")
    @SaCheckPermission("system:user:export")
    @PostMapping("/exportAsync")
    public ResponseResult<Long> exportAsync(@RequestBody(required = false) SysUserQueryDTO queryDTO) {
        Long taskId = exportService.exportAsync(userExportHandler, queryDTO,
                "用户数据导出", "sys_user");
        return ResponseResult.ok(taskId);
    }

    @Operation(summary = "下载用户导入模板")
    @SaCheckPermission("system:user:import")
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("用户导入模板", StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 提供示例数据
        List<SysUserImportDTO> sampleData = List.of(
                createSampleUser("zhangsan", "张三", "13800138001", "zhangsan@example.com", "男"),
                createSampleUser("lisi", "李四", "13800138002", "lisi@example.com", "女")
        );

        exportService.downloadTemplate(SysUserImportDTO.class, sampleData, response.getOutputStream());
    }

    @Operation(summary = "导入用户数据")
    @OperationLog(module = "用户管理", type = OperationType.IMPORT, desc = "导入用户数据")
    @SaCheckPermission("system:user:import")
    @PostMapping("/import")
    public ResponseResult<ImportResultVO> importData(@RequestParam("file") MultipartFile file) {
        SysUserImportListener listener = applicationContext.getBean(SysUserImportListener.class);
        return ResponseResult.ok(ImportResultVO.from(importService.importData(file, listener)));
    }

    private SysUserImportDTO createSampleUser(String username, String realName,
                                               String phone, String email, String gender) {
        SysUserImportDTO dto = new SysUserImportDTO();
        dto.setUsername(username);
        dto.setRealName(realName);
        dto.setPhone(phone);
        dto.setEmail(email);
        dto.setGender(gender);
        dto.setPassword("123456");
        return dto;
    }
}
