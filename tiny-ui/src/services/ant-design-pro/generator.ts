import { request } from '@umijs/max';

/** 代码生成表 */
export interface GenTable {
  tableId?: number;
  tableName?: string;
  tableComment?: string;
  moduleName?: string;
  packageName?: string;
  className?: string;
  businessName?: string;
  functionName?: string;
  author?: string;
  feModulePath?: string;
  feGenerateType?: string;
  genType?: string;
  genPath?: string;
  options?: string;
  createTime?: string;
  updateTime?: string;
  remark?: string;
  columns?: GenTableColumn[];
  pkColumn?: GenTableColumn;
}

/** 代码生成字段 */
export interface GenTableColumn {
  columnId?: number;
  tableId?: number;
  columnName?: string;
  columnComment?: string;
  columnType?: string;
  javaType?: string;
  javaField?: string;
  isPk?: string;
  isIncrement?: string;
  isRequired?: string;
  isInsert?: string;
  isEdit?: string;
  isList?: string;
  isQuery?: string;
  isDetail?: string;
  isExport?: string;
  queryType?: string;
  htmlType?: string;
  dictType?: string;
  sortOrder?: number;
}

/** 代码生成配置 */
export interface GenConfig {
  configId?: number;
  configKey?: string;
  configValue?: string;
  configDesc?: string;
}

/** 查询代码生成表列表 GET /api/gen/list */
export async function getGenTableList(options?: { [key: string]: any }) {
  return request<API.ResponseResult<GenTable[]>>('/api/gen/list', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 查询数据库表列表 GET /api/gen/db/list */
export async function getDbTableList(
  tableName?: string,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<GenTable[]>>('/api/gen/db/list', {
    method: 'GET',
    params: { tableName },
    ...(options || {}),
  });
}

/** 获取表配置详情 GET /api/gen/:tableId */
export async function getGenTableDetail(
  tableId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<GenTable>>(`/api/gen/${tableId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 导入表结构 POST /api/gen/import */
export async function importTable(
  tableNames: string[],
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/gen/import', {
    method: 'POST',
    data: tableNames,
    ...(options || {}),
  });
}

/** 更新表配置 PUT /api/gen */
export async function updateGenTable(
  data: GenTable,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/gen', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 删除表配置 DELETE /api/gen/:tableIds */
export async function deleteGenTable(
  tableIds: number[],
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/gen/${tableIds.join(',')}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 同步数据库表结构 POST /api/gen/sync/:tableId */
export async function syncTable(
  tableId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/gen/sync/${tableId}`, {
    method: 'POST',
    ...(options || {}),
  });
}

/** 重新生成表配置 POST /api/gen/regenerate/:tableId */
export async function regenerateConfig(
  tableId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/gen/regenerate/${tableId}`, {
    method: 'POST',
    ...(options || {}),
  });
}

/** 批量重新生成表配置 POST /api/gen/batchRegenerate */
export async function batchRegenerateConfig(
  tableIds: number[],
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/gen/batchRegenerate', {
    method: 'POST',
    data: tableIds,
    ...(options || {}),
  });
}

/** 批量同步数据库表结构 POST /api/gen/batchSync */
export async function batchSyncTable(
  tableIds: number[],
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/gen/batchSync', {
    method: 'POST',
    data: tableIds,
    ...(options || {}),
  });
}

/** 预览代码 GET /api/gen/preview/:tableId */
export async function previewCode(
  tableId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<Record<string, string>>>(`/api/gen/preview/${tableId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 下载代码 GET /api/gen/download/:tableId */
export function downloadCode(tableId: number) {
  window.open(`/api/gen/download/${tableId}`);
}

/** 批量下载代码 GET /api/gen/batchDownload */
export function batchDownloadCode(tableIds: number[]) {
  window.open(`/api/gen/batchDownload?tableIds=${tableIds.join(',')}`);
}

/** 生成代码到路径 POST /api/gen/genToPath/:tableId */
export async function genToPath(
  tableId: number,
  path?: string,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/gen/genToPath/${tableId}`, {
    method: 'POST',
    params: { path },
    ...(options || {}),
  });
}

/** 获取所有配置 GET /api/gen/config/list */
export async function getGenConfigList(options?: { [key: string]: any }) {
  return request<API.ResponseResult<GenConfig[]>>('/api/gen/config/list', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 批量更新配置 PUT /api/gen/config/batch */
export async function batchUpdateGenConfig(
  configs: GenConfig[],
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/gen/config/batch', {
    method: 'PUT',
    data: configs,
    ...(options || {}),
  });
}
