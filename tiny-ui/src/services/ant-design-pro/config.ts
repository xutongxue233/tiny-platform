import { request } from '@umijs/max';

/** 分页查询参数配置列表 POST /api/system/config/page */
export async function getConfigPage(
  params: API.SysConfigQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.SysConfig>>>('/api/system/config/page', {
    method: 'POST',
    data: {
      current: params.current || 1,
      size: params.size || 10,
      configName: params.configName,
      configKey: params.configKey,
      configType: params.configType,
      configGroup: params.configGroup,
      status: params.status,
    },
    ...(options || {}),
  });
}

/** 查询所有参数配置列表 GET /api/system/config/list */
export async function getConfigList(options?: { [key: string]: any }) {
  return request<API.ResponseResult<API.SysConfig[]>>('/api/system/config/list', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 根据分组查询参数配置列表 GET /api/system/config/list/:configGroup */
export async function getConfigListByGroup(
  configGroup: string,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysConfig[]>>(`/api/system/config/list/${configGroup}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取参数配置详情 GET /api/system/config/:configId */
export async function getConfigDetail(
  configId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysConfig>>(`/api/system/config/${configId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 根据键名获取参数值 GET /api/system/config/value/:configKey */
export async function getConfigValue(
  configKey: string,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<string>>(`/api/system/config/value/${configKey}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 新增参数配置 POST /api/system/config */
export async function addConfig(
  data: API.SysConfigDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/config', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

/** 更新参数配置 PUT /api/system/config */
export async function updateConfig(
  data: API.SysConfigDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/config', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 删除参数配置 DELETE /api/system/config/:configId */
export async function deleteConfig(
  configId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/system/config/${configId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 批量删除参数配置 DELETE /api/system/config/batch */
export async function deleteConfigBatch(
  data: API.DeleteDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/config/batch', {
    method: 'DELETE',
    data,
    ...(options || {}),
  });
}

/** 修改参数配置状态 PUT /api/system/config/status */
export async function changeConfigStatus(
  data: API.UpdateStatusDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/config/status', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 刷新参数配置缓存 POST /api/system/config/refreshCache */
export async function refreshConfigCache(options?: { [key: string]: any }) {
  return request<API.ResponseResult<void>>('/api/system/config/refreshCache', {
    method: 'POST',
    ...(options || {}),
  });
}
