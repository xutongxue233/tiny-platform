package com.tiny.generator.core.reader;

import com.tiny.generator.entity.GenTable;
import com.tiny.generator.entity.GenTableColumn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库元数据读取器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseMetadataReader {

    private final DataSource dataSource;

    /**
     * 获取数据库中所有表
     */
    public List<GenTable> getAllTables() {
        List<GenTable> tables = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String catalog = conn.getCatalog();

            try (ResultSet rs = metaData.getTables(catalog, null, "%", new String[]{"TABLE"})) {
                while (rs.next()) {
                    GenTable table = new GenTable();
                    table.setTableName(rs.getString("TABLE_NAME"));
                    table.setTableComment(rs.getString("REMARKS"));
                    tables.add(table);
                }
            }
        } catch (SQLException e) {
            log.error("读取数据库表信息失败", e);
            throw new RuntimeException("读取数据库表信息失败", e);
        }
        return tables;
    }

    /**
     * 读取指定表的元数据
     */
    public GenTable readTable(String tableName) {
        GenTable table = new GenTable();
        table.setTableName(tableName);

        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String catalog = conn.getCatalog();

            // 读取表信息
            try (ResultSet rs = metaData.getTables(catalog, null, tableName, new String[]{"TABLE"})) {
                if (rs.next()) {
                    table.setTableComment(rs.getString("REMARKS"));
                }
            }

            // 读取主键信息
            List<String> primaryKeys = new ArrayList<>();
            try (ResultSet rs = metaData.getPrimaryKeys(catalog, null, tableName)) {
                while (rs.next()) {
                    primaryKeys.add(rs.getString("COLUMN_NAME"));
                }
            }

            // 读取列信息
            List<GenTableColumn> columns = new ArrayList<>();
            try (ResultSet rs = metaData.getColumns(catalog, null, tableName, "%")) {
                int sortOrder = 0;
                while (rs.next()) {
                    GenTableColumn column = new GenTableColumn();
                    column.setColumnName(rs.getString("COLUMN_NAME"));
                    column.setColumnComment(rs.getString("REMARKS"));
                    column.setColumnType(rs.getString("TYPE_NAME"));
                    column.setSortOrder(sortOrder++);

                    // 判断是否主键
                    if (primaryKeys.contains(column.getColumnName())) {
                        column.setIsPk("1");
                        // 判断是否自增
                        String isAutoIncrement = rs.getString("IS_AUTOINCREMENT");
                        if ("YES".equalsIgnoreCase(isAutoIncrement)) {
                            column.setIsIncrement("1");
                        } else {
                            column.setIsIncrement("0");
                        }
                    } else {
                        column.setIsPk("0");
                        column.setIsIncrement("0");
                    }

                    // 判断是否可空
                    int nullable = rs.getInt("NULLABLE");
                    column.setIsRequired(nullable == DatabaseMetaData.columnNoNulls ? "1" : "0");

                    columns.add(column);
                }
            }

            table.setColumns(columns);

        } catch (SQLException e) {
            log.error("读取表[{}]信息失败", tableName, e);
            throw new RuntimeException("读取表信息失败: " + tableName, e);
        }

        return table;
    }

    /**
     * 查询表是否存在
     */
    public boolean tableExists(String tableName) {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String catalog = conn.getCatalog();

            try (ResultSet rs = metaData.getTables(catalog, null, tableName, new String[]{"TABLE"})) {
                return rs.next();
            }
        } catch (SQLException e) {
            log.error("检查表[{}]是否存在失败", tableName, e);
            return false;
        }
    }
}
