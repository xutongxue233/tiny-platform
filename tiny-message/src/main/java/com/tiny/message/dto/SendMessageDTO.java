package com.tiny.message.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 发送消息DTO
 */
@Data
public class SendMessageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 发送渠道(site站内/email邮件)
     */
    @NotBlank(message = "发送渠道不能为空")
    private String channel;

    /**
     * 模板编码(使用模板时必填)
     */
    private String templateCode;

    /**
     * 消息标题(不使用模板时必填)
     */
    private String title;

    /**
     * 消息内容(不使用模板时必填)
     */
    private String content;

    /**
     * 是否广播(发送给所有用户)
     */
    private Boolean broadcast;

    /**
     * 接收用户ID列表(站内消息时必填，broadcast=true时可不填)
     */
    private List<Long> userIds;

    /**
     * 邮箱地址列表(发送邮件时使用)
     */
    private List<String> emails;

    /**
     * 模板变量
     */
    private Map<String, Object> variables;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 业务ID
     */
    private String bizId;

    /**
     * 优先级(0普通 1重要 2紧急)
     */
    private Integer priority;
}
