-- 创建数据库
CREATE DATABASE IF NOT EXISTS tiny_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE tiny_admin;

-- 用户表
CREATE TABLE sys_user (
    user_id BIGINT AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    gender CHAR(1) DEFAULT '2' COMMENT '性别（0男 1女 2未知）',
    avatar VARCHAR(255) COMMENT '头像',
    dept_id BIGINT COMMENT '部门ID',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0正常 1删除）',
    remark VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (user_id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 角色表
CREATE TABLE sys_role (
    role_id BIGINT AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_key VARCHAR(50) NOT NULL COMMENT '角色标识',
    sort INT DEFAULT 0 COMMENT '显示顺序',
    data_scope CHAR(1) DEFAULT '1' COMMENT '数据范围（1全部 2自定义 3本部门 4本部门及以下 5仅本人）',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0正常 1删除）',
    remark VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (role_id),
    UNIQUE KEY uk_role_key (role_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 菜单表
CREATE TABLE sys_menu (
    menu_id BIGINT AUTO_INCREMENT COMMENT '菜单ID',
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    sort INT DEFAULT 0 COMMENT '显示顺序',
    path VARCHAR(200) COMMENT '路由地址',
    component VARCHAR(255) COMMENT '组件路径',
    menu_type CHAR(1) NOT NULL COMMENT '菜单类型（M目录 C菜单 F按钮）',
    visible CHAR(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
    status CHAR(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
    perms VARCHAR(100) COMMENT '权限标识',
    icon VARCHAR(100) COMMENT '菜单图标',
    is_frame CHAR(1) DEFAULT '0' COMMENT '是否外链（0否 1是）',
    is_cache CHAR(1) DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
    link VARCHAR(500) COMMENT '链接地址（外链时使用）',
    target VARCHAR(20) DEFAULT '_blank' COMMENT '打开方式（_blank新窗口 _self当前窗口）',
    badge VARCHAR(20) COMMENT '角标文字',
    badge_color VARCHAR(50) COMMENT '角标颜色',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0正常 1删除）',
    remark VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';

-- 用户角色关联表
CREATE TABLE sys_user_role (
    id BIGINT AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 角色菜单关联表
CREATE TABLE sys_role_menu (
    id BIGINT AUTO_INCREMENT COMMENT '主键ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_menu (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- 部门表
CREATE TABLE sys_dept (
    dept_id BIGINT AUTO_INCREMENT COMMENT '部门ID',
    dept_name VARCHAR(50) NOT NULL COMMENT '部门名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    ancestors VARCHAR(500) DEFAULT '' COMMENT '祖级列表',
    sort INT DEFAULT 0 COMMENT '显示顺序',
    leader VARCHAR(50) COMMENT '负责人',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    del_flag CHAR(1) DEFAULT '0' COMMENT '删除标志（0正常 1删除）',
    remark VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- 角色部门关联表
CREATE TABLE sys_role_dept (
    id BIGINT AUTO_INCREMENT COMMENT '主键ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    dept_id BIGINT NOT NULL COMMENT '部门ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_dept (role_id, dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色部门关联表';

-- 初始化数据
-- 初始化超级管理员（密码：123456）
INSERT INTO sys_user (username, password, real_name, status)
VALUES ('admin', '$2b$12$gis3AeExTCSgNeiYSBtwmOKyYBCrso5JP4P7Aexj/0t1fdB8z6Evy', '超级管理员', '0');

-- 初始化角色
INSERT INTO sys_role (role_name, role_key, sort, status)
VALUES ('超级管理员', 'admin', 1, '0');

INSERT INTO sys_role (role_name, role_key, sort, status)
VALUES ('普通用户', 'user', 2, '0');

-- 初始化菜单
INSERT INTO sys_menu (menu_name, parent_id, sort, path, component, menu_type, visible, status, perms, icon) VALUES
('系统管理', 0, 1, '/system', NULL, 'M', '0', '0', NULL, 'SettingOutlined'),
('用户管理', 1, 1, '/system/user', 'system/user/index', 'C', '0', '0', 'system:user:list', 'UserOutlined'),
('角色管理', 1, 2, '/system/role', 'system/role/index', 'C', '0', '0', 'system:role:list', 'TeamOutlined'),
('菜单管理', 1, 3, '/system/menu', 'system/menu/index', 'C', '0', '0', 'system:menu:list', 'MenuOutlined'),
('用户查询', 2, 1, NULL, NULL, 'F', '0', '0', 'system:user:query', NULL),
('用户新增', 2, 2, NULL, NULL, 'F', '0', '0', 'system:user:add', NULL),
('用户修改', 2, 3, NULL, NULL, 'F', '0', '0', 'system:user:edit', NULL),
('用户删除', 2, 4, NULL, NULL, 'F', '0', '0', 'system:user:remove', NULL),
('角色查询', 3, 1, NULL, NULL, 'F', '0', '0', 'system:role:query', NULL),
('角色新增', 3, 2, NULL, NULL, 'F', '0', '0', 'system:role:add', NULL),
('角色修改', 3, 3, NULL, NULL, 'F', '0', '0', 'system:role:edit', NULL),
('角色删除', 3, 4, NULL, NULL, 'F', '0', '0', 'system:role:remove', NULL),
('菜单查询', 4, 1, NULL, NULL, 'F', '0', '0', 'system:menu:query', NULL),
('菜单新增', 4, 2, NULL, NULL, 'F', '0', '0', 'system:menu:add', NULL),
('菜单修改', 4, 3, NULL, NULL, 'F', '0', '0', 'system:menu:edit', NULL),
('菜单删除', 4, 4, NULL, NULL, 'F', '0', '0', 'system:menu:remove', NULL),
('部门管理', 1, 4, '/system/dept', 'system/dept/index', 'C', '0', '0', 'system:dept:list', 'ApartmentOutlined'),
('部门查询', 17, 1, NULL, NULL, 'F', '0', '0', 'system:dept:query', NULL),
('部门新增', 17, 2, NULL, NULL, 'F', '0', '0', 'system:dept:add', NULL),
('部门修改', 17, 3, NULL, NULL, 'F', '0', '0', 'system:dept:edit', NULL),
('部门删除', 17, 4, NULL, NULL, 'F', '0', '0', 'system:dept:remove', NULL),
('系统监控', 0, 2, '/monitor', NULL, 'M', '0', '0', NULL, 'MonitorOutlined'),
('登录日志', 22, 1, '/monitor/loginLog', 'monitor/loginLog/index', 'C', '0', '0', 'monitor:loginLog:list', 'LoginOutlined'),
('操作日志', 22, 2, '/monitor/operationLog', 'monitor/operationLog/index', 'C', '0', '0', 'monitor:operationLog:list', 'FileTextOutlined'),
('登录日志查询', 23, 1, NULL, NULL, 'F', '0', '0', 'monitor:loginLog:query', NULL),
('登录日志删除', 23, 2, NULL, NULL, 'F', '0', '0', 'monitor:loginLog:remove', NULL),
('操作日志查询', 24, 1, NULL, NULL, 'F', '0', '0', 'monitor:operationLog:query', NULL),
('操作日志删除', 24, 2, NULL, NULL, 'F', '0', '0', 'monitor:operationLog:remove', NULL);

-- 初始化部门数据
INSERT INTO sys_dept (dept_name, parent_id, ancestors, sort, leader, phone, email, status) VALUES
('总公司', 0, '0', 0, '管理员', '13800000000', 'admin@company.com', '0'),
('研发部', 1, '0,1', 1, '', '', '', '0'),
('市场部', 1, '0,1', 2, '', '', '', '0'),
('财务部', 1, '0,1', 3, '', '', '', '0'),
('人事部', 1, '0,1', 4, '', '', '', '0');

-- 关联超级管理员角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

-- 更新管理员用户的部门
UPDATE sys_user SET dept_id = 1 WHERE username = 'admin';

-- 关联超级管理员所有菜单权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu;

-- 登录日志表
CREATE TABLE sys_login_log (
    login_log_id BIGINT AUTO_INCREMENT COMMENT '登录日志ID',
    user_id BIGINT COMMENT '用户ID',
    username VARCHAR(50) COMMENT '用户名',
    login_type VARCHAR(20) COMMENT '登录类型（login登录 logout登出）',
    ip_addr VARCHAR(128) COMMENT 'IP地址',
    login_location VARCHAR(255) COMMENT '登录地点',
    browser VARCHAR(50) COMMENT '浏览器类型',
    os VARCHAR(50) COMMENT '操作系统',
    user_agent VARCHAR(500) COMMENT 'User Agent',
    status CHAR(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
    error_msg VARCHAR(500) COMMENT '错误消息',
    login_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    PRIMARY KEY (login_log_id),
    KEY idx_username (username),
    KEY idx_login_time (login_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表';

-- 操作日志表
CREATE TABLE sys_operation_log (
    operation_log_id BIGINT AUTO_INCREMENT COMMENT '操作日志ID',
    user_id BIGINT COMMENT '用户ID',
    username VARCHAR(50) COMMENT '用户名',
    module_name VARCHAR(50) COMMENT '模块名称',
    operation_type VARCHAR(20) COMMENT '操作类型',
    operation_desc VARCHAR(200) COMMENT '操作描述',
    request_method VARCHAR(10) COMMENT '请求方式',
    request_url VARCHAR(500) COMMENT '请求URL',
    method_name VARCHAR(200) COMMENT '方法名称',
    request_param LONGTEXT COMMENT '请求参数',
    response_result LONGTEXT COMMENT '响应结果',
    status CHAR(1) DEFAULT '0' COMMENT '操作状态（0成功 1失败）',
    error_msg LONGTEXT COMMENT '错误消息',
    ip_addr VARCHAR(128) COMMENT 'IP地址',
    operation_location VARCHAR(255) COMMENT '操作地点',
    browser VARCHAR(50) COMMENT '浏览器类型',
    os VARCHAR(50) COMMENT '操作系统',
    execution_time BIGINT COMMENT '执行时长（毫秒）',
    operation_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (operation_log_id),
    KEY idx_username (username),
    KEY idx_operation_time (operation_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';
