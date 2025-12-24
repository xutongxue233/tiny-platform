package com.tiny.security.context;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * 登录用户信息
 */
@Data
public class LoginUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 是否超级管理员
     */
    private boolean superAdmin;

    /**
     * 权限标识集合
     */
    private Set<String> permissions;

    /**
     * 角色标识集合
     */
    private Set<String> roles;

    /**
     * 角色ID集合
     */
    private Set<Long> roleIds;

    /**
     * 数据范围（多角色时取最大权限范围）
     */
    private String dataScope;

    /**
     * 自定义数据权限的部门ID集合
     */
    private Set<Long> dataScopeDeptIds;
}
