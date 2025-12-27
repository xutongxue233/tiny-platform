-- ============================================
-- Tiny Platform Storage Module - Uninstall
-- Version: 1.0.0
-- Description: Remove storage module tables and data
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Remove role-menu relations
-- ----------------------------
DELETE FROM `sys_role_menu` WHERE `menu_id` IN (35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45);

-- ----------------------------
-- Remove menus
-- ----------------------------
DELETE FROM `sys_menu` WHERE `menu_id` IN (35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45);

-- ----------------------------
-- Remove dict items
-- ----------------------------
DELETE FROM `sys_dict_item` WHERE `dict_code` = 'sys_storage_type';

-- ----------------------------
-- Remove dict types
-- ----------------------------
DELETE FROM `sys_dict_type` WHERE `dict_code` = 'sys_storage_type';

-- ----------------------------
-- Drop tables
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_record`;
DROP TABLE IF EXISTS `sys_storage_config`;

SET FOREIGN_KEY_CHECKS = 1;
