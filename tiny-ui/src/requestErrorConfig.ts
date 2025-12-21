import type { RequestOptions } from '@@/plugin-request/request';
import type { RequestConfig } from '@umijs/max';
import { message, notification } from 'antd';
import { history } from '@umijs/max';
import { TokenUtil } from '@/services/ant-design-pro/api';

const loginPath = '/user/login';

/**
 * @name 错误处理
 * @doc https://umijs.org/docs/max/request#配置
 */
export const errorConfig: RequestConfig = {
  errorConfig: {
    errorThrower: (res) => {
      const { code, message: msg, data } = res as API.ResponseResult;
      if (code !== 200) {
        const error: any = new Error(msg);
        error.name = 'BizError';
        error.info = { code, message: msg, data };
        throw error;
      }
    },
    errorHandler: (error: any, opts: any) => {
      if (opts?.skipErrorHandler) throw error;

      if (error.name === 'BizError') {
        const errorInfo = error.info;
        if (errorInfo) {
          const { message: msg, code } = errorInfo;
          if (code === 401) {
            TokenUtil.removeToken();
            if (history.location.pathname !== loginPath) {
              message.error('登录已过期，请重新登录');
              history.push(loginPath);
            }
            return;
          }
          if (code === 403) {
            message.error('权限不足');
            return;
          }
          message.error(msg || '请求失败');
        }
      } else if (error.response) {
        const status = error.response.status;
        if (status === 401) {
          TokenUtil.removeToken();
          if (history.location.pathname !== loginPath) {
            message.error('登录已过期，请重新登录');
            history.push(loginPath);
          }
          return;
        }
        if (status === 403) {
          message.error('权限不足');
          return;
        }
        if (status === 404) {
          message.error('请求的资源不存在');
          return;
        }
        if (status >= 500) {
          message.error('服务器错误，请稍后重试');
          return;
        }
        message.error(`请求错误：${status}`);
      } else if (error.request) {
        message.error('网络异常，请检查网络连接');
      } else {
        message.error('请求失败，请重试');
      }
    },
  },

  requestInterceptors: [
    (config: RequestOptions) => {
      const token = TokenUtil.getToken();
      if (token) {
        config.headers = {
          ...config.headers,
          Authorization: token,
        };
      }
      return config;
    },
  ],

  responseInterceptors: [
    (response) => {
      const { data } = response as unknown as { data: API.ResponseResult };
      if (data?.code !== undefined && data.code !== 200) {
        const error: any = new Error(data.message || '请求失败');
        error.name = 'BizError';
        error.info = data;
        throw error;
      }
      return response;
    },
  ],
};
