package com.tiny.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录类型枚举
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {

    LOGIN("login", "登录"),
    LOGOUT("logout", "登出");

    private final String code;
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static LoginTypeEnum getByCode(String code) {
        for (LoginTypeEnum loginTypeEnum : values()) {
            if (loginTypeEnum.getCode().equals(code)) {
                return loginTypeEnum;
            }
        }
        return null;
    }
}
