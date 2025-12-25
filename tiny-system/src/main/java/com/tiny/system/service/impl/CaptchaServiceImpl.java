package com.tiny.system.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.StrUtil;
import com.tiny.common.exception.BusinessException;
import com.tiny.system.service.CaptchaService;
import com.tiny.system.vo.CaptchaVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务实现（使用Hutool Captcha）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 验证码Redis Key前缀
     */
    private static final String CAPTCHA_KEY_PREFIX = "captcha:";

    /**
     * 验证码过期时间（分钟）
     */
    private static final int CAPTCHA_EXPIRE_MINUTES = 5;

    /**
     * 验证码图片宽度
     */
    private static final int CAPTCHA_WIDTH = 120;

    /**
     * 验证码图片高度
     */
    private static final int CAPTCHA_HEIGHT = 40;

    /**
     * 验证码字符数
     */
    private static final int CAPTCHA_LENGTH = 4;

    /**
     * 干扰线数量
     */
    private static final int LINE_COUNT = 3;

    @Override
    public CaptchaVO generate() {
        // 使用Hutool生成扭曲干扰验证码（字符在同一水平线上）
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, CAPTCHA_LENGTH, LINE_COUNT);
        // 使用数字和大写字母作为验证码字符
        captcha.setGenerator(new RandomGenerator("0123456789ABCDEFGHJKLMNPQRSTUVWXYZ", CAPTCHA_LENGTH));

        // 获取验证码文本
        String captchaText = captcha.getCode();

        // 生成唯一Key
        String captchaKey = UUID.randomUUID().toString().replace("-", "");

        // 存储到Redis
        String redisKey = CAPTCHA_KEY_PREFIX + captchaKey;
        stringRedisTemplate.opsForValue().set(redisKey, captchaText, CAPTCHA_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 获取Base64图片
        String base64Image = captcha.getImageBase64Data();

        CaptchaVO vo = new CaptchaVO();
        vo.setCaptchaKey(captchaKey);
        vo.setCaptchaImage(base64Image);

        log.debug("生成验证码: key={}, text={}", captchaKey, captchaText);
        return vo;
    }

    @Override
    public boolean verify(String captchaKey, String captcha) {
        if (StrUtil.isBlank(captchaKey) || StrUtil.isBlank(captcha)) {
            return false;
        }

        String redisKey = CAPTCHA_KEY_PREFIX + captchaKey;
        String storedCaptcha = stringRedisTemplate.opsForValue().get(redisKey);

        if (StrUtil.isBlank(storedCaptcha)) {
            return false;
        }

        // 不区分大小写比较
        boolean result = storedCaptcha.equalsIgnoreCase(captcha);

        // 无论成功失败都删除验证码，防止重复使用
        stringRedisTemplate.delete(redisKey);

        return result;
    }

    @Override
    public void verifyAndThrow(String captchaKey, String captcha) {
        if (StrUtil.isBlank(captchaKey) || StrUtil.isBlank(captcha)) {
            throw new BusinessException("验证码不能为空");
        }

        String redisKey = CAPTCHA_KEY_PREFIX + captchaKey;
        String storedCaptcha = stringRedisTemplate.opsForValue().get(redisKey);

        if (StrUtil.isBlank(storedCaptcha)) {
            throw new BusinessException("验证码已过期，请刷新后重试");
        }

        // 无论成功失败都删除验证码，防止重复使用
        stringRedisTemplate.delete(redisKey);

        // 不区分大小写比较
        if (!storedCaptcha.equalsIgnoreCase(captcha)) {
            throw new BusinessException("验证码错误");
        }
    }
}
