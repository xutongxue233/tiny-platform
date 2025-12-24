package com.tiny.generator.core.engine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模板定义
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateDefinition {

    /**
     * 模板路径
     */
    private String templatePath;

    /**
     * 生成文件名模式
     */
    private String fileNamePattern;

    /**
     * 生成路径模式
     */
    private String filePathPattern;

    /**
     * 模板类型: backend/frontend
     */
    private String type;

    /**
     * 是否启用
     */
    private boolean enabled = true;
}
