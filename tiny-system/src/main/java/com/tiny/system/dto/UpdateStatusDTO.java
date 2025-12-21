package com.tiny.system.dto;

import jakarta.validation.constraints.NotNull;
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
     * 状态
     */
    @NotNull(message = "状态不能为空")
    private String status;
}