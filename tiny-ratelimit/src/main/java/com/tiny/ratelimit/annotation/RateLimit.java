package com.tiny.ratelimit.annotation;

import com.tiny.ratelimit.enums.LimitType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口限流注解
 * 基于Redis的滑动窗口算法实现
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 限流次数
     */
    int limit() default 10;

    /**
     * 时间窗口（秒）
     */
    int window() default 60;

    /**
     * 限流类型
     */
    LimitType type() default LimitType.IP;

    /**
     * 自定义限流Key（支持SpEL表达式）
     * 当type为CUSTOM时生效
     */
    String key() default "";

    /**
     * 限流提示信息
     */
    String message() default "请求过于频繁，请稍后重试";
}
