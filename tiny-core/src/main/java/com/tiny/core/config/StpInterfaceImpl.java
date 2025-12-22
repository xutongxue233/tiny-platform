package com.tiny.core.config;

import cn.dev33.satoken.stp.StpInterface;
import com.tiny.core.security.LoginUser;
import com.tiny.core.utils.LoginUserUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sa-Token权限认证接口实现
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     * 返回当前账号拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginUser loginUser = LoginUserUtil.getLoginUser();
        if (loginUser == null) {
            return new ArrayList<>();
        }
        // 超级管理员拥有所有权限
        if (loginUser.isSuperAdmin()) {
            return Collections.singletonList("*");
        }
        if (loginUser.getPermissions() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(loginUser.getPermissions());
    }

    /**
     * 返回当前账号拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        LoginUser loginUser = LoginUserUtil.getLoginUser();
        if (loginUser == null) {
            return new ArrayList<>();
        }
        // 超级管理员拥有所有角色
        if (loginUser.isSuperAdmin()) {
            return Collections.singletonList("*");
        }
        if (loginUser.getRoles() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(loginUser.getRoles());
    }
}