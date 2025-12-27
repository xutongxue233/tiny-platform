-- ============================================
-- Tiny Platform Monitor Module - Uninstall
-- Version: 1.0.0
-- Description: Remove monitor module tables and data
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Remove role-menu relations
-- ----------------------------
DELETE FROM `sys_role_menu` WHERE `menu_id` IN (22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32);

-- ----------------------------
-- Remove menus
-- ----------------------------
DELETE FROM `sys_menu` WHERE `menu_id` IN (22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32);

-- ----------------------------
-- Remove dict items
-- ----------------------------
DELETE FROM `sys_dict_item` WHERE `dict_code` IN ('sys_login_type', 'sys_login_status', 'sys_operation_type', 'sys_operation_status');

-- ----------------------------
-- Remove dict types
-- ----------------------------
DELETE FROM `sys_dict_type` WHERE `dict_code` IN ('sys_login_type', 'sys_login_status', 'sys_operation_type', 'sys_operation_status');

-- ----------------------------
-- Drop tables
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
DROP TABLE IF EXISTS `sys_operation_log`;

SET FOREIGN_KEY_CHECKS = 1;
