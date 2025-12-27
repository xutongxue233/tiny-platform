package com.tiny.export.excel.listener;

import com.alibaba.excel.read.listener.ReadListener;

import java.util.List;

/**
 * Excel导入监听器接口
 *
 * @param <T> 数据类型
 */
public interface ExcelImportListener<T> extends ReadListener<T> {

    /**
     * 获取数据类
     */
    Class<T> getDataClass();

    /**
     * 获取总记录数
     */
    int getTotalCount();

    /**
     * 获取成功记录数
     */
    int getSuccessCount();

    /**
     * 获取失败记录数
     */
    int getFailCount();

    /**
     * 获取错误信息列表
     */
    List<String> getErrorMessages();
}
