package com.tiny.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 验证码VO
 */
@Data
@Schema(description = "验证码响应")
public class CaptchaVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "验证码Key")
    private String captchaKey;

    @Schema(description = "验证码图片(Base64编码)")
    private String captchaImage;
}
