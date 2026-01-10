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

/** 用户注册 POST /api/auth/register */
export async function register(body: API.RegisterParams, options?: { [key: string]: any }) {
  return request<API.ResponseResult<void>>('/api/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
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
