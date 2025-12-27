package com.tiny.websocket.service;

import java.util.List;

/**
 * WebSocket推送服务接口
 */
public interface WebSocketService {

    /**
     * 向指定用户推送消息
     *
     * @param userId      用户ID
     * @param destination 目的地（如 /queue/message）
     * @param payload     消息内容
     */
    void sendToUser(Long userId, String destination, Object payload);

    /**
     * 向多个用户推送消息
     *
     * @param userIds     用户ID列表
     * @param destination 目的地
     * @param payload     消息内容
     */
    void sendToUsers(List<Long> userIds, String destination, Object payload);

    /**
     * 广播消息给所有在线用户
     *
     * @param destination 目的地（如 /topic/broadcast）
     * @param payload     消息内容
     */
    void broadcast(String destination, Object payload);

    /**
     * 检查用户是否在线
     *
     * @param userId 用户ID
     * @return 是否在线
     */
    boolean isOnline(Long userId);

    /**
     * 获取在线用户数量
     *
     * @return 在线用户数量
     */
    int getOnlineCount();
}
