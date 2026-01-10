package com.tiny.core.csrf;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * CSRF防护配置属性
 */
@ConfigurationProperties(prefix = "tiny.csrf")
public class CsrfProperties {

    /**
     * 是否启用CSRF防护
     */
    private boolean enabled = true;

    /**
     * 允许的域名列表（支持通配符）
     */
    private List<String> allowedOrigins = new ArrayList<>();

    /**
     * 排除的URL路径列表
     */
    private List<String> excludeUrls = new ArrayList<>();

    /**
     * 是否检查Referer头
     */
    private boolean checkReferer = true;

    /**
     * 是否检查Origin头
     */
    private boolean checkOrigin = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public List<String> getExcludeUrls() {
        return excludeUrls;
    }

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    public boolean isCheckReferer() {
        return checkReferer;
    }

    public void setCheckReferer(boolean checkReferer) {
        this.checkReferer = checkReferer;
    }

    public boolean isCheckOrigin() {
        return checkOrigin;
    }

    public void setCheckOrigin(boolean checkOrigin) {
        this.checkOrigin = checkOrigin;
    }
}
