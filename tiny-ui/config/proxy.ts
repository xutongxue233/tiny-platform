/**
 * @name 代理的配置
 * @see 在生产环境 代理是无法生效的，所以这里没有生产环境的配置
 * @doc https://umijs.org/docs/guides/proxy
 */
export default {
  dev: {
    '/api/ws': {
      target: 'http://localhost:8081',
      changeOrigin: true,
      ws: true,
    },
    '/api/': {
      target: 'http://localhost:8081',
      changeOrigin: true,
    },
  },
  test: {
    '/api/ws': {
      target: 'http://localhost:8081',
      changeOrigin: true,
      ws: true,
    },
    '/api/': {
      target: 'http://localhost:8081',
      changeOrigin: true,
    },
  },
  pre: {
    '/api/': {
      target: 'your pre url',
      changeOrigin: true,
    },
  },
};
