package com.tiny.message.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户消息VO(用于消息中心)
 */
@Data
public class UserMessageVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 接收记录ID
     */
    private Long recipientId;

    /**
     * 消息ID
     */
    private Long messageId;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 发送者名称
     */
    private String senderName;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 是否置顶(0否 1是)
     */
    private String isTop;

    /**
     * 公告类型(1通知 2公告)
     */
    private String noticeType;

    /**
     * 是否已读
     */
    private String isRead;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
