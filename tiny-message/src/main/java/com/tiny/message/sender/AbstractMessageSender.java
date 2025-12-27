package com.tiny.message.sender;

import lombok.extern.slf4j.Slf4j;

/**
 * 抽象消息发送器
 */
@Slf4j
public abstract class AbstractMessageSender implements MessageSender {

    @Override
    public SendResult send(SendContext context) {
        try {
            log.info("开始发送消息, channel={}, recipientAddress={}", getChannel(), context.getRecipientAddress());
            SendResult result = doSend(context);
            if (result.isSuccess()) {
                log.info("消息发送成功, channel={}, recipientAddress={}", getChannel(), context.getRecipientAddress());
            } else {
                log.warn("消息发送失败, channel={}, recipientAddress={}, error={}",
                        getChannel(), context.getRecipientAddress(), result.getErrorMsg());
            }
            return result;
        } catch (Exception e) {
            log.error("消息发送异常, channel={}, recipientAddress={}", getChannel(), context.getRecipientAddress(), e);
            return SendResult.fail("SEND_ERROR", e.getMessage());
        }
    }

    /**
     * 实际发送逻辑
     */
    protected abstract SendResult doSend(SendContext context);
}
