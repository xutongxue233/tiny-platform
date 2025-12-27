import { request } from '@umijs/max';

/** 获取导出任务分页列表 */
export async function getExportTaskPage(
  params: API.ExportTaskQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.ExportTask>>>('/api/export/task/page', {
    method: 'POST',
    data: params,
    ...(options || {}),
  });
}

/** 获取导出任务详情 */
export async function getExportTaskById(
  taskId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.ExportTask>>(`/api/export/task/${taskId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 删除导出任务 */
export async function deleteExportTask(
  taskId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/export/task/${taskId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 批量删除导出任务 */
export async function deleteExportTaskBatch(
  ids: number[],
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/export/task/batch', {
    method: 'DELETE',
    data: { ids },
    ...(options || {}),
  });
}

/** 清理过期任务 */
export async function cleanExpiredTasks(options?: { [key: string]: any }) {
  return request<API.ResponseResult<number>>('/api/export/task/clean', {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 获取导出文件下载URL */
export function getExportDownloadUrl(taskId: number): string {
  return `/api/export/task/download/${taskId}`;
}

/** 用户数据导出(同步) */
export async function exportUserData(
  params?: API.SysUserQueryParams,
  options?: { [key: string]: any },
) {
  return request('/api/system/user/export', {
    method: 'POST',
    data: params || {},
    responseType: 'blob',
    ...(options || {}),
  });
}

/** 用户数据异步导出 */
export async function exportUserDataAsync(
  params?: API.SysUserQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<number>>('/api/system/user/exportAsync', {
    method: 'POST',
    data: params || {},
    ...(options || {}),
  });
}

/** 获取用户导入模板URL */
export function getUserImportTemplateUrl(): string {
  return '/api/system/user/template';
}

/** 导入用户数据 */
export async function importUserData(
  file: File,
  options?: { [key: string]: any },
) {
  const formData = new FormData();
  formData.append('file', file);
  return request<API.ResponseResult<API.ImportResult>>('/api/system/user/import', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  });
}

/** 字典数据导出(同步) */
export async function exportDictItemData(
  params?: API.SysDictItemQueryParams,
  options?: { [key: string]: any },
) {
  return request('/api/system/dictItem/export', {
    method: 'POST',
    data: params || {},
    responseType: 'blob',
    ...(options || {}),
  });
}

/** 获取字典导入模板URL */
export function getDictItemImportTemplateUrl(): string {
  return '/api/system/dictItem/template';
}

/** 导入字典数据 */
export async function importDictItemData(
  file: File,
  options?: { [key: string]: any },
) {
  const formData = new FormData();
  formData.append('file', file);
  return request<API.ResponseResult<API.ImportResult>>('/api/system/dictItem/import', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  });
}
