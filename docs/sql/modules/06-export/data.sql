-- ============================================
-- Tiny Platform Export Module - Init Data
-- Version: 1.0.0
-- Description: Export module menus and permissions (optional)
-- ============================================

SET NAMES utf8mb4;

-- ----------------------------
-- Menus (export)
-- ----------------------------
INSERT INTO `sys_menu` VALUES (120, '导出任务', 'system.exportTask', 1, 8, '/system/exportTask', 'system/exportTask/index', 'C', '0', '0', 'system:export:list', 'ExportOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', '导出任务管理');
INSERT INTO `sys_menu` VALUES (121, '任务查询', NULL, 120, 1, NULL, NULL, 'F', '0', '0', 'system:export:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (122, '任务删除', NULL, 120, 2, NULL, NULL, 'F', '0', '0', 'system:export:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (123, '文件下载', NULL, 120, 3, NULL, NULL, 'F', '0', '0', 'system:export:download', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (124, '清理任务', NULL, 120, 4, NULL, NULL, 'F', '0', '0', 'system:export:clean', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);

-- ----------------------------
-- User export/import permissions (add to user menu, parent_id=2)
-- ----------------------------
INSERT INTO `sys_menu` VALUES (125, '用户导出', NULL, 2, 7, NULL, NULL, 'F', '0', '0', 'system:user:export', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (126, '用户导入', NULL, 2, 8, NULL, NULL, 'F', '0', '0', 'system:user:import', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);

-- ----------------------------
-- Dict export/import permissions (add to dict menu, parent_id=64)
-- ----------------------------
INSERT INTO `sys_menu` VALUES (127, '字典导出', NULL, 64, 5, NULL, NULL, 'F', '0', '0', 'system:dict:export', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (128, '字典导入', NULL, 64, 6, NULL, NULL, 'F', '0', '0', 'system:dict:import', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);

-- ----------------------------
-- Role-Menu relations (export - admin role)
-- ----------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 120);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 121);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 122);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 123);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 124);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 125);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 126);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 127);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 128);
