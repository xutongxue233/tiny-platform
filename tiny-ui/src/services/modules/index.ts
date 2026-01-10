/**
 * API 模块统一导出
 * 按业务模块组织 API，便于维护和代码分割
 */

// 认证相关
export * from './auth';

// 用户管理
export * from './user';

// 角色管理
export * from './role';

// 菜单管理
export * from './menu';

// 部门管理
export * from './dept';

// 监控管理
export * from './monitor';

// 存储管理
export * from './storage';

// 系统配置
export * from './config';
