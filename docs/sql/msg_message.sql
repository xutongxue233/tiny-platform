-- ----------------------------
-- 消息通知模块SQL脚本
-- ----------------------------

-- ----------------------------
-- Table structure for msg_template
-- ----------------------------
DROP TABLE IF EXISTS `msg_template`;
CREATE TABLE `msg_template` (
  `template_id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `template_code` varchar(100) NOT NULL COMMENT '模板编码',
  `template_name` varchar(200) NOT NULL COMMENT '模板名称',
  `template_type` varchar(20) NOT NULL COMMENT '模板类型(site站内/email邮件)',
  `template_content` text NOT NULL COMMENT '模板内容',
  `template_subject` varchar(200) DEFAULT NULL COMMENT '邮件主题',
  `variables` varchar(500) DEFAULT NULL COMMENT '变量列表JSON',
  `status` char(1) DEFAULT '0' COMMENT '状态(0正常 1停用)',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志(0正常 1删除)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`template_id`),
  UNIQUE KEY `uk_template_code` (`template_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息模板表';

-- ----------------------------
-- Table structure for msg_message
-- ----------------------------
DROP TABLE IF EXISTS `msg_message`;
CREATE TABLE `msg_message` (
  `message_id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `message_type` varchar(20) NOT NULL COMMENT '消息类型(system系统消息/user用户消息)',
  `channel` varchar(20) NOT NULL COMMENT '发送渠道(site站内/email邮件)',
  `template_id` bigint DEFAULT NULL COMMENT '关联模板ID',
  `title` varchar(200) NOT NULL COMMENT '消息标题',
  `content` text NOT NULL COMMENT '消息内容',
  `sender_id` bigint DEFAULT NULL COMMENT '发送者ID(系统消息为空)',
  `sender_type` varchar(20) DEFAULT 'system' COMMENT '发送者类型(system系统/user用户)',
  `biz_type` varchar(50) DEFAULT NULL COMMENT '业务类型(用于分类)',
  `biz_id` varchar(100) DEFAULT NULL COMMENT '业务ID(关联业务数据)',
  `priority` int DEFAULT 0 COMMENT '优先级(0普通 1重要 2紧急)',
  `status` char(1) DEFAULT '0' COMMENT '状态(0正常 1撤回)',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志(0正常 1删除)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`message_id`),
  KEY `idx_template_id` (`template_id`),
  KEY `idx_sender_id` (`sender_id`),
  KEY `idx_biz_type` (`biz_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- ----------------------------
-- Table structure for msg_recipient
-- ----------------------------
DROP TABLE IF EXISTS `msg_recipient`;
CREATE TABLE `msg_recipient` (
  `recipient_id` bigint NOT NULL AUTO_INCREMENT COMMENT '接收记录ID',
  `message_id` bigint NOT NULL COMMENT '消息ID',
  `user_id` bigint DEFAULT NULL COMMENT '接收用户ID',
  `recipient_address` varchar(200) DEFAULT NULL COMMENT '接收地址(邮箱/手机号)',
  `is_read` char(1) DEFAULT '0' COMMENT '是否已读(0未读 1已读)',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  `is_deleted` char(1) DEFAULT '0' COMMENT '用户是否删除(0否 1是)',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`recipient_id`),
  KEY `idx_message_id` (`message_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_read` (`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息接收记录表';

-- ----------------------------
-- Table structure for msg_send_log
-- ----------------------------
DROP TABLE IF EXISTS `msg_send_log`;
CREATE TABLE `msg_send_log` (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `message_id` bigint NOT NULL COMMENT '消息ID',
  `recipient_id` bigint DEFAULT NULL COMMENT '接收记录ID',
  `channel` varchar(20) NOT NULL COMMENT '发送渠道',
  `recipient_address` varchar(200) DEFAULT NULL COMMENT '接收地址',
  `send_status` char(1) NOT NULL COMMENT '发送状态(0待发送 1发送中 2成功 3失败)',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `success_time` datetime DEFAULT NULL COMMENT '成功时间',
  `retry_count` int DEFAULT 0 COMMENT '重试次数',
  `error_code` varchar(50) DEFAULT NULL COMMENT '错误码',
  `error_msg` varchar(500) DEFAULT NULL COMMENT '错误信息',
  `request_data` text DEFAULT NULL COMMENT '请求数据',
  `response_data` text DEFAULT NULL COMMENT '响应数据',
  PRIMARY KEY (`log_id`),
  KEY `idx_message_id` (`message_id`),
  KEY `idx_recipient_id` (`recipient_id`),
  KEY `idx_send_status` (`send_status`),
  KEY `idx_send_time` (`send_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息发送日志表';

-- ----------------------------
-- Table structure for msg_email_config
-- ----------------------------
DROP TABLE IF EXISTS `msg_email_config`;
CREATE TABLE `msg_email_config` (
  `config_id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_name` varchar(100) NOT NULL COMMENT '配置名称',
  `host` varchar(200) NOT NULL COMMENT 'SMTP服务器',
  `port` int NOT NULL COMMENT '端口',
  `username` varchar(200) NOT NULL COMMENT '用户名',
  `password` varchar(500) NOT NULL COMMENT '密码/授权码',
  `from_address` varchar(200) NOT NULL COMMENT '发件人地址',
  `from_name` varchar(100) DEFAULT NULL COMMENT '发件人名称',
  `ssl_enable` char(1) DEFAULT '1' COMMENT '启用SSL(0否 1是)',
  `is_default` char(1) DEFAULT '0' COMMENT '是否默认(0否 1是)',
  `status` char(1) DEFAULT '0' COMMENT '状态(0正常 1停用)',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志(0正常 1删除)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮件配置表';

-- ----------------------------
-- 字典类型
-- ----------------------------
INSERT INTO `sys_dict_type` (`dict_name`, `dict_code`, `status`, `create_by`, `create_time`, `del_flag`, `remark`) VALUES
('消息类型', 'msg_message_type', '0', 'admin', NOW(), '0', '消息类型'),
('消息渠道', 'msg_channel', '0', 'admin', NOW(), '0', '消息发送渠道'),
('消息发送状态', 'msg_send_status', '0', 'admin', NOW(), '0', '消息发送状态'),
('消息优先级', 'msg_priority', '0', 'admin', NOW(), '0', '消息优先级'),
('消息模板类型', 'msg_template_type', '0', 'admin', NOW(), '0', '消息模板类型');

-- ----------------------------
-- 字典项
-- ----------------------------
-- 消息类型
INSERT INTO `sys_dict_item` (`dict_code`, `item_label`, `item_value`, `item_sort`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `del_flag`) VALUES
('msg_message_type', '系统消息', 'system', 1, NULL, 'primary', 'Y', '0', 'admin', NOW(), '0'),
('msg_message_type', '用户消息', 'user', 2, NULL, 'success', 'N', '0', 'admin', NOW(), '0');

-- 消息渠道
INSERT INTO `sys_dict_item` (`dict_code`, `item_label`, `item_value`, `item_sort`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `del_flag`) VALUES
('msg_channel', '站内消息', 'site', 1, NULL, 'primary', 'Y', '0', 'admin', NOW(), '0'),
('msg_channel', '邮件', 'email', 2, NULL, 'success', 'N', '0', 'admin', NOW(), '0');

-- 消息发送状态
INSERT INTO `sys_dict_item` (`dict_code`, `item_label`, `item_value`, `item_sort`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `del_flag`) VALUES
('msg_send_status', '待发送', '0', 1, NULL, 'default', 'Y', '0', 'admin', NOW(), '0'),
('msg_send_status', '发送中', '1', 2, NULL, 'processing', 'N', '0', 'admin', NOW(), '0'),
('msg_send_status', '发送成功', '2', 3, NULL, 'success', 'N', '0', 'admin', NOW(), '0'),
('msg_send_status', '发送失败', '3', 4, NULL, 'error', 'N', '0', 'admin', NOW(), '0');

-- 消息优先级
INSERT INTO `sys_dict_item` (`dict_code`, `item_label`, `item_value`, `item_sort`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `del_flag`) VALUES
('msg_priority', '普通', '0', 1, NULL, 'default', 'Y', '0', 'admin', NOW(), '0'),
('msg_priority', '重要', '1', 2, NULL, 'warning', 'N', '0', 'admin', NOW(), '0'),
('msg_priority', '紧急', '2', 3, NULL, 'error', 'N', '0', 'admin', NOW(), '0');

-- 消息模板类型
INSERT INTO `sys_dict_item` (`dict_code`, `item_label`, `item_value`, `item_sort`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `del_flag`) VALUES
('msg_template_type', '站内消息', 'site', 1, NULL, 'primary', 'Y', '0', 'admin', NOW(), '0'),
('msg_template_type', '邮件', 'email', 2, NULL, 'success', 'N', '0', 'admin', NOW(), '0');

-- ----------------------------
-- 菜单数据
-- ----------------------------
-- 消息管理目录
INSERT INTO `sys_menu` (`menu_name`, `locale_key`, `parent_id`, `sort`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `is_frame`, `is_cache`, `link`, `target`, `badge`, `badge_color`, `create_by`, `create_time`, `del_flag`) VALUES
('消息管理', 'message', 0, 5, '/message', NULL, 'M', '0', '0', NULL, 'MessageOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0');

SET @message_menu_id = LAST_INSERT_ID();

-- 消息模板
INSERT INTO `sys_menu` (`menu_name`, `locale_key`, `parent_id`, `sort`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `is_frame`, `is_cache`, `link`, `target`, `badge`, `badge_color`, `create_by`, `create_time`, `del_flag`) VALUES
('消息模板', 'template', @message_menu_id, 1, '/message/template', 'message/template/index', 'C', '0', '0', 'message:template:list', 'FormOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0');

SET @template_menu_id = LAST_INSERT_ID();

INSERT INTO `sys_menu` (`menu_name`, `locale_key`, `parent_id`, `sort`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `is_frame`, `is_cache`, `link`, `target`, `badge`, `badge_color`, `create_by`, `create_time`, `del_flag`) VALUES
('模板查询', NULL, @template_menu_id, 1, NULL, NULL, 'F', '0', '0', 'message:template:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0'),
('模板新增', NULL, @template_menu_id, 2, NULL, NULL, 'F', '0', '0', 'message:template:add', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0'),
('模板修改', NULL, @template_menu_id, 3, NULL, NULL, 'F', '0', '0', 'message:template:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0'),
('模板删除', NULL, @template_menu_id, 4, NULL, NULL, 'F', '0', '0', 'message:template:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0');

-- 消息列表
INSERT INTO `sys_menu` (`menu_name`, `locale_key`, `parent_id`, `sort`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `is_frame`, `is_cache`, `link`, `target`, `badge`, `badge_color`, `create_by`, `create_time`, `del_flag`) VALUES
('消息列表', 'list', @message_menu_id, 2, '/message/list', 'message/list/index', 'C', '0', '0', 'message:list:list', 'UnorderedListOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0');

SET @list_menu_id = LAST_INSERT_ID();

INSERT INTO `sys_menu` (`menu_name`, `locale_key`, `parent_id`, `sort`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `is_frame`, `is_cache`, `link`, `target`, `badge`, `badge_color`, `create_by`, `create_time`, `del_flag`) VALUES
('消息查询', NULL, @list_menu_id, 1, NULL, NULL, 'F', '0', '0', 'message:list:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0'),
('发送消息', NULL, @list_menu_id, 2, NULL, NULL, 'F', '0', '0', 'message:list:send', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0'),
('撤回消息', NULL, @list_menu_id, 3, NULL, NULL, 'F', '0', '0', 'message:list:revoke', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0'),
('消息删除', NULL, @list_menu_id, 4, NULL, NULL, 'F', '0', '0', 'message:list:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0');

-- 发送记录
INSERT INTO `sys_menu` (`menu_name`, `locale_key`, `parent_id`, `sort`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `is_frame`, `is_cache`, `link`, `target`, `badge`, `badge_color`, `create_by`, `create_time`, `del_flag`) VALUES
('发送记录', 'sendLog', @message_menu_id, 3, '/message/send-log', 'message/sendLog/index', 'C', '0', '0', 'message:log:list', 'FileSearchOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0');

SET @log_menu_id = LAST_INSERT_ID();

INSERT INTO `sys_menu` (`menu_name`, `locale_key`, `parent_id`, `sort`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `is_frame`, `is_cache`, `link`, `target`, `badge`, `badge_color`, `create_by`, `create_time`, `del_flag`) VALUES
('记录查询', NULL, @log_menu_id, 1, NULL, NULL, 'F', '0', '0', 'message:log:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0'),
('重试发送', NULL, @log_menu_id, 2, NULL, NULL, 'F', '0', '0', 'message:log:retry', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0'),
('记录删除', NULL, @log_menu_id, 3, NULL, NULL, 'F', '0', '0', 'message:log:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0');

-- 邮件配置
INSERT INTO `sys_menu` (`menu_name`, `locale_key`, `parent_id`, `sort`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `is_frame`, `is_cache`, `link`, `target`, `badge`, `badge_color`, `create_by`, `create_time`, `del_flag`) VALUES
('邮件配置', 'emailConfig', @message_menu_id, 4, '/message/email-config', 'message/emailConfig/index', 'C', '0', '0', 'message:email:list', 'MailOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0');

SET @email_menu_id = LAST_INSERT_ID();

INSERT INTO `sys_menu` (`menu_name`, `locale_key`, `parent_id`, `sort`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `is_frame`, `is_cache`, `link`, `target`, `badge`, `badge_color`, `create_by`, `create_time`, `del_flag`) VALUES
('配置查询', NULL, @email_menu_id, 1, NULL, NULL, 'F', '0', '0', 'message:email:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0'),
('配置新增', NULL, @email_menu_id, 2, NULL, NULL, 'F', '0', '0', 'message:email:add', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0'),
('配置修改', NULL, @email_menu_id, 3, NULL, NULL, 'F', '0', '0', 'message:email:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0'),
('配置删除', NULL, @email_menu_id, 4, NULL, NULL, 'F', '0', '0', 'message:email:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0'),
('测试连接', NULL, @email_menu_id, 5, NULL, NULL, 'F', '0', '0', 'message:email:test', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), '0');

-- ----------------------------
-- 给管理员角色分配菜单权限
-- ----------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, menu_id FROM `sys_menu` WHERE `perms` LIKE 'message:%' OR (`menu_name` = '消息管理' AND `parent_id` = 0);

-- ----------------------------
-- 示例消息模板
-- ----------------------------
INSERT INTO `msg_template` (`template_code`, `template_name`, `template_type`, `template_content`, `template_subject`, `variables`, `status`, `create_by`, `create_time`, `del_flag`, `remark`) VALUES
('welcome_user', '欢迎新用户', 'site', '尊敬的${username}，欢迎加入我们的平台！您的账号已经成功创建。', NULL, '["username"]', '0', 'admin', NOW(), '0', '新用户注册欢迎消息'),
('password_reset', '密码重置通知', 'email', '尊敬的${username}，您正在重置密码。验证码为：${code}，有效期${minutes}分钟。如非本人操作，请忽略此邮件。', '密码重置验证码', '["username","code","minutes"]', '0', 'admin', NOW(), '0', '密码重置邮件模板'),
('system_notice', '系统通知', 'site', '${content}', NULL, '["content"]', '0', 'admin', NOW(), '0', '通用系统通知模板');
