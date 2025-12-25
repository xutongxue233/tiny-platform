/*
 Tiny Platform 数据初始化脚本
 仅包含初始化数据的INSERT语句
*/

SET NAMES utf8mb4;

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
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, '总公司', 0, '0', 0, '管理员', '13800000000', 'admin@company.com', '0', NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_dept` VALUES (2, '研发部', 1, '0,1', 1, '', '', '', '0', NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_dept` VALUES (3, '市场部', 1, '0,1', 2, '', '', '', '0', NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_dept` VALUES (4, '财务部', 1, '0,1', 3, '', '', '', '0', NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_dept` VALUES (5, '人事部', 1, '0,1', 4, '', '', '', '0', NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);

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
-- Records of sys_file_record
-- ----------------------------
INSERT INTO `sys_file_record` VALUES (1, 1, 'config.yaml', '85c51fa55c6e4cc6814ee0aee4161f9f.yaml', '2025/12/24/7d20a355f8c143b78695b45d7fea3ef7.yaml', 481810, 'application/octet-stream', 'yaml', 'local', '/api/static/2025/12/24/7d20a355f8c143b78695b45d7fea3ef7.yaml', '9e39f926f40176da68b0001e1cfdb018', NULL, '2025-12-24 11:38:47', NULL, '2025-12-24 12:12:40', '1', NULL);
INSERT INTO `sys_file_record` VALUES (2, 1, 'portable.ini', 'd07b0ace64bb4ee19a76c37ae4256361.ini', '2025/12/24/dc866d253ed044aeb955121b762baaf1.ini', 50, 'application/octet-stream', 'ini', 'local', '/api/file/2025/12/24/dc866d253ed044aeb955121b762baaf1.ini', 'c0eb619af3937dfcf2986fd97c5b3116', NULL, '2025-12-24 12:12:08', NULL, '2025-12-24 12:12:21', '1', NULL);
INSERT INTO `sys_file_record` VALUES (3, 2, 'config.yaml', 'cdb2d634e0b94564b037d2eceb67abf4.yaml', '2025/12/24/13e31667157a4dfba4d8e1f70f41c078.yaml', 481810, 'application/octet-stream', 'yaml', 'tencent_cos', 'http://tiny-1252430636.cos.ap-guangzhou.myqcloud.com/2025/12/24/13e31667157a4dfba4d8e1f70f41c078.yaml', '9e39f926f40176da68b0001e1cfdb018', NULL, '2025-12-24 15:04:46', NULL, '2025-12-24 15:05:28', '1', NULL);

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
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES (7, 1, 'admin', '操作日志', 'DELETE', '清空操作日志', 'DELETE', '/api/monitor/operationLog/clean', 'com.tiny.system.controller.SysOperationLogController.clean()', '{}', '{\"code\":200,\"message\":\"操作成功\",\"timestamp\":1766543978977}', '0', NULL, '127.0.0.1', '内网IP', 'Edge 143.0.0.0', 'Windows 10', 627, '2025-12-24 10:39:39');

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', '0', NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);
INSERT INTO `sys_role` VALUES (2, '普通用户', 'user', 2, '1', '0', NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:19:34', '0', NULL);

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
-- Records of sys_storage_config
-- ----------------------------
INSERT INTO `sys_storage_config` VALUES (1, '本地存储', 'local', '0', '0', NULL, NULL, NULL, NULL, NULL, NULL, 'D:/upload/files', '/api/file', '0', '', '2025-12-24 11:09:39', NULL, '2025-12-24 14:26:41', '0', '默认本地存储配置');
INSERT INTO `sys_storage_config` VALUES (2, '腾讯云COS', 'tencent_cos', '1', '0', 'your-bucket.cos.ap-guangzhou.myqcloud.com', 'your-bucket', 'your-access-key', 'your-secret-key', NULL, 'ap-guangzhou', NULL, NULL, '0', NULL, '2025-12-24 14:26:35', NULL, '2025-12-24 14:26:42', '0', NULL);

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2b$12$gis3AeExTCSgNeiYSBtwmOKyYBCrso5JP4P7Aexj/0t1fdB8z6Evy', '超级管理员', NULL, NULL, '2', NULL, 1, '0', NULL, '2025-12-23 22:19:34', NULL, '2025-12-23 22:20:34', '0', NULL, 1);
INSERT INTO `sys_user` VALUES (2, '测试', '$2a$10$MWscjqJkoFMbBb95Jl3oJOv2wBnlcZ7oZ6hhtnrnIDhwuPZcbQuRm', '测试', '18359838539', 'test@tiny.com', '0', NULL, 2, '0', NULL, '2025-12-23 22:21:16', NULL, '2025-12-23 22:21:16', '0', NULL, 0);

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2, 2);

-- ----------------------------
-- 初始配置数据
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '系统名称', 'sys.app.name', 'Tiny Platform', 'STRING', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '系统应用名称');
INSERT INTO `sys_config` VALUES (2, '系统版本', 'sys.app.version', '1.0.0', 'STRING', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '系统版本号');
INSERT INTO `sys_config` VALUES (3, '用户初始密码', 'sys.user.initPassword', '123456', 'STRING', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '新用户初始密码');
INSERT INTO `sys_config` VALUES (4, '账号自助注册', 'sys.account.registerEnabled', 'false', 'BOOLEAN', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '是否开启账号自助注册');
INSERT INTO `sys_config` VALUES (5, '验证码开关', 'sys.captcha.enabled', 'true', 'BOOLEAN', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '是否开启验证码');
INSERT INTO `sys_config` VALUES (6, '文件上传大小限制', 'sys.upload.maxSize', '10', 'NUMBER', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '文件上传大小限制(MB)');
INSERT INTO `sys_config` VALUES (7, '最大登录失败次数', 'sys.auth.maxFailCount', '5', 'NUMBER', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '连续登录失败达到此次数后锁定账号');
INSERT INTO `sys_config` VALUES (8, '失败计数有效期', 'sys.auth.failCountExpireMinutes', '10', 'NUMBER', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '登录失败计数有效期(分钟)');
INSERT INTO `sys_config` VALUES (9, '账号锁定时长', 'sys.auth.lockMinutes', '15', 'NUMBER', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '账号锁定时长(分钟)');
INSERT INTO `sys_config` VALUES (10, '密码最小长度', 'sys.password.minLength', '6', 'NUMBER', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '用户密码最小长度');
INSERT INTO `sys_config` VALUES (11, '密码最大长度', 'sys.password.maxLength', '20', 'NUMBER', 'SYSTEM', 'Y', '0', 'admin', NOW(), '', NULL, '0', '用户密码最大长度');

-- ----------------------------
-- 参数配置菜单
-- ----------------------------
INSERT INTO `sys_menu` VALUES (70, '参数配置', 1, 7, 'config', 'system/config/index', 'C', '0', '0', 'system:config:list', 'ControlOutlined', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '参数配置菜单');
INSERT INTO `sys_menu` VALUES (71, '参数查询', 70, 1, '', '', 'F', '0', '0', 'system:config:query', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '');
INSERT INTO `sys_menu` VALUES (72, '参数新增', 70, 2, '', '', 'F', '0', '0', 'system:config:add', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '');
INSERT INTO `sys_menu` VALUES (73, '参数修改', 70, 3, '', '', 'F', '0', '0', 'system:config:edit', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '');
INSERT INTO `sys_menu` VALUES (74, '参数删除', 70, 4, '', '', 'F', '0', '0', 'system:config:remove', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '');
INSERT INTO `sys_menu` VALUES (75, '刷新缓存', 70, 5, '', '', 'F', '0', '0', 'system:config:refresh', '', '0', '0', NULL, '_blank', NULL, NULL, 'admin', NOW(), NULL, NOW(), '0', '');

-- ----------------------------
-- 为超级管理员角色分配权限
-- ----------------------------
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 70);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 71);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 72);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 73);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 74);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1, 75);

-- ----------------------------
-- 参数分组字典
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (20, '参数分组', 'sys_config_group', '0', 'admin', NOW(), '', NULL, '0', '系统参数分组');
INSERT INTO `sys_dict_item` VALUES (80, 'sys_config_group', '系统类', 'SYSTEM', 1, NULL, 'blue', 'Y', '0', 'admin', NOW(), '', NULL, '0', '系统级参数');
INSERT INTO `sys_dict_item` VALUES (81, 'sys_config_group', '业务类', 'BUSINESS', 2, NULL, 'green', 'N', '0', 'admin', NOW(), '', NULL, '0', '业务级参数');

-- ----------------------------
-- 参数类型字典
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (21, '参数类型', 'sys_config_type', '0', 'admin', NOW(), '', NULL, '0', '参数值类型');
INSERT INTO `sys_dict_item` VALUES (82, 'sys_config_type', '字符串', 'STRING', 1, NULL, 'default', 'Y', '0', 'admin', NOW(), '', NULL, '0', '字符串类型');
INSERT INTO `sys_dict_item` VALUES (83, 'sys_config_type', '数字', 'NUMBER', 2, NULL, 'blue', 'N', '0', 'admin', NOW(), '', NULL, '0', '数字类型');
INSERT INTO `sys_dict_item` VALUES (84, 'sys_config_type', '布尔值', 'BOOLEAN', 3, NULL, 'green', 'N', '0', 'admin', NOW(), '', NULL, '0', '布尔类型');
INSERT INTO `sys_dict_item` VALUES (85, 'sys_config_type', 'JSON', 'JSON', 4, NULL, 'orange', 'N', '0', 'admin', NOW(), '', NULL, '0', 'JSON类型');
