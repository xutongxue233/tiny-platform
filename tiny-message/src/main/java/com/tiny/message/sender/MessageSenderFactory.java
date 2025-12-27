package com.tiny.message.sender;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息发送器工厂
 */
@Component
public class MessageSenderFactory {

    private final Map<String, MessageSender> senderMap = new HashMap<>();

    public MessageSenderFactory(List<MessageSender> senders) {
        for (MessageSender sender : senders) {
            senderMap.put(sender.getChannel(), sender);
        }
    }

    /**
     * 获取发送器
     */
    public MessageSender getSender(String channel) {
        MessageSender sender = senderMap.get(channel);
        if (sender == null) {
            throw new IllegalArgumentException("不支持的发送渠道: " + channel);
        }
        return sender;
    }

    /**
     * 是否支持该渠道
     */
    public boolean supports(String channel) {
        return senderMap.containsKey(channel);
    }
}
