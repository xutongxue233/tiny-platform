import { request } from '@umijs/max';

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

/** 获取在线用户分页列表 POST /api/monitor/online/page */
export async function getOnlineUserPage(
  params: API.OnlineUserQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.OnlineUser>>>('/api/monitor/online/page', {
    method: 'POST',
    data: {
      current: params.current || 1,
      size: params.size || 10,
      username: params.username,
      ipAddr: params.ipAddr,
    },
    ...(options || {}),
  });
}

/** 强制退出用户 DELETE /api/monitor/online/kickout/:tokenId */
export async function kickoutOnlineUser(
  tokenId: string,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/monitor/online/kickout/${encodeURIComponent(tokenId)}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 强制退出用户（根据用户ID） DELETE /api/monitor/online/kickoutByUserId/:userId */
export async function kickoutOnlineUserByUserId(
  userId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/monitor/online/kickoutByUserId/${userId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

/** 封禁用户账号 POST /api/monitor/online/disable/:userId */
export async function disableOnlineUser(
  userId: number,
  disableTime: number = -1,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/monitor/online/disable/${userId}`, {
    method: 'POST',
    params: { disableTime },
    ...(options || {}),
  });
}

/** 解除用户封禁 POST /api/monitor/online/untieDisable/:userId */
export async function untieDisableUser(
  userId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/monitor/online/untieDisable/${userId}`, {
    method: 'POST',
    ...(options || {}),
  });
}

/** 检查用户是否被封禁 GET /api/monitor/online/isDisabled/:userId */
export async function checkUserDisabled(
  userId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<boolean>>(`/api/monitor/online/isDisabled/${userId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取用户封禁剩余时间 GET /api/monitor/online/disableTime/:userId */
export async function getUserDisableTime(
  userId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<number>>(`/api/monitor/online/disableTime/${userId}`, {
    method: 'GET',
    ...(options || {}),
  });
}
