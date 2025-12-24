package ${packageName}.dto;

<#list importPackages as pkg>
import ${pkg};
</#list>
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * ${functionName!tableComment} 查询对象
 *
 * @author ${author}
 * @date ${datetime}
 */
@Data
@Schema(description = "${functionName!tableComment}查询对象")
public class ${className}Query implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

<#list queryColumns as column>
<#if column.queryType == "BETWEEN">
    /**
     * ${column.columnComment!column.columnName}开始
     */
    @Schema(description = "${column.columnComment!column.columnName}开始")
    private ${column.javaType} begin${column.capJavaField};

    /**
     * ${column.columnComment!column.columnName}结束
     */
    @Schema(description = "${column.columnComment!column.columnName}结束")
    private ${column.javaType} end${column.capJavaField};

<#else>
    /**
     * ${column.columnComment!column.columnName}
     */
    @Schema(description = "${column.columnComment!column.columnName}")
    private ${column.javaType} ${column.javaField};

</#if>
</#list>
}
