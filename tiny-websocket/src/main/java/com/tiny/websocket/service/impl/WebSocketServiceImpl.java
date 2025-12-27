package com.tiny.websocket.service.impl;

import com.tiny.websocket.manager.OnlineUserManager;
import com.tiny.websocket.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * WebSocket推送服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final OnlineUserManager onlineUserManager;

    @Override
    public void sendToUser(Long userId, String destination, Object payload) {
        if (userId == null) {
            log.warn("发送WebSocket消息失败: userId为空");
            return;
        }

        if (!onlineUserManager.isOnline(userId)) {
            log.debug("用户{}不在线，跳过WebSocket推送", userId);
            return;
        }

        try {
            // 使用用户ID作为目的地用户标识
            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    destination,
                    payload
            );
            log.debug("WebSocket消息已发送: userId={}, destination={}", userId, destination);
        } catch (Exception e) {
            log.error("发送WebSocket消息失败: userId={}, error={}", userId, e.getMessage());
        }
    }

    @Override
    public void sendToUsers(List<Long> userIds, String destination, Object payload) {
        if (userIds == null || userIds.isEmpty()) {
            return;
        }

        for (Long userId : userIds) {
            sendToUser(userId, destination, payload);
        }
    }

    @Override
    public void broadcast(String destination, Object payload) {
        try {
            messagingTemplate.convertAndSend(destination, payload);
            log.debug("WebSocket广播消息已发送: destination={}", destination);
        } catch (Exception e) {
            log.error("发送WebSocket广播消息失败: error={}", e.getMessage());
        }
    }

    @Override
    public boolean isOnline(Long userId) {
        return onlineUserManager.isOnline(userId);
    }

    @Override
    public int getOnlineCount() {
        return onlineUserManager.getOnlineCount();
    }
}
