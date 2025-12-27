package com.tiny.message.dto;

import com.tiny.common.core.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 发送日志查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgSendLogQueryDTO extends PageQuery {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    private Long messageId;

    /**
     * 发送渠道
     */
    private String channel;

    /**
     * 接收地址
     */
    private String recipientAddress;

    /**
     * 发送状态
     */
    private String sendStatus;
}
