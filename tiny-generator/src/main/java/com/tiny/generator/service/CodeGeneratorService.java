package com.tiny.generator.service;

import com.tiny.generator.entity.GenTable;

import java.util.Map;

/**
 * 代码生成服务
 */
public interface CodeGeneratorService {

    /**
     * 预览代码
     *
     * @param tableId 表ID
     * @return 文件路径 -> 代码内容
     */
    Map<String, String> preview(Long tableId);

    /**
     * 生成代码并下载
     *
     * @param tableId 表ID
     * @return ZIP字节数组
     */
    byte[] generate(Long tableId);

    /**
     * 批量生成代码
     *
     * @param tableIds 表ID数组
     * @return ZIP字节数组
     */
    byte[] batchGenerate(Long[] tableIds);

    /**
     * 生成代码到指定路径
     *
     * @param tableId  表ID
     * @param basePath 基础路径
     */
    void generateToPath(Long tableId, String basePath);

    /**
     * 同步数据库表结构
     *
     * @param tableId 表ID
     */
    void syncTable(Long tableId);
}
