package com.tiny.system.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 在线用户查询DTO
 */
@Data
public class OnlineUserQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前页
     */
    private Integer current = 1;

    /**
     * 每页大小
     */
    private Integer size = 10;

    /**
     * 用户名
     */
    private String username;

    /**
     * IP地址
     */
    private String ipAddr;
}
