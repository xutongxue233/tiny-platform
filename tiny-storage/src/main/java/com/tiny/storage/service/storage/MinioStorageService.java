package com.tiny.storage.service.storage;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.tiny.storage.entity.StorageConfig;
import com.tiny.storage.enums.StorageTypeEnum;
import com.tiny.storage.exception.StorageException;
import com.tiny.storage.vo.FileUploadVO;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * MinIO存储服务实现
 */
@Slf4j
public class MinioStorageService extends AbstractStorageService {

    private MinioClient minioClient;
    private String bucketName;
    private String domain;

    @Override
    public String getStorageType() {
        return StorageTypeEnum.MINIO.getCode();
    }

    @Override
    public void init(StorageConfig config) {
        super.init(config);
        this.bucketName = config.getBucketName();
        this.domain = config.getDomain();

        // 创建MinIO客户端
        this.minioClient = MinioClient.builder()
                .endpoint(config.getEndpoint())
                .credentials(config.getAccessKeyId(), config.getAccessKeySecret())
                .build();

        // 检查并创建存储桶
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("MinIO存储桶创建成功: {}", bucketName);
            }
        } catch (Exception e) {
            log.warn("MinIO存储桶检查失败: {}", e.getMessage());
        }

        log.info("MinIO存储初始化完成，Bucket: {}", bucketName);
    }

    @Override
    public FileUploadVO upload(InputStream inputStream, String path, String filename) {
        checkInitialized();
        try {
            String storedFilename = generateStoredFilename(filename);
            String storagePath = StrUtil.isNotBlank(path) ? path + "/" + storedFilename : generateStoragePath(filename);

            // 获取内容类型
            String mimeType = FileUtil.getMimeType(filename);
            String contentType = mimeType != null ? mimeType : "application/octet-stream";

            // 上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(storagePath)
                    .stream(inputStream, -1, 10485760)
                    .contentType(contentType)
                    .build());

            // 获取文件信息
            StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(storagePath)
                    .build());

            // 构建访问URL
            String url = buildUrl(storagePath);

            return FileUploadVO.builder()
                    .originalFilename(filename)
                    .storedFilename(storedFilename)
                    .filePath(storagePath)
                    .fileSize(stat.size())
                    .fileType(contentType)
                    .url(url)
                    .storageType(getStorageType())
                    .build();
        } catch (Exception e) {
            log.error("MinIO上传失败: {}", e.getMessage(), e);
            throw new StorageException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public InputStream download(String filePath) {
        checkInitialized();
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filePath)
                    .build());
        } catch (Exception e) {
            log.error("MinIO下载失败: {}", e.getMessage(), e);
            throw new StorageException("文件下载失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String filePath) {
        checkInitialized();
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filePath)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("MinIO删除失败: {}", e.getMessage(), e);
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
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(filePath)
                    .method(Method.GET)
                    .expiry((int) expireTime, TimeUnit.SECONDS)
                    .build());
        } catch (Exception e) {
            log.error("MinIO生成临时URL失败: {}", e.getMessage(), e);
            throw new StorageException("生成临时URL失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean exists(String filePath) {
        checkInitialized();
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filePath)
                    .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 构建文件访问URL
     */
    private String buildUrl(String filePath) {
        if (StrUtil.isNotBlank(domain)) {
            String protocol = "1".equals(config.getUseHttps()) ? "https://" : "http://";
            return protocol + domain + "/" + bucketName + "/" + filePath;
        }
        return config.getEndpoint() + "/" + bucketName + "/" + filePath;
    }
}
