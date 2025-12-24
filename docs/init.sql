/*
 Navicat Premium Dump SQL

 Source Server         : 华为云
 Source Server Type    : MySQL
 Source Server Version : 80044 (8.0.44)
 Source Host           : 139.9.115.138:3306
 Source Schema         : tiny_admin

 Target Server Type    : MySQL
 Target Server Version : 80044 (8.0.44)
 File Encoding         : 65001

 Date: 24/12/2025 23:48:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_config
-- ----------------------------
DROP TABLE IF EXISTS `gen_config`;
CREATE TABLE `gen_config`  (
  `config_id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置键',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '配置值',
  `config_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '配置描述',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`config_id`) USING BTREE,
  UNIQUE INDEX `uk_config_key`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成器配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_config
-- ----------------------------
INSERT INTO `gen_config` VALUES (1, 'gen.author', 'xutongxue', '代码作者', 'admin', '2025-12-24 17:06:06', '', NULL);
INSERT INTO `gen_config` VALUES (2, 'gen.packageName', 'com.tiny', '默认包路径', 'admin', '2025-12-24 17:06:06', '', NULL);
INSERT INTO `gen_config` VALUES (3, 'gen.tablePrefixes', 'sys_,gen_,biz_', '表前缀列表(逗号分隔)', 'admin', '2025-12-24 17:06:06', '', NULL);
INSERT INTO `gen_config` VALUES (4, 'gen.removePrefix', 'false', '是否去除表前缀', 'admin', '2025-12-24 17:06:06', '', NULL);
INSERT INTO `gen_config` VALUES (5, 'gen.backendPath', '', '后端生成路径(空则下载)', 'admin', '2025-12-24 17:06:06', '', NULL);
INSERT INTO `gen_config` VALUES (6, 'gen.frontendPath', '', '前端生成路径(空则下载)', 'admin', '2025-12-24 17:06:06', '', NULL);

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '表ID',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '表名',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表描述',
  `module_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '模块名',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '包路径',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '实体类名',
  `business_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '业务名',
  `function_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '功能名称',
  `author` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '作者',
  `fe_module_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '前端模块路径',
  `fe_generate_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '生成类型: 1单表 2主从',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '生成方式: 0zip 1路径',
  `gen_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '生成路径',
  `options` json NULL COMMENT '扩展选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志: 0正常 1删除',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE,
  UNIQUE INDEX `uk_table_name`(`table_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成表配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES (18, 'gen_config', '代码生成器配置', 'gen', 'com.tiny', 'GenConfig', 'genConfig', '代码生成器配置', 'xutongxue', '', '1', '0', '', NULL, NULL, '2025-12-24 18:05:37', NULL, '2025-12-24 18:05:37', '0', '');
INSERT INTO `gen_table` VALUES (19, 'gen_table', '代码生成表配置', 'gen', 'com.tiny', 'GenTable', 'genTable', '代码生成表配置', 'xutongxue', '', '1', '0', '', NULL, NULL, '2025-12-24 18:05:38', NULL, '2025-12-24 18:05:38', '0', '');
INSERT INTO `gen_table` VALUES (20, 'gen_table_column', '代码生成字段配置', 'gen', 'com.tiny', 'GenTableColumn', 'genTableColumn', '代码生成字段配置', 'xutongxue', '', '1', '0', '', NULL, NULL, '2025-12-24 18:05:42', NULL, '2025-12-24 18:05:42', '0', '');
INSERT INTO `gen_table` VALUES (21, 'gen_type_mapping', '类型映射配置', 'gen', 'com.tiny', 'GenTypeMapping', 'genTypeMapping', '类型映射配置', 'xutongxue', '', '1', '0', '', NULL, NULL, '2025-12-24 18:05:47', NULL, '2025-12-24 18:05:47', '0', '');
INSERT INTO `gen_table` VALUES (22, 'sys_dept', '部门表', 'sys', 'com.tiny', 'SysDept', 'sysDept', '部门表', 'xutongxue', '', '1', '0', '', NULL, NULL, '2025-12-24 18:05:49', NULL, '2025-12-24 18:05:49', '0', '');
INSERT INTO `gen_table` VALUES (23, 'sys_file_record', '文件记录表', 'sys', 'com.tiny', 'SysFileRecord', 'sysFileRecord', '文件记录表', 'xutongxue', '', '1', '0', '', NULL, NULL, '2025-12-24 18:05:51', NULL, '2025-12-24 18:05:51', '0', '');
INSERT INTO `gen_table` VALUES (24, 'sys_login_log', '登录日志表', 'sys', 'com.tiny', 'SysLoginLog', 'sysLoginLog', '登录日志表', 'xutongxue', '', '1', '0', '', NULL, NULL, '2025-12-24 18:05:55', NULL, '2025-12-24 18:05:55', '0', '');
INSERT INTO `gen_table` VALUES (25, 'sys_menu', '菜单表', 'sys', 'com.tiny', 'SysMenu', 'sysMenu', '菜单表', 'xutongxue', '', '1', '0', '', NULL, NULL, '2025-12-24 18:05:59', NULL, '2025-12-24 18:05:59', '0', '');
INSERT INTO `gen_table` VALUES (26, 'sys_operation_log', '操作日志表', 'sys', 'com.tiny', 'SysOperationLog', 'sysOperationLog', '操作日志表', 'xutongxue', '', '1', '0', '', NULL, NULL, '2025-12-24 18:06:06', NULL, '2025-12-24 18:06:06', '0', '');
INSERT INTO `gen_table` VALUES (27, 'sys_role', '角色表', 'sys', 'com.tiny', 'SysRole', 'sysRole', '角色表', 'xutongxue', '', '1', '0', '', NULL, NULL, '2025-12-24 18:06:13', NULL, '2025-12-24 18:06:13', '0', '');
INSERT INTO `gen_table` VALUES (28, 'sys_role_dept', '角色部门关联表', 'sys', 'com.tiny', 'SysRoleDept', 'sysRoleDept', '角色部门关联表', 'xutongxue', '', '1', '0', '', NULL, NULL, '2025-12-24 18:06:17', NULL, '2025-12-24 18:06:17', '0', '');
INSERT INTO `gen_table` VALUES (29, 'sys_role_menu', '角色菜单关联表', 'sys', 'com.tiny', 'SysRoleMenu', 'sysRoleMenu', '角色菜单关联表', 'xutongxue', '', '1', '0', '', NULL, NULL, '2025-12-24 18:06:19', NULL, '2025-12-24 18:06:19', '0', '');
INSERT INTO `gen_table` VALUES (30, 'sys_storage_config', '存储配置表', 'sys', 'com.tiny', 'SysStorageConfig', 'sysStorageConfig', '存储配置表', 'xutongxue', '', '1', '0', '', NULL, NULL, '2025-12-24 18:06:22', NULL, '2025-12-24 18:06:22', '0', '');
INSERT INTO `gen_table` VALUES (31, 'sys_user', '用户表', 'sys', 'com.tiny', 'SysUser', 'sysUser', '用户表', 'xutongxue', '', '1', '0', '', NULL, NULL, '2025-12-24 18:06:28', NULL, '2025-12-24 18:06:28', '0', '');
INSERT INTO `gen_table` VALUES (32, 'sys_user_role', '用户角色关联表', 'sys', 'com.tiny', 'SysUserRole', 'sysUserRole', '用户角色关联表', 'xutongxue', '', '1', '0', '', NULL, NULL, '2025-12-24 18:06:35', NULL, '2025-12-24 18:06:35', '0', '');

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '列ID',
  `table_id` bigint NOT NULL COMMENT '表ID',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '列名',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '数据库类型',
  `java_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT 'Java类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT 'Java字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否主键: 0否 1是',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否自增: 0否 1是',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否必填: 0否 1是',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否新增字段: 0否 1是',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否编辑字段: 0否 1是',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否列表显示: 0否 1是',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '是否查询条件: 0否 1是',
  `is_detail` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否详情显示: 0否 1是',
  `is_export` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否导出字段: 0否 1是',
  `query_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'EQ' COMMENT '查询方式: EQ NE GT GTE LT LTE LIKE LIKE_LEFT LIKE_RIGHT BETWEEN IN',
  `html_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'input' COMMENT '控件类型',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`column_id`) USING BTREE,
  INDEX `idx_table_id`(`table_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 397 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成字段配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
INSERT INTO `gen_table_column` VALUES (199, 18, 'config_id', '配置ID', 'BIGINT', 'Long', 'configId', '1', '1', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 0);
INSERT INTO `gen_table_column` VALUES (200, 18, 'config_key', '配置键', 'VARCHAR', 'String', 'configKey', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 1);
INSERT INTO `gen_table_column` VALUES (201, 18, 'config_value', '配置值', 'VARCHAR', 'String', 'configValue', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 2);
INSERT INTO `gen_table_column` VALUES (202, 18, 'config_desc', '配置描述', 'VARCHAR', 'String', 'configDesc', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 3);
INSERT INTO `gen_table_column` VALUES (203, 18, 'create_by', '创建者', 'VARCHAR', 'String', 'createBy', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 4);
INSERT INTO `gen_table_column` VALUES (204, 18, 'create_time', '创建时间', 'DATETIME', 'LocalDateTime', 'createTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 5);
INSERT INTO `gen_table_column` VALUES (205, 18, 'update_by', '更新者', 'VARCHAR', 'String', 'updateBy', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 6);
INSERT INTO `gen_table_column` VALUES (206, 18, 'update_time', '更新时间', 'DATETIME', 'LocalDateTime', 'updateTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 7);
INSERT INTO `gen_table_column` VALUES (207, 19, 'table_id', '表ID', 'BIGINT', 'Long', 'tableId', '1', '1', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 0);
INSERT INTO `gen_table_column` VALUES (208, 19, 'table_name', '表名', 'VARCHAR', 'String', 'tableName', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 1);
INSERT INTO `gen_table_column` VALUES (209, 19, 'table_comment', '表描述', 'VARCHAR', 'String', 'tableComment', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 2);
INSERT INTO `gen_table_column` VALUES (210, 19, 'module_name', '模块名', 'VARCHAR', 'String', 'moduleName', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 3);
INSERT INTO `gen_table_column` VALUES (211, 19, 'package_name', '包路径', 'VARCHAR', 'String', 'packageName', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 4);
INSERT INTO `gen_table_column` VALUES (212, 19, 'class_name', '实体类名', 'VARCHAR', 'String', 'className', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 5);
INSERT INTO `gen_table_column` VALUES (213, 19, 'business_name', '业务名', 'VARCHAR', 'String', 'businessName', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 6);
INSERT INTO `gen_table_column` VALUES (214, 19, 'function_name', '功能名称', 'VARCHAR', 'String', 'functionName', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 7);
INSERT INTO `gen_table_column` VALUES (215, 19, 'author', '作者', 'VARCHAR', 'String', 'author', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 8);
INSERT INTO `gen_table_column` VALUES (216, 19, 'fe_module_path', '前端模块路径', 'VARCHAR', 'String', 'feModulePath', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 9);
INSERT INTO `gen_table_column` VALUES (217, 19, 'fe_generate_type', '生成类型: 1单表 2主从', 'CHAR', 'String', 'feGenerateType', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 10);
INSERT INTO `gen_table_column` VALUES (218, 19, 'gen_type', '生成方式: 0zip 1路径', 'CHAR', 'String', 'genType', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 11);
INSERT INTO `gen_table_column` VALUES (219, 19, 'gen_path', '生成路径', 'VARCHAR', 'String', 'genPath', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 12);
INSERT INTO `gen_table_column` VALUES (220, 19, 'options', '扩展选项', 'JSON', 'String', 'options', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 13);
INSERT INTO `gen_table_column` VALUES (221, 19, 'create_by', '创建者', 'VARCHAR', 'String', 'createBy', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 14);
INSERT INTO `gen_table_column` VALUES (222, 19, 'create_time', '创建时间', 'DATETIME', 'LocalDateTime', 'createTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 15);
INSERT INTO `gen_table_column` VALUES (223, 19, 'update_by', '更新者', 'VARCHAR', 'String', 'updateBy', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 16);
INSERT INTO `gen_table_column` VALUES (224, 19, 'update_time', '更新时间', 'DATETIME', 'LocalDateTime', 'updateTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 17);
INSERT INTO `gen_table_column` VALUES (225, 19, 'del_flag', '删除标志: 0正常 1删除', 'CHAR', 'String', 'delFlag', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 18);
INSERT INTO `gen_table_column` VALUES (226, 19, 'remark', '备注', 'VARCHAR', 'String', 'remark', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 19);
INSERT INTO `gen_table_column` VALUES (227, 20, 'column_id', '列ID', 'BIGINT', 'Long', 'columnId', '1', '1', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 0);
INSERT INTO `gen_table_column` VALUES (228, 20, 'table_id', '表ID', 'BIGINT', 'Long', 'tableId', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 1);
INSERT INTO `gen_table_column` VALUES (229, 20, 'column_name', '列名', 'VARCHAR', 'String', 'columnName', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 2);
INSERT INTO `gen_table_column` VALUES (230, 20, 'column_comment', '列描述', 'VARCHAR', 'String', 'columnComment', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 3);
INSERT INTO `gen_table_column` VALUES (231, 20, 'column_type', '数据库类型', 'VARCHAR', 'String', 'columnType', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 4);
INSERT INTO `gen_table_column` VALUES (232, 20, 'java_type', 'Java类型', 'VARCHAR', 'String', 'javaType', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 5);
INSERT INTO `gen_table_column` VALUES (233, 20, 'java_field', 'Java字段名', 'VARCHAR', 'String', 'javaField', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 6);
INSERT INTO `gen_table_column` VALUES (234, 20, 'is_pk', '是否主键: 0否 1是', 'CHAR', 'String', 'isPk', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 7);
INSERT INTO `gen_table_column` VALUES (235, 20, 'is_increment', '是否自增: 0否 1是', 'CHAR', 'String', 'isIncrement', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 8);
INSERT INTO `gen_table_column` VALUES (236, 20, 'is_required', '是否必填: 0否 1是', 'CHAR', 'String', 'isRequired', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 9);
INSERT INTO `gen_table_column` VALUES (237, 20, 'is_insert', '是否新增字段: 0否 1是', 'CHAR', 'String', 'isInsert', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 10);
INSERT INTO `gen_table_column` VALUES (238, 20, 'is_edit', '是否编辑字段: 0否 1是', 'CHAR', 'String', 'isEdit', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 11);
INSERT INTO `gen_table_column` VALUES (239, 20, 'is_list', '是否列表显示: 0否 1是', 'CHAR', 'String', 'isList', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 12);
INSERT INTO `gen_table_column` VALUES (240, 20, 'is_query', '是否查询条件: 0否 1是', 'CHAR', 'String', 'isQuery', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 13);
INSERT INTO `gen_table_column` VALUES (241, 20, 'is_detail', '是否详情显示: 0否 1是', 'CHAR', 'String', 'isDetail', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 14);
INSERT INTO `gen_table_column` VALUES (242, 20, 'is_export', '是否导出字段: 0否 1是', 'CHAR', 'String', 'isExport', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 15);
INSERT INTO `gen_table_column` VALUES (243, 20, 'query_type', '查询方式: EQ NE GT GTE LT LTE LIKE LIKE_LEFT LIKE_RIGHT BETWEEN IN', 'VARCHAR', 'String', 'queryType', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 16);
INSERT INTO `gen_table_column` VALUES (244, 20, 'html_type', '控件类型', 'VARCHAR', 'String', 'htmlType', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 17);
INSERT INTO `gen_table_column` VALUES (245, 20, 'dict_type', '字典类型', 'VARCHAR', 'String', 'dictType', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 18);
INSERT INTO `gen_table_column` VALUES (246, 20, 'sort_order', '排序', 'INT', 'Integer', 'sortOrder', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 19);
INSERT INTO `gen_table_column` VALUES (247, 21, 'mapping_id', '映射ID', 'BIGINT', 'Long', 'mappingId', '1', '1', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 0);
INSERT INTO `gen_table_column` VALUES (248, 21, 'db_type', '数据库类型(正则)', 'VARCHAR', 'String', 'dbType', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 1);
INSERT INTO `gen_table_column` VALUES (249, 21, 'java_type', 'Java类型', 'VARCHAR', 'String', 'javaType', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 2);
INSERT INTO `gen_table_column` VALUES (250, 21, 'java_import', '需要导入的包', 'VARCHAR', 'String', 'javaImport', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 3);
INSERT INTO `gen_table_column` VALUES (251, 21, 'default_html_type', '默认控件类型', 'VARCHAR', 'String', 'defaultHtmlType', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 4);
INSERT INTO `gen_table_column` VALUES (252, 21, 'sort_order', '排序', 'INT', 'Integer', 'sortOrder', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 5);
INSERT INTO `gen_table_column` VALUES (253, 22, 'dept_id', '部门ID', 'BIGINT', 'Long', 'deptId', '1', '1', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 0);
INSERT INTO `gen_table_column` VALUES (254, 22, 'dept_name', '部门名称', 'VARCHAR', 'String', 'deptName', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 1);
INSERT INTO `gen_table_column` VALUES (255, 22, 'parent_id', '父部门ID', 'BIGINT', 'Long', 'parentId', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 2);
INSERT INTO `gen_table_column` VALUES (256, 22, 'ancestors', '祖级列表', 'VARCHAR', 'String', 'ancestors', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 3);
INSERT INTO `gen_table_column` VALUES (257, 22, 'sort', '显示顺序', 'INT', 'Integer', 'sort', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 4);
INSERT INTO `gen_table_column` VALUES (258, 22, 'leader', '负责人', 'VARCHAR', 'String', 'leader', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 5);
INSERT INTO `gen_table_column` VALUES (259, 22, 'phone', '联系电话', 'VARCHAR', 'String', 'phone', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 6);
INSERT INTO `gen_table_column` VALUES (260, 22, 'email', '邮箱', 'VARCHAR', 'String', 'email', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 7);
INSERT INTO `gen_table_column` VALUES (261, 22, 'status', '状态（0正常 1停用）', 'CHAR', 'String', 'status', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 8);
INSERT INTO `gen_table_column` VALUES (262, 22, 'create_by', '创建人', 'VARCHAR', 'String', 'createBy', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 9);
INSERT INTO `gen_table_column` VALUES (263, 22, 'create_time', '创建时间', 'DATETIME', 'LocalDateTime', 'createTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 10);
INSERT INTO `gen_table_column` VALUES (264, 22, 'update_by', '更新人', 'VARCHAR', 'String', 'updateBy', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 11);
INSERT INTO `gen_table_column` VALUES (265, 22, 'update_time', '更新时间', 'DATETIME', 'LocalDateTime', 'updateTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 12);
INSERT INTO `gen_table_column` VALUES (266, 22, 'del_flag', '删除标志（0正常 1删除）', 'CHAR', 'String', 'delFlag', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 13);
INSERT INTO `gen_table_column` VALUES (267, 22, 'remark', '备注', 'VARCHAR', 'String', 'remark', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 14);
INSERT INTO `gen_table_column` VALUES (268, 23, 'file_id', '文件ID', 'BIGINT', 'Long', 'fileId', '1', '1', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 0);
INSERT INTO `gen_table_column` VALUES (269, 23, 'config_id', '存储配置ID', 'BIGINT', 'Long', 'configId', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 1);
INSERT INTO `gen_table_column` VALUES (270, 23, 'original_filename', '原始文件名', 'VARCHAR', 'String', 'originalFilename', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 2);
INSERT INTO `gen_table_column` VALUES (271, 23, 'stored_filename', '存储文件名', 'VARCHAR', 'String', 'storedFilename', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 3);
INSERT INTO `gen_table_column` VALUES (272, 23, 'file_path', '文件路径', 'VARCHAR', 'String', 'filePath', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 4);
INSERT INTO `gen_table_column` VALUES (273, 23, 'file_size', '文件大小(字节)', 'BIGINT', 'Long', 'fileSize', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 5);
INSERT INTO `gen_table_column` VALUES (274, 23, 'file_type', '文件类型', 'VARCHAR', 'String', 'fileType', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 6);
INSERT INTO `gen_table_column` VALUES (275, 23, 'file_ext', '文件扩展名', 'VARCHAR', 'String', 'fileExt', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 7);
INSERT INTO `gen_table_column` VALUES (276, 23, 'storage_type', '存储类型', 'VARCHAR', 'String', 'storageType', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 8);
INSERT INTO `gen_table_column` VALUES (277, 23, 'file_url', '文件URL', 'VARCHAR', 'String', 'fileUrl', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 9);
INSERT INTO `gen_table_column` VALUES (278, 23, 'file_md5', '文件MD5', 'VARCHAR', 'String', 'fileMd5', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 10);
INSERT INTO `gen_table_column` VALUES (279, 23, 'create_by', '创建者', 'VARCHAR', 'String', 'createBy', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 11);
INSERT INTO `gen_table_column` VALUES (280, 23, 'create_time', '创建时间', 'DATETIME', 'LocalDateTime', 'createTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 12);
INSERT INTO `gen_table_column` VALUES (281, 23, 'update_by', '更新者', 'VARCHAR', 'String', 'updateBy', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 13);
INSERT INTO `gen_table_column` VALUES (282, 23, 'update_time', '更新时间', 'DATETIME', 'LocalDateTime', 'updateTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 14);
INSERT INTO `gen_table_column` VALUES (283, 23, 'del_flag', '删除标志(0正常 1删除)', 'CHAR', 'String', 'delFlag', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 15);
INSERT INTO `gen_table_column` VALUES (284, 23, 'remark', '备注', 'VARCHAR', 'String', 'remark', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 16);
INSERT INTO `gen_table_column` VALUES (285, 24, 'login_log_id', '登录日志ID', 'BIGINT', 'Long', 'loginLogId', '1', '1', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 0);
INSERT INTO `gen_table_column` VALUES (286, 24, 'user_id', '用户ID', 'BIGINT', 'Long', 'userId', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 1);
INSERT INTO `gen_table_column` VALUES (287, 24, 'username', '用户名', 'VARCHAR', 'String', 'username', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 2);
INSERT INTO `gen_table_column` VALUES (288, 24, 'login_type', '登录类型（login登录 logout登出）', 'VARCHAR', 'String', 'loginType', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 3);
INSERT INTO `gen_table_column` VALUES (289, 24, 'ip_addr', 'IP地址', 'VARCHAR', 'String', 'ipAddr', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 4);
INSERT INTO `gen_table_column` VALUES (290, 24, 'login_location', '登录地点', 'VARCHAR', 'String', 'loginLocation', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 5);
INSERT INTO `gen_table_column` VALUES (291, 24, 'browser', '浏览器类型', 'VARCHAR', 'String', 'browser', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 6);
INSERT INTO `gen_table_column` VALUES (292, 24, 'os', '操作系统', 'VARCHAR', 'String', 'os', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 7);
INSERT INTO `gen_table_column` VALUES (293, 24, 'user_agent', 'User Agent', 'VARCHAR', 'String', 'userAgent', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 8);
INSERT INTO `gen_table_column` VALUES (294, 24, 'status', '登录状态（0成功 1失败）', 'CHAR', 'String', 'status', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 9);
INSERT INTO `gen_table_column` VALUES (295, 24, 'error_msg', '错误消息', 'VARCHAR', 'String', 'errorMsg', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 10);
INSERT INTO `gen_table_column` VALUES (296, 24, 'login_time', '登录时间', 'DATETIME', 'LocalDateTime', 'loginTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 11);
INSERT INTO `gen_table_column` VALUES (297, 25, 'menu_id', '菜单ID', 'BIGINT', 'Long', 'menuId', '1', '1', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 0);
INSERT INTO `gen_table_column` VALUES (298, 25, 'menu_name', '菜单名称', 'VARCHAR', 'String', 'menuName', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 1);
INSERT INTO `gen_table_column` VALUES (299, 25, 'parent_id', '父菜单ID', 'BIGINT', 'Long', 'parentId', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 2);
INSERT INTO `gen_table_column` VALUES (300, 25, 'sort', '显示顺序', 'INT', 'Integer', 'sort', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 3);
INSERT INTO `gen_table_column` VALUES (301, 25, 'path', '路由地址', 'VARCHAR', 'String', 'path', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 4);
INSERT INTO `gen_table_column` VALUES (302, 25, 'component', '组件路径', 'VARCHAR', 'String', 'component', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 5);
INSERT INTO `gen_table_column` VALUES (303, 25, 'menu_type', '菜单类型（M目录 C菜单 F按钮）', 'CHAR', 'String', 'menuType', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 6);
INSERT INTO `gen_table_column` VALUES (304, 25, 'visible', '菜单状态（0显示 1隐藏）', 'CHAR', 'String', 'visible', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 7);
INSERT INTO `gen_table_column` VALUES (305, 25, 'status', '菜单状态（0正常 1停用）', 'CHAR', 'String', 'status', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 8);
INSERT INTO `gen_table_column` VALUES (306, 25, 'perms', '权限标识', 'VARCHAR', 'String', 'perms', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 9);
INSERT INTO `gen_table_column` VALUES (307, 25, 'icon', '菜单图标', 'VARCHAR', 'String', 'icon', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 10);
INSERT INTO `gen_table_column` VALUES (308, 25, 'is_frame', '是否外链（0否 1是）', 'CHAR', 'String', 'isFrame', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 11);
INSERT INTO `gen_table_column` VALUES (309, 25, 'is_cache', '是否缓存（0缓存 1不缓存）', 'CHAR', 'String', 'isCache', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 12);
INSERT INTO `gen_table_column` VALUES (310, 25, 'link', '链接地址（外链时使用）', 'VARCHAR', 'String', 'link', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 13);
INSERT INTO `gen_table_column` VALUES (311, 25, 'target', '打开方式（_blank新窗口 _self当前窗口）', 'VARCHAR', 'String', 'target', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 14);
INSERT INTO `gen_table_column` VALUES (312, 25, 'badge', '角标文字', 'VARCHAR', 'String', 'badge', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 15);
INSERT INTO `gen_table_column` VALUES (313, 25, 'badge_color', '角标颜色', 'VARCHAR', 'String', 'badgeColor', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 16);
INSERT INTO `gen_table_column` VALUES (314, 25, 'create_by', '创建人', 'VARCHAR', 'String', 'createBy', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 17);
INSERT INTO `gen_table_column` VALUES (315, 25, 'create_time', '创建时间', 'DATETIME', 'LocalDateTime', 'createTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 18);
INSERT INTO `gen_table_column` VALUES (316, 25, 'update_by', '更新人', 'VARCHAR', 'String', 'updateBy', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 19);
INSERT INTO `gen_table_column` VALUES (317, 25, 'update_time', '更新时间', 'DATETIME', 'LocalDateTime', 'updateTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 20);
INSERT INTO `gen_table_column` VALUES (318, 25, 'del_flag', '删除标志（0正常 1删除）', 'CHAR', 'String', 'delFlag', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 21);
INSERT INTO `gen_table_column` VALUES (319, 25, 'remark', '备注', 'VARCHAR', 'String', 'remark', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 22);
INSERT INTO `gen_table_column` VALUES (320, 26, 'operation_log_id', '操作日志ID', 'BIGINT', 'Long', 'operationLogId', '1', '1', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 0);
INSERT INTO `gen_table_column` VALUES (321, 26, 'user_id', '用户ID', 'BIGINT', 'Long', 'userId', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 1);
INSERT INTO `gen_table_column` VALUES (322, 26, 'username', '用户名', 'VARCHAR', 'String', 'username', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 2);
INSERT INTO `gen_table_column` VALUES (323, 26, 'module_name', '模块名称', 'VARCHAR', 'String', 'moduleName', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 3);
INSERT INTO `gen_table_column` VALUES (324, 26, 'operation_type', '操作类型', 'VARCHAR', 'String', 'operationType', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 4);
INSERT INTO `gen_table_column` VALUES (325, 26, 'operation_desc', '操作描述', 'VARCHAR', 'String', 'operationDesc', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 5);
INSERT INTO `gen_table_column` VALUES (326, 26, 'request_method', '请求方式', 'VARCHAR', 'String', 'requestMethod', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 6);
INSERT INTO `gen_table_column` VALUES (327, 26, 'request_url', '请求URL', 'VARCHAR', 'String', 'requestUrl', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 7);
INSERT INTO `gen_table_column` VALUES (328, 26, 'method_name', '方法名称', 'VARCHAR', 'String', 'methodName', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 8);
INSERT INTO `gen_table_column` VALUES (329, 26, 'request_param', '请求参数', 'LONGTEXT', 'String', 'requestParam', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'textarea', '', 9);
INSERT INTO `gen_table_column` VALUES (330, 26, 'response_result', '响应结果', 'LONGTEXT', 'String', 'responseResult', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'textarea', '', 10);
INSERT INTO `gen_table_column` VALUES (331, 26, 'status', '操作状态（0成功 1失败）', 'CHAR', 'String', 'status', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 11);
INSERT INTO `gen_table_column` VALUES (332, 26, 'error_msg', '错误消息', 'LONGTEXT', 'String', 'errorMsg', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'textarea', '', 12);
INSERT INTO `gen_table_column` VALUES (333, 26, 'ip_addr', 'IP地址', 'VARCHAR', 'String', 'ipAddr', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 13);
INSERT INTO `gen_table_column` VALUES (334, 26, 'operation_location', '操作地点', 'VARCHAR', 'String', 'operationLocation', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 14);
INSERT INTO `gen_table_column` VALUES (335, 26, 'browser', '浏览器类型', 'VARCHAR', 'String', 'browser', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 15);
INSERT INTO `gen_table_column` VALUES (336, 26, 'os', '操作系统', 'VARCHAR', 'String', 'os', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 16);
INSERT INTO `gen_table_column` VALUES (337, 26, 'execution_time', '执行时长（毫秒）', 'BIGINT', 'Long', 'executionTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 17);
INSERT INTO `gen_table_column` VALUES (338, 26, 'operation_time', '操作时间', 'DATETIME', 'LocalDateTime', 'operationTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 18);
INSERT INTO `gen_table_column` VALUES (339, 27, 'role_id', '角色ID', 'BIGINT', 'Long', 'roleId', '1', '1', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 0);
INSERT INTO `gen_table_column` VALUES (340, 27, 'role_name', '角色名称', 'VARCHAR', 'String', 'roleName', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 1);
INSERT INTO `gen_table_column` VALUES (341, 27, 'role_key', '角色标识', 'VARCHAR', 'String', 'roleKey', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 2);
INSERT INTO `gen_table_column` VALUES (342, 27, 'sort', '显示顺序', 'INT', 'Integer', 'sort', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 3);
INSERT INTO `gen_table_column` VALUES (343, 27, 'data_scope', '数据范围（1全部 2自定义 3本部门 4本部门及以下 5仅本人）', 'CHAR', 'String', 'dataScope', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 4);
INSERT INTO `gen_table_column` VALUES (344, 27, 'status', '状态（0正常 1停用）', 'CHAR', 'String', 'status', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 5);
INSERT INTO `gen_table_column` VALUES (345, 27, 'create_by', '创建人', 'VARCHAR', 'String', 'createBy', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 6);
INSERT INTO `gen_table_column` VALUES (346, 27, 'create_time', '创建时间', 'DATETIME', 'LocalDateTime', 'createTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 7);
INSERT INTO `gen_table_column` VALUES (347, 27, 'update_by', '更新人', 'VARCHAR', 'String', 'updateBy', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 8);
INSERT INTO `gen_table_column` VALUES (348, 27, 'update_time', '更新时间', 'DATETIME', 'LocalDateTime', 'updateTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 9);
INSERT INTO `gen_table_column` VALUES (349, 27, 'del_flag', '删除标志（0正常 1删除）', 'CHAR', 'String', 'delFlag', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 10);
INSERT INTO `gen_table_column` VALUES (350, 27, 'remark', '备注', 'VARCHAR', 'String', 'remark', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 11);
INSERT INTO `gen_table_column` VALUES (351, 28, 'id', '主键ID', 'BIGINT', 'Long', 'id', '1', '1', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 0);
INSERT INTO `gen_table_column` VALUES (352, 28, 'role_id', '角色ID', 'BIGINT', 'Long', 'roleId', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 1);
INSERT INTO `gen_table_column` VALUES (353, 28, 'dept_id', '部门ID', 'BIGINT', 'Long', 'deptId', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 2);
INSERT INTO `gen_table_column` VALUES (354, 29, 'id', '主键ID', 'BIGINT', 'Long', 'id', '1', '1', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 0);
INSERT INTO `gen_table_column` VALUES (355, 29, 'role_id', '角色ID', 'BIGINT', 'Long', 'roleId', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 1);
INSERT INTO `gen_table_column` VALUES (356, 29, 'menu_id', '菜单ID', 'BIGINT', 'Long', 'menuId', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 2);
INSERT INTO `gen_table_column` VALUES (357, 30, 'config_id', '配置ID', 'BIGINT', 'Long', 'configId', '1', '1', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 0);
INSERT INTO `gen_table_column` VALUES (358, 30, 'config_name', '配置名称', 'VARCHAR', 'String', 'configName', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 1);
INSERT INTO `gen_table_column` VALUES (359, 30, 'storage_type', '存储类型(local/aliyun_oss/minio/aws_s3)', 'VARCHAR', 'String', 'storageType', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 2);
INSERT INTO `gen_table_column` VALUES (360, 30, 'is_default', '是否默认(0否 1是)', 'CHAR', 'String', 'isDefault', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 3);
INSERT INTO `gen_table_column` VALUES (361, 30, 'status', '状态(0正常 1停用)', 'CHAR', 'String', 'status', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 4);
INSERT INTO `gen_table_column` VALUES (362, 30, 'endpoint', 'Endpoint', 'VARCHAR', 'String', 'endpoint', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 5);
INSERT INTO `gen_table_column` VALUES (363, 30, 'bucket_name', '存储桶名称', 'VARCHAR', 'String', 'bucketName', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 6);
INSERT INTO `gen_table_column` VALUES (364, 30, 'access_key_id', 'Access Key ID', 'VARCHAR', 'String', 'accessKeyId', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 7);
INSERT INTO `gen_table_column` VALUES (365, 30, 'access_key_secret', 'Access Key Secret', 'VARCHAR', 'String', 'accessKeySecret', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 8);
INSERT INTO `gen_table_column` VALUES (366, 30, 'domain', '自定义域名', 'VARCHAR', 'String', 'domain', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 9);
INSERT INTO `gen_table_column` VALUES (367, 30, 'region', '区域', 'VARCHAR', 'String', 'region', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 10);
INSERT INTO `gen_table_column` VALUES (368, 30, 'local_path', '本地存储路径', 'VARCHAR', 'String', 'localPath', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 11);
INSERT INTO `gen_table_column` VALUES (369, 30, 'local_url_prefix', '本地存储URL前缀', 'VARCHAR', 'String', 'localUrlPrefix', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 12);
INSERT INTO `gen_table_column` VALUES (370, 30, 'use_https', '是否使用HTTPS(0否 1是)', 'CHAR', 'String', 'useHttps', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 13);
INSERT INTO `gen_table_column` VALUES (371, 30, 'create_by', '创建者', 'VARCHAR', 'String', 'createBy', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 14);
INSERT INTO `gen_table_column` VALUES (372, 30, 'create_time', '创建时间', 'DATETIME', 'LocalDateTime', 'createTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 15);
INSERT INTO `gen_table_column` VALUES (373, 30, 'update_by', '更新者', 'VARCHAR', 'String', 'updateBy', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 16);
INSERT INTO `gen_table_column` VALUES (374, 30, 'update_time', '更新时间', 'DATETIME', 'LocalDateTime', 'updateTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 17);
INSERT INTO `gen_table_column` VALUES (375, 30, 'del_flag', '删除标志(0正常 1删除)', 'CHAR', 'String', 'delFlag', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 18);
INSERT INTO `gen_table_column` VALUES (376, 30, 'remark', '备注', 'VARCHAR', 'String', 'remark', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 19);
INSERT INTO `gen_table_column` VALUES (377, 31, 'user_id', '用户ID', 'BIGINT', 'Long', 'userId', '1', '1', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 0);
INSERT INTO `gen_table_column` VALUES (378, 31, 'username', '用户名', 'VARCHAR', 'String', 'username', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 1);
INSERT INTO `gen_table_column` VALUES (379, 31, 'password', '密码', 'VARCHAR', 'String', 'password', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 2);
INSERT INTO `gen_table_column` VALUES (380, 31, 'real_name', '真实姓名', 'VARCHAR', 'String', 'realName', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 3);
INSERT INTO `gen_table_column` VALUES (381, 31, 'phone', '手机号', 'VARCHAR', 'String', 'phone', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 4);
INSERT INTO `gen_table_column` VALUES (382, 31, 'email', '邮箱', 'VARCHAR', 'String', 'email', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 5);
INSERT INTO `gen_table_column` VALUES (383, 31, 'gender', '性别（0男 1女 2未知）', 'CHAR', 'String', 'gender', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 6);
INSERT INTO `gen_table_column` VALUES (384, 31, 'avatar', '头像', 'VARCHAR', 'String', 'avatar', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 7);
INSERT INTO `gen_table_column` VALUES (385, 31, 'dept_id', '部门ID', 'BIGINT', 'Long', 'deptId', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 8);
INSERT INTO `gen_table_column` VALUES (386, 31, 'status', '状态（0正常 1停用）', 'CHAR', 'String', 'status', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 9);
INSERT INTO `gen_table_column` VALUES (387, 31, 'create_by', '创建人', 'VARCHAR', 'String', 'createBy', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 10);
INSERT INTO `gen_table_column` VALUES (388, 31, 'create_time', '创建时间', 'DATETIME', 'LocalDateTime', 'createTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 11);
INSERT INTO `gen_table_column` VALUES (389, 31, 'update_by', '更新人', 'VARCHAR', 'String', 'updateBy', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 12);
INSERT INTO `gen_table_column` VALUES (390, 31, 'update_time', '更新时间', 'DATETIME', 'LocalDateTime', 'updateTime', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'datetime', '', 13);
INSERT INTO `gen_table_column` VALUES (391, 31, 'del_flag', '删除标志（0正常 1删除）', 'CHAR', 'String', 'delFlag', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 14);
INSERT INTO `gen_table_column` VALUES (392, 31, 'remark', '备注', 'VARCHAR', 'String', 'remark', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 15);
INSERT INTO `gen_table_column` VALUES (393, 31, 'super_admin', '是否超级管理员（0否 1是）', 'TINYINT', 'Integer', 'superAdmin', '0', '0', '0', '1', '1', '1', '0', '1', '1', 'EQ', 'select', '', 16);
INSERT INTO `gen_table_column` VALUES (394, 32, 'id', '主键ID', 'BIGINT', 'Long', 'id', '1', '1', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 0);
INSERT INTO `gen_table_column` VALUES (395, 32, 'user_id', '用户ID', 'BIGINT', 'Long', 'userId', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 1);
INSERT INTO `gen_table_column` VALUES (396, 32, 'role_id', '角色ID', 'BIGINT', 'Long', 'roleId', '0', '0', '1', '1', '1', '1', '0', '1', '1', 'EQ', 'input', '', 2);

-- ----------------------------
-- Table structure for gen_type_mapping
-- ----------------------------
DROP TABLE IF EXISTS `gen_type_mapping`;
CREATE TABLE `gen_type_mapping`  (
  `mapping_id` bigint NOT NULL AUTO_INCREMENT COMMENT '映射ID',
  `db_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '数据库类型(正则)',
  `java_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Java类型',
  `java_import` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '需要导入的包',
  `default_html_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'input' COMMENT '默认控件类型',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`mapping_id`) USING BTREE,
  UNIQUE INDEX `uk_db_type`(`db_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '类型映射配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_type_mapping
-- ----------------------------
INSERT INTO `gen_type_mapping` VALUES (1, 'bigint', 'Long', '', 'number', 1);
INSERT INTO `gen_type_mapping` VALUES (2, 'int', 'Integer', '', 'number', 2);
INSERT INTO `gen_type_mapping` VALUES (3, 'integer', 'Integer', '', 'number', 3);
INSERT INTO `gen_type_mapping` VALUES (4, 'tinyint\\(1\\)', 'Boolean', '', 'radio', 4);
INSERT INTO `gen_type_mapping` VALUES (5, 'tinyint', 'Integer', '', 'select', 5);
INSERT INTO `gen_type_mapping` VALUES (6, 'smallint', 'Integer', '', 'number', 6);
INSERT INTO `gen_type_mapping` VALUES (7, 'decimal', 'BigDecimal', 'java.math.BigDecimal', 'number', 7);
INSERT INTO `gen_type_mapping` VALUES (8, 'numeric', 'BigDecimal', 'java.math.BigDecimal', 'number', 8);
INSERT INTO `gen_type_mapping` VALUES (9, 'double', 'Double', '', 'number', 9);
INSERT INTO `gen_type_mapping` VALUES (10, 'float', 'Float', '', 'number', 10);
INSERT INTO `gen_type_mapping` VALUES (11, 'varchar', 'String', '', 'input', 11);
INSERT INTO `gen_type_mapping` VALUES (12, 'char', 'String', '', 'input', 12);
INSERT INTO `gen_type_mapping` VALUES (13, 'text', 'String', '', 'textarea', 13);
INSERT INTO `gen_type_mapping` VALUES (14, 'longtext', 'String', '', 'textarea', 14);
INSERT INTO `gen_type_mapping` VALUES (15, 'mediumtext', 'String', '', 'textarea', 15);
INSERT INTO `gen_type_mapping` VALUES (16, 'datetime', 'LocalDateTime', 'java.time.LocalDateTime', 'datetime', 16);
INSERT INTO `gen_type_mapping` VALUES (17, 'timestamp', 'LocalDateTime', 'java.time.LocalDateTime', 'datetime', 17);
INSERT INTO `gen_type_mapping` VALUES (18, 'date', 'LocalDate', 'java.time.LocalDate', 'date', 18);
INSERT INTO `gen_type_mapping` VALUES (19, 'time', 'LocalTime', 'java.time.LocalTime', 'input', 19);
INSERT INTO `gen_type_mapping` VALUES (20, 'blob', 'byte[]', '', 'upload', 20);
INSERT INTO `gen_type_mapping` VALUES (21, 'json', 'String', '', 'textarea', 21);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '部门名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门ID',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '祖级列表',
  `sort` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '删除标志（0正常 1删除）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, '总公司', 0, '0', 0, '管理员', '13800000000', 'admin@company.com', '0', NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_dept` VALUES (2, '研发部', 1, '0,1', 1, '', '', '', '0', NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_dept` VALUES (3, '市场部', 1, '0,1', 2, '', '', '', '0', NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_dept` VALUES (4, '财务部', 1, '0,1', 3, '', '', '', '0', NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_dept` VALUES (5, '人事部', 1, '0,1', 4, '', '', '', '0', NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item`  (
  `item_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典项主键',
  `dict_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典编码',
  `item_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典项标签',
  `item_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典项值',
  `item_sort` int NULL DEFAULT 0 COMMENT '字典项排序',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '样式属性',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0正常 1删除）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`item_id`) USING BTREE,
  INDEX `idx_dict_code`(`dict_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 79 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES (1, 'sys_user_gender', '男', '0', 1, NULL, NULL, 'Y', '0', 'admin', '2025-12-24 20:55:04', '', NULL, '0', '性别男');
INSERT INTO `sys_dict_item` VALUES (2, 'sys_user_gender', '女', '1', 2, NULL, NULL, 'N', '0', 'admin', '2025-12-24 20:55:04', '', NULL, '0', '性别女');
INSERT INTO `sys_dict_item` VALUES (3, 'sys_user_gender', '未知', '2', 3, NULL, NULL, 'N', '0', 'admin', '2025-12-24 20:55:04', '', NULL, '0', '性别未知');
INSERT INTO `sys_dict_item` VALUES (4, 'sys_common_status', '正常', '0', 1, NULL, NULL, 'Y', '0', 'admin', '2025-12-24 20:55:04', '', NULL, '0', '正常状态');
INSERT INTO `sys_dict_item` VALUES (5, 'sys_common_status', '停用', '1', 2, NULL, NULL, 'N', '0', 'admin', '2025-12-24 20:55:04', '', NULL, '0', '停用状态');
INSERT INTO `sys_dict_item` VALUES (6, 'sys_yes_no', '是', 'Y', 1, NULL, NULL, 'Y', '0', 'admin', '2025-12-24 20:55:04', '', NULL, '0', '是');
INSERT INTO `sys_dict_item` VALUES (7, 'sys_yes_no', '否', 'N', 2, NULL, NULL, 'N', '0', 'admin', '2025-12-24 20:55:04', '', NULL, '0', '否');
INSERT INTO `sys_dict_item` VALUES (8, 'sys_data_scope', '全部数据', '1', 1, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '可查看所有数据');
INSERT INTO `sys_dict_item` VALUES (9, 'sys_data_scope', '自定义数据', '2', 2, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '按自定义部门查看');
INSERT INTO `sys_dict_item` VALUES (10, 'sys_data_scope', '本部门数据', '3', 3, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '仅查看本部门数据');
INSERT INTO `sys_dict_item` VALUES (11, 'sys_data_scope', '本部门及以下', '4', 4, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '查看本部门及下级部门数据');
INSERT INTO `sys_dict_item` VALUES (12, 'sys_data_scope', '仅本人数据', '5', 5, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '仅查看本人数据');
INSERT INTO `sys_dict_item` VALUES (13, 'sys_menu_type', '目录', 'M', 1, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '菜单目录');
INSERT INTO `sys_dict_item` VALUES (14, 'sys_menu_type', '菜单', 'C', 2, NULL, NULL, 'Y', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '页面菜单');
INSERT INTO `sys_dict_item` VALUES (15, 'sys_menu_type', '按钮', 'F', 3, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '功能按钮');
INSERT INTO `sys_dict_item` VALUES (16, 'sys_visible_status', '显示', '0', 1, NULL, NULL, 'Y', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '菜单显示');
INSERT INTO `sys_dict_item` VALUES (17, 'sys_visible_status', '隐藏', '1', 2, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '菜单隐藏');
INSERT INTO `sys_dict_item` VALUES (18, 'sys_cache_status', '缓存', '0', 1, NULL, NULL, 'Y', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '开启路由缓存');
INSERT INTO `sys_dict_item` VALUES (19, 'sys_cache_status', '不缓存', '1', 2, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '不缓存路由');
INSERT INTO `sys_dict_item` VALUES (20, 'sys_link_target', '新窗口', '_blank', 1, NULL, NULL, 'Y', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '在新窗口打开');
INSERT INTO `sys_dict_item` VALUES (21, 'sys_link_target', '当前窗口', '_self', 2, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '在当前窗口打开');
INSERT INTO `sys_dict_item` VALUES (22, 'sys_yes_no_01', '否', '0', 1, NULL, NULL, 'Y', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '否');
INSERT INTO `sys_dict_item` VALUES (23, 'sys_yes_no_01', '是', '1', 2, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '是');
INSERT INTO `sys_dict_item` VALUES (24, 'sys_login_type', '登录', 'login', 1, NULL, NULL, 'Y', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '用户登录');
INSERT INTO `sys_dict_item` VALUES (25, 'sys_login_type', '登出', 'logout', 2, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '用户登出');
INSERT INTO `sys_dict_item` VALUES (26, 'sys_login_status', '成功', '0', 1, NULL, 'success', 'Y', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '登录成功');
INSERT INTO `sys_dict_item` VALUES (27, 'sys_login_status', '失败', '1', 2, NULL, 'error', 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '登录失败');
INSERT INTO `sys_dict_item` VALUES (28, 'sys_operation_type', '其他', 'OTHER', 0, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '其他操作');
INSERT INTO `sys_dict_item` VALUES (29, 'sys_operation_type', '新增', 'INSERT', 1, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '新增操作');
INSERT INTO `sys_dict_item` VALUES (30, 'sys_operation_type', '修改', 'UPDATE', 2, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '修改操作');
INSERT INTO `sys_dict_item` VALUES (31, 'sys_operation_type', '删除', 'DELETE', 3, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '删除操作');
INSERT INTO `sys_dict_item` VALUES (32, 'sys_operation_type', '查询', 'SELECT', 4, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '查询操作');
INSERT INTO `sys_dict_item` VALUES (33, 'sys_operation_type', '导入', 'IMPORT', 5, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '导入操作');
INSERT INTO `sys_dict_item` VALUES (34, 'sys_operation_type', '导出', 'EXPORT', 6, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '导出操作');
INSERT INTO `sys_dict_item` VALUES (35, 'sys_operation_type', '登录', 'LOGIN', 7, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '登录操作');
INSERT INTO `sys_dict_item` VALUES (36, 'sys_operation_type', '登出', 'LOGOUT', 8, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '登出操作');
INSERT INTO `sys_dict_item` VALUES (37, 'sys_operation_status', '成功', '0', 1, NULL, 'success', 'Y', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '操作成功');
INSERT INTO `sys_dict_item` VALUES (38, 'sys_operation_status', '失败', '1', 2, NULL, 'error', 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '操作失败');
INSERT INTO `sys_dict_item` VALUES (39, 'sys_storage_type', '本地存储', 'local', 1, NULL, NULL, 'Y', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '本地文件系统存储');
INSERT INTO `sys_dict_item` VALUES (40, 'sys_storage_type', '阿里云OSS', 'aliyun_oss', 2, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '阿里云对象存储');
INSERT INTO `sys_dict_item` VALUES (41, 'sys_storage_type', 'MinIO', 'minio', 3, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', 'MinIO对象存储');
INSERT INTO `sys_dict_item` VALUES (42, 'sys_storage_type', 'AWS S3', 'aws_s3', 4, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', 'AWS S3对象存储');
INSERT INTO `sys_dict_item` VALUES (43, 'sys_storage_type', '腾讯云COS', 'tencent_cos', 5, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '腾讯云对象存储');
INSERT INTO `sys_dict_item` VALUES (44, 'gen_html_type', '文本框', 'input', 1, NULL, NULL, 'Y', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '单行文本输入');
INSERT INTO `sys_dict_item` VALUES (45, 'gen_html_type', '文本域', 'textarea', 2, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '多行文本输入');
INSERT INTO `sys_dict_item` VALUES (46, 'gen_html_type', '下拉框', 'select', 3, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '下拉选择器');
INSERT INTO `sys_dict_item` VALUES (47, 'gen_html_type', '单选框', 'radio', 4, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '单选按钮组');
INSERT INTO `sys_dict_item` VALUES (48, 'gen_html_type', '复选框', 'checkbox', 5, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '复选框');
INSERT INTO `sys_dict_item` VALUES (49, 'gen_html_type', '日期时间', 'datetime', 6, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '日期时间选择器');
INSERT INTO `sys_dict_item` VALUES (50, 'gen_html_type', '日期', 'date', 7, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '日期选择器');
INSERT INTO `sys_dict_item` VALUES (51, 'gen_html_type', '数字输入', 'number', 8, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '数字输入框');
INSERT INTO `sys_dict_item` VALUES (52, 'gen_html_type', '图片上传', 'imageUpload', 9, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '图片上传组件');
INSERT INTO `sys_dict_item` VALUES (53, 'gen_html_type', '文件上传', 'fileUpload', 10, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '文件上传组件');
INSERT INTO `sys_dict_item` VALUES (54, 'gen_html_type', '富文本', 'editor', 11, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '富文本编辑器');
INSERT INTO `sys_dict_item` VALUES (55, 'gen_html_type', '树形选择', 'treeSelect', 12, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '树形选择器');
INSERT INTO `sys_dict_item` VALUES (56, 'gen_query_type', '等于', 'EQ', 1, NULL, NULL, 'Y', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '精确匹配 =');
INSERT INTO `sys_dict_item` VALUES (57, 'gen_query_type', '不等于', 'NE', 2, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '不等于 !=');
INSERT INTO `sys_dict_item` VALUES (58, 'gen_query_type', '大于', 'GT', 3, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '大于 >');
INSERT INTO `sys_dict_item` VALUES (59, 'gen_query_type', '大于等于', 'GE', 4, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '大于等于 >=');
INSERT INTO `sys_dict_item` VALUES (60, 'gen_query_type', '小于', 'LT', 5, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '小于 <');
INSERT INTO `sys_dict_item` VALUES (61, 'gen_query_type', '小于等于', 'LE', 6, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '小于等于 <=');
INSERT INTO `sys_dict_item` VALUES (62, 'gen_query_type', '模糊', 'LIKE', 7, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '模糊匹配 LIKE');
INSERT INTO `sys_dict_item` VALUES (63, 'gen_query_type', '左模糊', 'LIKE_LEFT', 8, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '左模糊 LIKE %value');
INSERT INTO `sys_dict_item` VALUES (64, 'gen_query_type', '右模糊', 'LIKE_RIGHT', 9, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '右模糊 LIKE value%');
INSERT INTO `sys_dict_item` VALUES (65, 'gen_query_type', '范围', 'BETWEEN', 10, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '范围查询 BETWEEN');
INSERT INTO `sys_dict_item` VALUES (66, 'gen_query_type', '包含', 'IN', 11, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '包含查询 IN');
INSERT INTO `sys_dict_item` VALUES (67, 'gen_java_type', 'Long', 'Long', 1, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '长整型');
INSERT INTO `sys_dict_item` VALUES (68, 'gen_java_type', 'String', 'String', 2, NULL, NULL, 'Y', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '字符串');
INSERT INTO `sys_dict_item` VALUES (69, 'gen_java_type', 'Integer', 'Integer', 3, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '整型');
INSERT INTO `sys_dict_item` VALUES (70, 'gen_java_type', 'Double', 'Double', 4, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '双精度浮点');
INSERT INTO `sys_dict_item` VALUES (71, 'gen_java_type', 'BigDecimal', 'BigDecimal', 5, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '高精度数值');
INSERT INTO `sys_dict_item` VALUES (72, 'gen_java_type', 'LocalDateTime', 'LocalDateTime', 6, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '日期时间');
INSERT INTO `sys_dict_item` VALUES (73, 'gen_java_type', 'LocalDate', 'LocalDate', 7, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '日期');
INSERT INTO `sys_dict_item` VALUES (74, 'gen_java_type', 'Boolean', 'Boolean', 8, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '布尔值');
INSERT INTO `sys_dict_item` VALUES (75, 'gen_type', 'ZIP下载', '0', 1, NULL, NULL, 'Y', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '生成ZIP包下载');
INSERT INTO `sys_dict_item` VALUES (76, 'gen_type', '指定路径', '1', 2, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '生成到指定目录');
INSERT INTO `sys_dict_item` VALUES (77, 'gen_fe_type', '单表', '1', 1, NULL, NULL, 'Y', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '单表CRUD');
INSERT INTO `sys_dict_item` VALUES (78, 'gen_fe_type', '主从表', '2', 2, NULL, NULL, 'N', '0', 'admin', '2025-12-24 22:11:50', '', NULL, '0', '主从表结构');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典编码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0正常 1删除）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `uk_dict_code`(`dict_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_gender', '0', 'admin', '2025-12-24 20:55:04', '', NULL, '0', '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '系统状态', 'sys_common_status', '0', 'admin', '2025-12-24 20:55:04', '', NULL, '0', '系统通用状态');
INSERT INTO `sys_dict_type` VALUES (3, '是否', 'sys_yes_no', '0', 'admin', '2025-12-24 20:55:04', '', NULL, '0', '是否选择');
INSERT INTO `sys_dict_type` VALUES (4, '数据权限范围', 'sys_data_scope', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '角色数据权限范围');
INSERT INTO `sys_dict_type` VALUES (5, '菜单类型', 'sys_menu_type', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '菜单类型列表');
INSERT INTO `sys_dict_type` VALUES (6, '显示状态', 'sys_visible_status', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '菜单显示状态');
INSERT INTO `sys_dict_type` VALUES (7, '缓存状态', 'sys_cache_status', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '路由缓存状态');
INSERT INTO `sys_dict_type` VALUES (8, '链接打开方式', 'sys_link_target', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '外链打开方式');
INSERT INTO `sys_dict_type` VALUES (9, '是否(0/1)', 'sys_yes_no_01', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '是否选项(0=否,1=是)');
INSERT INTO `sys_dict_type` VALUES (10, '登录类型', 'sys_login_type', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '登录日志类型');
INSERT INTO `sys_dict_type` VALUES (11, '登录状态', 'sys_login_status', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '登录结果状态');
INSERT INTO `sys_dict_type` VALUES (12, '操作类型', 'sys_operation_type', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '操作日志类型');
INSERT INTO `sys_dict_type` VALUES (13, '操作状态', 'sys_operation_status', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '操作结果状态');
INSERT INTO `sys_dict_type` VALUES (14, '存储类型', 'sys_storage_type', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '文件存储类型');
INSERT INTO `sys_dict_type` VALUES (15, '表单控件类型', 'gen_html_type', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '代码生成表单控件类型');
INSERT INTO `sys_dict_type` VALUES (16, '查询条件类型', 'gen_query_type', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '代码生成查询条件类型');
INSERT INTO `sys_dict_type` VALUES (17, 'Java数据类型', 'gen_java_type', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '代码生成Java数据类型');
INSERT INTO `sys_dict_type` VALUES (18, '生成方式', 'gen_type', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '代码生成输出方式');
INSERT INTO `sys_dict_type` VALUES (19, '前端表单类型', 'gen_fe_type', '0', 'admin', '2025-12-24 22:11:49', '', NULL, '0', '代码生成前端表单类型');

-- ----------------------------
-- Table structure for sys_file_record
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_record`;
CREATE TABLE `sys_file_record`  (
  `file_id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `config_id` bigint NULL DEFAULT NULL COMMENT '存储配置ID',
  `original_filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '原始文件名',
  `stored_filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '存储文件名',
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件路径',
  `file_size` bigint NULL DEFAULT NULL COMMENT '文件大小(字节)',
  `file_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件类型',
  `file_ext` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件扩展名',
  `storage_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '存储类型',
  `file_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件URL',
  `file_md5` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件MD5',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '删除标志(0正常 1删除)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`file_id`) USING BTREE,
  INDEX `idx_config_id`(`config_id` ASC) USING BTREE,
  INDEX `idx_storage_type`(`storage_type` ASC) USING BTREE,
  INDEX `idx_file_md5`(`file_md5` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文件记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file_record
-- ----------------------------
INSERT INTO `sys_file_record` VALUES (1, 1, 'config.yaml', '85c51fa55c6e4cc6814ee0aee4161f9f.yaml', '2025/12/24/7d20a355f8c143b78695b45d7fea3ef7.yaml', 481810, 'application/octet-stream', 'yaml', 'local', '/api/static/2025/12/24/7d20a355f8c143b78695b45d7fea3ef7.yaml', '9e39f926f40176da68b0001e1cfdb018', NULL, '2025-12-24 11:38:47', NULL, '2025-12-24 12:12:40', '1', NULL);
INSERT INTO `sys_file_record` VALUES (2, 1, 'portable.ini', 'd07b0ace64bb4ee19a76c37ae4256361.ini', '2025/12/24/dc866d253ed044aeb955121b762baaf1.ini', 50, 'application/octet-stream', 'ini', 'local', '/api/file/2025/12/24/dc866d253ed044aeb955121b762baaf1.ini', 'c0eb619af3937dfcf2986fd97c5b3116', NULL, '2025-12-24 12:12:08', NULL, '2025-12-24 12:12:21', '1', NULL);
INSERT INTO `sys_file_record` VALUES (3, 2, 'config.yaml', 'cdb2d634e0b94564b037d2eceb67abf4.yaml', '2025/12/24/13e31667157a4dfba4d8e1f70f41c078.yaml', 481810, 'application/octet-stream', 'yaml', 'tencent_cos', 'http://tiny-1252430636.cos.ap-guangzhou.myqcloud.com/2025/12/24/13e31667157a4dfba4d8e1f70f41c078.yaml', '9e39f926f40176da68b0001e1cfdb018', NULL, '2025-12-24 15:04:46', NULL, '2025-12-24 15:05:28', '1', NULL);

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `login_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '登录日志ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `login_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录类型（login登录 logout登出）',
  `ip_addr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作系统',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'User Agent',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `error_msg` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '错误消息',
  `login_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  PRIMARY KEY (`login_log_id`) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE,
  INDEX `idx_login_time`(`login_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '登录日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
INSERT INTO `sys_login_log` VALUES (16, 1, 'admin', 'login', '127.0.0.1', '内网IP', 'Edge 143.0.0.0', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '0', NULL, '2025-12-24 11:10:37');
INSERT INTO `sys_login_log` VALUES (17, 1, 'admin', 'logout', '127.0.0.1', '内网IP', 'Edge 143.0.0.0', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '0', NULL, '2025-12-24 11:24:01');
INSERT INTO `sys_login_log` VALUES (18, 1, 'admin', 'login', '127.0.0.1', '内网IP', 'Edge 143.0.0.0', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '0', NULL, '2025-12-24 11:24:06');
INSERT INTO `sys_login_log` VALUES (19, 1, 'admin', 'logout', '127.0.0.1', '内网IP', 'Edge 143.0.0.0', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '0', NULL, '2025-12-24 17:08:04');
INSERT INTO `sys_login_log` VALUES (20, 1, 'admin', 'login', '127.0.0.1', '内网IP', 'Edge 143.0.0.0', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '0', NULL, '2025-12-24 17:08:10');
INSERT INTO `sys_login_log` VALUES (21, 1, 'admin', 'logout', '127.0.0.1', '内网IP', 'Edge 143.0.0.0', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '0', NULL, '2025-12-24 17:22:49');
INSERT INTO `sys_login_log` VALUES (22, 1, 'admin', 'login', '127.0.0.1', '内网IP', 'Edge 143.0.0.0', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '0', NULL, '2025-12-24 17:22:56');
INSERT INTO `sys_login_log` VALUES (23, 1, 'admin', 'login', '127.0.0.1', '内网IP', 'Edge 143.0.0.0', 'Windows 10', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '0', NULL, '2025-12-24 20:56:29');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `sort` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件路径',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `is_frame` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '是否外链（0否 1是）',
  `is_cache` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `link` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '链接地址（外链时使用）',
  `target` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '_blank' COMMENT '打开方式（_blank新窗口 _self当前窗口）',
  `badge` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角标文字',
  `badge_color` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角标颜色',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '删除标志（0正常 1删除）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 69 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, '/system', NULL, 'M', '0', '0', NULL, 'SettingOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (2, '用户管理', 1, 1, '/system/user', 'system/user/index', 'C', '0', '0', 'system:user:list', 'UserOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (3, '角色管理', 1, 2, '/system/role', 'system/role/index', 'C', '0', '0', 'system:role:list', 'TeamOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (4, '菜单管理', 1, 3, '/system/menu', 'system/menu/index', 'C', '0', '0', 'system:menu:list', 'MenuOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (5, '用户查询', 2, 1, NULL, NULL, 'F', '0', '0', 'system:user:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (6, '用户新增', 2, 2, NULL, NULL, 'F', '0', '0', 'system:user:add', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (7, '用户修改', 2, 3, NULL, NULL, 'F', '0', '0', 'system:user:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (8, '用户删除', 2, 4, NULL, NULL, 'F', '0', '0', 'system:user:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (9, '角色查询', 3, 1, NULL, NULL, 'F', '0', '0', 'system:role:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (10, '角色新增', 3, 2, NULL, NULL, 'F', '0', '0', 'system:role:add', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (11, '角色修改', 3, 3, NULL, NULL, 'F', '0', '0', 'system:role:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (12, '角色删除', 3, 4, NULL, NULL, 'F', '0', '0', 'system:role:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (13, '菜单查询', 4, 1, NULL, NULL, 'F', '0', '0', 'system:menu:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (14, '菜单新增', 4, 2, NULL, NULL, 'F', '0', '0', 'system:menu:add', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (15, '菜单修改', 4, 3, NULL, NULL, 'F', '0', '0', 'system:menu:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (16, '菜单删除', 4, 4, NULL, NULL, 'F', '0', '0', 'system:menu:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (17, '部门管理', 1, 4, '/system/dept', 'system/dept/index', 'C', '0', '0', 'system:dept:list', 'ApartmentOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (18, '部门查询', 17, 1, NULL, NULL, 'F', '0', '0', 'system:dept:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (19, '部门新增', 17, 2, NULL, NULL, 'F', '0', '0', 'system:dept:add', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (20, '部门修改', 17, 3, NULL, NULL, 'F', '0', '0', 'system:dept:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (21, '部门删除', 17, 4, NULL, NULL, 'F', '0', '0', 'system:dept:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (22, '系统监控', 0, 2, '/monitor', NULL, 'M', '0', '0', NULL, 'MonitorOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (23, '登录日志', 22, 1, '/monitor/loginLog', 'monitor/loginLog/index', 'C', '0', '0', 'monitor:loginLog:list', 'LoginOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (24, '操作日志', 22, 2, '/monitor/operationLog', 'monitor/operationLog/index', 'C', '0', '0', 'monitor:operationLog:list', 'FileTextOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (25, '登录日志查询', 23, 1, NULL, NULL, 'F', '0', '0', 'monitor:loginLog:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (26, '登录日志删除', 23, 2, NULL, NULL, 'F', '0', '0', 'monitor:loginLog:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (27, '操作日志查询', 24, 1, NULL, NULL, 'F', '0', '0', 'monitor:operationLog:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (28, '操作日志删除', 24, 2, NULL, NULL, 'F', '0', '0', 'monitor:operationLog:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (29, '在线用户', 22, 3, '/monitor/onlineUser', 'monitor/onlineUser/index', 'C', '0', '0', 'monitor:online:list', 'TeamOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (30, '在线用户查询', 29, 1, NULL, NULL, 'F', '0', '0', 'monitor:online:list', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (31, '强制退出', 29, 2, NULL, NULL, 'F', '0', '0', 'monitor:online:kickout', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (32, '用户封禁', 29, 3, NULL, NULL, 'F', '0', '0', 'monitor:online:disable', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_menu` VALUES (33, '重置密码', 2, 5, NULL, NULL, 'F', '0', '0', 'system:user:resetPwd', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:47:43', NULL, '2025-12-23 22:47:43', '0', NULL);
INSERT INTO `sys_menu` VALUES (34, '封禁用户', 2, 6, NULL, NULL, 'F', '0', '0', 'system:user:disable', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-23 22:47:43', NULL, '2025-12-23 22:47:43', '0', NULL);
INSERT INTO `sys_menu` VALUES (35, '存储管理', 0, 3, '/storage', NULL, 'M', '0', '0', NULL, 'CloudServerOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-24 11:09:39', NULL, '2025-12-24 11:09:39', '0', NULL);
INSERT INTO `sys_menu` VALUES (36, '存储配置', 35, 1, '/storage/config', 'storage/config/index', 'C', '0', '0', 'storage:config:list', 'SettingOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-24 11:09:40', NULL, '2025-12-24 11:09:40', '0', NULL);
INSERT INTO `sys_menu` VALUES (37, '配置查询', 36, 1, NULL, NULL, 'F', '0', '0', 'storage:config:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-24 11:09:40', NULL, '2025-12-24 11:09:40', '0', NULL);
INSERT INTO `sys_menu` VALUES (38, '配置新增', 36, 2, NULL, NULL, 'F', '0', '0', 'storage:config:add', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-24 11:09:40', NULL, '2025-12-24 11:09:40', '0', NULL);
INSERT INTO `sys_menu` VALUES (39, '配置修改', 36, 3, NULL, NULL, 'F', '0', '0', 'storage:config:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-24 11:09:40', NULL, '2025-12-24 11:09:40', '0', NULL);
INSERT INTO `sys_menu` VALUES (40, '配置删除', 36, 4, NULL, NULL, 'F', '0', '0', 'storage:config:delete', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-24 11:09:40', NULL, '2025-12-24 11:09:40', '0', NULL);
INSERT INTO `sys_menu` VALUES (41, '文件管理', 35, 2, '/storage/file', 'storage/file/index', 'C', '0', '0', 'storage:file:list', 'FolderOutlined', '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-24 11:09:41', NULL, '2025-12-24 11:09:41', '0', NULL);
INSERT INTO `sys_menu` VALUES (42, '文件查询', 41, 1, NULL, NULL, 'F', '0', '0', 'storage:file:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-24 11:09:41', NULL, '2025-12-24 11:09:41', '0', NULL);
INSERT INTO `sys_menu` VALUES (43, '文件上传', 41, 2, NULL, NULL, 'F', '0', '0', 'storage:file:upload', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-24 11:09:41', NULL, '2025-12-24 11:09:41', '0', NULL);
INSERT INTO `sys_menu` VALUES (44, '文件下载', 41, 3, NULL, NULL, 'F', '0', '0', 'storage:file:download', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-24 11:09:41', NULL, '2025-12-24 11:09:41', '0', NULL);
INSERT INTO `sys_menu` VALUES (45, '文件删除', 41, 4, NULL, NULL, 'F', '0', '0', 'storage:file:delete', NULL, '0', '0', NULL, '_blank', NULL, NULL, NULL, '2025-12-24 11:09:42', NULL, '2025-12-24 11:09:42', '0', NULL);
INSERT INTO `sys_menu` VALUES (55, '开发工具', 0, 90, '/tool', NULL, 'M', '0', '0', '', 'ToolOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', '2025-12-24 17:25:41', NULL, '2025-12-24 17:25:41', '0', NULL);
INSERT INTO `sys_menu` VALUES (56, '代码生成', 55, 1, 'gen', 'tool/gen/index', 'C', '0', '0', 'tool:gen:list', 'CodeOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', '2025-12-24 17:25:41', NULL, '2025-12-24 17:25:41', '0', NULL);
INSERT INTO `sys_menu` VALUES (57, '生成查询', 56, 1, NULL, NULL, 'F', '0', '0', 'tool:gen:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', '2025-12-24 17:25:41', NULL, '2025-12-24 17:25:41', '0', NULL);
INSERT INTO `sys_menu` VALUES (58, '生成修改', 56, 2, NULL, NULL, 'F', '0', '0', 'tool:gen:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', '2025-12-24 17:25:41', NULL, '2025-12-24 17:25:41', '0', NULL);
INSERT INTO `sys_menu` VALUES (59, '生成删除', 56, 3, NULL, NULL, 'F', '0', '0', 'tool:gen:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', '2025-12-24 17:25:41', NULL, '2025-12-24 17:25:41', '0', NULL);
INSERT INTO `sys_menu` VALUES (60, '导入表', 56, 4, NULL, NULL, 'F', '0', '0', 'tool:gen:import', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', '2025-12-24 17:25:41', NULL, '2025-12-24 17:25:41', '0', NULL);
INSERT INTO `sys_menu` VALUES (61, '预览代码', 56, 5, NULL, NULL, 'F', '0', '0', 'tool:gen:preview', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', '2025-12-24 17:25:41', NULL, '2025-12-24 17:25:41', '0', NULL);
INSERT INTO `sys_menu` VALUES (62, '生成代码', 56, 6, NULL, NULL, 'F', '0', '0', 'tool:gen:code', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', '2025-12-24 17:25:41', NULL, '2025-12-24 17:25:41', '0', NULL);
INSERT INTO `sys_menu` VALUES (63, '生成配置', 55, 2, 'genConfig', 'tool/genConfig/index', 'C', '0', '0', 'tool:gen:config', 'SettingOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', '2025-12-24 17:25:41', NULL, '2025-12-24 17:25:41', '0', NULL);
INSERT INTO `sys_menu` VALUES (64, '字典管理', 1, 6, 'dict', 'system/dict/index', 'C', '0', '0', 'system:dict:list', 'BookOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', '2025-12-24 20:55:04', NULL, '2025-12-24 20:55:04', '0', '字典管理菜单');
INSERT INTO `sys_menu` VALUES (65, '字典查询', 64, 1, '', '', 'F', '0', '0', 'system:dict:query', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', '2025-12-24 20:55:04', NULL, '2025-12-24 20:55:04', '0', '');
INSERT INTO `sys_menu` VALUES (66, '字典新增', 64, 2, '', '', 'F', '0', '0', 'system:dict:add', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', '2025-12-24 20:55:04', NULL, '2025-12-24 20:55:04', '0', '');
INSERT INTO `sys_menu` VALUES (67, '字典修改', 64, 3, '', '', 'F', '0', '0', 'system:dict:edit', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', '2025-12-24 20:55:04', NULL, '2025-12-24 20:55:04', '0', '');
INSERT INTO `sys_menu` VALUES (68, '字典删除', 64, 4, '', '', 'F', '0', '0', 'system:dict:remove', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', '2025-12-24 20:55:04', NULL, '2025-12-24 20:55:04', '0', '');

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `operation_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '操作日志ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `module_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模块名称',
  `operation_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作类型',
  `operation_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作描述',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求方式',
  `request_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求URL',
  `method_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '方法名称',
  `request_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '请求参数',
  `response_result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '响应结果',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '操作状态（0成功 1失败）',
  `error_msg` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '错误消息',
  `ip_addr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `operation_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作系统',
  `execution_time` bigint NULL DEFAULT NULL COMMENT '执行时长（毫秒）',
  `operation_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`operation_log_id`) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE,
  INDEX `idx_operation_time`(`operation_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES (7, 1, 'admin', '操作日志', 'DELETE', '清空操作日志', 'DELETE', '/api/monitor/operationLog/clean', 'com.tiny.system.controller.SysOperationLogController.clean()', '{}', '{\"code\":200,\"message\":\"操作成功\",\"timestamp\":1766543978977}', '0', NULL, '127.0.0.1', '内网IP', 'Edge 143.0.0.0', 'Windows 10', 627, '2025-12-24 10:39:39');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色标识',
  `sort` int NULL DEFAULT 0 COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '1' COMMENT '数据范围（1全部 2自定义 3本部门 4本部门及以下 5仅本人）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '删除标志（0正常 1删除）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `uk_role_key`(`role_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', '0', NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_role` VALUES (2, '普通用户', 'user', 2, '1', '0', NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_dept`(`role_id` ASC, `dept_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_menu`(`role_id` ASC, `menu_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 146 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1, 1);
INSERT INTO `sys_role_menu` VALUES (2, 1, 2);
INSERT INTO `sys_role_menu` VALUES (3, 1, 3);
INSERT INTO `sys_role_menu` VALUES (4, 1, 4);
INSERT INTO `sys_role_menu` VALUES (5, 1, 5);
INSERT INTO `sys_role_menu` VALUES (6, 1, 6);
INSERT INTO `sys_role_menu` VALUES (7, 1, 7);
INSERT INTO `sys_role_menu` VALUES (8, 1, 8);
INSERT INTO `sys_role_menu` VALUES (9, 1, 9);
INSERT INTO `sys_role_menu` VALUES (10, 1, 10);
INSERT INTO `sys_role_menu` VALUES (11, 1, 11);
INSERT INTO `sys_role_menu` VALUES (12, 1, 12);
INSERT INTO `sys_role_menu` VALUES (13, 1, 13);
INSERT INTO `sys_role_menu` VALUES (14, 1, 14);
INSERT INTO `sys_role_menu` VALUES (15, 1, 15);
INSERT INTO `sys_role_menu` VALUES (16, 1, 16);
INSERT INTO `sys_role_menu` VALUES (17, 1, 17);
INSERT INTO `sys_role_menu` VALUES (18, 1, 18);
INSERT INTO `sys_role_menu` VALUES (19, 1, 19);
INSERT INTO `sys_role_menu` VALUES (20, 1, 20);
INSERT INTO `sys_role_menu` VALUES (21, 1, 21);
INSERT INTO `sys_role_menu` VALUES (22, 1, 22);
INSERT INTO `sys_role_menu` VALUES (23, 1, 23);
INSERT INTO `sys_role_menu` VALUES (24, 1, 24);
INSERT INTO `sys_role_menu` VALUES (25, 1, 25);
INSERT INTO `sys_role_menu` VALUES (26, 1, 26);
INSERT INTO `sys_role_menu` VALUES (27, 1, 27);
INSERT INTO `sys_role_menu` VALUES (28, 1, 28);
INSERT INTO `sys_role_menu` VALUES (29, 1, 29);
INSERT INTO `sys_role_menu` VALUES (30, 1, 30);
INSERT INTO `sys_role_menu` VALUES (31, 1, 31);
INSERT INTO `sys_role_menu` VALUES (32, 1, 32);
INSERT INTO `sys_role_menu` VALUES (96, 1, 33);
INSERT INTO `sys_role_menu` VALUES (97, 1, 34);
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
INSERT INTO `sys_role_menu` VALUES (131, 1, 55);
INSERT INTO `sys_role_menu` VALUES (132, 1, 56);
INSERT INTO `sys_role_menu` VALUES (133, 1, 57);
INSERT INTO `sys_role_menu` VALUES (134, 1, 58);
INSERT INTO `sys_role_menu` VALUES (135, 1, 59);
INSERT INTO `sys_role_menu` VALUES (136, 1, 60);
INSERT INTO `sys_role_menu` VALUES (137, 1, 61);
INSERT INTO `sys_role_menu` VALUES (138, 1, 62);
INSERT INTO `sys_role_menu` VALUES (139, 1, 63);
INSERT INTO `sys_role_menu` VALUES (64, 2, 1);
INSERT INTO `sys_role_menu` VALUES (65, 2, 2);
INSERT INTO `sys_role_menu` VALUES (70, 2, 3);
INSERT INTO `sys_role_menu` VALUES (75, 2, 4);
INSERT INTO `sys_role_menu` VALUES (66, 2, 5);
INSERT INTO `sys_role_menu` VALUES (67, 2, 6);
INSERT INTO `sys_role_menu` VALUES (68, 2, 7);
INSERT INTO `sys_role_menu` VALUES (69, 2, 8);
INSERT INTO `sys_role_menu` VALUES (71, 2, 9);
INSERT INTO `sys_role_menu` VALUES (72, 2, 10);
INSERT INTO `sys_role_menu` VALUES (73, 2, 11);
INSERT INTO `sys_role_menu` VALUES (74, 2, 12);
INSERT INTO `sys_role_menu` VALUES (76, 2, 13);
INSERT INTO `sys_role_menu` VALUES (77, 2, 14);
INSERT INTO `sys_role_menu` VALUES (78, 2, 15);
INSERT INTO `sys_role_menu` VALUES (79, 2, 16);
INSERT INTO `sys_role_menu` VALUES (80, 2, 17);
INSERT INTO `sys_role_menu` VALUES (81, 2, 18);
INSERT INTO `sys_role_menu` VALUES (82, 2, 19);
INSERT INTO `sys_role_menu` VALUES (83, 2, 20);
INSERT INTO `sys_role_menu` VALUES (84, 2, 21);
INSERT INTO `sys_role_menu` VALUES (85, 2, 22);
INSERT INTO `sys_role_menu` VALUES (86, 2, 23);
INSERT INTO `sys_role_menu` VALUES (89, 2, 24);
INSERT INTO `sys_role_menu` VALUES (87, 2, 25);
INSERT INTO `sys_role_menu` VALUES (88, 2, 26);
INSERT INTO `sys_role_menu` VALUES (90, 2, 27);
INSERT INTO `sys_role_menu` VALUES (91, 2, 28);
INSERT INTO `sys_role_menu` VALUES (92, 2, 29);
INSERT INTO `sys_role_menu` VALUES (93, 2, 30);
INSERT INTO `sys_role_menu` VALUES (94, 2, 31);
INSERT INTO `sys_role_menu` VALUES (95, 2, 32);
INSERT INTO `sys_role_menu` VALUES (99, 2, 33);
INSERT INTO `sys_role_menu` VALUES (100, 2, 34);

-- ----------------------------
-- Table structure for sys_storage_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_storage_config`;
CREATE TABLE `sys_storage_config`  (
  `config_id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置名称',
  `storage_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '存储类型(local/aliyun_oss/minio/aws_s3)',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '是否默认(0否 1是)',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '状态(0正常 1停用)',
  `endpoint` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Endpoint',
  `bucket_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '存储桶名称',
  `access_key_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Access Key ID',
  `access_key_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Access Key Secret',
  `domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '自定义域名',
  `region` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '区域',
  `local_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '本地存储路径',
  `local_url_prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '本地存储URL前缀',
  `use_https` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '1' COMMENT '是否使用HTTPS(0否 1是)',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '删除标志(0正常 1删除)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE,
  INDEX `idx_storage_type`(`storage_type` ASC) USING BTREE,
  INDEX `idx_is_default`(`is_default` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '存储配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_storage_config
-- ----------------------------
INSERT INTO `sys_storage_config` VALUES (1, '本地存储', 'local', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, 'D:/upload/files', '/api/file', '0', '', '2025-12-24 11:09:39', NULL, '2025-12-24 14:26:41', '0', '默认本地存储配置');
INSERT INTO `sys_storage_config` VALUES (2, '腾讯云COS', 'tencent_cos', '1', '0', 'your-bucket.cos.ap-guangzhou.myqcloud.com', 'your-bucket', 'your-access-key', 'your-secret-key', NULL, 'ap-guangzhou', NULL, NULL, '0', NULL, '2025-12-24 14:26:35', NULL, '2025-12-24 14:26:42', '0', NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '2' COMMENT '性别（0男 1女 2未知）',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '删除标志（0正常 1删除）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `super_admin` tinyint NULL DEFAULT 0 COMMENT '是否超级管理员（0否 1是）',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2b$12$gis3AeExTCSgNeiYSBtwmOKyYBCrso5JP4P7Aexj/0t1fdB8z6Evy', '超级管理员', NULL, NULL, '2', NULL, 1, '0', NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:20:34', '0', NULL, 1);
INSERT INTO `sys_user` VALUES (2, '测试', '$2a$10$MWscjqJkoFMbBb95Jl3oJOv2wBnlcZ7oZ6hhtnrnIDhwuPZcbQuRm', '测试', '18359838539', 'test@tiny.com', '0', NULL, 2, '0', NULL, '2025-12-23 22:21:16', NULL, '2025-12-23 22:21:16', '0', NULL, 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_role`(`user_id` ASC, `role_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2, 2);

SET FOREIGN_KEY_CHECKS = 1;
