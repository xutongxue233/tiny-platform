package com.tiny.storage.service.storage;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.tiny.storage.entity.StorageConfig;
import com.tiny.storage.enums.StorageTypeEnum;
import com.tiny.storage.exception.StorageException;
import com.tiny.storage.vo.FileUploadVO;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * 腾讯云COS存储服务实现
 */
@Slf4j
public class TencentCosStorageService extends AbstractStorageService {

    private COSClient cosClient;
    private String bucketName;
    private String domain;

    @Override
    public String getStorageType() {
        return StorageTypeEnum.TENCENT_COS.getCode();
    }

    @Override
    public void init(StorageConfig config) {
        super.init(config);
        this.bucketName = config.getBucketName();
        this.domain = config.getDomain();

        // 创建COS凭证
        COSCredentials credentials = new BasicCOSCredentials(
                config.getAccessKeyId(),
                config.getAccessKeySecret()
        );

        // 创建客户端配置
        Region region = new Region(config.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);

        // 设置协议
        if ("1".equals(config.getUseHttps())) {
            clientConfig.setHttpProtocol(HttpProtocol.https);
        } else {
            clientConfig.setHttpProtocol(HttpProtocol.http);
        }

        // 创建COS客户端
        this.cosClient = new COSClient(credentials, clientConfig);
        log.info("腾讯云COS存储初始化完成，Bucket: {}, Region: {}", bucketName, config.getRegion());
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
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, storagePath, inputStream, metadata);
            cosClient.putObject(putObjectRequest);

            // 获取文件信息
            ObjectMetadata objectMetadata = cosClient.getObjectMetadata(bucketName, storagePath);
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
            log.error("腾讯云COS上传失败: {}", e.getMessage(), e);
            throw new StorageException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public InputStream download(String filePath) {
        checkInitialized();
        try {
            COSObject cosObject = cosClient.getObject(bucketName, filePath);
            return cosObject.getObjectContent();
        } catch (Exception e) {
            log.error("腾讯云COS下载失败: {}", e.getMessage(), e);
            throw new StorageException("文件下载失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String filePath) {
        checkInitialized();
        try {
            cosClient.deleteObject(bucketName, filePath);
            return true;
        } catch (Exception e) {
            log.error("腾讯云COS删除失败: {}", e.getMessage(), e);
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
            URL url = cosClient.generatePresignedUrl(bucketName, filePath, expiration);
            return url.toString();
        } catch (Exception e) {
            log.error("腾讯云COS生成临时URL失败: {}", e.getMessage(), e);
            throw new StorageException("生成临时URL失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean exists(String filePath) {
        checkInitialized();
        try {
            return cosClient.doesObjectExist(bucketName, filePath);
        } catch (Exception e) {
            log.error("腾讯云COS检查文件存在失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (cosClient != null) {
            cosClient.shutdown();
            cosClient = null;
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
        String protocol = "1".equals(config.getUseHttps()) ? "https://" : "http://";
        return protocol + bucketName + ".cos." + config.getRegion() + ".myqcloud.com/" + filePath;
    }
}
