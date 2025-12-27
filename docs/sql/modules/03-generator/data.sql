-- ============================================
-- Tiny Platform Generator Module - Init Data
-- Version: 1.0.0
-- Description: Generator module menus, dictionaries and config (optional)
-- ============================================

SET NAMES utf8mb4;

-- ----------------------------
-- Dict types (generator)
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (15, '表单控件类型', 'gen_html_type', '0', 'admin', NOW(), '', NULL, '0', '代码生成表单控件类型');
INSERT INTO `sys_dict_type` VALUES (16, '查询条件类型', 'gen_query_type', '0', 'admin', NOW(), '', NULL, '0', '代码生成查询条件类型');
INSERT INTO `sys_dict_type` VALUES (17, 'Java数据类型', 'gen_java_type', '0', 'admin', NOW(), '', NULL, '0', '代码生成Java数据类型');
INSERT INTO `sys_dict_type` VALUES (18, '生成方式', 'gen_type', '0', 'admin', NOW(), '', NULL, '0', '代码生成输出方式');
INSERT INTO `sys_dict_type` VALUES (19, '前端表单类型', 'gen_fe_type', '0', 'admin', NOW(), '', NULL, '0', '代码生成前端表单类型');

-- ----------------------------
-- Dict items (generator)
-- ----------------------------
-- HTML type
INSERT INTO `sys_dict_item` VALUES (44, 'gen_html_type', '文本框', 'input', 1, NULL, NULL, 'Y', '0', 'admin', NOW(), '', NULL, '0', '单行文本输入');
INSERT INTO `sys_dict_item` VALUES (45, 'gen_html_type', '文本域', 'textarea', 2, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '多行文本输入');
INSERT INTO `sys_dict_item` VALUES (46, 'gen_html_type', '下拉框', 'select', 3, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '下拉选择器');
INSERT INTO `sys_dict_item` VALUES (47, 'gen_html_type', '单选框', 'radio', 4, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '单选按钮组');
INSERT INTO `sys_dict_item` VALUES (48, 'gen_html_type', '复选框', 'checkbox', 5, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '复选框');
INSERT INTO `sys_dict_item` VALUES (49, 'gen_html_type', '日期时间', 'datetime', 6, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '日期时间选择器');
INSERT INTO `sys_dict_item` VALUES (50, 'gen_html_type', '日期', 'date', 7, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '日期选择器');
INSERT INTO `sys_dict_item` VALUES (51, 'gen_html_type', '数字输入', 'number', 8, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '数字输入框');
INSERT INTO `sys_dict_item` VALUES (52, 'gen_html_type', '图片上传', 'imageUpload', 9, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '图片上传组件');
INSERT INTO `sys_dict_item` VALUES (53, 'gen_html_type', '文件上传', 'fileUpload', 10, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '文件上传组件');
INSERT INTO `sys_dict_item` VALUES (54, 'gen_html_type', '富文本', 'editor', 11, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '富文本编辑器');
INSERT INTO `sys_dict_item` VALUES (55, 'gen_html_type', '树形选择', 'treeSelect', 12, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '树形选择器');

-- Query type
INSERT INTO `sys_dict_item` VALUES (56, 'gen_query_type', '等于', 'EQ', 1, NULL, NULL, 'Y', '0', 'admin', NOW(), '', NULL, '0', '精确匹配 =');
INSERT INTO `sys_dict_item` VALUES (57, 'gen_query_type', '不等于', 'NE', 2, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '不等于 !=');
INSERT INTO `sys_dict_item` VALUES (58, 'gen_query_type', '大于', 'GT', 3, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '大于 >');
INSERT INTO `sys_dict_item` VALUES (59, 'gen_query_type', '大于等于', 'GE', 4, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '大于等于 >=');
INSERT INTO `sys_dict_item` VALUES (60, 'gen_query_type', '小于', 'LT', 5, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '小于 <');
INSERT INTO `sys_dict_item` VALUES (61, 'gen_query_type', '小于等于', 'LE', 6, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '小于等于 <=');
INSERT INTO `sys_dict_item` VALUES (62, 'gen_query_type', '模糊', 'LIKE', 7, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '模糊匹配 LIKE');
INSERT INTO `sys_dict_item` VALUES (63, 'gen_query_type', '左模糊', 'LIKE_LEFT', 8, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '左模糊 LIKE %value');
INSERT INTO `sys_dict_item` VALUES (64, 'gen_query_type', '右模糊', 'LIKE_RIGHT', 9, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '右模糊 LIKE value%');
INSERT INTO `sys_dict_item` VALUES (65, 'gen_query_type', '范围', 'BETWEEN', 10, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '范围查询 BETWEEN');
INSERT INTO `sys_dict_item` VALUES (66, 'gen_query_type', '包含', 'IN', 11, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '包含查询 IN');

-- Java type
INSERT INTO `sys_dict_item` VALUES (67, 'gen_java_type', 'Long', 'Long', 1, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '长整型');
INSERT INTO `sys_dict_item` VALUES (68, 'gen_java_type', 'String', 'String', 2, NULL, NULL, 'Y', '0', 'admin', NOW(), '', NULL, '0', '字符串');
INSERT INTO `sys_dict_item` VALUES (69, 'gen_java_type', 'Integer', 'Integer', 3, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '整型');
INSERT INTO `sys_dict_item` VALUES (70, 'gen_java_type', 'Double', 'Double', 4, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '双精度浮点');
INSERT INTO `sys_dict_item` VALUES (71, 'gen_java_type', 'BigDecimal', 'BigDecimal', 5, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '高精度数值');
INSERT INTO `sys_dict_item` VALUES (72, 'gen_java_type', 'LocalDateTime', 'LocalDateTime', 6, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '日期时间');
INSERT INTO `sys_dict_item` VALUES (73, 'gen_java_type', 'LocalDate', 'LocalDate', 7, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '日期');
INSERT INTO `sys_dict_item` VALUES (74, 'gen_java_type', 'Boolean', 'Boolean', 8, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '布尔值');

-- Gen type
INSERT INTO `sys_dict_item` VALUES (75, 'gen_type', 'ZIP下载', '0', 1, NULL, NULL, 'Y', '0', 'admin', NOW(), '', NULL, '0', '生成ZIP包下载');
INSERT INTO `sys_dict_item` VALUES (76, 'gen_type', '指定路径', '1', 2, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '生成到指定目录');

-- Frontend type
INSERT INTO `sys_dict_item` VALUES (77, 'gen_fe_type', '单表', '1', 1, NULL, NULL, 'Y', '0', 'admin', NOW(), '', NULL, '0', '单表CRUD');
INSERT INTO `sys_dict_item` VALUES (78, 'gen_fe_type', '主从表', '2', 2, NULL, NULL, 'N', '0', 'admin', NOW(), '', NULL, '0', '主从表结构');

-- ----------------------------
-- Menus (generator)
-- ----------------------------
INSERT INTO `sys_menu` VALUES (55, '开发工具', NULL, 0, 90, '/tool', NULL, 'M', '0', '0', '', 'ToolOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (56, '代码生成', NULL, 55, 1, 'gen', 'tool/gen/index', 'C', '0', '0', 'tool:gen:list', 'CodeOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (57, '生成查询', NULL, 56, 1, NULL, NULL, 'F', '0', '0', 'tool:gen:query', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (58, '生成修改', NULL, 56, 2, NULL, NULL, 'F', '0', '0', 'tool:gen:edit', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (59, '生成删除', NULL, 56, 3, NULL, NULL, 'F', '0', '0', 'tool:gen:remove', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (60, '导入表', NULL, 56, 4, NULL, NULL, 'F', '0', '0', 'tool:gen:import', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (61, '预览代码', NULL, 56, 5, NULL, NULL, 'F', '0', '0', 'tool:gen:preview', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (62, '生成代码', NULL, 56, 6, NULL, NULL, 'F', '0', '0', 'tool:gen:code', NULL, '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);
INSERT INTO `sys_menu` VALUES (63, '生成配置', NULL, 55, 2, 'genConfig', 'tool/genConfig/index', 'C', '0', '0', 'tool:gen:config', 'SettingOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', NULL);

-- ----------------------------
-- Role-Menu relations (generator - admin role)
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (131, 1, 55);
INSERT INTO `sys_role_menu` VALUES (132, 1, 56);
INSERT INTO `sys_role_menu` VALUES (133, 1, 57);
INSERT INTO `sys_role_menu` VALUES (134, 1, 58);
INSERT INTO `sys_role_menu` VALUES (135, 1, 59);
INSERT INTO `sys_role_menu` VALUES (136, 1, 60);
INSERT INTO `sys_role_menu` VALUES (137, 1, 61);
INSERT INTO `sys_role_menu` VALUES (138, 1, 62);
INSERT INTO `sys_role_menu` VALUES (139, 1, 63);

-- ----------------------------
-- Generator config
-- ----------------------------
INSERT INTO `gen_config` VALUES (1, 'gen.author', 'author', '代码作者', 'admin', NOW(), '', NULL);
INSERT INTO `gen_config` VALUES (2, 'gen.packageName', 'com.tiny', '默认包路径', 'admin', NOW(), '', NULL);
INSERT INTO `gen_config` VALUES (3, 'gen.tablePrefixes', 'sys_,gen_,biz_', '表前缀列表(逗号分隔)', 'admin', NOW(), '', NULL);
INSERT INTO `gen_config` VALUES (4, 'gen.removePrefix', 'false', '是否去除表前缀', 'admin', NOW(), '', NULL);
INSERT INTO `gen_config` VALUES (5, 'gen.backendPath', '', '后端生成路径(空则下载)', 'admin', NOW(), '', NULL);
INSERT INTO `gen_config` VALUES (6, 'gen.frontendPath', '', '前端生成路径(空则下载)', 'admin', NOW(), '', NULL);

-- ----------------------------
-- Type mapping
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
