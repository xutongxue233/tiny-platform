package com.tiny.system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 修改置顶状态DTO
 */
@Data
public class UpdateTopDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @NotNull(message = "ID不能为空")
    private Long id;

    /**
     * 是否置顶（0否 1是）
     */
    @NotNull(message = "置顶状态不能为空")
    @Pattern(regexp = "^[01]$", message = "置顶状态只能是0或1")
    private String isTop;
}
