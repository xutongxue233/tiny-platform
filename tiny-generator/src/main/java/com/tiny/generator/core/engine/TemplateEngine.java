package com.tiny.generator.core.engine;

import java.util.Map;

/**
 * 模板引擎接口
 */
public interface TemplateEngine {

    /**
     * 渲染模板
     *
     * @param templatePath 模板路径
     * @param variables    变量
     * @return 渲染后的内容
     */
    String render(String templatePath, Map<String, Object> variables);

    /**
     * 渲染模板字符串
     *
     * @param templateContent 模板内容
     * @param variables       变量
     * @return 渲染后的内容
     */
    String renderString(String templateContent, Map<String, Object> variables);
}
