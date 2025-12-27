-- ============================================
-- Tiny Platform Export Module - Uninstall
-- Version: 1.0.0
-- Description: Remove export module tables and data
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Remove role-menu relations
-- ----------------------------
DELETE FROM `sys_role_menu` WHERE `menu_id` IN (120, 121, 122, 123, 124, 125, 126, 127, 128);

-- ----------------------------
-- Remove menus
-- ----------------------------
DELETE FROM `sys_menu` WHERE `menu_id` IN (120, 121, 122, 123, 124, 125, 126, 127, 128);

-- ----------------------------
-- Drop tables
-- ----------------------------
DROP TABLE IF EXISTS `sys_export_task`;

SET FOREIGN_KEY_CHECKS = 1;
