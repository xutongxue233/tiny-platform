package com.tiny.generator.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.generator.config.GenConfigProvider;
import com.tiny.generator.core.reader.DatabaseMetadataReader;
import com.tiny.generator.entity.GenTable;
import com.tiny.generator.entity.GenTableColumn;
import com.tiny.generator.mapper.GenTableColumnMapper;
import com.tiny.generator.mapper.GenTableMapper;
import com.tiny.generator.service.GenTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 代码生成表Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements GenTableService {

    private final GenTableColumnMapper columnMapper;
    private final DatabaseMetadataReader metadataReader;
    private final GenConfigProvider configProvider;

    @Override
    public List<GenTable> selectDbTableList(String tableName) {
        List<GenTable> allTables = metadataReader.getAllTables();

        // 过滤已导入的表
        List<String> importedTables = list().stream()
                .map(GenTable::getTableName)
                .toList();

        return allTables.stream()
                .filter(t -> !importedTables.contains(t.getTableName()))
                .filter(t -> StrUtil.isBlank(tableName) ||
                        t.getTableName().toLowerCase().contains(tableName.toLowerCase()))
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importTable(List<String> tableNames) {
        for (String tableName : tableNames) {
            // 检查表是否已导入（忽略逻辑删除，检查是否有同名记录）
            GenTable existing = baseMapper.selectByTableNameIgnoreDeleted(tableName);
            if (existing != null && "0".equals(existing.getDelFlag())) {
                // 存在未删除的记录，跳过
                log.warn("表[{}]已存在，跳过导入", tableName);
                continue;
            }

            // 物理删除已逻辑删除的同名记录（避免唯一索引冲突）
            baseMapper.physicalDeleteByTableName(tableName);

            // 读取表结构
            GenTable table = metadataReader.readTable(tableName);

            // 初始化表配置
            initTableConfig(table);

            // 保存表
            save(table);

            // 初始化并保存字段
            for (GenTableColumn column : table.getColumns()) {
                column.setTableId(table.getTableId());
                initColumnConfig(column);
                columnMapper.insert(column);
            }

            log.info("导入表[{}]成功", tableName);
        }
    }

    @Override
    public void initColumnConfig(GenTableColumn column) {
        // 初始化Java字段名
        if (StrUtil.isBlank(column.getJavaField())) {
            column.setJavaField(StrUtil.toCamelCase(column.getColumnName()));
        }

        // 初始化Java类型
        if (StrUtil.isBlank(column.getJavaType())) {
            String dbType = column.getColumnType().toLowerCase();
            if (dbType.contains("bigint")) {
                column.setJavaType("Long");
            } else if (dbType.contains("int")) {
                column.setJavaType("Integer");
            } else if (dbType.contains("decimal") || dbType.contains("numeric") || dbType.contains("double") || dbType.contains("float")) {
                column.setJavaType("BigDecimal");
            } else if (dbType.contains("datetime") || dbType.contains("timestamp")) {
                column.setJavaType("LocalDateTime");
            } else if (dbType.contains("date")) {
                column.setJavaType("LocalDate");
            } else if (dbType.contains("time")) {
                column.setJavaType("LocalTime");
            } else {
                column.setJavaType("String");
            }
        }

        // 初始化HTML类型
        if (StrUtil.isBlank(column.getHtmlType())) {
            String dbType = column.getColumnType().toLowerCase();
            if (dbType.contains("text")) {
                column.setHtmlType("textarea");
            } else if (dbType.contains("datetime") || dbType.contains("timestamp")) {
                column.setHtmlType("datetime");
            } else if (dbType.contains("date")) {
                column.setHtmlType("date");
            } else if (dbType.contains("tinyint") || dbType.contains("smallint")) {
                column.setHtmlType("select");
            } else {
                column.setHtmlType("input");
            }
        }

        // 默认显示配置
        if (StrUtil.isBlank(column.getIsInsert())) {
            column.setIsInsert("1");
        }
        if (StrUtil.isBlank(column.getIsEdit())) {
            column.setIsEdit("1");
        }
        if (StrUtil.isBlank(column.getIsList())) {
            column.setIsList("1");
        }
        if (StrUtil.isBlank(column.getIsQuery())) {
            column.setIsQuery("0");
        }
        if (StrUtil.isBlank(column.getIsDetail())) {
            column.setIsDetail("1");
        }
        if (StrUtil.isBlank(column.getIsExport())) {
            column.setIsExport("1");
        }
        if (StrUtil.isBlank(column.getQueryType())) {
            column.setQueryType("EQ");
        }
    }

    @Override
    public GenTable getTableWithColumns(Long tableId) {
        GenTable table = getById(tableId);
        if (table != null) {
            List<GenTableColumn> columns = getColumnsByTableId(tableId);
            table.setColumns(columns);
            // 设置主键列
            columns.stream()
                    .filter(GenTableColumn::isPk)
                    .findFirst()
                    .ifPresent(table::setPkColumn);
        }
        return table;
    }

    @Override
    public List<GenTableColumn> getColumnsByTableId(Long tableId) {
        return columnMapper.selectList(
                new LambdaQueryWrapper<GenTableColumn>()
                        .eq(GenTableColumn::getTableId, tableId)
                        .orderByAsc(GenTableColumn::getSortOrder)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGenTable(GenTable table) {
        updateById(table);

        // 更新字段
        if (table.getColumns() != null) {
            for (GenTableColumn column : table.getColumns()) {
                columnMapper.updateById(column);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Long> tableIds) {
        // 删除字段
        for (Long tableId : tableIds) {
            columnMapper.delete(
                    new LambdaQueryWrapper<GenTableColumn>()
                            .eq(GenTableColumn::getTableId, tableId)
            );
        }
        // 删除表
        removeByIds(tableIds);
    }

    /**
     * 初始化表配置
     */
    private void initTableConfig(GenTable table) {
        String tableName = table.getTableName();

        // 去除前缀获取业务名
        String businessName = tableName;
        if (configProvider.isRemovePrefix()) {
            for (String prefix : configProvider.getTablePrefixes()) {
                if (tableName.toLowerCase().startsWith(prefix.toLowerCase())) {
                    businessName = tableName.substring(prefix.length());
                    break;
                }
            }
        }

        // 转换类名
        String className = Arrays.stream(businessName.split("_"))
                .filter(StrUtil::isNotBlank)
                .map(s -> StrUtil.upperFirst(s.toLowerCase()))
                .collect(Collectors.joining());

        table.setClassName(className);
        table.setBusinessName(StrUtil.lowerFirst(className));
        table.setPackageName(configProvider.getPackageName());
        table.setModuleName(getModuleName(tableName));
        table.setFunctionName(table.getTableComment());
        table.setAuthor(configProvider.getAuthor());
        table.setFeGenerateType("1");
        table.setGenType("0");
    }

    /**
     * 从表名推断模块名
     */
    private String getModuleName(String tableName) {
        for (String prefix : configProvider.getTablePrefixes()) {
            if (tableName.toLowerCase().startsWith(prefix.toLowerCase())) {
                return prefix.replace("_", "");
            }
        }
        return "system";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void regenerateConfig(Long tableId) {
        GenTable table = getById(tableId);
        if (table == null) {
            throw new RuntimeException("表配置不存在");
        }

        // 重新生成配置
        reInitTableConfig(table);

        // 更新表
        updateById(table);

        log.info("重新生成表[{}]配置成功", table.getTableName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRegenerateConfig(List<Long> tableIds) {
        for (Long tableId : tableIds) {
            regenerateConfig(tableId);
        }
    }

    /**
     * 重新初始化表配置（强制根据最新全局配置重新计算）
     */
    private void reInitTableConfig(GenTable table) {
        String tableName = table.getTableName();

        // 去除前缀获取业务名
        String businessName = tableName;
        if (configProvider.isRemovePrefix()) {
            for (String prefix : configProvider.getTablePrefixes()) {
                if (tableName.toLowerCase().startsWith(prefix.toLowerCase())) {
                    businessName = tableName.substring(prefix.length());
                    break;
                }
            }
        }

        // 转换类名
        String className = Arrays.stream(businessName.split("_"))
                .filter(StrUtil::isNotBlank)
                .map(s -> StrUtil.upperFirst(s.toLowerCase()))
                .collect(Collectors.joining());

        table.setClassName(className);
        table.setBusinessName(StrUtil.lowerFirst(className));
        table.setPackageName(configProvider.getPackageName());
        table.setModuleName(getModuleName(tableName));
        table.setAuthor(configProvider.getAuthor());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncTableColumns(GenTable table, List<String> dbColumnNames) {
        Long tableId = table.getTableId();

        // 获取现有的所有列
        List<GenTableColumn> existingColumns = getColumnsByTableId(tableId);

        // 删除数据库中已不存在的列
        for (GenTableColumn existingColumn : existingColumns) {
            if (!dbColumnNames.contains(existingColumn.getColumnName())) {
                columnMapper.deleteById(existingColumn.getColumnId());
                log.info("删除列[{}]", existingColumn.getColumnName());
            }
        }

        // 更新或新增列
        for (GenTableColumn column : table.getColumns()) {
            if (column.getColumnId() != null) {
                // 更新已有列
                columnMapper.updateById(column);
            } else {
                // 新增列
                columnMapper.insert(column);
                log.info("新增列[{}]", column.getColumnName());
            }
        }
    }
}
