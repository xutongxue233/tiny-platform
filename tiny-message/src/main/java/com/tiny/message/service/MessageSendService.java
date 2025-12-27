package com.tiny.message.service;

import com.tiny.message.dto.SendMessageDTO;

import java.util.List;
import java.util.Map;

/**
 * 消息发送Service接口
 */
public interface MessageSendService {

    /**
     * 发送消息
     */
    void send(SendMessageDTO dto);

    /**
     * 通过模板发送消息
     */
    void sendByTemplate(String templateCode, List<Long> userIds, Map<String, Object> variables);

    /**
     * 异步发送消息
     */
    void asyncSend(SendMessageDTO dto);

    /**
     * 重试发送
     */
    void retrySend(Long logId);
}
