package com.tiny.storage.service.storage;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.tiny.storage.entity.StorageConfig;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 存储服务抽象基类
 * 提供通用方法实现
 */
@Slf4j
public abstract class AbstractStorageService implements StorageService {

    protected StorageConfig config;

    protected volatile boolean initialized = false;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @Override
    public void init(StorageConfig config) {
        this.config = config;
        this.initialized = true;
        log.info("存储服务初始化完成，类型: {}", getStorageType());
    }

    /**
     * 生成存储路径
     * 格式：basePath/yyyy/MM/dd/uuid.ext
     *
     * @param filename 原始文件名
     * @return 存储路径
     */
    protected String generateStoragePath(String filename) {
        String datePath = LocalDate.now().format(DATE_FORMATTER);
        String ext = FileUtil.extName(filename);
        String storedFilename = IdUtil.fastSimpleUUID() + "." + ext;
        return datePath + "/" + storedFilename;
    }

    /**
     * 获取存储的文件名
     *
     * @param filename 原始文件名
     * @return 存储文件名
     */
    protected String generateStoredFilename(String filename) {
        String ext = FileUtil.extName(filename);
        return IdUtil.fastSimpleUUID() + "." + ext;
    }

    /**
     * 获取文件扩展名
     */
    protected String getFileExtension(String filename) {
        return FileUtil.extName(filename);
    }

    /**
     * 检查是否已初始化
     */
    protected void checkInitialized() {
        if (!initialized) {
            throw new IllegalStateException("存储服务未初始化");
        }
    }

    @Override
    public void destroy() {
        this.initialized = false;
        log.info("存储服务已销毁，类型: {}", getStorageType());
    }
}