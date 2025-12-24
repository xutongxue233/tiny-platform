package com.tiny.system.dto;

import com.tiny.common.core.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 字典项查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictItemQueryDTO extends PageQuery {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 字典项标签
     */
    private String itemLabel;

    /**
     * 状态（0正常 1停用）
     */
    private String status;
}