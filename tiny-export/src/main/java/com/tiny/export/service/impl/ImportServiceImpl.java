package com.tiny.export.service.impl;

import com.alibaba.excel.EasyExcel;
import com.tiny.common.exception.BusinessException;
import com.tiny.export.dto.ImportResultDTO;
import com.tiny.export.entity.ExportTask;
import com.tiny.export.enums.ExportStatusEnum;
import com.tiny.export.enums.ExportTypeEnum;
import com.tiny.export.excel.listener.ExcelImportListener;
import com.tiny.export.service.ExportTaskService;
import com.tiny.export.service.ImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * 导入服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    private final ExportTaskService exportTaskService;

    @Override
    public <T> ImportResultDTO importData(MultipartFile file, ExcelImportListener<T> listener) {
        try (InputStream inputStream = file.getInputStream()) {
            EasyExcel.read(inputStream, listener.getDataClass(), listener)
                    .sheet()
                    .doRead();

            return ImportResultDTO.builder()
                    .totalCount(listener.getTotalCount())
                    .successCount(listener.getSuccessCount())
                    .failCount(listener.getFailCount())
                    .errorMessages(listener.getErrorMessages())
                    .build();
        } catch (IOException e) {
            throw new BusinessException("读取Excel文件失败: " + e.getMessage());
        }
    }

    @Override
    public <T> Long importDataAsync(MultipartFile file, ExcelImportListener<T> listener,
                                    String taskName, String businessType) {
        // 创建任务记录
        ExportTask task = new ExportTask();
        task.setTaskName(taskName);
        task.setTaskType(ExportTypeEnum.IMPORT.getCode());
        task.setBusinessType(businessType);
        task.setStatus(ExportStatusEnum.PENDING.getCode());
        task.setFileName(file.getOriginalFilename());
        task.setFileSize(file.getSize());
        task.setTotalCount(0);
        task.setSuccessCount(0);
        task.setFailCount(0);

        exportTaskService.save(task);

        // 将文件内容读入内存(用于异步处理)
        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new BusinessException("读取文件失败: " + e.getMessage());
        }

        // 异步执行导入
        doAsyncImport(fileBytes, listener, task.getTaskId());

        return task.getTaskId();
    }

    @Async("exportExecutor")
    public <T> void doAsyncImport(byte[] fileBytes, ExcelImportListener<T> listener, Long taskId) {
        ExportTask task = exportTaskService.getById(taskId);
        task.setStatus(ExportStatusEnum.PROCESSING.getCode());
        task.setStartTime(LocalDateTime.now());
        exportTaskService.updateById(task);

        try {
            EasyExcel.read(new java.io.ByteArrayInputStream(fileBytes), listener.getDataClass(), listener)
                    .sheet()
                    .doRead();

            task.setStatus(ExportStatusEnum.SUCCESS.getCode());
            task.setTotalCount(listener.getTotalCount());
            task.setSuccessCount(listener.getSuccessCount());
            task.setFailCount(listener.getFailCount());

            if (listener.getFailCount() > 0) {
                String errorMsg = String.join("\n", listener.getErrorMessages());
                // 截断错误信息，避免超长
                if (errorMsg.length() > 2000) {
                    errorMsg = errorMsg.substring(0, 2000) + "...";
                }
                task.setErrorMsg(errorMsg);
            }

        } catch (Exception e) {
            log.error("异步导入失败, taskId: {}", taskId, e);
            task.setStatus(ExportStatusEnum.FAILED.getCode());
            task.setErrorMsg(e.getMessage());
        } finally {
            task.setEndTime(LocalDateTime.now());
            exportTaskService.updateById(task);
        }
    }
}
