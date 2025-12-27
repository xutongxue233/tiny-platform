package com.tiny.export.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.annotation.OperationLog.OperationType;
import com.tiny.core.web.ResponseResult;
import com.tiny.export.dto.dict.SysDictItemExportDTO;
import com.tiny.export.dto.dict.SysDictItemImportDTO;
import com.tiny.export.excel.handler.dict.SysDictItemExportHandler;
import com.tiny.export.excel.listener.dict.SysDictItemImportListener;
import com.tiny.export.service.ExportService;
import com.tiny.export.service.ImportService;
import com.tiny.export.vo.ImportResultVO;
import com.tiny.system.dto.SysDictItemQueryDTO;
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
 * 字典项导入导出Controller
 */
@Tag(name = "字典导入导出")
@RestController
@RequestMapping("/system/dictItem")
@RequiredArgsConstructor
public class SysDictItemExportController {

    private final ExportService exportService;
    private final ImportService importService;
    private final SysDictItemExportHandler dictItemExportHandler;
    private final ApplicationContext applicationContext;

    @Operation(summary = "导出字典数据")
    @OperationLog(module = "字典管理", type = OperationType.EXPORT, desc = "导出字典数据")
    @SaCheckPermission("system:dict:export")
    @PostMapping("/export")
    public void export(@RequestBody(required = false) SysDictItemQueryDTO queryDTO,
                       HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("字典数据", StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        exportService.exportSync(dictItemExportHandler, queryDTO, response.getOutputStream());
    }

    @Operation(summary = "下载字典导入模板")
    @SaCheckPermission("system:dict:import")
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("字典导入模板", StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 提供示例数据
        List<SysDictItemImportDTO> sampleData = List.of(
                createSampleDictItem("sys_user_sex", "男", "0", 1),
                createSampleDictItem("sys_user_sex", "女", "1", 2),
                createSampleDictItem("sys_user_sex", "未知", "2", 3)
        );

        exportService.downloadTemplate(SysDictItemImportDTO.class, sampleData, response.getOutputStream());
    }

    @Operation(summary = "导入字典数据")
    @OperationLog(module = "字典管理", type = OperationType.IMPORT, desc = "导入字典数据")
    @SaCheckPermission("system:dict:import")
    @PostMapping("/import")
    public ResponseResult<ImportResultVO> importData(@RequestParam("file") MultipartFile file) {
        SysDictItemImportListener listener = applicationContext.getBean(SysDictItemImportListener.class);
        return ResponseResult.ok(ImportResultVO.from(importService.importData(file, listener)));
    }

    private SysDictItemImportDTO createSampleDictItem(String dictCode, String label,
                                                       String value, Integer sort) {
        SysDictItemImportDTO dto = new SysDictItemImportDTO();
        dto.setDictCode(dictCode);
        dto.setItemLabel(label);
        dto.setItemValue(value);
        dto.setItemSort(sort);
        return dto;
    }
}
