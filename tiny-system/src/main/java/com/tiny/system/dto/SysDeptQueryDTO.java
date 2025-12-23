package com.tiny.system.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 部门查询DTO
 */
@Data
public class SysDeptQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 状态（0正常 1停用）
     */
    private String status;
}
