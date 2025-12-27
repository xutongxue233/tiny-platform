package com.tiny.export.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson2.JSON;
import com.tiny.common.exception.BusinessException;
import com.tiny.export.entity.ExportTask;
import com.tiny.export.enums.ExportStatusEnum;
import com.tiny.export.enums.ExportTypeEnum;
import com.tiny.export.excel.handler.ExcelExportHandler;
import com.tiny.export.service.ExportService;
import com.tiny.export.service.ExportTaskService;
import com.tiny.storage.factory.StorageFactory;
import com.tiny.storage.service.storage.StorageService;
import com.tiny.storage.vo.FileUploadVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 导出服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final ExportTaskService exportTaskService;
    private final StorageFactory storageFactory;

    /**
     * 同步导出阈值(超过此数量则强制使用异步)
     */
    private static final int SYNC_EXPORT_THRESHOLD = 5000;

    /**
     * 分批查询大小
     */
    private static final int BATCH_SIZE = 1000;

    /**
     * 文件过期天数
     */
    private static final int FILE_EXPIRE_DAYS = 7;

    @Override
    public <T, Q> void exportSync(ExcelExportHandler<T, Q> handler, Q params, OutputStream output) {
        // 查询数据
        List<T> dataList = handler.queryData(params);

        if (dataList.size() > SYNC_EXPORT_THRESHOLD) {
            throw new BusinessException("数据量超过" + SYNC_EXPORT_THRESHOLD +
                    "条,请使用异步导出");
        }

        // 写入Excel，不自动关闭流（由Controller控制）
        EasyExcel.write(output, handler.getDataClass())
                .autoCloseStream(false)
                .sheet(handler.getSheetName())
                .doWrite(dataList);
    }

    @Override
    public <T, Q> Long exportAsync(ExcelExportHandler<T, Q> handler, Q params,
                                   String taskName, String businessType) {
        // 创建任务记录
        ExportTask task = new ExportTask();
        task.setTaskName(taskName);
        task.setTaskType(ExportTypeEnum.EXPORT.getCode());
        task.setBusinessType(businessType);
        task.setStatus(ExportStatusEnum.PENDING.getCode());
        task.setQueryParams(JSON.toJSONString(params));
        task.setTotalCount(0);
        task.setSuccessCount(0);
        task.setFailCount(0);
        task.setExpireTime(LocalDateTime.now().plusDays(FILE_EXPIRE_DAYS));

        exportTaskService.save(task);

        // 异步执行导出
        doAsyncExport(handler, params, task.getTaskId());

        return task.getTaskId();
    }

    @Async("exportExecutor")
    public <T, Q> void doAsyncExport(ExcelExportHandler<T, Q> handler, Q params, Long taskId) {
        ExportTask task = exportTaskService.getById(taskId);
        task.setStatus(ExportStatusEnum.PROCESSING.getCode());
        task.setStartTime(LocalDateTime.now());
        exportTaskService.updateById(task);

        File tempFile = null;
        try {
            // 创建临时文件
            tempFile = Files.createTempFile("export_" + taskId, ".xlsx").toFile();

            // 分批写入Excel
            int totalCount = 0;
            try (ExcelWriter excelWriter = EasyExcel.write(tempFile, handler.getDataClass()).build()) {
                WriteSheet writeSheet = EasyExcel.writerSheet(handler.getSheetName()).build();

                int page = 1;
                List<T> dataList;
                do {
                    dataList = handler.queryDataByPage(params, page, BATCH_SIZE);
                    if (!dataList.isEmpty()) {
                        excelWriter.write(dataList, writeSheet);
                        totalCount += dataList.size();
                    }
                    page++;
                } while (dataList.size() == BATCH_SIZE);
            }

            // 上传到存储服务
            StorageService storageService = storageFactory.getDefaultStorage();
            String fileName = handler.getFileName() + "_" + IdUtil.simpleUUID() + ".xlsx";

            try (InputStream inputStream = new FileInputStream(tempFile)) {
                FileUploadVO uploadVO = storageService.upload(inputStream, "export", fileName);

                // 更新任务状态
                task.setStatus(ExportStatusEnum.SUCCESS.getCode());
                task.setFileId(uploadVO.getFileId());
                task.setFileName(fileName);
                task.setFileUrl(uploadVO.getUrl());
                task.setFileSize(tempFile.length());
                task.setTotalCount(totalCount);
                task.setSuccessCount(totalCount);
            }

        } catch (Exception e) {
            log.error("异步导出失败, taskId: {}", taskId, e);
            task.setStatus(ExportStatusEnum.FAILED.getCode());
            task.setErrorMsg(e.getMessage());
        } finally {
            task.setEndTime(LocalDateTime.now());
            exportTaskService.updateById(task);

            // 删除临时文件
            if (tempFile != null && tempFile.exists()) {
                try {
                    Files.delete(tempFile.toPath());
                } catch (IOException e) {
                    log.warn("删除临时文件失败: {}", tempFile.getAbsolutePath());
                }
            }
        }
    }

    @Override
    public <T> void downloadTemplate(Class<T> clazz, OutputStream output) {
        downloadTemplate(clazz, List.of(), output);
    }

    @Override
    public <T> void downloadTemplate(Class<T> clazz, List<T> sampleData, OutputStream output) {
        EasyExcel.write(output, clazz)
                .sheet("导入模板")
                .doWrite(sampleData);
    }
}
