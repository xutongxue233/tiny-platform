package com.tiny.message.template;

import java.util.Map;

/**
 * 模板引擎接口
 */
public interface TemplateEngine {

    /**
     * 渲染模板
     *
     * @param template  模板内容
     * @param variables 变量Map
     * @return 渲染后的内容
     */
    String render(String template, Map<String, Object> variables);
}
