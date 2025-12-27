-- ============================================
-- Tiny Platform 消息中心模块 - 表结构
-- 版本: 1.0.0
-- 说明: 消息中心表(可选)
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 消息模板表
-- ----------------------------
DROP TABLE IF EXISTS `msg_template`;
CREATE TABLE `msg_template` (
  `template_id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `template_code` varchar(100) NOT NULL COMMENT '模板编码',
  `template_name` varchar(200) NOT NULL COMMENT '模板名称',
  `template_type` varchar(20) NOT NULL COMMENT '模板类型(site站内 email邮件)',
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
  `version` int NULL DEFAULT 0 COMMENT '乐观锁版本号',
  PRIMARY KEY (`template_id`),
  UNIQUE KEY `uk_template_code` (`template_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息模板表';

-- ----------------------------
-- 消息表
-- ----------------------------
DROP TABLE IF EXISTS `msg_message`;
CREATE TABLE `msg_message` (
  `message_id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `message_type` varchar(20) NOT NULL COMMENT '消息类型(system系统消息 user用户消息)',
  `channel` varchar(20) NOT NULL COMMENT '发送渠道(site站内 email邮件)',
  `template_id` bigint DEFAULT NULL COMMENT '关联模板ID',
  `title` varchar(200) NOT NULL COMMENT '消息标题',
  `content` text NOT NULL COMMENT '消息内容',
  `sender_id` bigint DEFAULT NULL COMMENT '发送者ID(系统消息为空)',
  `sender_type` varchar(20) DEFAULT 'system' COMMENT '发送者类型(system系统 user用户)',
  `biz_type` varchar(50) DEFAULT NULL COMMENT '业务类型(用于分类)',
  `biz_id` varchar(100) DEFAULT NULL COMMENT '业务ID(关联业务数据)',
  `priority` int DEFAULT 0 COMMENT '优先级(0普通 1重要 2紧急)',
  `is_top` char(1) DEFAULT '0' COMMENT '是否置顶(0否 1是)',
  `notice_type` char(1) DEFAULT NULL COMMENT '公告类型(1通知 2公告)',
  `status` char(1) DEFAULT '0' COMMENT '状态(0正常 1撤回)',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志(0正常 1删除)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `version` int NULL DEFAULT 0 COMMENT '乐观锁版本号',
  PRIMARY KEY (`message_id`),
  KEY `idx_template_id` (`template_id`),
  KEY `idx_sender_id` (`sender_id`),
  KEY `idx_biz_type` (`biz_type`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_is_top` (`is_top`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- ----------------------------
-- 消息接收记录表
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
-- 消息发送日志表
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
-- 邮件配置表
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
  `version` int NULL DEFAULT 0 COMMENT '乐观锁版本号',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮件配置表';

SET FOREIGN_KEY_CHECKS = 1;
