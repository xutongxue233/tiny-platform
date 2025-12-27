package com.tiny.generator.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 代码生成器自动配置
 */
@Configuration
@EnableConfigurationProperties(GenProperties.class)
@ComponentScan("com.tiny.generator")
public class GeneratorAutoConfiguration {
}
