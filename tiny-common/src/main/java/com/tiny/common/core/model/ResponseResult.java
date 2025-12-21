package com.tiny.common.core.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一响应结果
 */
@Data
public class ResponseResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    public ResponseResult() {
        this.timestamp = System.currentTimeMillis();
    }

    public ResponseResult(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public ResponseResult(Integer code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    /**
     * 成功响应
     */
    public static <T> ResponseResult<T> ok() {
        return new ResponseResult<>(200, "操作成功");
    }

    /**
     * 成功响应
     */
    public static <T> ResponseResult<T> ok(T data) {
        return new ResponseResult<>(200, "操作成功", data);
    }

    /**
     * 成功响应
     */
    public static <T> ResponseResult<T> ok(String message, T data) {
        return new ResponseResult<>(200, message, data);
    }

    /**
     * 失败响应
     */
    public static <T> ResponseResult<T> fail() {
        return new ResponseResult<>(500, "操作失败");
    }

    /**
     * 失败响应
     */
    public static <T> ResponseResult<T> fail(String message) {
        return new ResponseResult<>(500, message);
    }

    /**
     * 失败响应
     */
    public static <T> ResponseResult<T> fail(Integer code, String message) {
        return new ResponseResult<>(code, message);
    }
}
