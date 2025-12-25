package com.tiny.system.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统参数配置VO
 */
@Data
public class SysConfigVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 参数配置ID
     */
    private Long configId;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 参数键值
     */
    private String configValue;

    /**
     * 参数类型
     */
    private String configType;

    /**
     * 参数分组
     */
    private String configGroup;

    /**
     * 是否内置
     */
    private String isBuiltin;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
