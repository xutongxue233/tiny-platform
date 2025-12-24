package com.tiny.generator.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.generator.entity.GenConfig;
import com.tiny.generator.mapper.GenConfigMapper;
import com.tiny.generator.service.GenConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器配置Service实现
 */
@Slf4j
@Service
public class GenConfigServiceImpl extends ServiceImpl<GenConfigMapper, GenConfig> implements GenConfigService {

    @Override
    public List<GenConfig> listAll() {
        return list(new LambdaQueryWrapper<GenConfig>().orderByAsc(GenConfig::getConfigId));
    }

    @Override
    public Map<String, String> getConfigMap() {
        List<GenConfig> configs = listAll();
        Map<String, String> map = new HashMap<>();
        for (GenConfig config : configs) {
            map.put(config.getConfigKey(), config.getConfigValue());
        }
        return map;
    }

    @Override
    public String getConfigValue(String configKey) {
        return getConfigValue(configKey, "");
    }

    @Override
    public String getConfigValue(String configKey, String defaultValue) {
        GenConfig config = getOne(
                new LambdaQueryWrapper<GenConfig>().eq(GenConfig::getConfigKey, configKey)
        );
        if (config != null && StrUtil.isNotBlank(config.getConfigValue())) {
            return config.getConfigValue();
        }
        return defaultValue;
    }

    @Override
    public void updateConfig(String configKey, String configValue) {
        update(new LambdaUpdateWrapper<GenConfig>()
                .eq(GenConfig::getConfigKey, configKey)
                .set(GenConfig::getConfigValue, configValue));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateConfig(List<GenConfig> configs) {
        for (GenConfig config : configs) {
            if (config.getConfigId() != null) {
                updateById(config);
            } else if (StrUtil.isNotBlank(config.getConfigKey())) {
                updateConfig(config.getConfigKey(), config.getConfigValue());
            }
        }
    }

    @Override
    public String getAuthor() {
        return getConfigValue(GenConfig.KEY_AUTHOR, "admin");
    }

    @Override
    public String getPackageName() {
        return getConfigValue(GenConfig.KEY_PACKAGE_NAME, "com.tiny");
    }

    @Override
    public List<String> getTablePrefixes() {
        String prefixes = getConfigValue(GenConfig.KEY_TABLE_PREFIXES, "sys_,gen_");
        return Arrays.asList(prefixes.split(","));
    }

    @Override
    public boolean isRemovePrefix() {
        return "true".equalsIgnoreCase(getConfigValue(GenConfig.KEY_REMOVE_PREFIX, "true"));
    }
}
