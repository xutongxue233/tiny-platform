package com.tiny.common.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 模块名称
     */
    String module() default "";

    /**
     * 操作类型
     */
    OperationType type() default OperationType.OTHER;

    /**
     * 操作描述
     */
    String desc() default "";

    /**
     * 是否保存请求参数
     */
    boolean saveRequestParam() default true;

    /**
     * 是否保存响应结果
     */
    boolean saveResponseResult() default true;

    /**
     * 排除的参数名称
     */
    String[] excludeParams() default {"password", "oldPassword", "newPassword", "confirmPassword"};

    /**
     * 操作类型枚举
     */
    enum OperationType {
        /**
         * 其他
         */
        OTHER,
        /**
         * 新增
         */
        INSERT,
        /**
         * 修改
         */
        UPDATE,
        /**
         * 删除
         */
        DELETE,
        /**
         * 查询
         */
        SELECT,
        /**
         * 导出
         */
        EXPORT,
        /**
         * 导入
         */
        IMPORT,
        /**
         * 授权
         */
        GRANT,
        /**
         * 强退
         */
        FORCE
    }
}
