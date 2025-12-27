package com.tiny.export.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 导出任务类型枚举
 */
@Getter
@AllArgsConstructor
public enum ExportTypeEnum {

    EXPORT("export", "导出"),
    IMPORT("import", "导入");

    private final String code;
    private final String desc;

    public static ExportTypeEnum getByCode(String code) {
        for (ExportTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
