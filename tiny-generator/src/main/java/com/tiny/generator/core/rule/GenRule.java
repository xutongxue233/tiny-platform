package com.tiny.generator.core.rule;

import com.tiny.generator.core.context.GenContext;

/**
 * 代码生成规则接口
 */
public interface GenRule {

    /**
     * 规则名称
     */
    String name();

    /**
     * 执行顺序
     */
    int order();

    /**
     * 执行规则
     */
    void apply(GenContext context);
}
