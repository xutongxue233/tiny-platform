import { request } from '@umijs/max';

/** 获取公开配置 GET /api/system/config/public */
export async function getPublicConfig(options?: { [key: string]: any }) {
  return request<API.ResponseResult<API.PublicConfig>>('/api/system/config/public', {
    method: 'GET',
    ...(options || {}),
  });
}
