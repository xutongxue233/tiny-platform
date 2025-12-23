package com.tiny.system.dto;

import com.tiny.common.core.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 登录日志查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysLoginLogQueryDTO extends PageQuery {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;

    /**
     * IP地址
     */
    private String ipAddr;

    /**
     * 登录类型
     */
    private String loginType;

    /**
     * 登录状态
     */
    private String status;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
