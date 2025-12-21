package com.tiny.system.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜单查询DTO
 */
@Data
public class SysMenuQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单状态（0正常 1停用）
     */
    private String status;
}