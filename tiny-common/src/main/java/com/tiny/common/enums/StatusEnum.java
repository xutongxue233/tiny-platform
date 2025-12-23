package com.tiny.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态枚举
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    NORMAL("0", "正常"),
    DISABLE("1", "停用");

    private final String code;
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static StatusEnum getByCode(String code) {
        for (StatusEnum statusEnum : values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }
}
