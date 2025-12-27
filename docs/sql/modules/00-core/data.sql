-- ============================================
-- Tiny Platform Core Module - Init Data
-- Version: 1.0.0
-- Description: Core initial data (required)
-- ============================================

SET NAMES utf8mb4;

-- ----------------------------
-- Init departments
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, '总公司', 0, '0', 0, '管理员', '13800000000', 'admin@company.com', '0', NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_dept` VALUES (2, '研发部', 1, '0,1', 1, '', '', '', '0', NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_dept` VALUES (3, '市场部', 1, '0,1', 2, '', '', '', '0', NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_dept` VALUES (4, '财务部', 1, '0,1', 3, '', '', '', '0', NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_dept` VALUES (5, '人事部', 1, '0,1', 4, '', '', '', '0', NULL, NOW(), NULL, NOW(), '0', NULL);

-- ----------------------------
-- Init roles
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', '0', NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_role` VALUES (2, '普通用户', 'user', 2, '1', '0', NULL, NOW(), NULL, NOW(), '0', NULL);

-- ----------------------------
-- Init users (password: 123456)
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2b$12$gis3AeExTCSgNeiYSBtwmOKyYBCrso5JP4P7Aexj/0t1fdB8z6Evy', '超级管理员', NULL, NULL, '2', NULL, 1, '0', NULL, NOW(), NULL, NOW(), '0', NULL, 1);
INSERT INTO `sys_user` VALUES (2, 'test', '$2a$10$MWscjqJkoFMbBb95Jl3oJOv2wBnlcZ7oZ6hhtnrnIDhwuPZcbQuRm', '测试用户', '18359838539', 'test@tiny.com', '0', NULL, 2, '0', NULL, NOW(), NULL, NOW(), '0', NULL, 0);

-- ----------------------------
-- User-Role relations
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2, 2);

-- ----------------------------
-- Dict types (core)
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_gender', '0', 'admin', NOW(), '', NULL, '0', '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '系统状态', 'sys_common_status', '0', 'admin', NOW(), '', NULL, '0', '系统通用状态');
INSERT INTO `sys_dict_type` VALUES (3, '是否', 'sys_yes_no', '0', 'admin', NOW(), '', NULL, '0', '是否选择');
INSERT INTO `sys_dict_type` VALUES (4, '数据权限范围', 'sys_data_scope', '0', 'admin', NOW(), '', NULL, '0', '角色数据权限范围');
INSERT INTO `sys_dict_type` VALUES (5, '菜单类型', 'sys_menu_type', '0', 'admin', NOW(), '', NULL, '0', '菜单类型列表');
INSERT INTO `sys_dict_type` VALUES (6, '显示状态', 'sys_visible_status', '0', 'admin', NOW(), '', NULL, '0', '菜单显示状态');
INSERT INTO `sys_dict_type` VALUES (7, '缓存状态', 'sys_cache_status', '0', 'admin', NOW(), '', NULL, '0', '路由缓存状态');
INSERT INTO `sys_dict_type` VALUES (8, '链接打开方式', 'sys_link_target', '0', 'admin', NOW(), '', NULL, '0', '外链打开方式');
INSERT INTO `sys_dict_type` VALUES (9, '是否(0/1)', 'sys_yes_no_01', '0', 'admin', NOW(), '', NULL, '0', '是否选项(0=否,1=是)');
INSERT INTO `sys_dict_type` VALUES (20, '参数分组', 'sys_config_group', '0', 'admin', NOW(), '', NULL, '0', '系统参数分组');
INSERT INTO `sys_dict_type` VALUES (21, '参数类型', 'sys_config_type', '0', 'admin', NOW(), '', NULL, '0', '参数值类型');

-- ----------------------------
-- Dict items (core)
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES (1, 'sys_user_gender', '男', '0', 1, NULL, NULL, 'Y', '0', 'admin', NOW(), '', NULL, '0', '性别男');
INSERT INTO `sys_dict_item` VALUES (2, 'sys_user_gender', '女', '1', 2, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '性别女');
INSERT INTO `sys_dict_item` VALUES (3, 'sys_user_gender', '未知', '2', 3, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '性别未知');
INSERT INTO `sys_dict_item` VALUES (4, 'sys_common_status', '正常', '0', 1, NULL, NULL, 'Y', '0', 'admin', NOW(), '', NULL, '0', '正常状态');
INSERT INTO `sys_dict_item` VALUES (5, 'sys_common_status', '停用', '1', 2, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '停用状态');
INSERT INTO `sys_dict_item` VALUES (6, 'sys_yes_no', '是', 'Y', 1, NULL, NULL, 'Y', '0', 'admin', NOW(), '', NULL, '0', '是');
INSERT INTO `sys_dict_item` VALUES (7, 'sys_yes_no', '否', 'N', 2, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '否');
INSERT INTO `sys_dict_item` VALUES (8, 'sys_data_scope', '全部数据', '1', 1, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '可查看所有数据');
INSERT INTO `sys_dict_item` VALUES (9, 'sys_data_scope', '自定义数据', '2', 2, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '按自定义部门查看');
INSERT INTO `sys_dict_item` VALUES (10, 'sys_data_scope', '本部门数据', '3', 3, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '仅查看本部门数据');
INSERT INTO `sys_dict_item` VALUES (11, 'sys_data_scope', '本部门及以下', '4', 4, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '查看本部门及下级部门数据');
INSERT INTO `sys_dict_item` VALUES (12, 'sys_data_scope', '仅本人数据', '5', 5, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '仅查看本人数据');
INSERT INTO `sys_dict_item` VALUES (13, 'sys_menu_type', '目录', 'M', 1, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '菜单目录');
INSERT INTO `sys_dict_item` VALUES (14, 'sys_menu_type', '菜单', 'C', 2, NULL, NULL, 'Y', '0', 'admin', NOW(), '', NULL, '0', '页面菜单');
INSERT INTO `sys_dict_item` VALUES (15, 'sys_menu_type', '按钮', 'F', 3, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '功能按钮');
INSERT INTO `sys_dict_item` VALUES (16, 'sys_visible_status', '显示', '0', 1, NULL, NULL, 'Y', '0', 'admin', NOW(), '', NULL, '0', '菜单显示');
INSERT INTO `sys_dict_item` VALUES (17, 'sys_visible_status', '隐藏', '1', 2, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '菜单隐藏');
INSERT INTO `sys_dict_item` VALUES (18, 'sys_cache_status', '缓存', '0', 1, NULL, NULL, 'Y', '0', 'admin', NOW(), '', NULL, '0', '开启路由缓存');
INSERT INTO `sys_dict_item` VALUES (19, 'sys_cache_status', '不缓存', '1', 2, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '不缓存路由');
INSERT INTO `sys_dict_item` VALUES (20, 'sys_link_target', '新窗口', '_blank', 1, NULL, NULL, 'Y', '0', 'admin', NOW(), '', NULL, '0', '在新窗口打开');
INSERT INTO `sys_dict_item` VALUES (21, 'sys_link_target', '当前窗口', '_self', 2, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '在当前窗口打开');
INSERT INTO `sys_dict_item` VALUES (22, 'sys_yes_no_01', '否', '0', 1, NULL, NULL, 'Y', '0', 'admin', NOW(), '', NULL, '0', '否');
INSERT INTO `sys_dict_item` VALUES (23, 'sys_yes_no_01', '是', '1', 2, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '是');
INSERT INTO `sys_dict_item` VALUES (80, 'sys_config_group', '系统类', 'SYSTEM', 1, NULL, 'blue', 'Y', '0', 'admin', NOW(), '', NULL, '0', '系统级参数');
INSERT INTO `sys_dict_item` VALUES (81, 'sys_config_group', '业务类', 'BUSINESS', 2, NULL, 'green', 'N', '0', 'admin', NOW(), '', NULL, '0', '业务级参数');
INSERT INTO `sys_dict_item` VALUES (82, 'sys_config_type', '字符串', 'STRING', 1, NULL, 'default', 'Y', '0', 'admin', NOW(), '', NULL, '0', '字符串类型');
INSERT INTO `sys_dict_item` VALUES (83, 'sys_config_type', '数字', 'NUMBER', 2, NULL, 'blue', 'N', '0', 'admin', NOW(), '', NULL, '0', '数字类型');
INSERT INTO `sys_dict_item` VALUES (84, 'sys_config_type', '布尔值', 'BOOLEAN', 3, NULL, 'green', 'N', '0', 'admin', NOW(), '', NULL, '0', '布尔类型');
INSERT INTO `sys_dict_item` VALUES (85, 'sys_config_type', 'JSON', 'JSON', 4, NULL, 'orange', 'N', '0', 'admin', NOW(), '', NULL, '0', 'JSON类型');

-- ----------------------------
-- Menus (core: system management)
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', NULL, 0, 1, '/system', NULL, 'M', '0', '0', NULL, 'SettingOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (2, '用户管理', NULL, 1, 1, '/system/user', 'system/user/index', 'C', '0', '0', 'system:user:list', 'UserOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (3, '角色管理', NULL, 1, 2, '/system/role', 'system/role/index', 'C', '0', '0', 'system:role:list', 'TeamOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (4, '菜单管理', NULL, 1, 3, '/system/menu', 'system/menu/index', 'C', '0', '0', 'system:menu:list', 'MenuOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (5, '用户查询', NULL, 2, 1, NULL, NULL, 'F', '0', '0', 'system:user:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (6, '用户新增', NULL, 2, 2, NULL, NULL, 'F', '0', '0', 'system:user:add', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (7, '用户修改', NULL, 2, 3, NULL, NULL, 'F', '0', '0', 'system:user:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (8, '用户删除', NULL, 2, 4, NULL, NULL, 'F', '0', '0', 'system:user:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (9, '角色查询', NULL, 3, 1, NULL, NULL, 'F', '0', '0', 'system:role:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (10, '角色新增', NULL, 3, 2, NULL, NULL, 'F', '0', '0', 'system:role:add', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (11, '角色修改', NULL, 3, 3, NULL, NULL, 'F', '0', '0', 'system:role:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (12, '角色删除', NULL, 3, 4, NULL, NULL, 'F', '0', '0', 'system:role:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (13, '菜单查询', NULL, 4, 1, NULL, NULL, 'F', '0', '0', 'system:menu:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (14, '菜单新增', NULL, 4, 2, NULL, NULL, 'F', '0', '0', 'system:menu:add', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (15, '菜单修改', NULL, 4, 3, NULL, NULL, 'F', '0', '0', 'system:menu:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (16, '菜单删除', NULL, 4, 4, NULL, NULL, 'F', '0', '0', 'system:menu:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (17, '部门管理', NULL, 1, 4, '/system/dept', 'system/dept/index', 'C', '0', '0', 'system:dept:list', 'ApartmentOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (18, '部门查询', NULL, 17, 1, NULL, NULL, 'F', '0', '0', 'system:dept:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (19, '部门新增', NULL, 17, 2, NULL, NULL, 'F', '0', '0', 'system:dept:add', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (20, '部门修改', NULL, 17, 3, NULL, NULL, 'F', '0', '0', 'system:dept:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (21, '部门删除', NULL, 17, 4, NULL, NULL, 'F', '0', '0', 'system:dept:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (33, '重置密码', NULL, 2, 5, NULL, NULL, 'F', '0', '0', 'system:user:resetPwd', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (34, '封禁用户', NULL, 2, 6, NULL, NULL, 'F', '0', '0', 'system:user:disable', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (64, '字典管理', NULL, 1, 6, 'dict', 'system/dict/index', 'C', '0', '0', 'system:dict:list', 'BookOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '字典管理菜单');
INSERT INTO `sys_menu` VALUES (65, '字典查询', NULL, 64, 1, '', '', 'F', '0', '0', 'system:dict:query', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '');
INSERT INTO `sys_menu` VALUES (66, '字典新增', NULL, 64, 2, '', '', 'F', '0', '0', 'system:dict:add', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '');
INSERT INTO `sys_menu` VALUES (67, '字典修改', NULL, 64, 3, '', '', 'F', '0', '0', 'system:dict:edit', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '');
INSERT INTO `sys_menu` VALUES (68, '字典删除', NULL, 64, 4, '', '', 'F', '0', '0', 'system:dict:remove', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '');
INSERT INTO `sys_menu` VALUES (70, '参数配置', NULL, 1, 7, 'config', 'system/config/index', 'C', '0', '0', 'system:config:list', 'ControlOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '参数配置菜单');
INSERT INTO `sys_menu` VALUES (71, '参数查询', NULL, 70, 1, '', '', 'F', '0', '0', 'system:config:query', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '');
INSERT INTO `sys_menu` VALUES (72, '参数新增', NULL, 70, 2, '', '', 'F', '0', '0', 'system:config:add', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '');
INSERT INTO `sys_menu` VALUES (73, '参数修改', NULL, 70, 3, '', '', 'F', '0', '0', 'system:config:edit', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '');
INSERT INTO `sys_menu` VALUES (74, '参数删除', NULL, 70, 4, '', '', 'F', '0', '0', 'system:config:remove', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '');
INSERT INTO `sys_menu` VALUES (75, '刷新缓存', NULL, 70, 5, '', '', 'F', '0', '0', 'system:config:refresh', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '');

-- ----------------------------
-- Role-Menu relations (core)
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1, 1);
INSERT INTO `sys_role_menu` VALUES (2, 1, 2);
INSERT INTO `sys_role_menu` VALUES (3, 1, 3);
INSERT INTO `sys_role_menu` VALUES (4, 1, 4);
INSERT INTO `sys_role_menu` VALUES (5, 1, 5);
INSERT INTO `sys_role_menu` VALUES (6, 1, 6);
INSERT INTO `sys_role_menu` VALUES (7, 1, 7);
INSERT INTO `sys_role_menu` VALUES (8, 1, 8);
INSERT INTO `sys_role_menu` VALUES (9, 1, 9);
INSERT INTO `sys_role_menu` VALUES (10, 1, 10);
INSERT INTO `sys_role_menu` VALUES (11, 1, 11);
INSERT INTO `sys_role_menu` VALUES (12, 1, 12);
INSERT INTO `sys_role_menu` VALUES (13, 1, 13);
INSERT INTO `sys_role_menu` VALUES (14, 1, 14);
INSERT INTO `sys_role_menu` VALUES (15, 1, 15);
INSERT INTO `sys_role_menu` VALUES (16, 1, 16);
INSERT INTO `sys_role_menu` VALUES (17, 1, 17);
INSERT INTO `sys_role_menu` VALUES (18, 1, 18);
INSERT INTO `sys_role_menu` VALUES (19, 1, 19);
INSERT INTO `sys_role_menu` VALUES (20, 1, 20);
INSERT INTO `sys_role_menu` VALUES (21, 1, 21);
INSERT INTO `sys_role_menu` VALUES (96, 1, 33);
INSERT INTO `sys_role_menu` VALUES (97, 1, 34);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 64);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 65);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 66);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 67);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 68);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 70);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 71);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 72);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 73);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 74);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 75);

-- Role 2 (普通用户) core menu permissions
INSERT INTO `sys_role_menu` VALUES (64, 2, 1);
INSERT INTO `sys_role_menu` VALUES (65, 2, 2);
INSERT INTO `sys_role_menu` VALUES (66, 2, 5);
INSERT INTO `sys_role_menu` VALUES (67, 2, 6);
INSERT INTO `sys_role_menu` VALUES (68, 2, 7);
INSERT INTO `sys_role_menu` VALUES (69, 2, 8);
INSERT INTO `sys_role_menu` VALUES (70, 2, 3);
INSERT INTO `sys_role_menu` VALUES (71, 2, 9);
INSERT INTO `sys_role_menu` VALUES (72, 2, 10);
INSERT INTO `sys_role_menu` VALUES (73, 2, 11);
INSERT INTO `sys_role_menu` VALUES (74, 2, 12);
INSERT INTO `sys_role_menu` VALUES (75, 2, 4);
INSERT INTO `sys_role_menu` VALUES (76, 2, 13);
INSERT INTO `sys_role_menu` VALUES (77, 2, 14);
INSERT INTO `sys_role_menu` VALUES (78, 2, 15);
INSERT INTO `sys_role_menu` VALUES (79, 2, 16);
INSERT INTO `sys_role_menu` VALUES (80, 2, 17);
INSERT INTO `sys_role_menu` VALUES (81, 2, 18);
INSERT INTO `sys_role_menu` VALUES (82, 2, 19);
INSERT INTO `sys_role_menu` VALUES (83, 2, 20);
INSERT INTO `sys_role_menu` VALUES (84, 2, 21);
INSERT INTO `sys_role_menu` VALUES (99, 2, 33);
INSERT INTO `sys_role_menu` VALUES (100, 2, 34);

-- ----------------------------
-- System config data
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '系统名称', 'sys.app.name', 'Tiny Platform', 'STRING', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '系统应用名称');
INSERT INTO `sys_config` VALUES (2, '系统版本', 'sys.app.version', '1.0.0', 'STRING', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '系统版本号');
INSERT INTO `sys_config` VALUES (3, '用户初始密码', 'sys.user.initPassword', '123456', 'STRING', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '新用户初始密码');
INSERT INTO `sys_config` VALUES (4, '账号自助注册', 'sys.account.registerEnabled', 'false', 'BOOLEAN', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '是否开启账号自助注册');
INSERT INTO `sys_config` VALUES (5, '验证码开关', 'sys.captcha.enabled', 'true', 'BOOLEAN', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '是否开启验证码');
INSERT INTO `sys_config` VALUES (6, '文件上传大小限制', 'sys.upload.maxSize', '10', 'NUMBER', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '文件上传大小限制(MB)');
INSERT INTO `sys_config` VALUES (7, '最大登录失败次数', 'sys.auth.maxFailCount', '5', 'NUMBER', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '连续登录失败达到此次数后锁定账号');
INSERT INTO `sys_config` VALUES (8, '失败计数有效期', 'sys.auth.failCountExpireMinutes', '10', 'NUMBER', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '登录失败计数有效期(分钟)');
INSERT INTO `sys_config` VALUES (9, '账号锁定时长', 'sys.auth.lockMinutes', '15', 'NUMBER', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '账号锁定时长(分钟)');
INSERT INTO `sys_config` VALUES (10, '密码最小长度', 'sys.password.minLength', '6', 'NUMBER', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '用户密码最小长度');
INSERT INTO `sys_config` VALUES (11, '密码最大长度', 'sys.password.maxLength', '20', 'NUMBER', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '用户密码最大长度');
