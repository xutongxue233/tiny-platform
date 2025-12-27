package com.tiny.message.sender;

/**
 * 消息发送器接口
 */
public interface MessageSender {

    /**
     * 获取渠道类型
     */
    String getChannel();

    /**
     * 发送消息
     */
    SendResult send(SendContext context);

    /**
     * 是否支持该渠道
     */
    default boolean supports(String channel) {
        return getChannel().equals(channel);
    }
}
