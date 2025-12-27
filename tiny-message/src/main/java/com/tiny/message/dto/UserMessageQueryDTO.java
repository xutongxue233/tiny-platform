package com.tiny.message.dto;

import com.tiny.common.core.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 用户消息查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserMessageQueryDTO extends PageQuery {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 是否已读
     */
    private String isRead;

    /**
     * 消息标题
     */
    private String title;
}
