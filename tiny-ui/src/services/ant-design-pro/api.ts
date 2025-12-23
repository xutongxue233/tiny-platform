import { request } from '@umijs/max';

const TOKEN_KEY = 'Authorization';

/** 用户登录 POST /api/auth/login */
export async function login(body: API.LoginParams, options?: { [key: string]: any }) {
  return request<API.ResponseResult<API.LoginResult>>('/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 用户登出 POST /api/auth/logout */
export async function logout(options?: { [key: string]: any }) {
  return request<API.ResponseResult<void>>('/api/auth/logout', {
    method: 'POST',
    ...(options || {}),
  });
}

/** 获取当前用户信息 GET /api/auth/getUserInfo */
export async function getCurrentUser(options?: { [key: string]: any }) {
  return request<API.ResponseResult<API.UserInfo>>('/api/auth/getUserInfo', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取验证码 GET /api/auth/captcha */
export async function getCaptcha(options?: { [key: string]: any }) {
  return request<API.ResponseResult<{ captchaKey: string; captchaImage: string }>>('/api/auth/captcha', {
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

/** 获取当前用户路由菜单 GET /api/system/menu/routers */
export async function getRouters(options?: { [key: string]: any }) {
  return request<API.ResponseResult<API.RouterItem[]>>('/api/system/menu/routers', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取菜单列表 POST /api/system/menu/list */
export async function getMenuList(
  params: API.SysMenuQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysMenu[]>>('/api/system/menu/list', {
    method: 'POST',
    data: params,
    ...(options || {}),
  });
}

/** 获取菜单树 POST /api/system/menu/tree */
export async function getMenuTree(
  params: API.SysMenuQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysMenu[]>>('/api/system/menu/tree', {
    method: 'POST',
    data: params,
    ...(options || {}),
  });
}

/** 获取菜单详情 GET /api/system/menu/detail/:menuId */
export async function getMenuDetail(
  menuId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysMenu>>(`/api/system/menu/detail/${menuId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 新增菜单 POST /api/system/menu */
export async function addMenu(
  data: API.SysMenuDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/menu', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

/** 更新菜单 PUT /api/system/menu */
export async function updateMenu(
  data: API.SysMenuDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/menu', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 删除菜单 DELETE /api/system/menu/:menuId */
export async function deleteMenu(
  menuId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/system/menu/${menuId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

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

/** 获取登录日志分页列表 POST /api/monitor/loginLog/page */
export async function getLoginLogPage(
  params: API.SysLoginLogQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.SysLoginLog>>>('/api/monitor/loginLog/page', {
    method: 'POST',
    data: {
      current: params.current || 1,
      size: params.size || 10,
      username: params.username,
      ipAddr: params.ipAddr,
      loginType: params.loginType,
      status: params.status,
      beginTime: params.beginTime,
      endTime: params.endTime,
    },
    ...(options || {}),
  });
}

/** 批量删除登录日志 DELETE /api/monitor/loginLog/batch */
export async function deleteLoginLogBatch(
  data: API.DeleteDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/monitor/loginLog/batch', {
    method: 'DELETE',
    data,
    ...(options || {}),
  });
}

/** 清空登录日志 DELETE /api/monitor/loginLog/clean */
export async function cleanLoginLog(options?: { [key: string]: any }) {
  return request<API.ResponseResult<void>>('/api/monitor/loginLog/clean', {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 获取操作日志分页列表 POST /api/monitor/operationLog/page */
export async function getOperationLogPage(
  params: API.SysOperationLogQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.SysOperationLog>>>('/api/monitor/operationLog/page', {
    method: 'POST',
    data: {
      current: params.current || 1,
      size: params.size || 10,
      username: params.username,
      moduleName: params.moduleName,
      operationType: params.operationType,
      status: params.status,
      beginTime: params.beginTime,
      endTime: params.endTime,
    },
    ...(options || {}),
  });
}

/** 获取操作日志详情 GET /api/monitor/operationLog/:operationLogId */
export async function getOperationLogDetail(
  operationLogId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysOperationLog>>(`/api/monitor/operationLog/${operationLogId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 批量删除操作日志 DELETE /api/monitor/operationLog/batch */
export async function deleteOperationLogBatch(
  data: API.DeleteDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/monitor/operationLog/batch', {
    method: 'DELETE',
    data,
    ...(options || {}),
  });
}

/** 清空操作日志 DELETE /api/monitor/operationLog/clean */
export async function cleanOperationLog(options?: { [key: string]: any }) {
  return request<API.ResponseResult<void>>('/api/monitor/operationLog/clean', {
    method: 'DELETE',
    ...(options || {}),
  });
}
