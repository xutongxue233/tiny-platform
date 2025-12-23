package com.tiny.system.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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
    @Min(value = 1, message = "页码最小为1")
    private Integer current = 1;

    /**
     * 每页大小
     */
    @Min(value = 1, message = "每页大小最小为1")
    @Max(value = 100, message = "每页大小最大为100")
    private Integer size = 10;

    /**
     * 用户名
     */
    @Size(max = 50, message = "用户名长度不能超过50")
    private String username;

    /**
     * IP地址
     */
    @Size(max = 50, message = "IP地址长度不能超过50")
    private String ipAddr;
}
