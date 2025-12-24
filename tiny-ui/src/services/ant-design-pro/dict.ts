import { request } from '@umijs/max';

/** 分页查询字典类型列表 POST /api/system/dictType/page */
export async function getDictTypePage(
  params: API.SysDictTypeQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.SysDictType>>>('/api/system/dictType/page', {
    method: 'POST',
    data: {
      current: params.current || 1,
      size: params.size || 10,
      dictName: params.dictName,
      dictCode: params.dictCode,
      status: params.status,
    },
    ...(options || {}),
  });
}

/** 查询所有字典类型列表 GET /api/system/dictType/list */
export async function getDictTypeList(options?: { [key: string]: any }) {
  return request<API.ResponseResult<API.SysDictType[]>>('/api/system/dictType/list', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取字典类型详情 GET /api/system/dictType/:dictId */
export async function getDictTypeDetail(
  dictId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysDictType>>(`/api/system/dictType/${dictId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 新增字典类型 POST /api/system/dictType */
export async function addDictType(
  data: API.SysDictTypeDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/dictType', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

/** 更新字典类型 PUT /api/system/dictType */
export async function updateDictType(
  data: API.SysDictTypeDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/dictType', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 删除字典类型 DELETE /api/system/dictType/:dictId */
export async function deleteDictType(
  dictId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/system/dictType/${dictId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 批量删除字典类型 DELETE /api/system/dictType/batch */
export async function deleteDictTypeBatch(
  data: API.DeleteDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/dictType/batch', {
    method: 'DELETE',
    data,
    ...(options || {}),
  });
}

/** 修改字典类型状态 PUT /api/system/dictType/status */
export async function changeDictTypeStatus(
  data: API.UpdateStatusDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/dictType/status', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 分页查询字典项列表 POST /api/system/dictItem/page */
export async function getDictItemPage(
  params: API.SysDictItemQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.SysDictItem>>>('/api/system/dictItem/page', {
    method: 'POST',
    data: {
      current: params.current || 1,
      size: params.size || 10,
      dictCode: params.dictCode,
      itemLabel: params.itemLabel,
      status: params.status,
    },
    ...(options || {}),
  });
}

/** 根据字典编码查询字典项列表 GET /api/system/dictItem/list/:dictCode */
export async function getDictItemListByCode(
  dictCode: string,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysDictItem[]>>(`/api/system/dictItem/list/${dictCode}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 根据字典编码查询启用状态的字典项列表 GET /api/system/dictItem/enabled/:dictCode */
export async function getDictItemEnabledListByCode(
  dictCode: string,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysDictItem[]>>(`/api/system/dictItem/enabled/${dictCode}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取字典项详情 GET /api/system/dictItem/:itemId */
export async function getDictItemDetail(
  itemId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysDictItem>>(`/api/system/dictItem/${itemId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 新增字典项 POST /api/system/dictItem */
export async function addDictItem(
  data: API.SysDictItemDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/dictItem', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

/** 更新字典项 PUT /api/system/dictItem */
export async function updateDictItem(
  data: API.SysDictItemDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/dictItem', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 删除字典项 DELETE /api/system/dictItem/:itemId */
export async function deleteDictItem(
  itemId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/system/dictItem/${itemId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 批量删除字典项 DELETE /api/system/dictItem/batch */
export async function deleteDictItemBatch(
  data: API.DeleteDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/dictItem/batch', {
    method: 'DELETE',
    data,
    ...(options || {}),
  });
}

/** 修改字典项状态 PUT /api/system/dictItem/status */
export async function changeDictItemStatus(
  data: API.UpdateStatusDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/dictItem/status', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 根据字典编码查询字典项 GET /api/dict/:dictCode */
export async function getDictByCode(
  dictCode: string,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysDictItem[]>>(`/api/dict/${dictCode}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 批量查询多个字典编码的字典项列表 GET /api/dict/batch */
export async function getDictBatch(
  dictCodes: string[],
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<Record<string, API.SysDictItem[]>>>('/api/dict/batch', {
    method: 'GET',
    params: { dictCodes: dictCodes.join(',') },
    ...(options || {}),
  });
}

const dictCache: Map<string, API.SysDictItem[]> = new Map();

/** 获取字典项 */
export async function getDictItemsByCode(dictCode: string): Promise<API.SysDictItem[]> {
  if (dictCache.has(dictCode)) {
    return dictCache.get(dictCode)!;
  }
  const res = await getDictByCode(dictCode);
  if (res.code === 200 && res.data) {
    dictCache.set(dictCode, res.data);
    return res.data;
  }
  return [];
}

/** 清除字典缓存 */
export function clearDictCache(dictCode?: string) {
  if (dictCode) {
    dictCache.delete(dictCode);
  } else {
    dictCache.clear();
  }
}

/** 根据字典编码和值获取标签 */
export async function getDictLabel(dictCode: string, value: string): Promise<string> {
  const items = await getDictItemsByCode(dictCode);
  const item = items.find((i) => i.itemValue === value);
  return item?.itemLabel || value;
}

/** 获取字典项映射 */
export async function getDictValueEnum(dictCode: string): Promise<Record<string, { text: string }>> {
  const items = await getDictItemsByCode(dictCode);
  const valueEnum: Record<string, { text: string }> = {};
  items.forEach((item) => {
    if (item.itemValue) {
      valueEnum[item.itemValue] = { text: item.itemLabel || '' };
    }
  });
  return valueEnum;
}
