package com.tiny.message.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息模板VO
 */
@Data
public class MsgTemplateVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板类型(site站内/email邮件)
     */
    private String templateType;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 邮件主题
     */
    private String templateSubject;

    /**
     * 变量列表JSON
     */
    private String variables;

    /**
     * 状态(0正常 1停用)
     */
    private String status;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;
}
