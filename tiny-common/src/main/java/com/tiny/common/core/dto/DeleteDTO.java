package com.tiny.common.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 批量删除请求DTO
 */
@Data
@Schema(description = "批量删除请求")
public class DeleteDTO {

    @Schema(description = "要删除的ID集合", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "删除ID列表不能为空")
    @Size(min = 1, max = 100, message = "批量删除数量限制1-100条")
    private List<Long> ids;
}