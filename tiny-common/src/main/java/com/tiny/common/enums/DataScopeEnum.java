package com.tiny.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据范围枚举
 */
@Getter
@AllArgsConstructor
public enum DataScopeEnum {

    /**
     * 全部数据权限
     */
    ALL("1", "全部数据权限"),

    /**
     * 自定义数据权限
     */
    CUSTOM("2", "自定义数据权限"),

    /**
     * 本部门数据权限
     */
    DEPT("3", "本部门数据权限"),

    /**
     * 本部门及以下数据权限
     */
    DEPT_AND_CHILD("4", "本部门及以下数据权限"),

    /**
     * 仅本人数据权限
     */
    SELF("5", "仅本人数据权限");

    private final String code;
    private final String desc;

    public static DataScopeEnum getByCode(String code) {
        for (DataScopeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
