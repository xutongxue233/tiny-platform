package com.tiny.core.config;

/**
 * 配置提供者接口
 * 用于解耦模块间的配置依赖
 */
public interface ConfigProvider {

    /**
     * 根据参数键名获取参数值
     *
     * @param configKey 参数键名
     * @return 参数值
     */
    String getConfigValue(String configKey);

    /**
     * 根据参数键名获取参数值(带默认值)
     *
     * @param configKey    参数键名
     * @param defaultValue 默认值
     * @return 参数值
     */
    String getConfigValue(String configKey, String defaultValue);

    /**
     * 根据参数键名获取布尔值
     *
     * @param configKey 参数键名
     * @return 布尔值
     */
    boolean getConfigBoolean(String configKey);

    /**
     * 根据参数键名获取整数值
     *
     * @param configKey 参数键名
     * @return 整数值
     */
    Integer getConfigInteger(String configKey);
}
