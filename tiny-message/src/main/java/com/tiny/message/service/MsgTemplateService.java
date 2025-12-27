package com.tiny.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.common.core.page.PageResult;
import com.tiny.message.dto.MsgTemplateDTO;
import com.tiny.message.dto.MsgTemplateQueryDTO;
import com.tiny.message.entity.MsgTemplate;
import com.tiny.message.vo.MsgTemplateVO;

import java.util.List;

/**
 * 消息模板Service接口
 */
public interface MsgTemplateService extends IService<MsgTemplate> {

    /**
     * 分页查询消息模板
     */
    PageResult<MsgTemplateVO> page(MsgTemplateQueryDTO queryDTO);

    /**
     * 查询模板详情
     */
    MsgTemplateVO getDetail(Long templateId);

    /**
     * 根据模板编码查询
     */
    MsgTemplate getByCode(String templateCode);

    /**
     * 新增模板
     */
    void add(MsgTemplateDTO dto);

    /**
     * 修改模板
     */
    void update(MsgTemplateDTO dto);

    /**
     * 删除模板
     */
    void delete(Long templateId);

    /**
     * 批量删除模板
     */
    void deleteBatch(List<Long> templateIds);

    /**
     * 修改模板状态
     */
    void updateStatus(Long templateId, String status);

    /**
     * 获取所有启用的模板列表
     */
    List<MsgTemplateVO> listAll();
}
