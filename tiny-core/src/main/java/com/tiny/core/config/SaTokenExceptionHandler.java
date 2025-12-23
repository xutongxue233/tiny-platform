package com.tiny.core.config;

import cn.dev33.satoken.exception.DisableServiceException;
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

    /**
     * 账号被封禁异常
     */
    @ExceptionHandler(DisableServiceException.class)
    public ResponseResult<?> handleDisableServiceException(DisableServiceException e) {
        log.warn("账号被封禁异常：{}", e.getMessage());
        long disableTime = e.getDisableTime();
        String msg = disableTime == -1 ? "账号已被永久封禁" : "账号已被封禁，剩余时间：" + formatDisableTime(disableTime);
        return ResponseResult.fail(403, msg);
    }

    private String formatDisableTime(long seconds) {
        if (seconds <= 0) {
            return "0秒";
        }
        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("天");
        }
        if (hours > 0) {
            sb.append(hours).append("小时");
        }
        if (minutes > 0) {
            sb.append(minutes).append("分钟");
        }
        if (secs > 0 && days == 0) {
            sb.append(secs).append("秒");
        }
        return sb.toString();
    }
}
