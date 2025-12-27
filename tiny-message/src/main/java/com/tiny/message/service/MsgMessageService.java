package com.tiny.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.common.core.page.PageResult;
import com.tiny.message.dto.MsgMessageDTO;
import com.tiny.message.dto.MsgMessageQueryDTO;
import com.tiny.message.entity.MsgMessage;
import com.tiny.message.vo.MsgMessageVO;

import java.util.List;

/**
 * 消息Service接口
 */
public interface MsgMessageService extends IService<MsgMessage> {

    /**
     * 分页查询消息
     */
    PageResult<MsgMessageVO> page(MsgMessageQueryDTO queryDTO);

    /**
     * 查询消息详情
     */
    MsgMessageVO getDetail(Long messageId);

    /**
     * 新增消息
     */
    void add(MsgMessageDTO dto);

    /**
     * 撤回消息
     */
    void revoke(Long messageId);

    /**
     * 删除消息
     */
    void delete(Long messageId);

    /**
     * 批量删除消息
     */
    void deleteBatch(List<Long> messageIds);
}
