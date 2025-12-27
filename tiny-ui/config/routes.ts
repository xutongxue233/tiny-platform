/**
 * @name umi 的路由配置
 * @description 只支持 path,component,routes,redirect,wrappers,name,icon 的配置
 * @param path  path 只支持两种占位符配置，第一种是动态参数 :id 的形式，第二种是 * 通配符，通配符只能出现路由字符串的最后。
 * @param component 配置 location 和 path 匹配后用于渲染的 React 组件路径。可以是绝对路径，也可以是相对路径，如果是相对路径，会从 src/pages 开始找起。
 * @param routes 配置子路由，通常在需要为多个路径增加 layout 组件时使用。
 * @param redirect 配置路由跳转
 * @param wrappers 配置路由组件的包装组件，通过包装组件可以为当前的路由组件组合进更多的功能。 比如，可以用于路由级别的权限校验
 * @param name 配置路由的标题，默认读取国际化文件 menu.ts 中 menu.xxxx 的值，如配置 name 为 login，则读取 menu.ts 中 menu.login 的取值作为标题
 * @param icon 配置路由的图标，取值参考 https://ant.design/components/icon-cn， 注意去除风格后缀和大小写，如想要配置图标为 <StepBackwardOutlined /> 则取值应为 stepBackward 或 StepBackward，如想要配置图标为 <UserOutlined /> 则取值应为 user 或者 User
 * @doc https://umijs.org/docs/guides/routes
 */
export default [
  {
    path: '/user',
    layout: false,
    routes: [
      {
        name: 'login',
        path: '/user/login',
        component: './user/login',
      },
      {
        name: 'register',
        path: '/user/register',
        component: './user/register',
      },
    ],
  },
  {
    path: '/welcome',
    component: './Welcome',
    hideInMenu: true,
  },
  {
    path: '/admin',
    hideInMenu: true,
    routes: [
      {
        path: '/admin',
        redirect: '/admin/sub-page',
      },
      {
        path: '/admin/sub-page',
        component: './Admin',
      },
    ],
  },
  {
    path: '/list',
    component: './table-list',
    hideInMenu: true,
  },
  {
    path: '/system',
    hideInMenu: true,
    routes: [
      {
        path: '/system',
        redirect: '/system/user',
      },
      {
        path: '/system/user',
        component: './system/user/index',
      },
      {
        path: '/system/role',
        component: './system/role/index',
      },
      {
        path: '/system/menu',
        component: './system/menu/index',
      },
      {
        path: '/system/dept',
        component: './system/dept/index',
      },
      {
        path: '/system/storage',
        component: './system/storage/index',
      },
      {
        path: '/system/dict',
        component: './system/dict/index',
      },
      {
        path: '/system/config',
        component: './system/config/index',
      },
    ],
  },
  {
    path: '/storage',
    hideInMenu: true,
    routes: [
      {
        path: '/storage',
        redirect: '/storage/file',
      },
      {
        path: '/storage/file',
        component: './storage/file/index',
      },
      {
        path: '/storage/config',
        component: './system/storage/index',
      },
    ],
  },
  {
    path: '/monitor',
    hideInMenu: true,
    routes: [
      {
        path: '/monitor',
        redirect: '/monitor/loginLog',
      },
      {
        path: '/monitor/loginLog',
        component: './monitor/loginLog/index',
      },
      {
        path: '/monitor/operationLog',
        component: './monitor/operationLog/index',
      },
      {
        path: '/monitor/onlineUser',
        component: './monitor/onlineUser/index',
      },
    ],
  },
  {
    path: '/tool',
    hideInMenu: true,
    routes: [
      {
        path: '/tool',
        redirect: '/tool/gen',
      },
      {
        path: '/tool/gen',
        component: './tool/gen/index',
      },
      {
        path: '/tool/gen/edit/:tableId',
        component: './tool/gen/edit',
      },
      {
        path: '/tool/genConfig',
        component: './tool/genConfig/index',
      },
    ],
  },
  {
    path: '/info',
    hideInMenu: true,
    routes: [
      {
        path: '/info',
        redirect: '/info/notice',
      },
      {
        path: '/info/notice',
        component: './info/notice/index',
      },
    ],
  },
  {
    path: '/message',
    hideInMenu: true,
    routes: [
      {
        path: '/message',
        redirect: '/message/template',
      },
      {
        path: '/message/template',
        component: './message/template/index',
      },
      {
        path: '/message/list',
        component: './message/list/index',
      },
      {
        path: '/message/send-log',
        component: './message/sendLog/index',
      },
      {
        path: '/message/email-config',
        component: './message/emailConfig/index',
      },
      {
        path: '/message/center',
        component: './message/center/index',
      },
    ],
  },
  {
    path: '/',
    redirect: '/welcome',
  },
  {
    path: '*',
    layout: false,
    component: './404',
  },
];
