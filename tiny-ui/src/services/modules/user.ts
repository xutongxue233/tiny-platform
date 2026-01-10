import { request } from '@umijs/max';

/** 获取用户分页列表 POST /api/system/user/page */
export async function getUserPage(
  params: API.SysUserQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.SysUser>>>('/api/system/user/page', {
    method: 'POST',
    data: {
      current: params.current || 1,
      size: params.size || 10,
      username: params.username,
      realName: params.realName,
      phone: params.phone,
      status: params.status,
      deptId: params.deptId,
    },
    ...(options || {}),
  });
}

/** 获取用户详情 GET /api/system/user/:id */
export async function getUserDetail(
  userId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysUser>>(`/api/system/user/${userId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 新增用户 POST /api/system/user */
export async function addUser(
  data: API.SysUserDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/user', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

/** 更新用户 PUT /api/system/user */
export async function updateUser(
  data: API.SysUserDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/user', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 删除单个用户 DELETE /api/system/user/:userId */
export async function deleteUser(
  userId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/system/user/${userId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 批量删除用户 DELETE /api/system/user/batch */
export async function deleteUserBatch(
  data: API.DeleteDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/user/batch', {
    method: 'DELETE',
    data,
    ...(options || {}),
  });
}

/** 重置用户密码 PUT /api/system/user/resetPassword */
export async function resetUserPassword(
  data: API.ResetPasswordDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/user/resetPassword', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 修改用户状态 PUT /api/system/user/status */
export async function changeUserStatus(
  data: API.UpdateStatusDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/user/status', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 获取角色列表 GET /api/system/user/roles */
export async function getUserRoles(options?: { [key: string]: any }) {
  return request<API.ResponseResult<{ roleId: number; roleName: string }[]>>('/api/system/user/roles', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 检查用户名是否存在 GET /api/system/user/checkUsername */
export async function checkUsername(
  username: string,
  userId?: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<boolean>>('/api/system/user/checkUsername', {
    method: 'GET',
    params: { username, userId },
    ...(options || {}),
  });
}

/** 获取所有用户简单列表 GET /api/system/user/list/simple */
export async function getUserListAll(options?: { [key: string]: any }) {
  return request<API.ResponseResult<API.SysUser[]>>('/api/system/user/list/simple', {
    method: 'GET',
    ...(options || {}),
  });
}
