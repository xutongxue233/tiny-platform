package com.tiny.core.csrf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CSRF防护自动配置
 */
@Configuration
@EnableConfigurationProperties(CsrfProperties.class)
@ConditionalOnProperty(prefix = "tiny.csrf", name = "enabled", havingValue = "true", matchIfMissing = false)
public class CsrfAutoConfiguration {

    @Bean
    public FilterRegistrationBean<CsrfFilter> csrfFilterRegistration(CsrfProperties properties) {
        FilterRegistrationBean<CsrfFilter> registration = new FilterRegistrationBean<>();
        CsrfFilter csrfFilter = new CsrfFilter(properties);
        registration.setFilter(csrfFilter);
        registration.addUrlPatterns("/*");
        registration.setName("csrfFilter");
        registration.setOrder(0);
        return registration;
    }
}
