package com.tiny.system.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录日志VO
 */
@Data
public class SysLoginLogVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 登录日志ID
     */
    private Long loginLogId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录类型（login登录 logout登出）
     */
    private String loginType;

    /**
     * IP地址
     */
    private String ipAddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录状态（0成功 1失败）
     */
    private String status;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
}
