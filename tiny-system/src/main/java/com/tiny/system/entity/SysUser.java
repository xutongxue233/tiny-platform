package com.tiny.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tiny.common.core.model.BaseEntity;
import cn.hutool.core.bean.BeanUtil;
import com.tiny.system.vo.SysUserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 用户表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别（0男 1女 2未知）
     */
    private String gender;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 是否超级管理员（0否 1是）
     */
    private Integer superAdmin;

    /**
     * 转换为VO
     */
    public SysUserVO toVO() {
        return BeanUtil.copyProperties(this, SysUserVO.class);
    }
}