package com.tiny.storage.service;

import com.tiny.common.core.page.PageResult;
import com.tiny.storage.dto.FileRecordQueryDTO;
import com.tiny.storage.vo.FileRecordVO;
import com.tiny.storage.vo.FileUploadVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * 文件记录服务接口
 */
public interface FileRecordService {

    /**
     * 分页查询文件记录
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResult<FileRecordVO> page(FileRecordQueryDTO queryDTO);

    /**
     * 根据ID查询文件记录
     *
     * @param fileId 文件ID
     * @return 文件记录
     */
    FileRecordVO getById(Long fileId);

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 上传结果
     */
    FileUploadVO upload(MultipartFile file);

    /**
     * 上传文件到指定路径
     *
     * @param file 文件
     * @param path 存储路径
     * @return 上传结果
     */
    FileUploadVO upload(MultipartFile file, String path);

    /**
     * 使用指定存储配置上传文件
     *
     * @param file     文件
     * @param configId 存储配置ID
     * @return 上传结果
     */
    FileUploadVO uploadWithConfig(MultipartFile file, Long configId);

    /**
     * 批量上传文件
     *
     * @param files 文件列表
     * @return 上传结果列表
     */
    List<FileUploadVO> uploadBatch(MultipartFile[] files);

    /**
     * 下载文件
     *
     * @param fileId 文件ID
     * @return 文件输入流
     */
    InputStream download(Long fileId);

    /**
     * 删除文件
     *
     * @param fileId 文件ID
     * @return 删除结果
     */
    boolean delete(Long fileId);

    /**
     * 批量删除文件
     *
     * @param fileIds 文件ID列表
     * @return 删除结果
     */
    boolean deleteBatch(List<Long> fileIds);

    /**
     * 获取文件访问URL
     *
     * @param fileId 文件ID
     * @return 文件URL
     */
    String getUrl(Long fileId);

    /**
     * 获取文件临时访问URL
     *
     * @param fileId     文件ID
     * @param expireTime 过期时间（秒）
     * @return 临时URL
     */
    String getTempUrl(Long fileId, long expireTime);
}
