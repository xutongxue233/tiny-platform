package com.tiny.storage.service;

import com.tiny.common.core.page.PageResult;
import com.tiny.storage.dto.StorageConfigDTO;
import com.tiny.storage.dto.StorageConfigQueryDTO;
import com.tiny.storage.entity.StorageConfig;
import com.tiny.storage.vo.StorageConfigVO;

import java.util.List;

/**
 * 存储配置服务接口
 */
public interface StorageConfigService {

    /**
     * 分页查询存储配置
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResult<StorageConfigVO> page(StorageConfigQueryDTO queryDTO);

    /**
     * 查询所有存储配置
     *
     * @return 配置列表
     */
    List<StorageConfigVO> list();

    /**
     * 根据ID查询存储配置
     *
     * @param configId 配置ID
     * @return 存储配置
     */
    StorageConfigVO getById(Long configId);

    /**
     * 获取默认存储配置
     *
     * @return 默认存储配置
     */
    StorageConfig getDefaultConfig();

    /**
     * 新增存储配置
     *
     * @param dto 配置信息
     * @return 新增结果
     */
    boolean add(StorageConfigDTO dto);

    /**
     * 修改存储配置
     *
     * @param dto 配置信息
     * @return 修改结果
     */
    boolean update(StorageConfigDTO dto);

    /**
     * 删除存储配置
     *
     * @param configId 配置ID
     * @return 删除结果
     */
    boolean delete(Long configId);

    /**
     * 设置默认存储配置
     *
     * @param configId 配置ID
     * @return 设置结果
     */
    boolean setDefault(Long configId);

    /**
     * 测试存储配置连接
     *
     * @param dto 配置信息
     * @return 测试结果
     */
    boolean testConnection(StorageConfigDTO dto);

    /**
     * 刷新存储服务
     *
     * @param configId 配置ID
     */
    void refreshStorage(Long configId);

    /**
     * 初始化所有存储服务
     */
    void initAllStorage();
}
