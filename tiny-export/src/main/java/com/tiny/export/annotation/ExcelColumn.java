package com.tiny.export.annotation;

import java.lang.annotation.*;

/**
 * Excel列注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {

    /**
     * 列标题
     */
    String value() default "";

    /**
     * 列顺序(从0开始)
     */
    int order() default 0;

    /**
     * 列宽(字符数)
     */
    int width() default 20;

    /**
     * 日期格式
     */
    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    /**
     * 数字格式
     */
    String numberFormat() default "";

    /**
     * 字典类型(用于字典值转换)
     */
    String dictType() default "";

    /**
     * 是否导出
     */
    boolean export() default true;

    /**
     * 是否导入
     */
    boolean importable() default true;

    /**
     * 是否必填(导入时校验)
     */
    boolean required() default false;

    /**
     * 导入时的校验正则表达式
     */
    String pattern() default "";

    /**
     * 校验失败提示信息
     */
    String patternMessage() default "";
}
