package com.tiny.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜单DTO
 */
@Data
public class SysMenuDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;

    /**
     * 国际化key
     */
    @Size(max = 100, message = "国际化key长度不能超过100个字符")
    private String localeKey;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 路由地址
     */
    @Size(max = 200, message = "路由地址长度不能超过200个字符")
    private String path;

    /**
     * 组件路径
     */
    @Size(max = 255, message = "组件路径长度不能超过255个字符")
    private String component;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    private String menuType;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    private String visible;

    /**
     * 菜单状态（0正常 1停用）
     */
    private String status;

    /**
     * 权限标识
     */
    @Size(max = 100, message = "权限标识长度不能超过100个字符")
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 是否外链（0否 1是）
     */
    private String isFrame;

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    private String isCache;

    /**
     * 链接地址（外链时使用）
     */
    @Size(max = 500, message = "链接地址长度不能超过500个字符")
    private String link;

    /**
     * 打开方式（_blank新窗口 _self当前窗口）
     */
    private String target;

    /**
     * 角标文字
     */
    @Size(max = 20, message = "角标文字长度不能超过20个字符")
    private String badge;

    /**
     * 角标颜色
     */
    @Size(max = 50, message = "角标颜色长度不能超过50个字符")
    private String badgeColor;

    /**
     * 备注
     */
    private String remark;
}