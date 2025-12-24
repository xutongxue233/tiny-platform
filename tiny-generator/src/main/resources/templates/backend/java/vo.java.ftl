package ${packageName}.vo;

<#list importPackages as pkg>
import ${pkg};
</#list>
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * ${functionName!tableComment} VO
 *
 * @author ${author}
 * @date ${datetime}
 */
@Data
@Schema(description = "${functionName!tableComment}VO")
public class ${className}VO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

<#list columns as column>
<#if column.list || column.detail>
    /**
     * ${column.columnComment!column.columnName}
     */
    @Schema(description = "${column.columnComment!column.columnName}")
    private ${column.javaType} ${column.javaField};

</#if>
</#list>
}
