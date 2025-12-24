package com.tiny.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 登录保护服务
 * 防止暴力破解，限制登录失败次数
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginProtectionService {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 登录失败计数器 Redis Key 前缀
     */
    private static final String LOGIN_FAIL_COUNT_KEY = "login:fail:count:";

    /**
     * 账号锁定 Redis Key 前缀
     */
    private static final String LOGIN_LOCK_KEY = "login:lock:";

    /**
     * 最大失败次数
     */
    private static final int MAX_FAIL_COUNT = 5;

    /**
     * 失败计数有效期（分钟）
     */
    private static final int FAIL_COUNT_EXPIRE_MINUTES = 10;

    /**
     * 锁定时间（分钟）
     */
    private static final int LOCK_MINUTES = 15;

    /**
     * 检查账号是否被锁定
     *
     * @param username 用户名
     * @return true-已锁定，false-未锁定
     */
    public boolean isLocked(String username) {
        String lockKey = LOGIN_LOCK_KEY + username;
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(lockKey));
    }

    /**
     * 获取锁定剩余时间（秒）
     *
     * @param username 用户名
     * @return 剩余秒数，未锁定返回0
     */
    public long getLockRemainSeconds(String username) {
        String lockKey = LOGIN_LOCK_KEY + username;
        Long expire = stringRedisTemplate.getExpire(lockKey, TimeUnit.SECONDS);
        return expire != null && expire > 0 ? expire : 0;
    }

    /**
     * 记录登录失败
     *
     * @param username 用户名
     * @return 当前失败次数
     */
    public int recordFailure(String username) {
        String countKey = LOGIN_FAIL_COUNT_KEY + username;

        // 递增失败次数
        Long count = stringRedisTemplate.opsForValue().increment(countKey);
        if (count == null) {
            count = 1L;
        }

        // 首次失败，设置过期时间
        if (count == 1) {
            stringRedisTemplate.expire(countKey, FAIL_COUNT_EXPIRE_MINUTES, TimeUnit.MINUTES);
        }

        // 达到最大失败次数，锁定账号
        if (count >= MAX_FAIL_COUNT) {
            lockAccount(username);
            log.warn("账号 {} 因连续{}次登录失败被锁定{}分钟", username, MAX_FAIL_COUNT, LOCK_MINUTES);
        }

        return count.intValue();
    }

    /**
     * 锁定账号
     *
     * @param username 用户名
     */
    private void lockAccount(String username) {
        String lockKey = LOGIN_LOCK_KEY + username;
        stringRedisTemplate.opsForValue().set(lockKey, "1", LOCK_MINUTES, TimeUnit.MINUTES);
        // 清除失败计数
        clearFailCount(username);
    }

    /**
     * 登录成功，清除失败记录
     *
     * @param username 用户名
     */
    public void clearFailCount(String username) {
        String countKey = LOGIN_FAIL_COUNT_KEY + username;
        stringRedisTemplate.delete(countKey);
    }

    /**
     * 获取剩余尝试次数
     *
     * @param username 用户名
     * @return 剩余次数
     */
    public int getRemainAttempts(String username) {
        String countKey = LOGIN_FAIL_COUNT_KEY + username;
        String countStr = stringRedisTemplate.opsForValue().get(countKey);
        if (countStr == null) {
            return MAX_FAIL_COUNT;
        }
        int count = Integer.parseInt(countStr);
        return Math.max(0, MAX_FAIL_COUNT - count);
    }

    /**
     * 手动解锁账号
     *
     * @param username 用户名
     */
    public void unlock(String username) {
        String lockKey = LOGIN_LOCK_KEY + username;
        String countKey = LOGIN_FAIL_COUNT_KEY + username;
        stringRedisTemplate.delete(lockKey);
        stringRedisTemplate.delete(countKey);
        log.info("账号 {} 已手动解锁", username);
    }
}