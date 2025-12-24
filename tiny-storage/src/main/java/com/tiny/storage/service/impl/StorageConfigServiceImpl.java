package com.tiny.storage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.exception.BusinessException;
import com.tiny.storage.dto.StorageConfigDTO;
import com.tiny.storage.dto.StorageConfigQueryDTO;
import com.tiny.storage.entity.StorageConfig;
import com.tiny.storage.enums.StorageTypeEnum;
import com.tiny.storage.exception.StorageException;
import com.tiny.storage.factory.StorageFactory;
import com.tiny.storage.mapper.StorageConfigMapper;
import com.tiny.storage.service.StorageConfigService;
import com.tiny.storage.service.storage.StorageService;
import com.tiny.storage.vo.StorageConfigVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 存储配置服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StorageConfigServiceImpl implements StorageConfigService {

    private final StorageConfigMapper storageConfigMapper;
    private final StorageFactory storageFactory;

    /**
     * 应用启动时初始化所有存储服务
     */
    @PostConstruct
    public void init() {
        initAllStorage();
    }

    @Override
    public PageResult<StorageConfigVO> page(StorageConfigQueryDTO queryDTO) {
        Page<StorageConfig> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        LambdaQueryWrapper<StorageConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getConfigName()), StorageConfig::getConfigName, queryDTO.getConfigName())
                .eq(StrUtil.isNotBlank(queryDTO.getStorageType()), StorageConfig::getStorageType, queryDTO.getStorageType())
                .eq(StrUtil.isNotBlank(queryDTO.getStatus()), StorageConfig::getStatus, queryDTO.getStatus())
                .orderByDesc(StorageConfig::getIsDefault)
                .orderByDesc(StorageConfig::getCreateTime);

        Page<StorageConfig> result = storageConfigMapper.selectPage(page, wrapper);

        return PageResult.of(result, this::toVO);
    }

    @Override
    public List<StorageConfigVO> list() {
        LambdaQueryWrapper<StorageConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StorageConfig::getStatus, "0")
                .orderByDesc(StorageConfig::getIsDefault)
                .orderByDesc(StorageConfig::getCreateTime);

        return storageConfigMapper.selectList(wrapper).stream()
                .map(this::toVO)
                .toList();
    }

    @Override
    public StorageConfigVO getById(Long configId) {
        StorageConfig config = storageConfigMapper.selectById(configId);
        if (config == null) {
            throw new BusinessException("存储配置不存在");
        }
        return toVO(config);
    }

    @Override
    public StorageConfig getDefaultConfig() {
        LambdaQueryWrapper<StorageConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StorageConfig::getIsDefault, "1")
                .eq(StorageConfig::getStatus, "0");
        return storageConfigMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(StorageConfigDTO dto) {
        // 检查存储类型
        if (StorageTypeEnum.getByCode(dto.getStorageType()) == null) {
            throw new BusinessException("不支持的存储类型");
        }

        StorageConfig config = new StorageConfig();
        BeanUtil.copyProperties(dto, config);

        // 如果设置为默认，先取消其他默认配置
        if ("1".equals(dto.getIsDefault())) {
            cancelOtherDefault(null);
        }

        int result = storageConfigMapper.insert(config);

        // 如果状态为启用，注册存储服务
        if ("0".equals(config.getStatus())) {
            try {
                storageFactory.registerStorage(config);
            } catch (Exception e) {
                log.error("注册存储服务失败: {}", e.getMessage());
            }
        }

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(StorageConfigDTO dto) {
        if (dto.getConfigId() == null) {
            throw new BusinessException("配置ID不能为空");
        }

        StorageConfig existConfig = storageConfigMapper.selectById(dto.getConfigId());
        if (existConfig == null) {
            throw new BusinessException("存储配置不存在");
        }

        StorageConfig config = new StorageConfig();
        BeanUtil.copyProperties(dto, config);

        // 如果设置为默认，先取消其他默认配置
        if ("1".equals(dto.getIsDefault())) {
            cancelOtherDefault(dto.getConfigId());
        }

        int result = storageConfigMapper.updateById(config);

        // 刷新存储服务
        if ("0".equals(config.getStatus())) {
            StorageConfig updatedConfig = storageConfigMapper.selectById(dto.getConfigId());
            try {
                storageFactory.refreshStorage(updatedConfig);
            } catch (Exception e) {
                log.error("刷新存储服务失败: {}", e.getMessage());
            }
        } else {
            // 如果禁用，注销存储服务
            storageFactory.unregisterStorage(dto.getConfigId());
        }

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long configId) {
        StorageConfig config = storageConfigMapper.selectById(configId);
        if (config == null) {
            throw new BusinessException("存储配置不存在");
        }

        if ("1".equals(config.getIsDefault())) {
            throw new BusinessException("默认配置不能删除");
        }

        // 注销存储服务
        storageFactory.unregisterStorage(configId);

        return storageConfigMapper.deleteById(configId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefault(Long configId) {
        StorageConfig config = storageConfigMapper.selectById(configId);
        if (config == null) {
            throw new BusinessException("存储配置不存在");
        }

        if (!"0".equals(config.getStatus())) {
            throw new BusinessException("存储配置未启用，不能设为默认");
        }

        // 取消其他默认配置
        cancelOtherDefault(configId);

        // 设置当前配置为默认
        StorageConfig updateConfig = new StorageConfig();
        updateConfig.setConfigId(configId);
        updateConfig.setIsDefault("1");
        storageConfigMapper.updateById(updateConfig);

        // 更新工厂默认存储服务
        storageFactory.setDefaultStorage(configId);

        return true;
    }

    @Override
    public boolean testConnection(StorageConfigDTO dto) {
        try {
            StorageConfig config = new StorageConfig();
            BeanUtil.copyProperties(dto, config);
            config.setConfigId(-1L);

            // 创建临时存储服务
            StorageService service = storageFactory.createStorageService(dto.getStorageType());
            service.init(config);

            // 尝试上传测试文件
            String testContent = "Storage connection test - " + System.currentTimeMillis();
            ByteArrayInputStream testInputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));
            var uploadResult = service.upload(testInputStream, "test", "connection-test.txt");

            // 删除测试文件
            service.delete(uploadResult.getFilePath());

            // 销毁临时服务
            service.destroy();

            return true;
        } catch (Exception e) {
            log.error("存储连接测试失败: {}", e.getMessage(), e);
            throw new StorageException("连接测试失败: " + e.getMessage());
        }
    }

    @Override
    public void refreshStorage(Long configId) {
        StorageConfig config = storageConfigMapper.selectById(configId);
        if (config == null) {
            throw new BusinessException("存储配置不存在");
        }

        if ("0".equals(config.getStatus())) {
            storageFactory.refreshStorage(config);
        } else {
            storageFactory.unregisterStorage(configId);
        }
    }

    @Override
    public void initAllStorage() {
        log.info("开始初始化所有存储服务...");

        LambdaQueryWrapper<StorageConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StorageConfig::getStatus, "0");

        List<StorageConfig> configs = storageConfigMapper.selectList(wrapper);

        for (StorageConfig config : configs) {
            try {
                storageFactory.registerStorage(config);
            } catch (Exception e) {
                log.error("初始化存储服务失败，配置ID: {}, 错误: {}", config.getConfigId(), e.getMessage());
            }
        }

        log.info("存储服务初始化完成，共注册 {} 个服务", configs.size());
    }

    /**
     * 取消其他默认配置
     */
    private void cancelOtherDefault(Long excludeId) {
        LambdaQueryWrapper<StorageConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StorageConfig::getIsDefault, "1");
        if (excludeId != null) {
            wrapper.ne(StorageConfig::getConfigId, excludeId);
        }

        List<StorageConfig> defaultConfigs = storageConfigMapper.selectList(wrapper);
        for (StorageConfig config : defaultConfigs) {
            StorageConfig updateConfig = new StorageConfig();
            updateConfig.setConfigId(config.getConfigId());
            updateConfig.setIsDefault("0");
            storageConfigMapper.updateById(updateConfig);
        }
    }

    /**
     * 实体转VO
     */
    private StorageConfigVO toVO(StorageConfig config) {
        StorageConfigVO vo = new StorageConfigVO();
        BeanUtil.copyProperties(config, vo);

        // 设置存储类型描述
        StorageTypeEnum type = StorageTypeEnum.getByCode(config.getStorageType());
        if (type != null) {
            vo.setStorageTypeDesc(type.getDesc());
        }

        return vo;
    }
}
