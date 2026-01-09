package com.tiny.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.exception.BusinessException;
import com.tiny.system.dto.SysConfigDTO;
import com.tiny.system.dto.SysConfigQueryDTO;
import com.tiny.system.entity.SysConfig;
import com.tiny.system.mapper.SysConfigMapper;
import com.tiny.system.service.SysConfigService;
import com.tiny.system.vo.SysConfigVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 系统参数配置服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 参数配置缓存Key前缀
     */
    private static final String CONFIG_CACHE_KEY = "sys:config:";

    /**
     * 配置键名集合Key，用于记录所有已缓存的配置键
     */
    private static final String CONFIG_KEYS_SET = "sys:config:keys";

    /**
     * 空值缓存标记，用于防止缓存穿透
     */
    private static final String NULL_VALUE = "__NULL__";

    /**
     * 空值缓存过期时间（秒）
     */
    private static final int NULL_EXPIRE_SECONDS = 60;

    /**
     * 启动时加载配置到缓存
     */
    @PostConstruct
    public void init() {
        refreshCache();
    }

    @Override
    public PageResult<SysConfigVO> page(SysConfigQueryDTO queryDTO) {
        Page<SysConfig> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        LambdaQueryWrapper<SysConfig> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getConfigName()), SysConfig::getConfigName, queryDTO.getConfigName())
                .like(StrUtil.isNotBlank(queryDTO.getConfigKey()), SysConfig::getConfigKey, queryDTO.getConfigKey())
                .eq(StrUtil.isNotBlank(queryDTO.getConfigType()), SysConfig::getConfigType, queryDTO.getConfigType())
                .eq(StrUtil.isNotBlank(queryDTO.getConfigGroup()), SysConfig::getConfigGroup, queryDTO.getConfigGroup())
                .eq(StrUtil.isNotBlank(queryDTO.getStatus()), SysConfig::getStatus, queryDTO.getStatus())
                .orderByDesc(SysConfig::getCreateTime);

        Page<SysConfig> result = baseMapper.selectPage(page, wrapper);
        return PageResult.of(result, SysConfig::toVO);
    }

    @Override
    public List<SysConfigVO> listAll() {
        List<SysConfig> list = this.list(Wrappers.<SysConfig>lambdaQuery()
                .orderByDesc(SysConfig::getCreateTime));
        return list.stream()
                .map(SysConfig::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SysConfigVO> listByGroup(String configGroup) {
        List<SysConfig> list = this.list(Wrappers.<SysConfig>lambdaQuery()
                .eq(SysConfig::getConfigGroup, configGroup)
                .eq(SysConfig::getStatus, "0")
                .orderByDesc(SysConfig::getCreateTime));
        return list.stream()
                .map(SysConfig::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public SysConfigVO getDetail(Long configId) {
        SysConfig config = this.getById(configId);
        if (config == null) {
            throw new BusinessException("参数配置不存在");
        }
        return config.toVO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysConfigDTO dto) {
        if (checkConfigKeyExists(dto.getConfigKey(), null)) {
            throw new BusinessException("参数键名已存在");
        }

        SysConfig config = BeanUtil.copyProperties(dto, SysConfig.class);
        if (StrUtil.isBlank(config.getStatus())) {
            config.setStatus("0");
        }
        if (StrUtil.isBlank(config.getConfigType())) {
            config.setConfigType("STRING");
        }
        if (StrUtil.isBlank(config.getConfigGroup())) {
            config.setConfigGroup("SYSTEM");
        }
        if (StrUtil.isBlank(config.getIsBuiltin())) {
            config.setIsBuiltin("N");
        }
        this.save(config);

        // 添加缓存
        if ("0".equals(config.getStatus())) {
            setConfigCache(config.getConfigKey(), config.getConfigValue());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysConfigDTO dto) {
        SysConfig config = this.getById(dto.getConfigId());
        if (config == null) {
            throw new BusinessException("参数配置不存在");
        }

        if (checkConfigKeyExists(dto.getConfigKey(), dto.getConfigId())) {
            throw new BusinessException("参数键名已存在");
        }

        String oldConfigKey = config.getConfigKey();
        BeanUtil.copyProperties(dto, config, "configId");
        this.updateById(config);

        // 更新缓存
        if (!oldConfigKey.equals(dto.getConfigKey())) {
            clearCache(oldConfigKey);
        }
        if ("0".equals(config.getStatus())) {
            setConfigCache(config.getConfigKey(), config.getConfigValue());
        } else {
            clearCache(config.getConfigKey());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long configId) {
        SysConfig config = this.getById(configId);
        if (config == null) {
            throw new BusinessException("参数配置不存在");
        }

        if ("Y".equals(config.getIsBuiltin())) {
            throw new BusinessException("内置参数不允许删除");
        }

        this.removeById(configId);
        clearCache(config.getConfigKey());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> configIds) {
        if (CollUtil.isEmpty(configIds)) {
            return;
        }

        List<SysConfig> configs = this.listByIds(configIds);
        for (SysConfig config : configs) {
            if ("Y".equals(config.getIsBuiltin())) {
                throw new BusinessException("参数[" + config.getConfigName() + "]是内置参数，不允许删除");
            }
        }

        this.removeByIds(configIds);

        // 清除缓存
        for (SysConfig config : configs) {
            clearCache(config.getConfigKey());
        }
    }

    @Override
    public void updateStatus(Long configId, String status) {
        SysConfig config = this.getById(configId);
        if (config == null) {
            throw new BusinessException("参数配置不存在");
        }

        config.setStatus(status);
        this.updateById(config);

        // 更新缓存
        if ("0".equals(status)) {
            setConfigCache(config.getConfigKey(), config.getConfigValue());
        } else {
            clearCache(config.getConfigKey());
        }
    }

    @Override
    public String getConfigValue(String configKey) {
        return getConfigValue(configKey, null);
    }

    @Override
    public String getConfigValue(String configKey, String defaultValue) {
        // 先从缓存获取
        String cacheKey = CONFIG_CACHE_KEY + configKey;
        String value = stringRedisTemplate.opsForValue().get(cacheKey);

        // 命中缓存
        if (value != null) {
            // 空值标记，直接返回默认值（防止缓存穿透）
            if (NULL_VALUE.equals(value)) {
                return defaultValue;
            }
            return value;
        }

        // 缓存没有则从数据库获取
        SysConfig config = this.getOne(Wrappers.<SysConfig>lambdaQuery()
                .eq(SysConfig::getConfigKey, configKey)
                .eq(SysConfig::getStatus, "0"));

        if (config != null && StrUtil.isNotBlank(config.getConfigValue())) {
            // 写入缓存
            setConfigCache(configKey, config.getConfigValue());
            return config.getConfigValue();
        }

        // 缓存空值，防止缓存穿透
        stringRedisTemplate.opsForValue().set(cacheKey, NULL_VALUE, NULL_EXPIRE_SECONDS, TimeUnit.SECONDS);
        return defaultValue;
    }

    @Override
    public boolean getConfigBoolean(String configKey) {
        String value = getConfigValue(configKey, "false");
        return "true".equalsIgnoreCase(value) || "1".equals(value) || "Y".equalsIgnoreCase(value);
    }

    @Override
    public Integer getConfigInteger(String configKey) {
        String value = getConfigValue(configKey);
        if (StrUtil.isNotBlank(value)) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                log.warn("参数[{}]的值[{}]无法转换为整数", configKey, value);
            }
        }
        return null;
    }

    @Override
    public void refreshCache() {
        log.info("开始刷新系统参数配置缓存...");

        // 使用SCAN命令替代KEYS命令，避免阻塞Redis
        int deletedCount = clearAllConfigCacheUsingScan();

        // 清除键名集合
        stringRedisTemplate.delete(CONFIG_KEYS_SET);

        // 重新加载启用状态的配置
        List<SysConfig> configs = this.list(Wrappers.<SysConfig>lambdaQuery()
                .eq(SysConfig::getStatus, "0"));

        for (SysConfig config : configs) {
            if (StrUtil.isNotBlank(config.getConfigValue())) {
                setConfigCache(config.getConfigKey(), config.getConfigValue());
            }
        }
        log.info("系统参数配置缓存刷新完成，删除{}个旧缓存，加载{}条配置", deletedCount, configs.size());
    }

    /**
     * 使用SCAN命令清除所有配置缓存（替代KEYS命令，避免阻塞Redis）
     *
     * @return 删除的缓存数量
     */
    private int clearAllConfigCacheUsingScan() {
        int deletedCount = 0;
        ScanOptions options = ScanOptions.scanOptions()
                .match(CONFIG_CACHE_KEY + "*")
                .count(100)
                .build();

        try (Cursor<String> cursor = stringRedisTemplate.scan(options)) {
            List<String> batch = new ArrayList<>();
            while (cursor.hasNext()) {
                batch.add(cursor.next());
                if (batch.size() >= 100) {
                    stringRedisTemplate.delete(batch);
                    deletedCount += batch.size();
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                stringRedisTemplate.delete(batch);
                deletedCount += batch.size();
            }
        } catch (Exception e) {
            log.error("使用SCAN清除配置缓存失败", e);
        }

        return deletedCount;
    }

    @Override
    public void clearCache(String configKey) {
        String cacheKey = CONFIG_CACHE_KEY + configKey;
        stringRedisTemplate.delete(cacheKey);
        // 从键名集合中移除
        stringRedisTemplate.opsForSet().remove(CONFIG_KEYS_SET, configKey);
    }

    /**
     * 设置配置缓存
     */
    private void setConfigCache(String configKey, String configValue) {
        String cacheKey = CONFIG_CACHE_KEY + configKey;
        stringRedisTemplate.opsForValue().set(cacheKey, configValue != null ? configValue : "");
        // 记录键名到集合中，便于后续维护
        stringRedisTemplate.opsForSet().add(CONFIG_KEYS_SET, configKey);
    }

    /**
     * 检查参数键名是否存在
     */
    private boolean checkConfigKeyExists(String configKey, Long excludeConfigId) {
        LambdaQueryWrapper<SysConfig> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysConfig::getConfigKey, configKey);
        if (excludeConfigId != null) {
            wrapper.ne(SysConfig::getConfigId, excludeConfigId);
        }
        return this.count(wrapper) > 0;
    }
}
