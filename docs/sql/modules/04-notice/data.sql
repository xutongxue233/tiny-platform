-- ============================================
-- Tiny Platform Notice Module - Init Data
-- Version: 1.0.0
-- Description: Notice module menus and dictionaries (optional)
-- ============================================

SET NAMES utf8mb4;

-- ----------------------------
-- Dict types (notice)
-- ----------------------------
INSERT INTO `sys_dict_type` (`dict_id`, `dict_code`, `dict_name`, `status`, `create_by`, `create_time`, `del_flag`, `remark`)
VALUES (22, 'sys_notice_type', '通知类型', '0', 'admin', NOW(), '0', '通知公告类型');

-- ----------------------------
-- Dict items (notice)
-- ----------------------------
INSERT INTO `sys_dict_item` (`dict_code`, `item_label`, `item_value`, `item_sort`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `del_flag`, `remark`)
VALUES
('sys_notice_type', '通知', '1', 1, '', 'processing', 'Y', '0', 'admin', NOW(), '0', ''),
('sys_notice_type', '公告', '2', 2, '', 'warning', 'N', '0', 'admin', NOW(), '0', '');

-- ----------------------------
-- Menus (notice)
-- ----------------------------
INSERT INTO `sys_menu` VALUES (80, '信息管理', NULL, 0, 4, '/info', NULL, 'M', '0', '0', NULL, 'NotificationOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '信息管理目录');
INSERT INTO `sys_menu` VALUES (81, '通知公告', NULL, 80, 1, 'notice', 'info/notice/index', 'C', '0', '0', 'info:notice:list', 'SoundOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '通知公告菜单');
INSERT INTO `sys_menu` VALUES (82, '公告查询', NULL, 81, 1, NULL, NULL, 'F', '0', '0', 'info:notice:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (83, '公告新增', NULL, 81, 2, NULL, NULL, 'F', '0', '0', 'info:notice:add', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (84, '公告修改', NULL, 81, 3, NULL, NULL, 'F', '0', '0', 'info:notice:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (85, '公告删除', NULL, 81, 4, NULL, NULL, 'F', '0', '0', 'info:notice:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);

-- ----------------------------
-- Role-Menu relations (notice - admin role)
-- ----------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 80);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 81);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 82);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 83);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 84);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 85);

-- ----------------------------
-- Sample data
-- ----------------------------
INSERT INTO `sys_notice` (`notice_title`, `notice_type`, `notice_content`, `is_top`, `status`, `create_by`, `create_time`, `remark`)
VALUES
('系统升级通知', '1', '## 系统升级通知\n\n系统将于本周末进行升级维护，届时系统将暂停服务，请提前做好准备。\n\n### 升级时间\n- 开始时间：2025-01-01 00:00\n- 结束时间：2025-01-01 06:00\n\n### 注意事项\n1. 请提前保存工作内容\n2. 升级期间无法访问系统', '1', '0', 'admin', NOW(), ''),
('关于加强安全管理的公告', '2', '## 安全管理公告\n\n为加强系统安全管理，请所有用户遵守以下规定：\n\n### 密码要求\n- 密码长度不少于8位\n- 包含大小写字母和数字\n- 定期更换密码\n\n### 安全提醒\n```\n请勿将密码告知他人\n请勿在公共场所登录系统\n```', '0', '0', 'admin', NOW(), '');
