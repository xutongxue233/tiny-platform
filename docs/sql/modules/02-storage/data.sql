-- ============================================
-- Tiny Platform Storage Module - Init Data
-- Version: 1.0.0
-- Description: Storage module menus and dictionaries (optional)
-- ============================================

SET NAMES utf8mb4;

-- ----------------------------
-- Dict types (storage)
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (14, '存储类型', 'sys_storage_type', '0', 'admin', NOW(), '', NULL, '0', '文件存储类型');

-- ----------------------------
-- Dict items (storage)
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES (39, 'sys_storage_type', '本地存储', 'local', 1, NULL, NULL, 'Y', '0', 'admin', NOW(), '', NULL, '0', '本地文件系统存储');
INSERT INTO `sys_dict_item` VALUES (40, 'sys_storage_type', '阿里云OSS', 'aliyun_oss', 2, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '阿里云对象存储');
INSERT INTO `sys_dict_item` VALUES (41, 'sys_storage_type', 'MinIO', 'minio', 3, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', 'MinIO对象存储');
INSERT INTO `sys_dict_item` VALUES (42, 'sys_storage_type', 'AWS S3', 'aws_s3', 4, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', 'AWS S3对象存储');
INSERT INTO `sys_dict_item` VALUES (43, 'sys_storage_type', '腾讯云COS', 'tencent_cos', 5, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '腾讯云对象存储');

-- ----------------------------
-- Menus (storage)
-- ----------------------------
INSERT INTO `sys_menu` VALUES (35, '存储管理', NULL, 0, 3, '/storage', NULL, 'M', '0', '0', NULL, 'CloudServerOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (36, '存储配置', NULL, 35, 1, '/storage/config', 'storage/config/index', 'C', '0', '0', 'storage:config:list', 'SettingOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (37, '配置查询', NULL, 36, 1, NULL, NULL, 'F', '0', '0', 'storage:config:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (38, '配置新增', NULL, 36, 2, NULL, NULL, 'F', '0', '0', 'storage:config:add', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (39, '配置修改', NULL, 36, 3, NULL, NULL, 'F', '0', '0', 'storage:config:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (40, '配置删除', NULL, 36, 4, NULL, NULL, 'F', '0', '0', 'storage:config:delete', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (41, '文件管理', NULL, 35, 2, '/storage/file', 'storage/file/index', 'C', '0', '0', 'storage:file:list', 'FolderOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (42, '文件查询', NULL, 41, 1, NULL, NULL, 'F', '0', '0', 'storage:file:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (43, '文件上传', NULL, 41, 2, NULL, NULL, 'F', '0', '0', 'storage:file:upload', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (44, '文件下载', NULL, 41, 3, NULL, NULL, 'F', '0', '0', 'storage:file:download', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (45, '文件删除', NULL, 41, 4, NULL, NULL, 'F', '0', '0', 'storage:file:delete', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, NOW(), NULL, NOW(), '0', NULL);

-- ----------------------------
-- Role-Menu relations (storage - admin role)
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (101, 1, 35);
INSERT INTO `sys_role_menu` VALUES (102, 1, 36);
INSERT INTO `sys_role_menu` VALUES (103, 1, 37);
INSERT INTO `sys_role_menu` VALUES (104, 1, 38);
INSERT INTO `sys_role_menu` VALUES (105, 1, 39);
INSERT INTO `sys_role_menu` VALUES (106, 1, 40);
INSERT INTO `sys_role_menu` VALUES (107, 1, 41);
INSERT INTO `sys_role_menu` VALUES (108, 1, 42);
INSERT INTO `sys_role_menu` VALUES (109, 1, 43);
INSERT INTO `sys_role_menu` VALUES (110, 1, 44);
INSERT INTO `sys_role_menu` VALUES (111, 1, 45);

-- ----------------------------
-- Default storage config
-- ----------------------------
INSERT INTO `sys_storage_config` VALUES (1, '本地存储', 'local', '1', '0', NULL, NULL, NULL, NULL, NULL, NULL, 'D:/upload/files', '/api/file', '0', '', NOW(), NULL, NOW(), '0', '默认本地存储配置');
