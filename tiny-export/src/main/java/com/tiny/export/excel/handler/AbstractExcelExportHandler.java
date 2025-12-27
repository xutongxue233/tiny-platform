package com.tiny.export.excel.handler;

import java.util.List;

/**
 * 抽象导出处理器
 */
public abstract class AbstractExcelExportHandler<T, Q> implements ExcelExportHandler<T, Q> {

    @Override
    public List<T> queryData(Q params) {
        // 默认实现: 使用分页查询获取全部数据
        return queryDataByPage(params, 1, Integer.MAX_VALUE);
    }
}
