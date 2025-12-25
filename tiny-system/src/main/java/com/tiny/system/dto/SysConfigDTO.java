package com.tiny.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统参数配置DTO
 */
@Data
public class SysConfigDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 参数配置ID
     */
    private Long configId;

    /**
     * 参数名称
     */
    @NotBlank(message = "参数名称不能为空")
    @Size(max = 100, message = "参数名称长度不能超过100个字符")
    private String configName;

    /**
     * 参数键名
     */
    @NotBlank(message = "参数键名不能为空")
    @Size(max = 100, message = "参数键名长度不能超过100个字符")
    private String configKey;

    /**
     * 参数键值
     */
    private String configValue;

    /**
     * 参数类型(STRING/NUMBER/BOOLEAN/JSON)
     */
    private String configType;

    /**
     * 参数分组(SYSTEM/BUSINESS)
     */
    private String configGroup;

    /**
     * 是否内置(Y是 N否)
     */
    private String isBuiltin;

    /**
     * 状态(0正常 1停用)
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
