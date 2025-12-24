package com.tiny.generator.config;

import com.tiny.generator.service.GenConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 代码生成配置提供者
 * 优先从数据库读取配置，数据库无配置时使用默认值
 */
@Component
@RequiredArgsConstructor
public class GenConfigProvider {

    private final GenConfigService genConfigService;
    private final GenProperties genProperties;

    /**
     * 获取作者
     */
    public String getAuthor() {
        try {
            String author = genConfigService.getAuthor();
            return author != null && !author.isEmpty() ? author : genProperties.getAuthor();
        } catch (Exception e) {
            return genProperties.getAuthor();
        }
    }

    /**
     * 获取包名
     */
    public String getPackageName() {
        try {
            String packageName = genConfigService.getPackageName();
            return packageName != null && !packageName.isEmpty() ? packageName : genProperties.getPackageName();
        } catch (Exception e) {
            return genProperties.getPackageName();
        }
    }

    /**
     * 获取表前缀列表
     */
    public List<String> getTablePrefixes() {
        try {
            List<String> prefixes = genConfigService.getTablePrefixes();
            return prefixes != null && !prefixes.isEmpty() ? prefixes : genProperties.getTablePrefixes();
        } catch (Exception e) {
            return genProperties.getTablePrefixes();
        }
    }

    /**
     * 是否去除前缀
     */
    public boolean isRemovePrefix() {
        try {
            return genConfigService.isRemovePrefix();
        } catch (Exception e) {
            return genProperties.isRemovePrefix();
        }
    }

    /**
     * 获取后端生成路径
     */
    public String getBackendPath() {
        try {
            return genConfigService.getConfigValue("gen.backendPath", genProperties.getGenPath());
        } catch (Exception e) {
            return genProperties.getGenPath();
        }
    }

    /**
     * 获取前端生成路径
     */
    public String getFrontendPath() {
        try {
            return genConfigService.getConfigValue("gen.frontendPath", "");
        } catch (Exception e) {
            return "";
        }
    }
}
