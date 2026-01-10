package com.tiny.core.xss;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * XSS过滤配置属性
 */
@ConfigurationProperties(prefix = "tiny.xss")
public class XssProperties {

    /**
     * 是否启用XSS过滤
     */
    private boolean enabled = true;

    /**
     * 排除的URL路径列表
     */
    private List<String> excludeUrls = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getExcludeUrls() {
        return excludeUrls;
    }

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }
}
