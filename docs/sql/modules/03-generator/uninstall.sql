-- ============================================
-- Tiny Platform Generator Module - Uninstall
-- Version: 1.0.0
-- Description: Remove generator module tables and data
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Remove role-menu relations
-- ----------------------------
DELETE FROM `sys_role_menu` WHERE `menu_id` IN (55, 56, 57, 58, 59, 60, 61, 62, 63);

-- ----------------------------
-- Remove menus
-- ----------------------------
DELETE FROM `sys_menu` WHERE `menu_id` IN (55, 56, 57, 58, 59, 60, 61, 62, 63);

-- ----------------------------
-- Remove dict items
-- ----------------------------
DELETE FROM `sys_dict_item` WHERE `dict_code` IN ('gen_html_type', 'gen_query_type', 'gen_java_type', 'gen_type', 'gen_fe_type');

-- ----------------------------
-- Remove dict types
-- ----------------------------
DELETE FROM `sys_dict_type` WHERE `dict_code` IN ('gen_html_type', 'gen_query_type', 'gen_java_type', 'gen_type', 'gen_fe_type');

-- ----------------------------
-- Drop tables
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
DROP TABLE IF EXISTS `gen_table`;
DROP TABLE IF EXISTS `gen_type_mapping`;
DROP TABLE IF EXISTS `gen_config`;

SET FOREIGN_KEY_CHECKS = 1;
