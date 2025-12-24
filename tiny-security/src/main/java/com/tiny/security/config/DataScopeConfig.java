package com.tiny.security.config;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.tiny.security.datascope.DataScopeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据权限配置
 */
@Configuration
public class DataScopeConfig {

    @Bean
    public InnerInterceptor dataScopeInterceptor() {
        return new DataScopeInterceptor();
    }
}
