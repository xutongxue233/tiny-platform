package com.tiny.export.service;

import com.tiny.export.excel.handler.ExcelExportHandler;

import java.io.OutputStream;
import java.util.List;

/**
 * 导出服务接口
 */
public interface ExportService {

    /**
     * 同步导出(小数据量,直接返回文件流)
     *
     * @param handler 导出处理器
     * @param params  查询参数
     * @param output  输出流
     * @param <T>     数据类型
     * @param <Q>     查询参数类型
     */
    <T, Q> void exportSync(ExcelExportHandler<T, Q> handler, Q params, OutputStream output);

    /**
     * 异步导出(大数据量,返回任务ID)
     *
     * @param handler      导出处理器
     * @param params       查询参数
     * @param taskName     任务名称
     * @param businessType 业务类型
     * @param <T>          数据类型
     * @param <Q>          查询参数类型
     * @return 任务ID
     */
    <T, Q> Long exportAsync(ExcelExportHandler<T, Q> handler, Q params,
                            String taskName, String businessType);

    /**
     * 下载导出模板
     *
     * @param clazz  数据类
     * @param output 输出流
     * @param <T>    数据类型
     */
    <T> void downloadTemplate(Class<T> clazz, OutputStream output);

    /**
     * 下载导出模板(带示例数据)
     *
     * @param clazz      数据类
     * @param sampleData 示例数据
     * @param output     输出流
     * @param <T>        数据类型
     */
    <T> void downloadTemplate(Class<T> clazz, List<T> sampleData, OutputStream output);
}
