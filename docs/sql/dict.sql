-- ----------------------------
-- 字典类型表
-- ----------------------------
DROP TABLE IF EXISTS sys_dict_type;
CREATE TABLE sys_dict_type (
    dict_id         BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '字典主键',
    dict_name       VARCHAR(100)    DEFAULT ''                 COMMENT '字典名称',
    dict_code       VARCHAR(100)    DEFAULT ''                 COMMENT '字典编码',
    status          CHAR(1)         DEFAULT '0'                COMMENT '状态（0正常 1停用）',
    create_by       VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
    create_time     DATETIME                                   COMMENT '创建时间',
    update_by       VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
    update_time     DATETIME                                   COMMENT '更新时间',
    del_flag        CHAR(1)         DEFAULT '0'                COMMENT '删除标志（0正常 1删除）',
    remark          VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
    PRIMARY KEY (dict_id),
    UNIQUE KEY uk_dict_code (dict_code)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='字典类型表';

-- ----------------------------
-- 字典数据表
-- ----------------------------
DROP TABLE IF EXISTS sys_dict_item;
CREATE TABLE sys_dict_item (
    item_id         BIGINT(20)      NOT NULL AUTO_INCREMENT    COMMENT '字典项主键',
    dict_code       VARCHAR(100)    DEFAULT ''                 COMMENT '字典编码',
    item_label      VARCHAR(100)    DEFAULT ''                 COMMENT '字典项标签',
    item_value      VARCHAR(100)    DEFAULT ''                 COMMENT '字典项值',
    item_sort       INT(4)          DEFAULT 0                  COMMENT '字典项排序',
    css_class       VARCHAR(100)    DEFAULT NULL               COMMENT '样式属性',
    list_class      VARCHAR(100)    DEFAULT NULL               COMMENT '表格回显样式',
    is_default      CHAR(1)         DEFAULT 'N'                COMMENT '是否默认（Y是 N否）',
    status          CHAR(1)         DEFAULT '0'                COMMENT '状态（0正常 1停用）',
    create_by       VARCHAR(64)     DEFAULT ''                 COMMENT '创建者',
    create_time     DATETIME                                   COMMENT '创建时间',
    update_by       VARCHAR(64)     DEFAULT ''                 COMMENT '更新者',
    update_time     DATETIME                                   COMMENT '更新时间',
    del_flag        CHAR(1)         DEFAULT '0'                COMMENT '删除标志（0正常 1删除）',
    remark          VARCHAR(500)    DEFAULT NULL               COMMENT '备注',
    PRIMARY KEY (item_id),
    KEY idx_dict_code (dict_code)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='字典数据表';

-- ----------------------------
-- 初始化数据
-- ----------------------------
INSERT INTO sys_dict_type (dict_name, dict_code, status, create_by, create_time, remark) VALUES
('用户性别', 'sys_user_gender', '0', 'admin', NOW(), '用户性别列表'),
('系统状态', 'sys_common_status', '0', 'admin', NOW(), '系统通用状态'),
('是否', 'sys_yes_no', '0', 'admin', NOW(), '是否选择');

INSERT INTO sys_dict_item (dict_code, item_label, item_value, item_sort, is_default, status, create_by, create_time, remark) VALUES
('sys_user_gender', '男', '0', 1, 'Y', '0', 'admin', NOW(), '性别男'),
('sys_user_gender', '女', '1', 2, 'N', '0', 'admin', NOW(), '性别女'),
('sys_user_gender', '未知', '2', 3, 'N', '0', 'admin', NOW(), '性别未知'),
('sys_common_status', '正常', '0', 1, 'Y', '0', 'admin', NOW(), '正常状态'),
('sys_common_status', '停用', '1', 2, 'N', '0', 'admin', NOW(), '停用状态'),
('sys_yes_no', '是', 'Y', 1, 'Y', '0', 'admin', NOW(), '是'),
('sys_yes_no', '否', 'N', 2, 'N', '0', 'admin', NOW(), '否');

-- ----------------------------
-- 添加字典管理菜单
-- ----------------------------
INSERT INTO sys_menu (menu_name, parent_id, sort, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark) VALUES
('字典管理', 1, 6, 'dict', 'system/dict/index', 'C', '0', '0', 'system:dict:list', 'BookOutlined', 'admin', NOW(), '字典管理菜单');

SET @dictMenuId = LAST_INSERT_ID();

INSERT INTO sys_menu (menu_name, parent_id, sort, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark) VALUES
('字典查询', @dictMenuId, 1, '', '', 'F', '0', '0', 'system:dict:query', '', 'admin', NOW(), ''),
('字典新增', @dictMenuId, 2, '', '', 'F', '0', '0', 'system:dict:add', '', 'admin', NOW(), ''),
('字典修改', @dictMenuId, 3, '', '', 'F', '0', '0', 'system:dict:edit', '', 'admin', NOW(), ''),
('字典删除', @dictMenuId, 4, '', '', 'F', '0', '0', 'system:dict:remove', '', 'admin', NOW(), '');