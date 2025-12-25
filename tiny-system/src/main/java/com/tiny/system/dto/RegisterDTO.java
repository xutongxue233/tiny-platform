package com.tiny.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册DTO
 */
@Data
@Schema(description = "用户注册请求参数")
public class RegisterDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "确认密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    @Schema(description = "验证码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "验证码不能为空")
    private String captcha;

    @Schema(description = "验证码Key", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "验证码Key不能为空")
    private String captchaKey;
}
