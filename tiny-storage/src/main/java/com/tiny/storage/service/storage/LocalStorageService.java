package com.tiny.storage.service.storage;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.tiny.storage.entity.StorageConfig;
import com.tiny.storage.enums.StorageTypeEnum;
import com.tiny.storage.exception.StorageException;
import com.tiny.storage.vo.FileUploadVO;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * 本地存储服务实现
 */
@Slf4j
public class LocalStorageService extends AbstractStorageService {

    private String basePath;
    private String urlPrefix;

    @Override
    public String getStorageType() {
        return StorageTypeEnum.LOCAL.getCode();
    }

    @Override
    public void init(StorageConfig config) {
        super.init(config);
        this.basePath = config.getLocalPath();
        this.urlPrefix = config.getLocalUrlPrefix();

        // 确保存储目录存在
        File baseDir = new File(basePath);
        if (!baseDir.exists()) {
            boolean created = baseDir.mkdirs();
            if (!created) {
                throw new StorageException("无法创建本地存储目录: " + basePath);
            }
        }
        log.info("本地存储初始化完成，路径: {}", basePath);
    }

    @Override
    public FileUploadVO upload(InputStream inputStream, String path, String filename) {
        checkInitialized();
        try {
            String storedFilename = generateStoredFilename(filename);
            String storagePath = StrUtil.isNotBlank(path) ? path + "/" + storedFilename : generateStoragePath(filename);
            String fullPath = basePath + "/" + storagePath;

            // 确保目录存在
            Path filePath = Paths.get(fullPath);
            Files.createDirectories(filePath.getParent());

            // 写入文件
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            // 获取文件大小
            long fileSize = Files.size(filePath);

            // 构建访问URL
            String url = urlPrefix + "/" + storagePath;

            // 获取内容类型
            String mimeType = FileUtil.getMimeType(fullPath);
            String contentType = mimeType != null ? mimeType : "application/octet-stream";

            return FileUploadVO.builder()
                    .originalFilename(filename)
                    .storedFilename(storedFilename)
                    .filePath(storagePath)
                    .fileSize(fileSize)
                    .fileType(contentType)
                    .url(url)
                    .storageType(getStorageType())
                    .build();
        } catch (Exception e) {
            log.error("本地存储上传失败: {}", e.getMessage(), e);
            throw new StorageException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public InputStream download(String filePath) {
        checkInitialized();
        try {
            String fullPath = basePath + "/" + filePath;
            File file = new File(fullPath);
            if (!file.exists()) {
                throw new StorageException("文件不存在: " + filePath);
            }
            return new FileInputStream(file);
        } catch (StorageException e) {
            throw e;
        } catch (Exception e) {
            log.error("本地存储下载失败: {}", e.getMessage(), e);
            throw new StorageException("文件下载失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String filePath) {
        checkInitialized();
        try {
            String fullPath = basePath + "/" + filePath;
            return FileUtil.del(fullPath);
        } catch (Exception e) {
            log.error("本地存储删除失败: {}", e.getMessage(), e);
            throw new StorageException("文件删除失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String getUrl(String filePath) {
        checkInitialized();
        return urlPrefix + "/" + filePath;
    }

    @Override
    public String getUrl(String filePath, long expireTime) {
        // 本地存储不支持临时URL，直接返回永久URL
        return getUrl(filePath);
    }

    @Override
    public boolean exists(String filePath) {
        checkInitialized();
        String fullPath = basePath + "/" + filePath;
        return FileUtil.exist(fullPath);
    }
}
