package com.tiny.message.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息VO
 */
@Data
public class MsgMessageVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    private Long messageId;

    /**
     * 消息类型(system系统消息/user用户消息)
     */
    private String messageType;

    /**
     * 发送渠道(site站内/email邮件)
     */
    private String channel;

    /**
     * 关联模板ID
     */
    private Long templateId;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 发送者类型
     */
    private String senderType;

    /**
     * 发送者名称
     */
    private String senderName;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 业务ID
     */
    private String bizId;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 状态
     */
    private String status;

    /**
     * 接收人数
     */
    private Integer recipientCount;

    /**
     * 成功数
     */
    private Integer successCount;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 备注
     */
    private String remark;
}
