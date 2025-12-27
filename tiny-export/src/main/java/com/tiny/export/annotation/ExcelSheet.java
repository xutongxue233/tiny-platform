package com.tiny.export.annotation;

import java.lang.annotation.*;

/**
 * Excel表注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelSheet {

    /**
     * Sheet名称
     */
    String value() default "Sheet1";

    /**
     * 标题行高度
     */
    int headRowHeight() default 20;

    /**
     * 数据行高度
     */
    int dataRowHeight() default 18;
}
