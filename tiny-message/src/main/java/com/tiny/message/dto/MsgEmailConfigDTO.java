package com.tiny.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 邮件配置DTO
 */
@Data
public class MsgEmailConfigDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    private Long configId;

    /**
     * 配置名称
     */
    @NotBlank(message = "配置名称不能为空")
    @Size(max = 100, message = "配置名称长度不能超过100个字符")
    private String configName;

    /**
     * SMTP服务器
     */
    @NotBlank(message = "SMTP服务器不能为空")
    @Size(max = 200, message = "SMTP服务器长度不能超过200个字符")
    private String host;

    /**
     * 端口
     */
    @NotNull(message = "端口不能为空")
    private Integer port;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(max = 200, message = "用户名长度不能超过200个字符")
    private String username;

    /**
     * 密码/授权码
     */
    @Size(max = 500, message = "密码长度不能超过500个字符")
    private String password;

    /**
     * 发件人地址
     */
    @NotBlank(message = "发件人地址不能为空")
    @Size(max = 200, message = "发件人地址长度不能超过200个字符")
    private String fromAddress;

    /**
     * 发件人名称
     */
    @Size(max = 100, message = "发件人名称长度不能超过100个字符")
    private String fromName;

    /**
     * 启用SSL
     */
    private String sslEnable;

    /**
     * 是否默认
     */
    private String isDefault;

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
