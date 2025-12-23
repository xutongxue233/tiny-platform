package com.tiny.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 删除标志枚举
 */
@Getter
@AllArgsConstructor
public enum DelFlagEnum {

    NORMAL("0", "未删除"),
    DELETED("1", "已删除");

    private final String code;
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static DelFlagEnum getByCode(String code) {
        for (DelFlagEnum delFlagEnum : values()) {
            if (delFlagEnum.getCode().equals(code)) {
                return delFlagEnum;
            }
        }
        return null;
    }
}
