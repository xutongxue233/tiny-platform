package com.tiny.export.service;

import com.tiny.export.dto.ImportResultDTO;
import com.tiny.export.excel.listener.ExcelImportListener;
import org.springframework.web.multipart.MultipartFile;

/**
 * 导入服务接口
 */
public interface ImportService {

    /**
     * 导入Excel数据
     *
     * @param file     Excel文件
     * @param listener 导入监听器
     * @param <T>      数据类型
     * @return 导入结果
     */
    <T> ImportResultDTO importData(MultipartFile file, ExcelImportListener<T> listener);

    /**
     * 异步导入Excel数据(大数据量)
     *
     * @param file         Excel文件
     * @param listener     导入监听器
     * @param taskName     任务名称
     * @param businessType 业务类型
     * @param <T>          数据类型
     * @return 任务ID
     */
    <T> Long importDataAsync(MultipartFile file, ExcelImportListener<T> listener,
                             String taskName, String businessType);
}
