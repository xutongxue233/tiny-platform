package com.tiny.system.service;

import com.tiny.system.vo.CaptchaVO;

/**
 * 验证码服务接口
 */
public interface CaptchaService {

    /**
     * 生成验证码
     *
     * @return 验证码信息（包含key和图片）
     */
    CaptchaVO generate();

    /**
     * 校验验证码
     *
     * @param captchaKey 验证码Key
     * @param captcha    用户输入的验证码
     * @return 校验是否通过
     */
    boolean verify(String captchaKey, String captcha);

    /**
     * 校验验证码（校验后删除）
     *
     * @param captchaKey 验证码Key
     * @param captcha    用户输入的验证码
     * @throws com.tiny.common.exception.BusinessException 验证码错误时抛出异常
     */
    void verifyAndThrow(String captchaKey, String captcha);
}
