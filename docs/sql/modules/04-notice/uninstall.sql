-- ============================================
-- Tiny Platform Notice Module - Uninstall
-- Version: 1.0.0
-- Description: Remove notice module tables and data
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Remove role-menu relations
-- ----------------------------
DELETE FROM `sys_role_menu` WHERE `menu_id` IN (80, 81, 82, 83, 84, 85);

-- ----------------------------
-- Remove menus
-- ----------------------------
DELETE FROM `sys_menu` WHERE `menu_id` IN (80, 81, 82, 83, 84, 85);

-- ----------------------------
-- Remove dict items
-- ----------------------------
DELETE FROM `sys_dict_item` WHERE `dict_code` = 'sys_notice_type';

-- ----------------------------
-- Remove dict types
-- ----------------------------
DELETE FROM `sys_dict_type` WHERE `dict_code` = 'sys_notice_type';

-- ----------------------------
-- Drop tables
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice_read`;
DROP TABLE IF EXISTS `sys_notice`;

SET FOREIGN_KEY_CHECKS = 1;
