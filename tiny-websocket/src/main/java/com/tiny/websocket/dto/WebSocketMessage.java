package com.tiny.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * WebSocket推送消息DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 业务数据
     */
    private Object data;

    /**
     * 发送时间
     */
    private LocalDateTime timestamp;

    /**
     * 创建站内消息通知
     */
    public static WebSocketMessage siteMessage(String title, String content, Object data) {
        return WebSocketMessage.builder()
                .type("SITE_MESSAGE")
                .title(title)
                .content(content)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 创建系统通知
     */
    public static WebSocketMessage systemNotice(String title, String content) {
        return WebSocketMessage.builder()
                .type("SYSTEM_NOTICE")
                .title(title)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 创建未读数更新通知
     */
    public static WebSocketMessage unreadCountUpdate(int count) {
        return WebSocketMessage.builder()
                .type("UNREAD_COUNT")
                .data(count)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
