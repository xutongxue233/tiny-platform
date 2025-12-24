package ${packageName}.dto;

<#list importPackages as pkg>
import ${pkg};
</#list>
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * ${functionName!tableComment} DTO
 *
 * @author ${author}
 * @date ${datetime}
 */
@Data
@Schema(description = "${functionName!tableComment}DTO")
public class ${className}DTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

<#list columns as column>
<#if column.insert || column.edit>
    /**
     * ${column.columnComment!column.columnName}
     */
<#if column.required && !column.pk>
<#if column.javaType == "String">
    @NotBlank(message = "${column.columnComment!column.javaField}不能为空")
<#else>
    @NotNull(message = "${column.columnComment!column.javaField}不能为空")
</#if>
</#if>
    @Schema(description = "${column.columnComment!column.columnName}")
    private ${column.javaType} ${column.javaField};

</#if>
</#list>
}
