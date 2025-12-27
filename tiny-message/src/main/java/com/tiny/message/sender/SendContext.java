package com.tiny.message.sender;

import lombok.Data;

import java.util.Map;

/**
 * 发送上下文
 */
@Data
public class SendContext {

    /**
     * 消息ID
     */
    private Long messageId;

    /**
     * 接收记录ID
     */
    private Long recipientId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 接收地址(邮箱/手机号)
     */
    private String recipientAddress;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送渠道
     */
    private String channel;

    /**
     * 模板变量
     */
    private Map<String, Object> variables;
}
