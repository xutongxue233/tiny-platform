package com.tiny.system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 修改状态DTO
 */
@Data
public class UpdateStatusDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * 状态（0正常 1停用）
     */
    @NotNull(message = "状态不能为空")
    @Pattern(regexp = "^[01]$", message = "状态值只能是0或1")
    private String status;
}