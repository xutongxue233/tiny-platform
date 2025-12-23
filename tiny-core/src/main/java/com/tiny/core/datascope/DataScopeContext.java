package com.tiny.core.datascope;

/**
 * 数据权限上下文
 */
public class DataScopeContext {

    private static final ThreadLocal<DataScopeParam> CONTEXT = new ThreadLocal<>();

    /**
     * 设置数据权限参数
     */
    public static void set(DataScopeParam param) {
        CONTEXT.set(param);
    }

    /**
     * 获取数据权限参数
     */
    public static DataScopeParam get() {
        return CONTEXT.get();
    }

    /**
     * 清除数据权限参数
     */
    public static void clear() {
        CONTEXT.remove();
    }
}
