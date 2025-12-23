package com.tiny.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.tiny.common.enums.ResultCodeEnum;
import com.tiny.common.exception.BusinessException;

import java.util.Collection;

/**
 * 抛异常工具类
 */
public class ThrowUtils {

    private ThrowUtils() {
    }

    // ============ 条件判断 ============

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param e         异常
     */
    public static void throwIf(boolean condition, RuntimeException e) {
        if (condition) {
            throw e;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param message   错误消息
     */
    public static void throwIf(boolean condition, String message) {
        throwIf(condition, new BusinessException(message));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition  条件
     * @param resultCode 错误码枚举
     */
    public static void throwIf(boolean condition, ResultCodeEnum resultCode) {
        throwIf(condition, new BusinessException(resultCode.getCode(), resultCode.getDesc()));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition  条件
     * @param resultCode 错误码枚举
     * @param message    自定义错误消息
     */
    public static void throwIf(boolean condition, ResultCodeEnum resultCode, String message) {
        throwIf(condition, new BusinessException(resultCode.getCode(), message));
    }

    // ============ 空值校验 ============

    /**
     * 对象为null则抛异常
     *
     * @param obj     对象
     * @param message 错误消息
     */
    public static void throwIfNull(Object obj, String message) {
        throwIf(obj == null, message);
    }

    /**
     * 对象为null则抛异常
     *
     * @param obj        对象
     * @param resultCode 错误码枚举
     */
    public static void throwIfNull(Object obj, ResultCodeEnum resultCode) {
        throwIf(obj == null, resultCode);
    }

    /**
     * 字符串为空白则抛异常
     *
     * @param str     字符串
     * @param message 错误消息
     */
    public static void throwIfBlank(String str, String message) {
        throwIf(StrUtil.isBlank(str), message);
    }

    /**
     * 字符串为空白则抛异常
     *
     * @param str        字符串
     * @param resultCode 错误码枚举
     */
    public static void throwIfBlank(String str, ResultCodeEnum resultCode) {
        throwIf(StrUtil.isBlank(str), resultCode);
    }

    /**
     * 集合为空则抛异常
     *
     * @param coll    集合
     * @param message 错误消息
     */
    public static void throwIfEmpty(Collection<?> coll, String message) {
        throwIf(CollUtil.isEmpty(coll), message);
    }

    /**
     * 集合为空则抛异常
     *
     * @param coll       集合
     * @param resultCode 错误码枚举
     */
    public static void throwIfEmpty(Collection<?> coll, ResultCodeEnum resultCode) {
        throwIf(CollUtil.isEmpty(coll), resultCode);
    }
}
