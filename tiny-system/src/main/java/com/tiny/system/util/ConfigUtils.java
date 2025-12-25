package com.tiny.system.util;

import com.tiny.system.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 系统参数配置工具类
 */
@Component
public class ConfigUtils {

    private static SysConfigService configService;

    @Autowired
    public void setConfigService(SysConfigService configService) {
        ConfigUtils.configService = configService;
    }

    /**
     * 根据参数键名获取参数值
     *
     * @param configKey 参数键名
     * @return 参数值
     */
    public static String getValue(String configKey) {
        return configService.getConfigValue(configKey);
    }

    /**
     * 根据参数键名获取参数值(带默认值)
     *
     * @param configKey    参数键名
     * @param defaultValue 默认值
     * @return 参数值
     */
    public static String getValue(String configKey, String defaultValue) {
        return configService.getConfigValue(configKey, defaultValue);
    }

    /**
     * 根据参数键名获取布尔值
     *
     * @param configKey 参数键名
     * @return 布尔值
     */
    public static boolean getBoolean(String configKey) {
        return configService.getConfigBoolean(configKey);
    }

    /**
     * 根据参数键名获取整数值
     *
     * @param configKey 参数键名
     * @return 整数值
     */
    public static Integer getInteger(String configKey) {
        return configService.getConfigInteger(configKey);
    }
}
