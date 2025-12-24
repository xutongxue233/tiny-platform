package com.tiny.generator.core.rule;

import com.tiny.generator.core.context.GenContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * 规则链执行器
 */
@Slf4j
@Component
public class RuleChainExecutor {

    private final List<GenRule> rules;

    public RuleChainExecutor(List<GenRule> rules) {
        this.rules = rules.stream()
                .sorted(Comparator.comparingInt(GenRule::order))
                .toList();
        log.info("加载代码生成规则: {}", this.rules.stream().map(GenRule::name).toList());
    }

    /**
     * 执行规则链
     */
    public void execute(GenContext context) {
        for (GenRule rule : rules) {
            log.debug("执行规则: {}", rule.name());
            rule.apply(context);
        }
    }
}
