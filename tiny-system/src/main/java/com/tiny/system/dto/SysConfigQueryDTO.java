package com.tiny.system.dto;

import com.tiny.common.core.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统参数配置查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysConfigQueryDTO extends PageQuery {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 参数类型
     */
    private String configType;

    /**
     * 参数分组
     */
    private String configGroup;

    /**
     * 状态
     */
    private String status;
}
