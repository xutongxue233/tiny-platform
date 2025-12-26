-- ----------------------------
-- 通知公告表
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `notice_id` bigint NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告类型(1通知 2公告)',
  `notice_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '公告内容',
  `is_top` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '是否置顶(0否 1是)',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态(0正常 1关闭)',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '删除标志(0正常 1删除)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`notice_id`),
  INDEX `idx_is_top` (`is_top`),
  INDEX `idx_notice_type` (`notice_type`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知公告表' ROW_FORMAT=Dynamic;

-- ----------------------------
-- 字典类型: 通知类型
-- ----------------------------
INSERT INTO `sys_dict_type` (`dict_code`, `dict_name`, `status`, `create_by`, `create_time`, `remark`)
VALUES ('sys_notice_type', '通知类型', '0', 'admin', NOW(), '通知公告类型');

-- ----------------------------
-- 字典项: 通知类型
-- ----------------------------
INSERT INTO `sys_dict_item` (`dict_code`, `item_label`, `item_value`, `item_sort`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`)
VALUES
('sys_notice_type', '通知', '1', 1, '', 'processing', '0', '0', 'admin', NOW(), ''),
('sys_notice_type', '公告', '2', 2, '', 'warning', '0', '0', 'admin', NOW(), '');

-- ----------------------------
-- 菜单: 信息管理(父菜单)
-- ----------------------------
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `sort`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `remark`)
VALUES ('信息管理', 0, 3, '/info', '', 'M', '0', '0', '', 'NotificationOutlined', 'admin', NOW(), '信息管理目录');

SET @info_menu_id = LAST_INSERT_ID();

-- ----------------------------
-- 菜单: 通知公告
-- ----------------------------
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `sort`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `remark`)
VALUES ('通知公告', @info_menu_id, 1, 'notice', 'info/notice/index', 'C', '0', '0', 'info:notice:list', 'SoundOutlined', 'admin', NOW(), '通知公告菜单');

SET @notice_menu_id = LAST_INSERT_ID();

-- ----------------------------
-- 按钮权限
-- ----------------------------
INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `sort`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `remark`)
VALUES
('查询', @notice_menu_id, 1, '', '', 'F', '0', '0', 'info:notice:query', '', 'admin', NOW(), ''),
('新增', @notice_menu_id, 2, '', '', 'F', '0', '0', 'info:notice:add', '', 'admin', NOW(), ''),
('修改', @notice_menu_id, 3, '', '', 'F', '0', '0', 'info:notice:edit', '', 'admin', NOW(), ''),
('删除', @notice_menu_id, 4, '', '', 'F', '0', '0', 'info:notice:remove', '', 'admin', NOW(), '');

-- ----------------------------
-- 示例数据
-- ----------------------------
INSERT INTO `sys_notice` (`notice_title`, `notice_type`, `notice_content`, `is_top`, `status`, `create_by`, `create_time`, `remark`)
VALUES
('系统升级通知', '1', '## 系统升级通知\n\n系统将于本周末进行升级维护，届时系统将暂停服务，请提前做好准备。\n\n### 升级时间\n- 开始时间：2025-01-01 00:00\n- 结束时间：2025-01-01 06:00\n\n### 注意事项\n1. 请提前保存工作内容\n2. 升级期间无法访问系统', '1', '0', 'admin', NOW(), ''),
('关于加强安全管理的公告', '2', '## 安全管理公告\n\n为加强系统安全管理，请所有用户遵守以下规定：\n\n### 密码要求\n- 密码长度不少于8位\n- 包含大小写字母和数字\n- 定期更换密码\n\n### 安全提醒\n```\n请勿将密码告知他人\n请勿在公共场所登录系统\n```', '0', '0', 'admin', NOW(), '');

-- ----------------------------
-- 为管理员角色分配通知公告菜单权限
-- ----------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, menu_id FROM `sys_menu` WHERE `perms` LIKE 'info:notice:%' OR `path` = 'notice' OR `path` = '/info';

-- ----------------------------
-- 公告已读记录表
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice_read`;
CREATE TABLE `sys_notice_read` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `notice_id` bigint NOT NULL COMMENT '公告ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `read_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '阅读时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_notice_user` (`notice_id`, `user_id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_notice_id` (`notice_id`)
) ENGINE=InnoDB CHARACTER SET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告已读记录表' ROW_FORMAT=Dynamic;
