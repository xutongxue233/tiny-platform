package com.tiny.generator.core.context;

import com.tiny.generator.entity.GenTable;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 代码生成上下文
 */
@Data
public class GenContext {

    /**
     * 表配置
     */
    private GenTable table;

    /**
     * 模板变量
     */
    private Map<String, Object> variables = new HashMap<>();

    /**
     * 需要导入的Java包
     */
    private Set<String> importPackages = new TreeSet<>();

    /**
     * 生成的代码文件
     */
    private Map<String, String> generatedFiles = new LinkedHashMap<>();

    public GenContext(GenTable table) {
        this.table = table;
    }

    /**
     * 设置变量
     */
    public void setVariable(String key, Object value) {
        this.variables.put(key, value);
    }

    /**
     * 获取变量
     */
    @SuppressWarnings("unchecked")
    public <T> T getVariable(String key) {
        return (T) this.variables.get(key);
    }

    /**
     * 添加导入包
     */
    public void addImport(String packageName) {
        if (packageName != null && !packageName.isEmpty()) {
            this.importPackages.add(packageName);
        }
    }

    /**
     * 添加生成的文件
     */
    public void addGeneratedFile(String filePath, String content) {
        this.generatedFiles.put(filePath, content);
    }
}
