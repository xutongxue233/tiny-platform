package com.tiny.system.dto;

import com.tiny.common.core.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 用户查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserQueryDTO extends PageQuery {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 部门ID
     */
    private Long deptId;
}