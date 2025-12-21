package com.tiny.core.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.tiny.common.core.domain.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Sa-Token异常处理
 */
@Slf4j
@RestControllerAdvice
@Order(1)
public class SaTokenExceptionHandler {

    /**
     * 未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public ResponseResult<?> handleNotLoginException(NotLoginException e) {
        log.warn("未登录异常：{}", e.getMessage());
        return ResponseResult.fail(401, "未登录或登录已过期");
    }

    /**
     * 权限不足异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public ResponseResult<?> handleNotPermissionException(NotPermissionException e) {
        log.warn("权限不足异常：{}", e.getMessage());
        return ResponseResult.fail(403, "权限不足");
    }

    /**
     * 角色不足异常
     */
    @ExceptionHandler(NotRoleException.class)
    public ResponseResult<?> handleNotRoleException(NotRoleException e) {
        log.warn("角色不足异常：{}", e.getMessage());
        return ResponseResult.fail(403, "角色不足");
    }
}
