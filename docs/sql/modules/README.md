# Tiny Platform SQL Modules

SQL脚本按模块拆分，支持按需安装和卸载。

## 模块列表

| 模块 | 目录 | 必须 | 说明 |
|------|------|------|------|
| 核心模块 | 00-core | 是 | 用户、角色、菜单、部门、字典、系统配置 |
| 监控日志 | 01-monitor | 否 | 登录日志、操作日志、在线用户 |
| 文件存储 | 02-storage | 否 | 存储配置、文件记录 |
| 代码生成 | 03-generator | 否 | 代码生成器配置、表配置、类型映射 |
| 通知公告 | 04-notice | 否 | 通知公告、已读记录 |
| 消息中心 | 05-message | 否 | 消息模板、消息记录、发送日志、邮件配置 |

## 目录结构

```
docs/sql/modules/
├── 00-core/
│   ├── schema.sql      # 表结构
│   └── data.sql        # 初始数据
├── 01-monitor/
│   ├── schema.sql
│   ├── data.sql
│   └── uninstall.sql   # 卸载脚本
├── 02-storage/
│   ├── schema.sql
│   ├── data.sql
│   └── uninstall.sql
├── 03-generator/
│   ├── schema.sql
│   ├── data.sql
│   └── uninstall.sql
├── 04-notice/
│   ├── schema.sql
│   ├── data.sql
│   └── uninstall.sql
├── 05-message/
│   ├── schema.sql
│   ├── data.sql
│   └── uninstall.sql
├── install-all.sql     # 全量安装脚本
└── README.md
```

## 安装顺序

安装时必须按以下顺序执行（依赖关系）：

1. **00-core** (必须首先安装)
2. 01-monitor (可选)
3. 02-storage (可选)
4. 03-generator (可选)
5. 04-notice (可选)
6. 05-message (可选，建议最后安装)

### 安装单个模块

```bash
# 1. 先执行表结构
mysql -u root -p your_database < docs/sql/modules/01-monitor/schema.sql

# 2. 再执行初始数据
mysql -u root -p your_database < docs/sql/modules/01-monitor/data.sql
```

### 全量安装

```bash
mysql -u root -p your_database < docs/sql/modules/install-all.sql
```

## 卸载顺序

卸载时必须按以下**逆序**执行：

1. 05-message (先卸载)
2. 04-notice
3. 03-generator
4. 02-storage
5. 01-monitor
6. 00-core (不可卸载)

### 卸载模块

```bash
mysql -u root -p your_database < docs/sql/modules/05-message/uninstall.sql
```

## 后端模块卸载

如需完全卸载后端模块，除了执行SQL卸载脚本外，还需：

### 卸载 tiny-message 模块

1. 修改 `pom.xml`: 从 modules 中移除 tiny-message
2. 修改 `tiny-admin/pom.xml`: 删除 tiny-message 依赖
3. 修改 `tiny-system/pom.xml`: 删除 tiny-message 依赖
4. 处理 UserProvider 接口依赖
5. 删除 `tiny-message/` 目录

### 卸载 tiny-websocket 模块

1. 修改 `pom.xml`: 从 modules 中移除 tiny-websocket
2. 修改 `tiny-admin/pom.xml`: 删除 tiny-websocket 依赖
3. 删除 `tiny-websocket/` 目录

### 卸载 tiny-storage 模块

1. 修改 `pom.xml`: 从 modules 中移除 tiny-storage
2. 修改 `tiny-admin/pom.xml`: 删除 tiny-storage 依赖
3. 删除 `tiny-storage/` 目录

### 卸载 tiny-generator 模块

1. 修改 `pom.xml`: 从 modules 中移除 tiny-generator
2. 修改 `tiny-admin/pom.xml`: 删除 tiny-generator 依赖
3. 删除 `tiny-generator/` 目录

## 前端资源

卸载后端模块后，需要同步清理前端资源：

| 模块 | 路由路径 | 页面目录 |
|------|---------|---------|
| 消息中心 | /message/* | src/pages/message/ |
| 通知公告 | /info/* | src/pages/info/ |
| 代码生成 | /tool/* | src/pages/tool/ |
| 文件存储 | /storage/* | src/pages/storage/ |
| 监控日志 | /monitor/* | src/pages/monitor/ |

清理步骤：
1. 修改 `tiny-ui/config/routes.ts` 移除对应路由
2. 删除对应的页面目录
3. 删除对应的service文件

## 注意事项

1. 核心模块(00-core)不可卸载，是系统运行的基础
2. 卸载模块前请确保没有其他模块依赖该模块
3. 卸载SQL脚本会删除相关表和数据，请提前备份
4. 菜单ID使用固定值，避免不同环境ID不一致
5. 字典ID使用固定值，确保引用正确
