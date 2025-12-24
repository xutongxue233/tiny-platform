package com.tiny.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.generator.entity.GenConfig;

import java.util.List;
import java.util.Map;

/**
 * 代码生成器配置Service
 */
public interface GenConfigService extends IService<GenConfig> {

    /**
     * 获取所有配置
     */
    List<GenConfig> listAll();

    /**
     * 获取配置Map
     */
    Map<String, String> getConfigMap();

    /**
     * 获取配置值
     */
    String getConfigValue(String configKey);

    /**
     * 获取配置值，带默认值
     */
    String getConfigValue(String configKey, String defaultValue);

    /**
     * 更新配置
     */
    void updateConfig(String configKey, String configValue);

    /**
     * 批量更新配置
     */
    void batchUpdateConfig(List<GenConfig> configs);

    /**
     * 获取作者
     */
    String getAuthor();

    /**
     * 获取包名
     */
    String getPackageName();

    /**
     * 获取表前缀列表
     */
    List<String> getTablePrefixes();

    /**
     * 是否去除前缀
     */
    boolean isRemovePrefix();
}
