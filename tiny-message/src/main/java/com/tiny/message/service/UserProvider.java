package com.tiny.message.service;

import java.util.List;

/**
 * 用户信息提供者接口
 * 用于解耦消息模块与用户模块的依赖
 */
public interface UserProvider {

    /**
     * 根据用户ID获取邮箱
     *
     * @param userId 用户ID
     * @return 邮箱地址，不存在返回null
     */
    String getEmailByUserId(Long userId);

    /**
     * 根据用户ID获取用户名
     *
     * @param userId 用户ID
     * @return 用户名，不存在返回null
     */
    String getUsernameByUserId(Long userId);

    /**
     * 获取所有正常状态用户的ID列表
     *
     * @return 用户ID列表
     */
    List<Long> getAllActiveUserIds();
}
