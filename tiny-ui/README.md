# Tiny Platform Frontend

Tiny Platform 前端项目，基于 React 19 + Ant Design 5 + UmiJS Max 构建。

## 技术栈

| 技术 | 版本 | 说明 |
|-----|------|-----|
| React | 19 | UI 框架 |
| Ant Design | 5 | UI 组件库 |
| Ant Design Pro | - | 企业级 UI 解决方案 |
| UmiJS Max | 4.3.24 | React 应用框架 |
| TypeScript | 5.6.3 | 类型安全的 JavaScript |
| TailwindCSS | 3 | CSS 框架 |

## 环境要求

- Node.js 20+
- npm 或 pnpm

## 快速开始

### 安装依赖

```bash
npm install
```

### 开发模式

```bash
# 使用 Mock 数据
npm run dev

# 连接真实后端
npm run start:dev
```

访问地址：http://localhost:8000

### 生产构建

```bash
npm run build
```

### 代码检查

```bash
# ESLint 检查
npm run lint

# ESLint 自动修复
npm run lint:fix

# TypeScript 类型检查
npm run tsc
```

### 运行测试

```bash
npm test
```

## 项目结构

```
tiny-ui/
├── config/              # 配置文件
│   ├── config.ts        # UmiJS 配置
│   ├── routes.ts        # 路由配置
│   └── proxy.ts         # 代理配置
├── mock/                # Mock 数据
├── public/              # 静态资源
├── src/
│   ├── components/      # 公共组件
│   ├── hooks/           # 自定义 Hooks
│   ├── locales/         # 国际化文件
│   ├── pages/           # 页面组件
│   │   ├── system/      # 系统管理（用户、角色、菜单、部门、字典）
│   │   ├── monitor/     # 监控管理（登录日志、操作日志、在线用户）
│   │   ├── storage/     # 存储管理（文件管理）
│   │   └── tool/        # 开发工具（代码生成器）
│   ├── services/        # API 服务
│   ├── utils/           # 工具函数
│   ├── access.ts        # 权限控制
│   ├── app.tsx          # 应用入口
│   └── global.less      # 全局样式
├── tests/               # 测试文件
├── types/               # TypeScript 类型定义
├── package.json
├── tailwind.config.js   # TailwindCSS 配置
└── tsconfig.json        # TypeScript 配置
```

## 功能模块

### 系统管理
- 用户管理 - 用户增删改查、状态管理、密码重置
- 角色管理 - 角色分配、权限配置
- 菜单管理 - 树形结构、路由配置、按钮权限
- 部门管理 - 树形结构、数据权限
- 数据字典 - 字典类型与字典项管理

### 监控管理
- 登录日志 - 登录/登出记录
- 操作日志 - 操作记录查询
- 在线用户 - 在线用户管理

### 存储管理
- 文件管理 - 文件上传下载

### 开发工具
- 代码生成器 - 表导入、代码预览、批量生成
- 生成器配置 - 模板配置管理

## 默认账号

- 用户名：`admin`
- 密码：`admin123`

## 相关文档

- [Ant Design](https://ant.design/docs/react/introduce-cn)
- [Ant Design Pro](https://pro.ant.design/zh-CN/docs/overview)
- [UmiJS](https://umijs.org/docs/guides/getting-started)
- [TailwindCSS](https://tailwindcss.com/docs)
