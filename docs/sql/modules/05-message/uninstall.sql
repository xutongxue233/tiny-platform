-- ============================================
-- Tiny Platform Message Module - Uninstall
-- Version: 1.0.0
-- Description: Remove message module tables and data
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Remove role-menu relations
-- ----------------------------
DELETE FROM `sys_role_menu` WHERE `menu_id` IN (90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110);

-- ----------------------------
-- Remove menus
-- ----------------------------
DELETE FROM `sys_menu` WHERE `menu_id` IN (90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110);

-- ----------------------------
-- Remove dict items
-- ----------------------------
DELETE FROM `sys_dict_item` WHERE `dict_code` IN ('msg_message_type', 'msg_channel', 'msg_send_status', 'msg_priority', 'msg_template_type');

-- ----------------------------
-- Remove dict types
-- ----------------------------
DELETE FROM `sys_dict_type` WHERE `dict_code` IN ('msg_message_type', 'msg_channel', 'msg_send_status', 'msg_priority', 'msg_template_type');

-- ----------------------------
-- Drop tables
-- ----------------------------
DROP TABLE IF EXISTS `msg_send_log`;
DROP TABLE IF EXISTS `msg_recipient`;
DROP TABLE IF EXISTS `msg_message`;
DROP TABLE IF EXISTS `msg_template`;
DROP TABLE IF EXISTS `msg_email_config`;

SET FOREIGN_KEY_CHECKS = 1;
