package com.tiny.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.common.core.page.PageResult;
import com.tiny.system.dto.SysConfigDTO;
import com.tiny.system.dto.SysConfigQueryDTO;
import com.tiny.system.entity.SysConfig;
import com.tiny.system.vo.SysConfigVO;

import java.util.List;

/**
 * 系统参数配置服务接口
 */
public interface SysConfigService extends IService<SysConfig> {

    /**
     * 分页查询参数配置列表
     */
    PageResult<SysConfigVO> page(SysConfigQueryDTO queryDTO);

    /**
     * 查询所有参数配置列表
     */
    List<SysConfigVO> listAll();

    /**
     * 根据分组查询参数配置列表
     */
    List<SysConfigVO> listByGroup(String configGroup);

    /**
     * 查询参数配置详情
     */
    SysConfigVO getDetail(Long configId);

    /**
     * 新增参数配置
     */
    void add(SysConfigDTO dto);

    /**
     * 修改参数配置
     */
    void update(SysConfigDTO dto);

    /**
     * 删除参数配置
     */
    void delete(Long configId);

    /**
     * 批量删除参数配置
     */
    void deleteBatch(List<Long> configIds);

    /**
     * 修改参数配置状态
     */
    void updateStatus(Long configId, String status);

    /**
     * 根据参数键名获取参数值
     */
    String getConfigValue(String configKey);

    /**
     * 根据参数键名获取参数值(带默认值)
     */
    String getConfigValue(String configKey, String defaultValue);

    /**
     * 根据参数键名获取布尔值
     */
    boolean getConfigBoolean(String configKey);

    /**
     * 根据参数键名获取整数值
     */
    Integer getConfigInteger(String configKey);

    /**
     * 刷新缓存
     */
    void refreshCache();

    /**
     * 清除指定key的缓存
     */
    void clearCache(String configKey);
}
