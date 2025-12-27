package com.tiny.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.common.core.page.PageResult;
import com.tiny.message.dto.UserMessageQueryDTO;
import com.tiny.message.entity.MsgRecipient;
import com.tiny.message.vo.UserMessageVO;

import java.util.List;

/**
 * 消息接收记录Service接口
 */
public interface MsgRecipientService extends IService<MsgRecipient> {

    /**
     * 批量创建接收记录
     */
    void createBatch(Long messageId, List<Long> userIds, List<String> emails);

    /**
     * 分页查询用户消息
     */
    PageResult<UserMessageVO> pageUserMessages(UserMessageQueryDTO queryDTO);

    /**
     * 标记消息已读
     */
    void markAsRead(Long recipientId);

    /**
     * 标记所有消息已读
     */
    void markAllAsRead();

    /**
     * 用户删除消息
     */
    void deleteByUser(Long recipientId);

    /**
     * 获取未读消息数量
     */
    int getUnreadCount();
}
