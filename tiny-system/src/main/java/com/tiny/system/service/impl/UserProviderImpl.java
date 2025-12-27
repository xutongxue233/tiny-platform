package com.tiny.system.service.impl;

import com.tiny.message.service.UserProvider;
import com.tiny.system.entity.SysUser;
import com.tiny.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户信息提供者实现
 * 实现消息模块的UserProvider接口，解耦模块间依赖
 */
@Service
@RequiredArgsConstructor
public class UserProviderImpl implements UserProvider {

    private final SysUserService userService;

    @Override
    public String getEmailByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        SysUser user = userService.getById(userId);
        return user != null ? user.getEmail() : null;
    }

    @Override
    public String getUsernameByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        SysUser user = userService.getById(userId);
        return user != null ? user.getUsername() : null;
    }

    @Override
    public List<Long> getAllActiveUserIds() {
        return userService.listAllSimple().stream()
                .map(user -> user.getUserId())
                .toList();
    }
}
