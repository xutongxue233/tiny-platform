package com.tiny.websocket.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import com.tiny.websocket.manager.OnlineUserManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

/**
 * WebSocket认证拦截器
 * 在STOMP连接时验证Sa-Token
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final OnlineUserManager onlineUserManager;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            // 从请求头获取token
            List<String> tokenList = accessor.getNativeHeader("Authorization");
            String token = null;

            if (tokenList != null && !tokenList.isEmpty()) {
                token = tokenList.get(0);
                // 移除Bearer前缀
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }
            }

            if (token == null || token.isEmpty()) {
                log.warn("WebSocket连接缺少token");
                throw new RuntimeException("未授权的连接");
            }

            try {
                // 使用Sa-Token验证token
                Object loginId = StpUtil.getLoginIdByToken(token);
                if (loginId == null) {
                    log.warn("WebSocket token无效");
                    throw new RuntimeException("token无效或已过期");
                }

                Long userId = Long.parseLong(loginId.toString());

                // 创建用户Principal
                WebSocketPrincipal principal = new WebSocketPrincipal(userId);
                accessor.setUser(principal);

                // 记录用户上线
                String sessionId = accessor.getSessionId();
                onlineUserManager.userOnline(userId, sessionId);

                log.info("WebSocket用户连接成功: userId={}, sessionId={}", userId, sessionId);

            } catch (Exception e) {
                log.error("WebSocket认证失败: {}", e.getMessage());
                throw new RuntimeException("认证失败: " + e.getMessage());
            }
        }

        return message;
    }

    /**
     * WebSocket用户Principal
     */
    public static class WebSocketPrincipal implements Principal {
        private final Long userId;

        public WebSocketPrincipal(Long userId) {
            this.userId = userId;
        }

        @Override
        public String getName() {
            return userId.toString();
        }

        public Long getUserId() {
            return userId;
        }
    }
}
