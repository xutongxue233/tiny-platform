package com.tiny.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 消息DTO
 */
@Data
public class MsgMessageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    private Long messageId;

    /**
     * 消息类型(system系统消息/user用户消息)
     */
    @NotBlank(message = "消息类型不能为空")
    private String messageType;

    /**
     * 发送渠道(site站内/email邮件)
     */
    @NotBlank(message = "发送渠道不能为空")
    private String channel;

    /**
     * 关联模板ID
     */
    private Long templateId;

    /**
     * 消息标题
     */
    @NotBlank(message = "消息标题不能为空")
    @Size(max = 200, message = "消息标题长度不能超过200个字符")
    private String title;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    private String content;

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
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
