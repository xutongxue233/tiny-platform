package com.tiny.system.service;

import com.tiny.common.core.page.PageResult;
import com.tiny.system.dto.OnlineUserQueryDTO;
import com.tiny.system.vo.OnlineUserVO;

/**
 * 在线用户Service
 */
public interface OnlineUserService {

    /**
     * 分页查询在线用户
     */
    PageResult<OnlineUserVO> page(OnlineUserQueryDTO queryDTO);

    /**
     * 强制退出用户
     */
    void kickout(String tokenId);

    /**
     * 强制退出用户（根据用户ID踢出所有会话）
     */
    void kickoutByUserId(Long userId);

    /**
     * 封禁用户账号
     *
     * @param userId      用户ID
     * @param disableTime 封禁时长（秒），-1表示永久封禁
     */
    void disableUser(Long userId, long disableTime);

    /**
     * 解除用户封禁
     */
    void untieDisable(Long userId);

    /**
     * 检查用户是否被封禁
     */
    boolean isDisabled(Long userId);

    /**
     * 获取用户封禁剩余时间（秒）
     */
    long getDisableTime(Long userId);
}
