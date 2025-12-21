package com.tiny.common.core.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 通用删除DTO
 */
@Data
public class DeleteDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID列表
     */
    @NotEmpty(message = "ID列表不能为空")
    private List<Long> ids;
}