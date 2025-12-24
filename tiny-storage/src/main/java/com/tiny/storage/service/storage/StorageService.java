package com.tiny.storage.service.storage;

import com.tiny.storage.entity.StorageConfig;
import com.tiny.storage.vo.FileUploadVO;

import java.io.InputStream;

/**
 * 统一存储服务接口
 * 所有存储实现都必须实现此接口
 */
public interface StorageService {

    /**
     * 获取存储类型
     *
     * @return 存储类型代码
     */
    String getStorageType();

    /**
     * 初始化存储客户端
     *
     * @param config 存储配置
     */
    void init(StorageConfig config);

    /**
     * 上传文件
     *
     * @param inputStream 文件输入流
     * @param path        存储路径
     * @param filename    文件名
     * @return 上传结果
     */
    FileUploadVO upload(InputStream inputStream, String path, String filename);

    /**
     * 下载文件
     *
     * @param filePath 文件路径
     * @return 文件输入流
     */
    InputStream download(String filePath);

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    boolean delete(String filePath);

    /**
     * 获取文件访问URL
     *
     * @param filePath 文件路径
     * @return 文件访问URL
     */
    String getUrl(String filePath);

    /**
     * 获取文件访问URL（带过期时间）
     *
     * @param filePath   文件路径
     * @param expireTime 过期时间（秒）
     * @return 文件访问URL
     */
    String getUrl(String filePath, long expireTime);

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return 是否存在
     */
    boolean exists(String filePath);

    /**
     * 销毁存储客户端
     */
    void destroy();
}