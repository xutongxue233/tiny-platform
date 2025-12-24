/**
 * ${functionName!tableComment} VO
 */
export interface ${className}VO {
<#list columns as column>
<#if column.list || column.detail>
  /** ${column.columnComment!column.columnName} */
  ${column.javaField}?: <#if column.javaType == 'Long' || column.javaType == 'Integer'>number<#elseif column.javaType == 'Boolean'>boolean<#else>string</#if>;
</#if>
</#list>
}

/**
 * ${functionName!tableComment} DTO
 */
export interface ${className}DTO {
<#list columns as column>
<#if column.insert || column.edit>
  /** ${column.columnComment!column.columnName} */
  ${column.javaField}?: <#if column.javaType == 'Long' || column.javaType == 'Integer'>number<#elseif column.javaType == 'Boolean'>boolean<#else>string</#if>;
</#if>
</#list>
}

/**
 * ${functionName!tableComment} 查询对象
 */
export interface ${className}Query {
<#list queryColumns as column>
<#if column.queryType == 'BETWEEN'>
  /** ${column.columnComment!column.columnName}开始 */
  begin${column.capJavaField}?: <#if column.javaType == 'Long' || column.javaType == 'Integer'>number<#else>string</#if>;
  /** ${column.columnComment!column.columnName}结束 */
  end${column.capJavaField}?: <#if column.javaType == 'Long' || column.javaType == 'Integer'>number<#else>string</#if>;
<#else>
  /** ${column.columnComment!column.columnName} */
  ${column.javaField}?: <#if column.javaType == 'Long' || column.javaType == 'Integer'>number<#elseif column.javaType == 'Boolean'>boolean<#else>string</#if>;
</#if>
</#list>
}
