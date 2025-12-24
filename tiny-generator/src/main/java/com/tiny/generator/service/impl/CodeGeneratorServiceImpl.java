package com.tiny.generator.service.impl;

import cn.hutool.core.util.StrUtil;
import com.tiny.generator.config.GenProperties;
import com.tiny.generator.core.context.GenContext;
import com.tiny.generator.core.engine.TemplateDefinition;
import com.tiny.generator.core.engine.TemplateEngine;
import com.tiny.generator.core.engine.TemplateRegistry;
import com.tiny.generator.core.reader.DatabaseMetadataReader;
import com.tiny.generator.core.rule.RuleChainExecutor;
import com.tiny.generator.core.writer.CodeWriter;
import com.tiny.generator.entity.GenTable;
import com.tiny.generator.entity.GenTableColumn;
import com.tiny.generator.service.CodeGeneratorService;
import com.tiny.generator.service.GenTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CodeGeneratorServiceImpl implements CodeGeneratorService {

    private final GenTableService genTableService;
    private final RuleChainExecutor ruleChainExecutor;
    private final TemplateEngine templateEngine;
    private final TemplateRegistry templateRegistry;
    private final CodeWriter codeWriter;
    private final GenProperties genProperties;
    private final DatabaseMetadataReader metadataReader;

    @Override
    public Map<String, String> preview(Long tableId) {
        // 获取表配置
        GenTable table = genTableService.getTableWithColumns(tableId);
        if (table == null) {
            throw new RuntimeException("表配置不存在");
        }

        // 构建上下文
        GenContext context = buildContext(table);

        // 执行规则链
        ruleChainExecutor.execute(context);

        // 渲染所有模板
        Map<String, String> result = new LinkedHashMap<>();
        for (TemplateDefinition template : templateRegistry.getAllTemplates()) {
            try {
                String content = templateEngine.render(template.getTemplatePath(), context.getVariables());
                String filePath = resolveFilePath(template, context.getVariables());
                result.put(filePath, content);
            } catch (Exception e) {
                log.warn("渲染模板失败: {}", template.getTemplatePath(), e);
            }
        }

        return result;
    }

    @Override
    public byte[] generate(Long tableId) {
        Map<String, String> codes = preview(tableId);
        return codeWriter.packageAsZip(codes);
    }

    @Override
    public byte[] batchGenerate(Long[] tableIds) {
        Map<String, String> allCodes = new LinkedHashMap<>();

        for (Long tableId : tableIds) {
            Map<String, String> codes = preview(tableId);
            allCodes.putAll(codes);
        }

        return codeWriter.packageAsZip(allCodes);
    }

    @Override
    public void generateToPath(Long tableId, String basePath) {
        Map<String, String> codes = preview(tableId);

        String path = StrUtil.isNotBlank(basePath) ? basePath : genProperties.getGenPath();
        if (StrUtil.isBlank(path)) {
            throw new RuntimeException("生成路径未配置");
        }

        for (Map.Entry<String, String> entry : codes.entrySet()) {
            codeWriter.write(path, entry.getKey(), entry.getValue());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncTable(Long tableId) {
        GenTable table = genTableService.getTableWithColumns(tableId);
        if (table == null) {
            throw new RuntimeException("表配置不存在");
        }

        // 从数据库读取最新结构
        GenTable dbTable = metadataReader.readTable(table.getTableName());
        List<GenTableColumn> existingColumns = table.getColumns();

        // 创建现有列的映射
        Map<String, GenTableColumn> existingColumnMap = new LinkedHashMap<>();
        if (existingColumns != null) {
            for (GenTableColumn col : existingColumns) {
                existingColumnMap.put(col.getColumnName(), col);
            }
        }

        // 创建数据库列名集合，用于检测已删除的列
        List<String> dbColumnNames = dbTable.getColumns().stream()
                .map(GenTableColumn::getColumnName)
                .toList();

        // 同步列
        List<GenTableColumn> syncedColumns = new ArrayList<>();
        for (GenTableColumn dbColumn : dbTable.getColumns()) {
            GenTableColumn existingColumn = existingColumnMap.get(dbColumn.getColumnName());
            if (existingColumn != null) {
                // 保留已有配置，只更新数据库相关属性
                existingColumn.setColumnType(dbColumn.getColumnType());
                existingColumn.setIsRequired(dbColumn.getIsRequired());
                existingColumn.setIsPk(dbColumn.getIsPk());
                existingColumn.setIsIncrement(dbColumn.getIsIncrement());
                existingColumn.setSortOrder(dbColumn.getSortOrder());
                syncedColumns.add(existingColumn);
            } else {
                // 新增列，初始化配置
                dbColumn.setTableId(tableId);
                genTableService.initColumnConfig(dbColumn);
                syncedColumns.add(dbColumn);
            }
        }

        // 更新表（会处理新增和更新的列）
        table.setColumns(syncedColumns);
        genTableService.syncTableColumns(table, dbColumnNames);

        log.info("同步表[{}]成功，共{}列", table.getTableName(), syncedColumns.size());
    }

    /**
     * 构建生成上下文
     */
    private GenContext buildContext(GenTable table) {
        GenContext context = new GenContext(table);

        // 设置基础变量
        context.setVariable("table", table);

        return context;
    }

    /**
     * 解析文件路径
     */
    private String resolveFilePath(TemplateDefinition template, Map<String, Object> variables) {
        String filePath = template.getFilePathPattern();
        String fileName = template.getFileNamePattern();

        // 替换变量
        filePath = replaceVariables(filePath, variables);
        fileName = replaceVariables(fileName, variables);

        return filePath + "/" + fileName;
    }

    /**
     * 替换路径中的变量
     */
    private String replaceVariables(String pattern, Map<String, Object> variables) {
        String result = pattern;

        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String key = "${" + entry.getKey() + "}";
            Object value = entry.getValue();
            if (value != null) {
                result = result.replace(key, value.toString());
            }
        }

        // 特殊处理packagePath
        if (result.contains("${packagePath}")) {
            String packageName = (String) variables.get("packageName");
            if (packageName != null) {
                String packagePath = packageName.replace(".", "/");
                result = result.replace("${packagePath}", packagePath);
            }
        }

        return result;
    }
}
