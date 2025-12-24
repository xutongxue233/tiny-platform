-- =============================================
-- 代码生成器菜单 SQL
-- 需要在 generator.sql 执行后单独执行
-- =============================================

-- 代码生成菜单(一级目录)
INSERT INTO sys_menu (menu_name, parent_id, sort, path, component, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
('开发工具', 0, 90, '/tool', NULL, 'M', '0', '0', '', 'ToolOutlined', 'admin', NOW());

SET @toolMenuId = LAST_INSERT_ID();

-- 代码生成(二级菜单)
INSERT INTO sys_menu (menu_name, parent_id, sort, path, component, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
('代码生成', @toolMenuId, 1, 'gen', 'tool/gen/index', 'C', '0', '0', 'tool:gen:list', 'CodeOutlined', 'admin', NOW());

SET @genMenuId = LAST_INSERT_ID();

-- 代码生成按钮权限
INSERT INTO sys_menu (menu_name, parent_id, sort, path, component, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
('生成查询', @genMenuId, 1, NULL, NULL, 'F', '0', '0', 'tool:gen:query', NULL, 'admin', NOW()),
('生成修改', @genMenuId, 2, NULL, NULL, 'F', '0', '0', 'tool:gen:edit', NULL, 'admin', NOW()),
('生成删除', @genMenuId, 3, NULL, NULL, 'F', '0', '0', 'tool:gen:remove', NULL, 'admin', NOW()),
('导入表', @genMenuId, 4, NULL, NULL, 'F', '0', '0', 'tool:gen:import', NULL, 'admin', NOW()),
('预览代码', @genMenuId, 5, NULL, NULL, 'F', '0', '0', 'tool:gen:preview', NULL, 'admin', NOW()),
('生成代码', @genMenuId, 6, NULL, NULL, 'F', '0', '0', 'tool:gen:code', NULL, 'admin', NOW());

-- 生成器配置(二级菜单)
INSERT INTO sys_menu (menu_name, parent_id, sort, path, component, menu_type, visible, status, perms, icon, create_by, create_time) VALUES
('生成配置', @toolMenuId, 2, 'genConfig', 'tool/genConfig/index', 'C', '0', '0', 'tool:gen:config', 'SettingOutlined', 'admin', NOW());

-- 关联菜单权限到超级管理员角色
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE menu_id >= @toolMenuId;
