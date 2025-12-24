package com.tiny.storage.service.storage;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.tiny.storage.entity.StorageConfig;
import com.tiny.storage.enums.StorageTypeEnum;
import com.tiny.storage.exception.StorageException;
import com.tiny.storage.vo.FileUploadVO;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * 阿里云OSS存储服务实现
 */
@Slf4j
public class AliyunOssStorageService extends AbstractStorageService {

    private OSS ossClient;
    private String bucketName;
    private String domain;

    @Override
    public String getStorageType() {
        return StorageTypeEnum.ALIYUN_OSS.getCode();
    }

    @Override
    public void init(StorageConfig config) {
        super.init(config);
        this.bucketName = config.getBucketName();
        this.domain = config.getDomain();

        // 创建OSS客户端
        this.ossClient = new OSSClientBuilder().build(
                config.getEndpoint(),
                config.getAccessKeyId(),
                config.getAccessKeySecret()
        );
        log.info("阿里云OSS存储初始化完成，Bucket: {}", bucketName);
    }

    @Override
    public FileUploadVO upload(InputStream inputStream, String path, String filename) {
        checkInitialized();
        try {
            String storedFilename = generateStoredFilename(filename);
            String storagePath = StrUtil.isNotBlank(path) ? path + "/" + storedFilename : generateStoragePath(filename);

            // 设置文件元数据
            ObjectMetadata metadata = new ObjectMetadata();
            String mimeType = FileUtil.getMimeType(filename);
            metadata.setContentType(mimeType != null ? mimeType : "application/octet-stream");

            // 上传文件
            ossClient.putObject(bucketName, storagePath, inputStream, metadata);

            // 获取文件信息
            ObjectMetadata objectMetadata = ossClient.getObjectMetadata(bucketName, storagePath);
            long fileSize = objectMetadata.getContentLength();

            // 构建访问URL
            String url = buildUrl(storagePath);

            return FileUploadVO.builder()
                    .originalFilename(filename)
                    .storedFilename(storedFilename)
                    .filePath(storagePath)
                    .fileSize(fileSize)
                    .fileType(objectMetadata.getContentType())
                    .url(url)
                    .storageType(getStorageType())
                    .build();
        } catch (Exception e) {
            log.error("阿里云OSS上传失败: {}", e.getMessage(), e);
            throw new StorageException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public InputStream download(String filePath) {
        checkInitialized();
        try {
            OSSObject ossObject = ossClient.getObject(bucketName, filePath);
            return ossObject.getObjectContent();
        } catch (Exception e) {
            log.error("阿里云OSS下载失败: {}", e.getMessage(), e);
            throw new StorageException("文件下载失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String filePath) {
        checkInitialized();
        try {
            ossClient.deleteObject(bucketName, filePath);
            return true;
        } catch (Exception e) {
            log.error("阿里云OSS删除失败: {}", e.getMessage(), e);
            throw new StorageException("文件删除失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String getUrl(String filePath) {
        checkInitialized();
        return buildUrl(filePath);
    }

    @Override
    public String getUrl(String filePath, long expireTime) {
        checkInitialized();
        try {
            Date expiration = new Date(System.currentTimeMillis() + expireTime * 1000);
            URL url = ossClient.generatePresignedUrl(bucketName, filePath, expiration);
            return url.toString();
        } catch (Exception e) {
            log.error("阿里云OSS生成临时URL失败: {}", e.getMessage(), e);
            throw new StorageException("生成临时URL失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean exists(String filePath) {
        checkInitialized();
        try {
            return ossClient.doesObjectExist(bucketName, filePath);
        } catch (Exception e) {
            log.error("阿里云OSS检查文件存在失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (ossClient != null) {
            ossClient.shutdown();
            ossClient = null;
        }
    }

    /**
     * 构建文件访问URL
     */
    private String buildUrl(String filePath) {
        if (StrUtil.isNotBlank(domain)) {
            String protocol = "1".equals(config.getUseHttps()) ? "https://" : "http://";
            return protocol + domain + "/" + filePath;
        }
        return "https://" + bucketName + "." + config.getEndpoint() + "/" + filePath;
    }
}
