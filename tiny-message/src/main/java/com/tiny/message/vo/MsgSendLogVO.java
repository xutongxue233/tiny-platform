package com.tiny.message.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息发送日志VO
 */
@Data
public class MsgSendLogVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    private Long logId;

    /**
     * 消息ID
     */
    private Long messageId;

    /**
     * 消息标题
     */
    private String messageTitle;

    /**
     * 接收记录ID
     */
    private Long recipientId;

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

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 成功时间
     */
    private LocalDateTime successTime;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 请求数据
     */
    private String requestData;

    /**
     * 响应数据
     */
    private String responseData;
}
