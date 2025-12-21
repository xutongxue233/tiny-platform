/**
 * @name 代理的配置
 * @see 在生产环境 代理是无法生效的，所以这里没有生产环境的配置
 * @doc https://umijs.org/docs/guides/proxy
 */
export default {
  dev: {
    '/auth/': {
      target: 'http://localhost:8081',
      changeOrigin: true,
    },
    '/api/': {
      target: 'http://localhost:8081',
      changeOrigin: true,
    },
    '/system/': {
      target: 'http://localhost:8081',
      changeOrigin: true,
    },
  },
  test: {
    '/auth/': {
      target: 'http://localhost:8081',
      changeOrigin: true,
    },
    '/api/': {
      target: 'http://localhost:8081',
      changeOrigin: true,
    },
  },
  pre: {
    '/auth/': {
      target: 'your pre url',
      changeOrigin: true,
    },
    '/api/': {
      target: 'your pre url',
      changeOrigin: true,
    },
  },
};
