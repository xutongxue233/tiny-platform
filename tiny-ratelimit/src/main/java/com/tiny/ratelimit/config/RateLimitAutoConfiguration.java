package com.tiny.ratelimit.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 限流模块自动配置
 */
@Configuration
@ComponentScan("com.tiny.ratelimit")
public class RateLimitAutoConfiguration {
}
