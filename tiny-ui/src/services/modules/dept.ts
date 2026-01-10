import { request } from '@umijs/max';

/** 获取部门树形列表 GET /api/system/dept/tree */
export async function getDeptTree(
  params?: API.SysDeptQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysDept[]>>('/api/system/dept/tree', {
    method: 'GET',
    params,
    ...(options || {}),
  });
}

/** 获取部门下拉树（用于选择） GET /api/system/dept/tree/select */
export async function getDeptTreeSelect(options?: { [key: string]: any }) {
  return request<API.ResponseResult<API.SysDept[]>>('/api/system/dept/tree/select', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取部门下拉树（排除指定部门） GET /api/system/dept/tree/exclude/:deptId */
export async function getDeptTreeExclude(
  deptId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysDept[]>>(`/api/system/dept/tree/exclude/${deptId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取部门详情 GET /api/system/dept/:deptId */
export async function getDeptDetail(
  deptId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysDept>>(`/api/system/dept/${deptId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 新增部门 POST /api/system/dept */
export async function addDept(
  data: API.SysDeptDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/dept', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

/** 更新部门 PUT /api/system/dept */
export async function updateDept(
  data: API.SysDeptDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/dept', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 删除部门 DELETE /api/system/dept/:deptId */
export async function deleteDept(
  deptId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/system/dept/${deptId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 修改部门状态 PUT /api/system/dept/status */
export async function changeDeptStatus(
  data: API.UpdateStatusDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/dept/status', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}
