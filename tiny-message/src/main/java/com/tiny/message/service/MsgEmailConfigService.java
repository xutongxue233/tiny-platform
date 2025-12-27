package com.tiny.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.common.core.page.PageResult;
import com.tiny.message.dto.MsgEmailConfigDTO;
import com.tiny.message.dto.MsgTemplateQueryDTO;
import com.tiny.message.entity.MsgEmailConfig;
import com.tiny.message.vo.MsgEmailConfigVO;

import java.util.List;

/**
 * 邮件配置Service接口
 */
public interface MsgEmailConfigService extends IService<MsgEmailConfig> {

    /**
     * 查询所有邮件配置
     */
    List<MsgEmailConfigVO> listAll();

    /**
     * 分页查询邮件配置
     */
    PageResult<MsgEmailConfigVO> page(MsgTemplateQueryDTO queryDTO);

    /**
     * 查询配置详情
     */
    MsgEmailConfigVO getDetail(Long configId);

    /**
     * 获取默认配置
     */
    MsgEmailConfig getDefaultConfig();

    /**
     * 新增配置
     */
    void add(MsgEmailConfigDTO dto);

    /**
     * 修改配置
     */
    void update(MsgEmailConfigDTO dto);

    /**
     * 删除配置
     */
    void delete(Long configId);

    /**
     * 设置为默认
     */
    void setDefault(Long configId);

    /**
     * 修改配置状态
     */
    void updateStatus(Long configId, String status);

    /**
     * 测试连接
     */
    boolean testConnection(Long configId);
}
