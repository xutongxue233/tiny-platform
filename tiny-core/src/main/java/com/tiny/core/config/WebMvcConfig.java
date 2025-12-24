package com.tiny.core.config;

import com.tiny.core.web.TraceInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // TraceId拦截器，最先执行
        registry.addInterceptor(new TraceInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/druid/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/doc.html"
                )
                .order(-100);
    }
}