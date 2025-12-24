package com.tiny.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.generator.entity.GenTable;
import com.tiny.generator.entity.GenTableColumn;

import java.util.List;

/**
 * 代码生成表Service
 */
public interface GenTableService extends IService<GenTable> {

    /**
     * 查询数据库表列表
     */
    List<GenTable> selectDbTableList(String tableName);

    /**
     * 导入表结构
     */
    void importTable(List<String> tableNames);

    /**
     * 根据表ID获取表信息
     */
    GenTable getTableWithColumns(Long tableId);

    /**
     * 获取表字段列表
     */
    List<GenTableColumn> getColumnsByTableId(Long tableId);

    /**
     * 更新表配置
     */
    void updateGenTable(GenTable table);

    /**
     * 删除表配置
     */
    void deleteByIds(List<Long> tableIds);

    /**
     * 重新生成表配置（根据最新全局配置重新计算类名等）
     */
    void regenerateConfig(Long tableId);

    /**
     * 批量重新生成表配置
     */
    void batchRegenerateConfig(List<Long> tableIds);

    /**
     * 同步表字段（更新已有列、新增列、删除已移除的列）
     */
    void syncTableColumns(GenTable table, List<String> dbColumnNames);

    /**
     * 初始化列配置（Java类型、Java属性、HTML类型等）
     */
    void initColumnConfig(GenTableColumn column);
}
