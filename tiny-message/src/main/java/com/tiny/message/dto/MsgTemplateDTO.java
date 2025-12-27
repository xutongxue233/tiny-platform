package com.tiny.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 消息模板DTO
 */
@Data
public class MsgTemplateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 模板编码
     */
    @NotBlank(message = "模板编码不能为空")
    @Size(max = 100, message = "模板编码长度不能超过100个字符")
    private String templateCode;

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称不能为空")
    @Size(max = 200, message = "模板名称长度不能超过200个字符")
    private String templateName;

    /**
     * 模板类型(site站内/email邮件)
     */
    @NotBlank(message = "模板类型不能为空")
    private String templateType;

    /**
     * 模板内容
     */
    @NotBlank(message = "模板内容不能为空")
    private String templateContent;

    /**
     * 邮件主题
     */
    @Size(max = 200, message = "邮件主题长度不能超过200个字符")
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
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
