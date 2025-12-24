-- =============================================
-- 代码生成器模块 SQL 初始化脚本
-- 可单独执行，也已集成到 init.sql
-- =============================================

-- 代码生成器配置表
DROP TABLE IF EXISTS gen_config;
CREATE TABLE gen_config (
    config_id         BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID',
    config_key        VARCHAR(100) NOT NULL COMMENT '配置键',
    config_value      VARCHAR(500) DEFAULT '' COMMENT '配置值',
    config_desc       VARCHAR(200) DEFAULT '' COMMENT '配置描述',
    create_by         VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time       DATETIME COMMENT '创建时间',
    update_by         VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time       DATETIME COMMENT '更新时间',
    UNIQUE KEY uk_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代码生成器配置';

-- 初始化配置数据
INSERT INTO gen_config (config_key, config_value, config_desc, create_by, create_time) VALUES
('gen.author', 'admin', '代码作者', 'admin', NOW()),
('gen.packageName', 'com.tiny', '默认包路径', 'admin', NOW()),
('gen.tablePrefixes', 'sys_,gen_,biz_', '表前缀列表(逗号分隔)', 'admin', NOW()),
('gen.removePrefix', 'true', '是否去除表前缀', 'admin', NOW()),
('gen.backendPath', '', '后端生成路径(空则下载)', 'admin', NOW()),
('gen.frontendPath', '', '前端生成路径(空则下载)', 'admin', NOW());

-- 代码生成表配置
DROP TABLE IF EXISTS gen_table;
CREATE TABLE gen_table (
    table_id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '表ID',
    table_name        VARCHAR(200) NOT NULL COMMENT '表名',
    table_comment     VARCHAR(500) DEFAULT '' COMMENT '表描述',
    module_name       VARCHAR(100) DEFAULT '' COMMENT '模块名',
    package_name      VARCHAR(200) DEFAULT '' COMMENT '包路径',
    class_name        VARCHAR(100) DEFAULT '' COMMENT '实体类名',
    business_name     VARCHAR(100) DEFAULT '' COMMENT '业务名',
    function_name     VARCHAR(100) DEFAULT '' COMMENT '功能名称',
    author            VARCHAR(100) DEFAULT '' COMMENT '作者',
    fe_module_path    VARCHAR(200) DEFAULT '' COMMENT '前端模块路径',
    fe_generate_type  CHAR(1) DEFAULT '1' COMMENT '生成类型: 1单表 2主从',
    gen_type          CHAR(1) DEFAULT '0' COMMENT '生成方式: 0zip 1路径',
    gen_path          VARCHAR(500) DEFAULT '' COMMENT '生成路径',
    options           JSON COMMENT '扩展选项',
    create_by         VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time       DATETIME COMMENT '创建时间',
    update_by         VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time       DATETIME COMMENT '更新时间',
    del_flag          CHAR(1) DEFAULT '0' COMMENT '删除标志: 0正常 1删除',
    remark            VARCHAR(500) DEFAULT '' COMMENT '备注',
    UNIQUE KEY uk_table_name (table_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代码生成表配置';

-- 代码生成字段配置
DROP TABLE IF EXISTS gen_table_column;
CREATE TABLE gen_table_column (
    column_id         BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '列ID',
    table_id          BIGINT NOT NULL COMMENT '表ID',
    column_name       VARCHAR(200) NOT NULL COMMENT '列名',
    column_comment    VARCHAR(500) DEFAULT '' COMMENT '列描述',
    column_type       VARCHAR(100) NOT NULL COMMENT '数据库类型',
    java_type         VARCHAR(100) DEFAULT '' COMMENT 'Java类型',
    java_field        VARCHAR(200) DEFAULT '' COMMENT 'Java字段名',
    is_pk             CHAR(1) DEFAULT '0' COMMENT '是否主键: 0否 1是',
    is_increment      CHAR(1) DEFAULT '0' COMMENT '是否自增: 0否 1是',
    is_required       CHAR(1) DEFAULT '0' COMMENT '是否必填: 0否 1是',
    is_insert         CHAR(1) DEFAULT '1' COMMENT '是否新增字段: 0否 1是',
    is_edit           CHAR(1) DEFAULT '1' COMMENT '是否编辑字段: 0否 1是',
    is_list           CHAR(1) DEFAULT '1' COMMENT '是否列表显示: 0否 1是',
    is_query          CHAR(1) DEFAULT '0' COMMENT '是否查询条件: 0否 1是',
    is_detail         CHAR(1) DEFAULT '1' COMMENT '是否详情显示: 0否 1是',
    is_export         CHAR(1) DEFAULT '1' COMMENT '是否导出字段: 0否 1是',
    query_type        VARCHAR(50) DEFAULT 'EQ' COMMENT '查询方式: EQ NE GT GTE LT LTE LIKE LIKE_LEFT LIKE_RIGHT BETWEEN IN',
    html_type         VARCHAR(50) DEFAULT 'input' COMMENT '控件类型',
    dict_type         VARCHAR(200) DEFAULT '' COMMENT '字典类型',
    sort_order        INT DEFAULT 0 COMMENT '排序',
    KEY idx_table_id (table_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代码生成字段配置';

-- 类型映射配置
DROP TABLE IF EXISTS gen_type_mapping;
CREATE TABLE gen_type_mapping (
    mapping_id        BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '映射ID',
    db_type           VARCHAR(100) NOT NULL COMMENT '数据库类型(正则)',
    java_type         VARCHAR(100) NOT NULL COMMENT 'Java类型',
    java_import       VARCHAR(200) DEFAULT '' COMMENT '需要导入的包',
    default_html_type VARCHAR(50) DEFAULT 'input' COMMENT '默认控件类型',
    sort_order        INT DEFAULT 0 COMMENT '排序',
    UNIQUE KEY uk_db_type (db_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='类型映射配置';

-- 初始化类型映射数据
INSERT INTO gen_type_mapping (db_type, java_type, java_import, default_html_type, sort_order) VALUES
('bigint', 'Long', '', 'number', 1),
('int', 'Integer', '', 'number', 2),
('integer', 'Integer', '', 'number', 3),
('tinyint\\(1\\)', 'Boolean', '', 'radio', 4),
('tinyint', 'Integer', '', 'select', 5),
('smallint', 'Integer', '', 'number', 6),
('decimal', 'BigDecimal', 'java.math.BigDecimal', 'number', 7),
('numeric', 'BigDecimal', 'java.math.BigDecimal', 'number', 8),
('double', 'Double', '', 'number', 9),
('float', 'Float', '', 'number', 10),
('varchar', 'String', '', 'input', 11),
('char', 'String', '', 'input', 12),
('text', 'String', '', 'textarea', 13),
('longtext', 'String', '', 'textarea', 14),
('mediumtext', 'String', '', 'textarea', 15),
('datetime', 'LocalDateTime', 'java.time.LocalDateTime', 'datetime', 16),
('timestamp', 'LocalDateTime', 'java.time.LocalDateTime', 'datetime', 17),
('date', 'LocalDate', 'java.time.LocalDate', 'date', 18),
('time', 'LocalTime', 'java.time.LocalTime', 'input', 19),
('blob', 'byte[]', '', 'upload', 20),
('json', 'String', '', 'textarea', 21);
