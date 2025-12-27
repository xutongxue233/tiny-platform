package com.tiny.export.excel.handler;

import java.util.List;

/**
 * Excel导出处理器接口
 *
 * @param <T> 数据类型
 * @param <Q> 查询参数类型
 */
public interface ExcelExportHandler<T, Q> {

    /**
     * 获取数据类
     */
    Class<T> getDataClass();

    /**
     * 获取Sheet名称
     */
    default String getSheetName() {
        return "Sheet1";
    }

    /**
     * 获取导出文件名(不含扩展名)
     */
    String getFileName();

    /**
     * 查询全部数据(用于同步导出)
     */
    List<T> queryData(Q params);

    /**
     * 分页查询数据(用于异步导出)
     */
    List<T> queryDataByPage(Q params, int page, int size);

    /**
     * 获取总记录数(用于异步导出进度计算)
     */
    default long countTotal(Q params) {
        return 0;
    }
}
