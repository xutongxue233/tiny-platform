import { request } from '@umijs/max';

/** 获取角色分页列表 POST /api/system/role/page */
export async function getRolePage(
  params: API.SysRoleQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.SysRole>>>('/api/system/role/page', {
    method: 'POST',
    data: {
      current: params.current || 1,
      size: params.size || 10,
      roleName: params.roleName,
      roleKey: params.roleKey,
      status: params.status,
    },
    ...(options || {}),
  });
}

/** 获取所有角色列表 GET /api/system/role/list */
export async function getRoleList(options?: { [key: string]: any }) {
  return request<API.ResponseResult<API.SysRole[]>>('/api/system/role/list', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取角色详情 GET /api/system/role/:roleId */
export async function getRoleDetail(
  roleId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysRole>>(`/api/system/role/${roleId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 新增角色 POST /api/system/role */
export async function addRole(
  data: API.SysRoleDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/role', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

/** 更新角色 PUT /api/system/role */
export async function updateRole(
  data: API.SysRoleDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/role', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 删除单个角色 DELETE /api/system/role/:roleId */
export async function deleteRole(
  roleId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/system/role/${roleId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 批量删除角色 DELETE /api/system/role/batch */
export async function deleteRoleBatch(
  data: API.DeleteDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/role/batch', {
    method: 'DELETE',
    data,
    ...(options || {}),
  });
}

/** 修改角色状态 PUT /api/system/role/status */
export async function changeRoleStatus(
  data: API.UpdateStatusDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/role/status', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 获取菜单树（用于角色权限分配） GET /api/system/role/menuTree */
export async function getRoleMenuTree(options?: { [key: string]: any }) {
  return request<API.ResponseResult<API.SysMenu[]>>('/api/system/role/menuTree', {
    method: 'GET',
    ...(options || {}),
  });
}
