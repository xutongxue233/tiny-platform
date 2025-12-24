package com.tiny.security.utils;

import cn.dev33.satoken.stp.StpUtil;
import com.tiny.security.context.LoginUser;

/**
 * 登录用户工具类
 */
public class LoginUserUtil {

    private static final String LOGIN_USER_KEY = "loginUser";

    /**
     * 设置登录用户信息
     */
    public static void setLoginUser(LoginUser loginUser) {
        StpUtil.getSession().set(LOGIN_USER_KEY, loginUser);
    }

    /**
     * 获取登录用户信息
     */
    public static LoginUser getLoginUser() {
        return (LoginUser) StpUtil.getSession().get(LOGIN_USER_KEY);
    }

    /**
     * 获取当前用户ID
     */
    public static Long getUserId() {
        return StpUtil.getLoginIdAsLong();
    }

    /**
     * 获取当前用户名
     */
    public static String getUsername() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUsername() : null;
    }
}
