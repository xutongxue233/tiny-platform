package com.tiny.storage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.exception.BusinessException;
import com.tiny.storage.dto.FileRecordQueryDTO;
import com.tiny.storage.entity.FileRecord;
import com.tiny.storage.entity.StorageConfig;
import com.tiny.storage.factory.StorageFactory;
import com.tiny.storage.mapper.FileRecordMapper;
import com.tiny.storage.service.FileRecordService;
import com.tiny.storage.service.StorageConfigService;
import com.tiny.storage.service.storage.StorageService;
import com.tiny.storage.vo.FileRecordVO;
import com.tiny.storage.vo.FileUploadVO;
import com.tiny.system.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件记录服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileRecordServiceImpl implements FileRecordService {

    private final FileRecordMapper fileRecordMapper;
    private final StorageConfigService storageConfigService;
    private final StorageFactory storageFactory;
    private final SysConfigService configService;

    @Override
    public PageResult<FileRecordVO> page(FileRecordQueryDTO queryDTO) {
        Page<FileRecord> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        LambdaQueryWrapper<FileRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getOriginalFilename()), FileRecord::getOriginalFilename, queryDTO.getOriginalFilename())
                .eq(StrUtil.isNotBlank(queryDTO.getStorageType()), FileRecord::getStorageType, queryDTO.getStorageType())
                .eq(StrUtil.isNotBlank(queryDTO.getFileType()), FileRecord::getFileType, queryDTO.getFileType())
                .orderByDesc(FileRecord::getCreateTime);

        Page<FileRecord> result = fileRecordMapper.selectPage(page, wrapper);

        return PageResult.of(result, this::toVO);
    }

    @Override
    public FileRecordVO getById(Long fileId) {
        FileRecord record = fileRecordMapper.selectById(fileId);
        if (record == null) {
            throw new BusinessException("文件记录不存在");
        }
        return toVO(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileUploadVO upload(MultipartFile file) {
        return upload(file, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileUploadVO upload(MultipartFile file, String path) {
        // 获取默认存储配置
        StorageConfig config = storageConfigService.getDefaultConfig();
        if (config == null) {
            throw new BusinessException("未配置默认存储");
        }
        return doUpload(file, path, config.getConfigId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileUploadVO uploadWithConfig(MultipartFile file, Long configId) {
        return doUpload(file, null, configId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FileUploadVO> uploadBatch(MultipartFile[] files) {
        List<FileUploadVO> results = new ArrayList<>();
        for (MultipartFile file : files) {
            results.add(upload(file));
        }
        return results;
    }

    @Override
    public InputStream download(Long fileId) {
        FileRecord record = fileRecordMapper.selectById(fileId);
        if (record == null) {
            throw new BusinessException("文件不存在");
        }

        StorageService storageService = storageFactory.getStorage(record.getConfigId());
        return storageService.download(record.getFilePath());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long fileId) {
        FileRecord record = fileRecordMapper.selectById(fileId);
        if (record == null) {
            throw new BusinessException("文件不存在");
        }

        // 从存储中删除
        try {
            if (storageFactory.hasStorage(record.getConfigId())) {
                StorageService storageService = storageFactory.getStorage(record.getConfigId());
                storageService.delete(record.getFilePath());
            }
        } catch (Exception e) {
            log.warn("从存储中删除文件失败: {}", e.getMessage());
        }

        // 删除记录
        return fileRecordMapper.deleteById(fileId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(List<Long> fileIds) {
        for (Long fileId : fileIds) {
            delete(fileId);
        }
        return true;
    }

    @Override
    public String getUrl(Long fileId) {
        FileRecord record = fileRecordMapper.selectById(fileId);
        if (record == null) {
            throw new BusinessException("文件不存在");
        }

        if (storageFactory.hasStorage(record.getConfigId())) {
            StorageService storageService = storageFactory.getStorage(record.getConfigId());
            return storageService.getUrl(record.getFilePath());
        }

        return record.getFileUrl();
    }

    @Override
    public String getTempUrl(Long fileId, long expireTime) {
        FileRecord record = fileRecordMapper.selectById(fileId);
        if (record == null) {
            throw new BusinessException("文件不存在");
        }

        StorageService storageService = storageFactory.getStorage(record.getConfigId());
        return storageService.getUrl(record.getFilePath(), expireTime);
    }

    /**
     * 校验文件大小
     */
    private void validateFileSize(long fileSize) {
        Integer maxSizeMB = configService.getConfigInteger("sys.upload.maxSize");
        int maxSize = (maxSizeMB != null ? maxSizeMB : 10);
        long maxSizeBytes = (long) maxSize * 1024 * 1024;
        if (fileSize > maxSizeBytes) {
            throw new BusinessException("文件大小不能超过" + maxSize + "MB");
        }
    }

    /**
     * 执行上传
     */
    private FileUploadVO doUpload(MultipartFile file, String path, Long configId) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        // 校验文件大小
        validateFileSize(file.getSize());

        try {
            String originalFilename = file.getOriginalFilename();
            String contentType = file.getContentType();
            long fileSize = file.getSize();

            // 计算文件MD5
            String fileMd5 = DigestUtil.md5Hex(file.getInputStream());

            // 获取存储服务
            StorageService storageService = storageFactory.getStorage(configId);

            // 上传文件
            FileUploadVO uploadVO = storageService.upload(file.getInputStream(), path, originalFilename);

            // 保存文件记录
            FileRecord record = new FileRecord();
            BeanUtil.copyProperties(uploadVO, record);
            record.setConfigId(configId);
            record.setFileSize(fileSize);
            record.setFileType(contentType);
            record.setFileExt(FileUtil.extName(originalFilename));
            record.setFileUrl(uploadVO.getUrl());
            record.setFileMd5(fileMd5);

            fileRecordMapper.insert(record);

            // 设置文件ID
            uploadVO.setFileId(record.getFileId());
            uploadVO.setFileSize(fileSize);

            return uploadVO;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("文件上传失败: {}", e.getMessage(), e);
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 实体转VO
     */
    private FileRecordVO toVO(FileRecord record) {
        FileRecordVO vo = new FileRecordVO();
        BeanUtil.copyProperties(record, vo);

        // 格式化文件大小
        vo.setFileSizeDesc(DataSizeUtil.format(record.getFileSize()));

        return vo;
    }
}
