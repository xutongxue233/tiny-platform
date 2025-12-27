package com.tiny.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息接收记录表
 */
@Data
@TableName("msg_recipient")
public class MsgRecipient implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 接收记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long recipientId;

    /**
     * 消息ID
     */
    private Long messageId;

    /**
     * 接收用户ID
     */
    private Long userId;

    /**
     * 接收地址(邮箱/手机号)
     */
    private String recipientAddress;

    /**
     * 是否已读(0未读 1已读)
     */
    private String isRead;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

    /**
     * 用户是否删除(0否 1是)
     */
    private String isDeleted;

    /**
     * 删除时间
     */
    private LocalDateTime deleteTime;
}
