-- ============================================
-- Tiny Platform Message Module - Init Data
-- Version: 1.0.0
-- Description: Message module menus, dictionaries and templates (optional)
-- ============================================

SET NAMES utf8mb4;

-- ----------------------------
-- Dict types (message)
-- ----------------------------
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_code`, `status`, `create_by`, `create_time`, `del_flag`, `remark`) VALUES
(30, '消息类型', 'msg_message_type', '0', 'admin', NOW(), '0', '消息类型'),
(31, '消息渠道', 'msg_channel', '0', 'admin', NOW(), '0', '消息发送渠道'),
(32, '消息发送状态', 'msg_send_status', '0', 'admin', NOW(), '0', '消息发送状态'),
(33, '消息优先级', 'msg_priority', '0', 'admin', NOW(), '0', '消息优先级'),
(34, '消息模板类型', 'msg_template_type', '0', 'admin', NOW(), '0', '消息模板类型');

-- ----------------------------
-- Dict items (message)
-- ----------------------------
-- Message type
INSERT INTO `sys_dict_item` (`dict_code`, `item_label`, `item_value`, `item_sort`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `del_flag`, `remark`) VALUES
('msg_message_type', '系统消息', 'system', 1, NULL, 'primary', 'Y', '0', 'admin', NOW(), '0', ''),
('msg_message_type', '用户消息', 'user', 2, NULL, 'success', 'N', '0', 'admin', NOW(), '0', '');

-- Message channel
INSERT INTO `sys_dict_item` (`dict_code`, `item_label`, `item_value`, `item_sort`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `del_flag`, `remark`) VALUES
('msg_channel', '站内消息', 'site', 1, NULL, 'primary', 'Y', '0', 'admin', NOW(), '0', ''),
('msg_channel', '邮件', 'email', 2, NULL, 'success', 'N', '0', 'admin', NOW(), '0', '');

-- Send status
INSERT INTO `sys_dict_item` (`dict_code`, `item_label`, `item_value`, `item_sort`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `del_flag`, `remark`) VALUES
('msg_send_status', '待发送', '0', 1, NULL, 'default', 'Y', '0', 'admin', NOW(), '0', ''),
('msg_send_status', '发送中', '1', 2, NULL, 'processing', 'N', '0', 'admin', NOW(), '0', ''),
('msg_send_status', '发送成功', '2', 3, NULL, 'success', 'N', '0', 'admin', NOW(), '0', ''),
('msg_send_status', '发送失败', '3', 4, NULL, 'error', 'N', '0', 'admin', NOW(), '0', '');

-- Priority
INSERT INTO `sys_dict_item` (`dict_code`, `item_label`, `item_value`, `item_sort`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `del_flag`, `remark`) VALUES
('msg_priority', '普通', '0', 1, NULL, 'default', 'Y', '0', 'admin', NOW(), '0', ''),
('msg_priority', '重要', '1', 2, NULL, 'warning', 'N', '0', 'admin', NOW(), '0', ''),
('msg_priority', '紧急', '2', 3, NULL, 'error', 'N', '0', 'admin', NOW(), '0', '');

-- Template type
INSERT INTO `sys_dict_item` (`dict_code`, `item_label`, `item_value`, `item_sort`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `del_flag`, `remark`) VALUES
('msg_template_type', '站内消息', 'site', 1, NULL, 'primary', 'Y', '0', 'admin', NOW(), '0', ''),
('msg_template_type', '邮件', 'email', 2, NULL, 'success', 'N', '0', 'admin', NOW(), '0', '');

-- ----------------------------
-- Menus (message)
-- ----------------------------
INSERT INTO `sys_menu` VALUES (90, '消息管理', NULL, 0, 5, '/message', NULL, 'M', '0', '0', NULL, 'MessageOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (91, '消息模板', NULL, 90, 1, '/message/template', 'message/template/index', 'C', '0', '0', 'message:template:list', 'FormOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (92, '模板查询', NULL, 91, 1, NULL, NULL, 'F', '0', '0', 'message:template:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (93, '模板新增', NULL, 91, 2, NULL, NULL, 'F', '0', '0', 'message:template:add', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (94, '模板修改', NULL, 91, 3, NULL, NULL, 'F', '0', '0', 'message:template:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (95, '模板删除', NULL, 91, 4, NULL, NULL, 'F', '0', '0', 'message:template:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (96, '消息列表', NULL, 90, 2, '/message/list', 'message/list/index', 'C', '0', '0', 'message:list:list', 'UnorderedListOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (97, '消息查询', NULL, 96, 1, NULL, NULL, 'F', '0', '0', 'message:list:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (98, '发送消息', NULL, 96, 2, NULL, NULL, 'F', '0', '0', 'message:list:send', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (99, '撤回消息', NULL, 96, 3, NULL, NULL, 'F', '0', '0', 'message:list:revoke', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (100, '消息删除', NULL, 96, 4, NULL, NULL, 'F', '0', '0', 'message:list:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (101, '发送记录', NULL, 90, 3, '/message/send-log', 'message/sendLog/index', 'C', '0', '0', 'message:log:list', 'FileSearchOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (102, '记录查询', NULL, 101, 1, NULL, NULL, 'F', '0', '0', 'message:log:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (103, '重试发送', NULL, 101, 2, NULL, NULL, 'F', '0', '0', 'message:log:retry', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (104, '记录删除', NULL, 101, 3, NULL, NULL, 'F', '0', '0', 'message:log:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (105, '邮件配置', NULL, 90, 4, '/message/email-config', 'message/emailConfig/index', 'C', '0', '0', 'message:email:list', 'MailOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (106, '配置查询', NULL, 105, 1, NULL, NULL, 'F', '0', '0', 'message:email:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (107, '配置新增', NULL, 105, 2, NULL, NULL, 'F', '0', '0', 'message:email:add', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (108, '配置修改', NULL, 105, 3, NULL, NULL, 'F', '0', '0', 'message:email:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (109, '配置删除', NULL, 105, 4, NULL, NULL, 'F', '0', '0', 'message:email:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (110, '测试连接', NULL, 105, 5, NULL, NULL, 'F', '0', '0', 'message:email:test', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);

-- ----------------------------
-- Role-Menu relations (message - admin role)
-- ----------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 90);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 91);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 92);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 93);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 94);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 95);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 96);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 97);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 98);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 99);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 100);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 101);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 102);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 103);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 104);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 105);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 106);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 107);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 108);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 109);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 110);

-- ----------------------------
-- Sample templates
-- ----------------------------
INSERT INTO `msg_template` (`template_code`, `template_name`, `template_type`, `template_content`, `template_subject`, `variables`, `status`, `create_by`, `create_time`, `del_flag`, `remark`) VALUES
('welcome_user', '欢迎新用户', 'site', '尊敬的${username}，欢迎加入我们的平台！您的账号已经成功创建。', NULL, '["username"]', '0', 'admin', NOW(), '0', '新用户注册欢迎消息'),
('password_reset', '密码重置通知', 'email', '尊敬的${username}，您正在重置密码。验证码为：${code}，有效期${minutes}分钟。如非本人操作，请忽略此邮件。', '密码重置验证码', '["username","code","minutes"]', '0', 'admin', NOW(), '0', '密码重置邮件模板'),
('system_notice', '系统通知', 'site', '${content}', NULL, '["content"]', '0', 'admin', NOW(), '0', '通用系统通知模板');
