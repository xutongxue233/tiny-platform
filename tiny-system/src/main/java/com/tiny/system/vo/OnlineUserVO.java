package com.tiny.system.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 在线用户VO
 */
@Data
public class OnlineUserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 会话token
     */
    private String tokenId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * IP地址
     */
    private String ipAddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * Token剩余有效时间（秒）
     */
    private Long tokenTimeout;
}
