package com.tiny.websocket.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在线用户管理器
 * 管理WebSocket连接的用户会话
 */
@Slf4j
@Component
public class OnlineUserManager {

    /**
     * 用户ID -> 会话ID集合的映射
     * 一个用户可能有多个连接（多设备/多标签页）
     */
    private final Map<Long, Set<String>> userSessions = new ConcurrentHashMap<>();

    /**
     * 会话ID -> 用户ID的映射
     */
    private final Map<String, Long> sessionUsers = new ConcurrentHashMap<>();

    /**
     * 用户上线
     *
     * @param userId    用户ID
     * @param sessionId 会话ID
     */
    public void userOnline(Long userId, String sessionId) {
        // 添加到用户会话映射
        userSessions.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(sessionId);
        // 添加到会话用户映射
        sessionUsers.put(sessionId, userId);

        log.info("用户上线: userId={}, sessionId={}, 当前在线用户数: {}", userId, sessionId, userSessions.size());
    }

    /**
     * 用户下线
     *
     * @param sessionId 会话ID
     */
    public void userOffline(String sessionId) {
        Long userId = sessionUsers.remove(sessionId);
        if (userId != null) {
            Set<String> sessions = userSessions.get(userId);
            if (sessions != null) {
                sessions.remove(sessionId);
                // 如果该用户没有其他会话，则从在线用户列表中移除
                if (sessions.isEmpty()) {
                    userSessions.remove(userId);
                }
            }
            log.info("用户下线: userId={}, sessionId={}, 当前在线用户数: {}", userId, sessionId, userSessions.size());
        }
    }

    /**
     * 检查用户是否在线
     *
     * @param userId 用户ID
     * @return 是否在线
     */
    public boolean isOnline(Long userId) {
        Set<String> sessions = userSessions.get(userId);
        return sessions != null && !sessions.isEmpty();
    }

    /**
     * 获取用户的所有会话ID
     *
     * @param userId 用户ID
     * @return 会话ID集合
     */
    public Set<String> getUserSessions(Long userId) {
        return userSessions.getOrDefault(userId, Collections.emptySet());
    }

    /**
     * 获取会话对应的用户ID
     *
     * @param sessionId 会话ID
     * @return 用户ID
     */
    public Long getUserIdBySession(String sessionId) {
        return sessionUsers.get(sessionId);
    }

    /**
     * 获取所有在线用户ID
     *
     * @return 在线用户ID集合
     */
    public Set<Long> getOnlineUsers() {
        return Collections.unmodifiableSet(userSessions.keySet());
    }

    /**
     * 获取在线用户数量
     *
     * @return 在线用户数量
     */
    public int getOnlineCount() {
        return userSessions.size();
    }
}
