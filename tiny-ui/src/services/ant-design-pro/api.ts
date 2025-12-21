import { request } from '@umijs/max';

const TOKEN_KEY = 'Authorization';

/** 用户登录 POST /auth/login */
export async function login(body: API.LoginParams, options?: { [key: string]: any }) {
  return request<API.ResponseResult<API.LoginResult>>('/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 用户登出 POST /auth/logout */
export async function logout(options?: { [key: string]: any }) {
  return request<API.ResponseResult<void>>('/auth/logout', {
    method: 'POST',
    ...(options || {}),
  });
}

/** 获取当前用户信息 GET /auth/getUserInfo */
export async function getCurrentUser(options?: { [key: string]: any }) {
  return request<API.ResponseResult<API.UserInfo>>('/auth/getUserInfo', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取验证码 GET /auth/captcha */
export async function getCaptcha(options?: { [key: string]: any }) {
  return request<API.ResponseResult<{ captchaKey: string; captchaImage: string }>>('/auth/captcha', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取规则列表 GET /api/rule */
export async function rule(
  params: {
    current?: number;
    pageSize?: number;
  },
  options?: { [key: string]: any },
) {
  return request<API.RuleList>('/api/rule', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 更新规则 PUT /api/rule */
export async function updateRule(options?: { [key: string]: any }) {
  return request<API.RuleListItem>('/api/rule', {
    method: 'POST',
    data: {
      method: 'update',
      ...(options || {}),
    },
  });
}

/** 新建规则 POST /api/rule */
export async function addRule(options?: { [key: string]: any }) {
  return request<API.RuleListItem>('/api/rule', {
    method: 'POST',
    data: {
      method: 'post',
      ...(options || {}),
    },
  });
}

/** 删除规则 DELETE /api/rule */
export async function removeRule(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/rule', {
    method: 'POST',
    data: {
      method: 'delete',
      ...(options || {}),
    },
  });
}

/** Token 管理工具 */
export const TokenUtil = {
  getToken: (): string | null => {
    return localStorage.getItem(TOKEN_KEY);
  },
  setToken: (token: string) => {
    localStorage.setItem(TOKEN_KEY, token);
  },
  removeToken: () => {
    localStorage.removeItem(TOKEN_KEY);
  },
};

/** 获取用户分页列表 POST /system/user/page */
export async function getUserPage(
  params: API.SysUserQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.SysUser>>>('/system/user/page', {
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

/** 获取用户详情 GET /system/user/:id */
export async function getUserDetail(
  userId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysUser>>(`/system/user/${userId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 新增用户 POST /system/user */
export async function addUser(
  data: API.SysUserDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/system/user', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

/** 更新用户 PUT /system/user */
export async function updateUser(
  data: API.SysUserDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/system/user', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 删除单个用户 DELETE /system/user/:userId */
export async function deleteUser(
  userId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/system/user/${userId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 批量删除用户 DELETE /system/user/batch */
export async function deleteUserBatch(
  userIds: number[],
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/system/user/batch', {
    method: 'DELETE',
    data: userIds,
    ...(options || {}),
  });
}

/** 重置用户密码 PUT /system/user/resetPassword */
export async function resetUserPassword(
  data: API.ResetPasswordDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/system/user/resetPassword', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 修改用户状态 PUT /system/user/status */
export async function changeUserStatus(
  data: API.UpdateStatusDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/system/user/status', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 获取角色列表 GET /system/user/roles */
export async function getUserRoles(options?: { [key: string]: any }) {
  return request<API.ResponseResult<{ roleId: number; roleName: string }[]>>('/system/user/roles', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 检查用户名是否存在 GET /system/user/checkUsername */
export async function checkUsername(
  username: string,
  userId?: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<boolean>>('/system/user/checkUsername', {
    method: 'GET',
    params: { username, userId },
    ...(options || {}),
  });
}
