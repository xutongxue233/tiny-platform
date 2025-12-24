package ${packageName}.entity;

<#list importPackages as pkg>
import ${pkg};
</#list>
import com.baomidou.mybatisplus.annotation.*;
import com.tiny.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ${functionName!tableComment} 实体类
 *
 * @author ${author}
 * @date ${datetime}
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("${tableName}")
public class ${className} extends BaseEntity {

<#list columns as column>
<#if !column.columnName?lower_case?matches("create_by|create_time|update_by|update_time|del_flag|remark")>
    /**
     * ${column.columnComment!column.columnName}
     */
<#if column.pk>
    @TableId(type = IdType.AUTO)
</#if>
    private ${column.javaType} ${column.javaField};

</#if>
</#list>
}
