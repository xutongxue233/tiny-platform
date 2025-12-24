import { request } from '@umijs/max';
import type { ${className}VO, ${className}DTO, ${className}Query } from './${businessName}.d';

/**
 * 查询${functionName!tableComment}列表
 */
export async function list${className}(params?: ${className}Query) {
  return request<API.ResponseResult<${className}VO[]>>('/${businessName}/list', {
    method: 'GET',
    params,
  });
}

/**
 * 获取${functionName!tableComment}详情
 */
export async function get${className}(${pkColumn.javaField}: ${pkColumn.javaType}) {
  return request<API.ResponseResult<${className}VO>>(`/${businessName}/<#noparse>${</#noparse>${pkColumn.javaField}}`, {
    method: 'GET',
  });
}

/**
 * 新增${functionName!tableComment}
 */
export async function add${className}(data: ${className}DTO) {
  return request<API.ResponseResult<void>>('/${businessName}', {
    method: 'POST',
    data,
  });
}

/**
 * 修改${functionName!tableComment}
 */
export async function update${className}(data: ${className}DTO) {
  return request<API.ResponseResult<void>>('/${businessName}', {
    method: 'PUT',
    data,
  });
}

/**
 * 删除${functionName!tableComment}
 */
export async function remove${className}(${pkColumn.javaField}s: ${pkColumn.javaType}[]) {
  return request<API.ResponseResult<void>>(`/${businessName}/<#noparse>${</#noparse>${pkColumn.javaField}s.join(',')}`, {
    method: 'DELETE',
  });
}
