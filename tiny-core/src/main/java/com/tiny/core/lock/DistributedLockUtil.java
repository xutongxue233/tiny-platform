package com.tiny.core.lock;

import com.tiny.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁工具类
 * 基于Redisson实现
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnClass(RedissonClient.class)
public class DistributedLockUtil {

    private final RedissonClient redissonClient;

    /**
     * 默认等待时间（秒）
     */
    private static final long DEFAULT_WAIT_TIME = 5L;

    /**
     * 默认锁持有时间（秒）
     */
    private static final long DEFAULT_LEASE_TIME = 30L;

    /**
     * 执行带锁的操作（有返回值）
     *
     * @param lockKey  锁的key
     * @param supplier 需要执行的操作
     * @param <T>      返回类型
     * @return 操作结果
     */
    public <T> T executeWithLock(String lockKey, Supplier<T> supplier) {
        return executeWithLock(lockKey, DEFAULT_WAIT_TIME, DEFAULT_LEASE_TIME, supplier);
    }

    /**
     * 执行带锁的操作（有返回值）
     *
     * @param lockKey   锁的key
     * @param waitTime  等待获取锁的时间（秒）
     * @param leaseTime 锁持有时间（秒）
     * @param supplier  需要执行的操作
     * @param <T>       返回类型
     * @return 操作结果
     */
    public <T> T executeWithLock(String lockKey, long waitTime, long leaseTime, Supplier<T> supplier) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (!lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                log.warn("获取分布式锁失败, lockKey: {}", lockKey);
                throw new BusinessException("操作繁忙，请稍后重试");
            }
            log.debug("获取分布式锁成功, lockKey: {}", lockKey);
            return supplier.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("获取分布式锁被中断, lockKey: {}", lockKey, e);
            throw new BusinessException("操作被中断");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.debug("释放分布式锁, lockKey: {}", lockKey);
            }
        }
    }

    /**
     * 执行带锁的操作（无返回值）
     *
     * @param lockKey  锁的key
     * @param runnable 需要执行的操作
     */
    public void executeWithLock(String lockKey, Runnable runnable) {
        executeWithLock(lockKey, DEFAULT_WAIT_TIME, DEFAULT_LEASE_TIME, runnable);
    }

    /**
     * 执行带锁的操作（无返回值）
     *
     * @param lockKey   锁的key
     * @param waitTime  等待获取锁的时间（秒）
     * @param leaseTime 锁持有时间（秒）
     * @param runnable  需要执行的操作
     */
    public void executeWithLock(String lockKey, long waitTime, long leaseTime, Runnable runnable) {
        executeWithLock(lockKey, waitTime, leaseTime, () -> {
            runnable.run();
            return null;
        });
    }

    /**
     * 尝试获取锁（不阻塞）
     *
     * @param lockKey 锁的key
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey) {
        return tryLock(lockKey, DEFAULT_LEASE_TIME);
    }

    /**
     * 尝试获取锁（不阻塞）
     *
     * @param lockKey   锁的key
     * @param leaseTime 锁持有时间（秒）
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(0, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 释放锁
     *
     * @param lockKey 锁的key
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    /**
     * 检查锁是否被持有
     *
     * @param lockKey 锁的key
     * @return 是否被持有
     */
    public boolean isLocked(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        return lock.isLocked();
    }
}
