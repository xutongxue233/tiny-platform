package com.tiny.message.sender;

import org.springframework.stereotype.Component;

/**
 * 站内消息发送器
 */
@Component
public class SiteMessageSender extends AbstractMessageSender {

    public static final String CHANNEL = "site";

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    protected SendResult doSend(SendContext context) {
        // 站内消息只需要创建接收记录即可，已在Service层处理
        // 这里直接返回成功
        return SendResult.success();
    }
}
