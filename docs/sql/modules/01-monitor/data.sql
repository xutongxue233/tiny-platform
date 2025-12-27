-- ============================================
-- Tiny Platform Monitor Module - Init Data
-- Version: 1.0.0
-- Description: Monitor module menus and dictionaries (optional)
-- ============================================

SET NAMES utf8mb4;

-- ----------------------------
-- Dict types (monitor)
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (10, '登录类型', 'sys_login_type', '0', 'admin', NOW(), '', NULL, '0', '登录日志类型');
INSERT INTO `sys_dict_type` VALUES (11, '登录状态', 'sys_login_status', '0', 'admin', NOW(), '', NULL, '0', '登录结果状态');
INSERT INTO `sys_dict_type` VALUES (12, '操作类型', 'sys_operation_type', '0', 'admin', NOW(), '', NULL, '0', '操作日志类型');
INSERT INTO `sys_dict_type` VALUES (13, '操作状态', 'sys_operation_status', '0', 'admin', NOW(), '', NULL, '0', '操作结果状态');

-- ----------------------------
-- Dict items (monitor)
-- ----------------------------
-- Login type
INSERT INTO `sys_dict_item` VALUES (24, 'sys_login_type', '登录', 'login', 1, NULL, NULL, 'Y', '0', 'admin', NOW(), '', NULL, '0', '用户登录');
INSERT INTO `sys_dict_item` VALUES (25, 'sys_login_type', '登出', 'logout', 2, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '用户登出');

-- Login status
INSERT INTO `sys_dict_item` VALUES (26, 'sys_login_status', '成功', '0', 1, NULL, 'success', 'Y', '0', 'admin', NOW(), '', NULL, '0', '登录成功');
INSERT INTO `sys_dict_item` VALUES (27, 'sys_login_status', '失败', '1', 2, NULL, 'error', 'N', '0', 'admin', NOW(), '', NULL, '0', '登录失败');

-- Operation type
INSERT INTO `sys_dict_item` VALUES (28, 'sys_operation_type', '其他', 'OTHER', 0, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '其他操作');
INSERT INTO `sys_dict_item` VALUES (29, 'sys_operation_type', '新增', 'INSERT', 1, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '新增操作');
INSERT INTO `sys_dict_item` VALUES (30, 'sys_operation_type', '修改', 'UPDATE', 2, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '修改操作');
INSERT INTO `sys_dict_item` VALUES (31, 'sys_operation_type', '删除', 'DELETE', 3, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '删除操作');
INSERT INTO `sys_dict_item` VALUES (32, 'sys_operation_type', '查询', 'SELECT', 4, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '查询操作');
INSERT INTO `sys_dict_item` VALUES (33, 'sys_operation_type', '导入', 'IMPORT', 5, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '导入操作');
INSERT INTO `sys_dict_item` VALUES (34, 'sys_operation_type', '导出', 'EXPORT', 6, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '导出操作');
INSERT INTO `sys_dict_item` VALUES (35, 'sys_operation_type', '登录', 'LOGIN', 7, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '登录操作');
INSERT INTO `sys_dict_item` VALUES (36, 'sys_operation_type', '登出', 'LOGOUT', 8, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '登出操作');

-- Operation status
INSERT INTO `sys_dict_item` VALUES (37, 'sys_operation_status', '成功', '0', 1, NULL, 'success', 'Y', '0', 'admin', NOW(), '', NULL, '0', '操作成功');
INSERT INTO `sys_dict_item` VALUES (38, 'sys_operation_status', '失败', '1', 2, NULL, 'error', 'N', '0', 'admin', NOW(), '', NULL, '0', '操作失败');

-- ----------------------------
-- Menus (monitor)
-- ----------------------------
INSERT INTO `sys_menu` VALUES (22, '系统监控', NULL, 0, 2, '/monitor', NULL, 'M', '0', '0', NULL, 'MonitorOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (23, '登录日志', NULL, 22, 1, '/monitor/loginLog', 'monitor/loginLog/index', 'C', '0', '0', 'monitor:loginLog:list', 'LoginOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (24, '操作日志', NULL, 22, 2, '/monitor/operationLog', 'monitor/operationLog/index', 'C', '0', '0', 'monitor:operationLog:list', 'FileTextOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (25, '登录日志查询', NULL, 23, 1, NULL, NULL, 'F', '0', '0', 'monitor:loginLog:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (26, '登录日志删除', NULL, 23, 2, NULL, NULL, 'F', '0', '0', 'monitor:loginLog:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (27, '操作日志查询', NULL, 24, 1, NULL, NULL, 'F', '0', '0', 'monitor:operationLog:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (28, '操作日志删除', NULL, 24, 2, NULL, NULL, 'F', '0', '0', 'monitor:operationLog:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (29, '在线用户', NULL, 22, 3, '/monitor/onlineUser', 'monitor/onlineUser/index', 'C', '0', '0', 'monitor:online:list', 'TeamOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (30, '在线用户查询', NULL, 29, 1, NULL, NULL, 'F', '0', '0', 'monitor:online:list', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (31, '强制退出', NULL, 29, 2, NULL, NULL, 'F', '0', '0', 'monitor:online:kickout', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (32, '用户封禁', NULL, 29, 3, NULL, NULL, 'F', '0', '0', 'monitor:online:disable', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);

-- ----------------------------
-- Role-Menu relations (monitor - admin role)
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (22, 1, 22);
INSERT INTO `sys_role_menu` VALUES (23, 1, 23);
INSERT INTO `sys_role_menu` VALUES (24, 1, 24);
INSERT INTO `sys_role_menu` VALUES (25, 1, 25);
INSERT INTO `sys_role_menu` VALUES (26, 1, 26);
INSERT INTO `sys_role_menu` VALUES (27, 1, 27);
INSERT INTO `sys_role_menu` VALUES (28, 1, 28);
INSERT INTO `sys_role_menu` VALUES (29, 1, 29);
INSERT INTO `sys_role_menu` VALUES (30, 1, 30);
INSERT INTO `sys_role_menu` VALUES (31, 1, 31);
INSERT INTO `sys_role_menu` VALUES (32, 1, 32);

-- Role 2 (user) monitor permissions
INSERT INTO `sys_role_menu` VALUES (85, 2, 22);
INSERT INTO `sys_role_menu` VALUES (86, 2, 23);
INSERT INTO `sys_role_menu` VALUES (87, 2, 25);
INSERT INTO `sys_role_menu` VALUES (88, 2, 26);
INSERT INTO `sys_role_menu` VALUES (89, 2, 24);
INSERT INTO `sys_role_menu` VALUES (90, 2, 27);
INSERT INTO `sys_role_menu` VALUES (91, 2, 28);
INSERT INTO `sys_role_menu` VALUES (92, 2, 29);
INSERT INTO `sys_role_menu` VALUES (93, 2, 30);
INSERT INTO `sys_role_menu` VALUES (94, 2, 31);
INSERT INTO `sys_role_menu` VALUES (95, 2, 32);
