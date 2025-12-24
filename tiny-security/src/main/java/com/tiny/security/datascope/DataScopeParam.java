package com.tiny.security.datascope;

import lombok.Builder;
import lombok.Data;

/**
 * 数据权限参数
 */
@Data
@Builder
public class DataScopeParam {

    /**
     * 部门表别名
     */
    private String deptAlias;

    /**
     * 用户表别名
     */
    private String userAlias;

    /**
     * 部门ID字段名
     */
    private String deptIdColumn;

    /**
     * 用户ID字段名
     */
    private String userIdColumn;
}
