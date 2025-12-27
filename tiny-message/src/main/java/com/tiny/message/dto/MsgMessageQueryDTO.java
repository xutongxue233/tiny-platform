package com.tiny.message.dto;

import com.tiny.common.core.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 消息查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgMessageQueryDTO extends PageQuery {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 发送渠道
     */
    private String channel;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 发送者
     */
    private String createBy;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 状态
     */
    private String status;
}
