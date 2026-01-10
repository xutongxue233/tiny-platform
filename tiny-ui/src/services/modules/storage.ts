import { request } from '@umijs/max';

/** 获取存储配置分页列表 GET /api/storage/config/page */
export async function getStorageConfigPage(
  params: API.StorageConfigQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.StorageConfigVO>>>('/api/storage/config/page', {
    method: 'GET',
    params: {
      current: params.current || 1,
      size: params.size || 10,
      configName: params.configName,
      storageType: params.storageType,
      status: params.status,
    },
    ...(options || {}),
  });
}

/** 获取所有存储配置列表 GET /api/storage/config/list */
export async function getStorageConfigList(options?: { [key: string]: any }) {
  return request<API.ResponseResult<API.StorageConfigVO[]>>('/api/storage/config/list', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取存储配置详情 GET /api/storage/config/:configId */
export async function getStorageConfigById(
  configId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.StorageConfigVO>>(`/api/storage/config/${configId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 新增存储配置 POST /api/storage/config */
export async function addStorageConfig(
  data: API.StorageConfigDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/storage/config', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

/** 修改存储配置 PUT /api/storage/config */
export async function updateStorageConfig(
  data: API.StorageConfigDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/storage/config', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 删除存储配置 DELETE /api/storage/config/:configId */
export async function deleteStorageConfig(
  configId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/storage/config/${configId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 设置默认存储配置 PUT /api/storage/config/default/:configId */
export async function setDefaultStorageConfig(
  configId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/storage/config/default/${configId}`, {
    method: 'PUT',
    ...(options || {}),
  });
}

/** 测试存储配置连接 POST /api/storage/config/test */
export async function testStorageConnection(
  data: API.StorageConfigDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<boolean>>('/api/storage/config/test', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

/** 获取存储类型列表 GET /api/storage/config/types */
export async function getStorageTypes(options?: { [key: string]: any }) {
  return request<API.ResponseResult<API.StorageTypeOption[]>>('/api/storage/config/types', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取文件分页列表 GET /api/storage/file/page */
export async function getFilePage(
  params: API.FileQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.FileInfo>>>('/api/storage/file/page', {
    method: 'GET',
    params: {
      current: params.current || 1,
      size: params.size || 10,
      originalFilename: params.originalFilename,
      fileType: params.fileType,
      storageType: params.storageType,
    },
    ...(options || {}),
  });
}

/** 上传文件 POST /api/storage/file/upload */
export async function uploadFile(
  file: File,
  options?: { [key: string]: any },
) {
  const formData = new FormData();
  formData.append('file', file);
  return request<API.ResponseResult<API.FileUploadResult>>('/api/storage/file/upload', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  });
}

/** 删除文件 DELETE /api/storage/file/:fileId */
export async function deleteFile(
  fileId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/storage/file/${fileId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 批量删除文件 DELETE /api/storage/file/batch */
export async function deleteFileBatch(
  fileIds: number[],
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/storage/file/batch', {
    method: 'DELETE',
    data: fileIds,
    ...(options || {}),
  });
}

/** 获取文件访问URL GET /api/storage/file/url/:fileId */
export async function getFileUrl(
  fileId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<string>>(`/api/storage/file/url/${fileId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取文件临时访问URL GET /api/storage/file/temp-url/:fileId */
export async function getFileTempUrl(
  fileId: number,
  expireTime: number = 3600,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<string>>(`/api/storage/file/temp-url/${fileId}`, {
    method: 'GET',
    params: { expireTime },
    ...(options || {}),
  });
}
