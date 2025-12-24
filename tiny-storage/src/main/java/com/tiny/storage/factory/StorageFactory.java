package com.tiny.storage.factory;

import com.tiny.storage.entity.StorageConfig;
import com.tiny.storage.enums.StorageTypeEnum;
import com.tiny.storage.exception.StorageException;
import com.tiny.storage.service.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储服务工厂
 * 使用策略模式管理不同类型的存储服务实例
 * 支持动态切换存储方式，无需重启服务
 */
@Slf4j
@Component
public class StorageFactory {

    /**
     * 存储服务实例缓存
     * key: configId
     * value: StorageService实例
     */
    private final Map<Long, StorageService> storageServiceCache = new ConcurrentHashMap<>();

    /**
     * 当前默认存储服务
     */
    private volatile StorageService defaultStorageService;

    /**
     * 当前默认配置ID
     */
    private volatile Long defaultConfigId;

    /**
     * 获取默认存储服务
     *
     * @return 存储服务实例
     */
    public StorageService getDefaultStorage() {
        if (defaultStorageService == null) {
            throw new StorageException("未配置默认存储服务");
        }
        return defaultStorageService;
    }

    /**
     * 根据配置ID获取存储服务
     *
     * @param configId 配置ID
     * @return 存储服务实例
     */
    public StorageService getStorage(Long configId) {
        StorageService storageService = storageServiceCache.get(configId);
        if (storageService == null) {
            throw new StorageException("存储服务不存在，配置ID: " + configId);
        }
        return storageService;
    }

    /**
     * 根据存储类型创建存储服务实例
     *
     * @param storageType 存储类型
     * @return 存储服务实例
     */
    public StorageService createStorageService(String storageType) {
        StorageTypeEnum type = StorageTypeEnum.getByCode(storageType);
        if (type == null) {
            throw new StorageException("不支持的存储类型: " + storageType);
        }

        return switch (type) {
            case LOCAL -> new LocalStorageService();
            case ALIYUN_OSS -> new AliyunOssStorageService();
            case MINIO -> new MinioStorageService();
            case AWS_S3 -> new AwsS3StorageService();
            case TENCENT_COS -> new TencentCosStorageService();
        };
    }

    /**
     * 注册存储服务
     *
     * @param config 存储配置
     */
    public synchronized void registerStorage(StorageConfig config) {
        Long configId = config.getConfigId();

        // 如果存在旧的服务实例，先销毁
        StorageService oldService = storageServiceCache.get(configId);
        if (oldService != null) {
            try {
                oldService.destroy();
            } catch (Exception e) {
                log.warn("销毁旧存储服务失败: {}", e.getMessage());
            }
        }

        // 创建新的服务实例
        StorageService newService = createStorageService(config.getStorageType());
        newService.init(config);

        // 放入缓存
        storageServiceCache.put(configId, newService);

        // 如果是默认配置，更新默认服务
        if ("1".equals(config.getIsDefault())) {
            setDefaultStorage(configId, newService);
        }

        log.info("存储服务注册成功，配置ID: {}, 类型: {}", configId, config.getStorageType());
    }

    /**
     * 设置默认存储服务
     *
     * @param configId 配置ID
     * @param service  存储服务实例
     */
    public synchronized void setDefaultStorage(Long configId, StorageService service) {
        this.defaultConfigId = configId;
        this.defaultStorageService = service;
        log.info("已设置默认存储服务，配置ID: {}", configId);
    }

    /**
     * 设置默认存储服务
     *
     * @param configId 配置ID
     */
    public synchronized void setDefaultStorage(Long configId) {
        StorageService service = storageServiceCache.get(configId);
        if (service == null) {
            throw new StorageException("存储服务不存在，配置ID: " + configId);
        }
        setDefaultStorage(configId, service);
    }

    /**
     * 注销存储服务
     *
     * @param configId 配置ID
     */
    public synchronized void unregisterStorage(Long configId) {
        StorageService service = storageServiceCache.remove(configId);
        if (service != null) {
            try {
                service.destroy();
            } catch (Exception e) {
                log.warn("销毁存储服务失败: {}", e.getMessage());
            }
        }

        // 如果是默认服务，清除默认设置
        if (configId.equals(defaultConfigId)) {
            defaultConfigId = null;
            defaultStorageService = null;
        }

        log.info("存储服务已注销，配置ID: {}", configId);
    }

    /**
     * 刷新存储服务
     *
     * @param config 存储配置
     */
    public void refreshStorage(StorageConfig config) {
        registerStorage(config);
    }

    /**
     * 检查存储服务是否存在
     *
     * @param configId 配置ID
     * @return 是否存在
     */
    public boolean hasStorage(Long configId) {
        return storageServiceCache.containsKey(configId);
    }

    /**
     * 获取当前默认配置ID
     *
     * @return 默认配置ID
     */
    public Long getDefaultConfigId() {
        return defaultConfigId;
    }

    /**
     * 销毁所有存储服务
     */
    public synchronized void destroyAll() {
        storageServiceCache.values().forEach(service -> {
            try {
                service.destroy();
            } catch (Exception e) {
                log.warn("销毁存储服务失败: {}", e.getMessage());
            }
        });
        storageServiceCache.clear();
        defaultStorageService = null;
        defaultConfigId = null;
        log.info("所有存储服务已销毁");
    }
}
