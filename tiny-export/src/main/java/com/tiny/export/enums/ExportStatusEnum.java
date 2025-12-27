package com.tiny.export.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 导出任务状态枚举
 */
@Getter
@AllArgsConstructor
public enum ExportStatusEnum {

    PENDING("0", "待处理"),
    PROCESSING("1", "处理中"),
    SUCCESS("2", "成功"),
    FAILED("3", "失败");

    private final String code;
    private final String desc;

    public static ExportStatusEnum getByCode(String code) {
        for (ExportStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
