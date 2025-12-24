package com.tiny.storage.service.storage;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.tiny.storage.entity.StorageConfig;
import com.tiny.storage.enums.StorageTypeEnum;
import com.tiny.storage.exception.StorageException;
import com.tiny.storage.vo.FileUploadVO;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.net.URI;
import java.time.Duration;

/**
 * AWS S3存储服务实现
 */
@Slf4j
public class AwsS3StorageService extends AbstractStorageService {

    private S3Client s3Client;
    private S3Presigner s3Presigner;
    private String bucketName;
    private String domain;
    private String region;

    @Override
    public String getStorageType() {
        return StorageTypeEnum.AWS_S3.getCode();
    }

    @Override
    public void init(StorageConfig config) {
        super.init(config);
        this.bucketName = config.getBucketName();
        this.domain = config.getDomain();
        this.region = config.getRegion();

        // 创建凭证
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                config.getAccessKeyId(),
                config.getAccessKeySecret()
        );
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

        // 构建S3客户端
        var builder = S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.of(region));

        // 如果有自定义endpoint
        if (StrUtil.isNotBlank(config.getEndpoint())) {
            builder.endpointOverride(URI.create(config.getEndpoint()));
        }

        this.s3Client = builder.build();

        // 构建Presigner
        var presignerBuilder = S3Presigner.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.of(region));

        if (StrUtil.isNotBlank(config.getEndpoint())) {
            presignerBuilder.endpointOverride(URI.create(config.getEndpoint()));
        }

        this.s3Presigner = presignerBuilder.build();

        log.info("AWS S3存储初始化完成，Bucket: {}, Region: {}", bucketName, region);
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

            // 读取输入流内容
            byte[] content = inputStream.readAllBytes();

            // 上传文件
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(storagePath)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(content));

            // 获取文件信息
            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(storagePath)
                    .build();
            HeadObjectResponse headResponse = s3Client.headObject(headRequest);

            // 构建访问URL
            String url = buildUrl(storagePath);

            return FileUploadVO.builder()
                    .originalFilename(filename)
                    .storedFilename(storedFilename)
                    .filePath(storagePath)
                    .fileSize(headResponse.contentLength())
                    .fileType(contentType)
                    .url(url)
                    .storageType(getStorageType())
                    .build();
        } catch (Exception e) {
            log.error("AWS S3上传失败: {}", e.getMessage(), e);
            throw new StorageException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public InputStream download(String filePath) {
        checkInitialized();
        try {
            GetObjectRequest getRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath)
                    .build();
            return s3Client.getObject(getRequest);
        } catch (Exception e) {
            log.error("AWS S3下载失败: {}", e.getMessage(), e);
            throw new StorageException("文件下载失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String filePath) {
        checkInitialized();
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath)
                    .build();
            s3Client.deleteObject(deleteRequest);
            return true;
        } catch (Exception e) {
            log.error("AWS S3删除失败: {}", e.getMessage(), e);
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
            GetObjectRequest getRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(getRequest)
                    .signatureDuration(Duration.ofSeconds(expireTime))
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            return presignedRequest.url().toString();
        } catch (Exception e) {
            log.error("AWS S3生成临时URL失败: {}", e.getMessage(), e);
            throw new StorageException("生成临时URL失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean exists(String filePath) {
        checkInitialized();
        try {
            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filePath)
                    .build();
            s3Client.headObject(headRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (Exception e) {
            log.error("AWS S3检查文件存在失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (s3Client != null) {
            s3Client.close();
            s3Client = null;
        }
        if (s3Presigner != null) {
            s3Presigner.close();
            s3Presigner = null;
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
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + filePath;
    }
}
