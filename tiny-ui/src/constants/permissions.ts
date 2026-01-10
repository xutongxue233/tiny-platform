/**
 * 权限常量定义
 * 统一管理所有权限字符串，避免硬编码
 */

/** 系统管理权限 */
export const SYSTEM = {
  /** 用户管理 */
  USER: {
    ADD: 'system:user:add',
    EDIT: 'system:user:edit',
    REMOVE: 'system:user:remove',
    EXPORT: 'system:user:export',
    IMPORT: 'system:user:import',
  },
  /** 角色管理 */
  ROLE: {
    ADD: 'system:role:add',
    EDIT: 'system:role:edit',
    REMOVE: 'system:role:remove',
  },
  /** 菜单管理 */
  MENU: {
    ADD: 'system:menu:add',
    EDIT: 'system:menu:edit',
    REMOVE: 'system:menu:remove',
  },
  /** 部门管理 */
  DEPT: {
    ADD: 'system:dept:add',
    EDIT: 'system:dept:edit',
    REMOVE: 'system:dept:remove',
  },
  /** 字典管理 */
  DICT: {
    ADD: 'system:dict:add',
    EDIT: 'system:dict:edit',
    REMOVE: 'system:dict:remove',
    EXPORT: 'system:dict:export',
    IMPORT: 'system:dict:import',
  },
  /** 参数配置 */
  CONFIG: {
    ADD: 'system:config:add',
    EDIT: 'system:config:edit',
    REMOVE: 'system:config:remove',
    REFRESH: 'system:config:refresh',
  },
} as const;

/** 监控管理权限 */
export const MONITOR = {
  /** 登录日志 */
  LOGIN_LOG: {
    REMOVE: 'monitor:loginLog:remove',
    CLEAN: 'monitor:loginLog:clean',
    EXPORT: 'monitor:loginLog:export',
  },
  /** 操作日志 */
  OPERATION_LOG: {
    REMOVE: 'monitor:operationLog:remove',
    CLEAN: 'monitor:operationLog:clean',
    EXPORT: 'monitor:operationLog:export',
  },
  /** 在线用户 */
  ONLINE: {
    KICKOUT: 'monitor:online:kickout',
    DISABLE: 'monitor:online:disable',
  },
} as const;

/** 工具管理权限 */
export const TOOL = {
  /** 代码生成 */
  GEN: {
    IMPORT: 'tool:gen:import',
    EDIT: 'tool:gen:edit',
    REMOVE: 'tool:gen:remove',
    PREVIEW: 'tool:gen:preview',
    CODE: 'tool:gen:code',
    CONFIG: 'tool:gen:config',
  },
} as const;

/** 消息管理权限 */
export const MESSAGE = {
  /** 邮件配置 */
  EMAIL: {
    ADD: 'message:email:add',
    EDIT: 'message:email:edit',
    REMOVE: 'message:email:remove',
    TEST: 'message:email:test',
  },
  /** 消息模板 */
  TEMPLATE: {
    ADD: 'message:template:add',
    EDIT: 'message:template:edit',
    REMOVE: 'message:template:remove',
  },
  /** 消息列表 */
  LIST: {
    SEND: 'message:list:send',
    REVOKE: 'message:list:revoke',
    REMOVE: 'message:list:remove',
  },
  /** 发送日志 */
  LOG: {
    RETRY: 'message:log:retry',
    REMOVE: 'message:log:remove',
  },
} as const;

/** 存储管理权限 */
export const STORAGE = {
  /** 存储配置 */
  CONFIG: {
    ADD: 'storage:config:add',
    EDIT: 'storage:config:edit',
    REMOVE: 'storage:config:remove',
  },
  /** 文件管理 */
  FILE: {
    UPLOAD: 'storage:file:upload',
    REMOVE: 'storage:file:remove',
  },
} as const;

/** 所有权限常量 */
const PERMISSIONS = {
  SYSTEM,
  MONITOR,
  TOOL,
  MESSAGE,
  STORAGE,
} as const;

export default PERMISSIONS;
