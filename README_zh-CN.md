<h1 align="center">Tiny Platform</h1>

<p align="center">
  <strong>模块化中后台管理系统</strong>
</p>

<p align="center">
  <a href="https://github.com/xutongxue233/tiny-platform/blob/main/LICENSE">
    <img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="License">
  </a>
  <a href="https://www.oracle.com/java/technologies/downloads/#java21">
    <img src="https://img.shields.io/badge/JDK-21-green.svg" alt="JDK Version">
  </a>
  <a href="https://spring.io/projects/spring-boot">
    <img src="https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg" alt="Spring Boot">
  </a>
  <a href="https://react.dev/">
    <img src="https://img.shields.io/badge/React-19-blue.svg" alt="React">
  </a>
  <a href="https://ant.design/">
    <img src="https://img.shields.io/badge/Ant%20Design-5-blue.svg" alt="Ant Design">
  </a>
</p>

<p align="center">
  简体中文 | <a href="./README.md">English</a>
</p>

---

## 项目介绍

Tiny Platform 是一个基于 **Spring Boot 3 + MyBatis Plus + Sa-Token + React + Ant Design Pro** 构建的模块化中后台管理系统。采用多模块设计，便于后期维护和功能扩展，包含独立的安全模块和存储模块，提供企业级功能支持。

## Star 趋势

<a href="https://star-history.com/#xutongxue233/tiny-platform&Date">
 <picture>
   <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/svg?repos=xutongxue233/tiny-platform&type=Date&theme=dark" />
   <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=xutongxue233/tiny-platform&type=Date" />
   <img alt="Star History Chart" src="https://api.star-history.com/svg?repos=xutongxue233/tiny-platform&type=Date" />
 </picture>
</a>

## 技术栈

### 后端
| 技术 | 版本 | 说明 |
|-----|------|-----|
| Spring Boot | 3.2.0 | 核心框架 |
| JDK | 21 | Java 版本 |
| MyBatis Plus | 3.5.15 | ORM 框架 |
| Sa-Token | 1.37.0 | 权限认证框架 |
| MySQL | 8.0 | 数据库 |
| Redis | 6.0+ | 缓存与会话 |
| Knife4j | 4.4.0 | API 文档 |
| Hutool | 5.8.23 | 工具库 |
| Druid | 1.2.27 | 数据库连接池 |

### 前端
| 技术 | 版本 | 说明 |
|-----|------|-----|
| React | 19 | UI 框架 |
| Ant Design | 5 | UI 组件库 |
| Ant Design Pro | - | 企业级 UI 解决方案 |
| UmiJS Max | - | React 应用框架 |
| TypeScript | - | 类型安全的 JavaScript |
| TailwindCSS | - | CSS 框架 |

## 功能特性

### 认证授权
- 基于 Sa-Token 的 Token 认证
- Token 有效期 24 小时
- 支持同一账号多地登录
- 基于 RBAC 的权限控制
- 注解式权限校验

### 系统管理
- 用户管理（增删改查、状态管理、密码重置）
- 用户注册（可配置注册功能、验证码校验）
- 角色管理（角色分配、权限配置）
- 菜单管理（树形结构、路由配置、按钮权限）
- 部门管理（树形结构、数据权限）
- 数据字典（字典类型与字典项管理、缓存支持）
- 系统参数配置（全局参数配置、缓存刷新）

### 信息管理
- 通知公告（发布、置顶、状态管理）
- 公告阅读状态（已读/未读、批量标记）
- 消息通知（未读数量统计、全部已读）

### 日志系统
- 操作日志（基于 AOP 切面自动记录，支持注解配置）
- 登录日志（记录登录/登出，IP 归属地解析）
- 在线用户管理

### 数据功能
- 逻辑删除
- 自动填充（创建时间、更新时间）
- 分页查询
- 数据权限控制

### 文件存储
- 多存储后端支持（本地存储、MinIO、阿里云 OSS、AWS S3）
- 存储配置管理
- 文件上传下载与记录追踪
- 统一的存储抽象层

### 开发工具
- 代码生成器（表导入、代码预览、批量生成）
- 可自定义代码模板（Entity、Mapper、Service、Controller、DTO、VO）
- 前端代码生成（React 页面、API 服务）
- 生成器配置管理

## 项目结构

```
tiny-platform/
├── tiny-common/          # 公共模块（注解、常量、基础实体）
│   ├── core/             # 核心类（BaseEntity、PageDTO）
│   ├── constant/         # 常量定义
│   └── annotation/       # 自定义注解
├── tiny-core/            # 核心模块（Web 基础设施）
│   ├── config/           # MyBatis Plus、WebMvc 配置
│   ├── exception/        # 全局异常处理器
│   └── web/              # ResponseResult、WebUtil、TraceInterceptor
├── tiny-security/        # 安全模块（认证授权）
│   ├── config/           # Sa-Token 配置
│   ├── aspect/           # 操作日志切面、数据权限切面
│   ├── datascope/        # 数据权限拦截器
│   └── context/          # 登录用户上下文
├── tiny-storage/         # 存储模块（文件管理）
│   ├── service/storage/  # 存储实现（本地、MinIO、OSS、S3）
│   ├── factory/          # 存储工厂
│   └── controller/       # 文件上传下载接口
├── tiny-system/          # 系统管理模块
│   ├── entity/           # 实体类（用户、角色、菜单、部门、字典等）
│   ├── mapper/           # 数据访问层
│   ├── service/          # 业务逻辑层
│   ├── controller/       # 控制器层
│   ├── dto/              # 数据传输对象
│   └── vo/               # 视图对象
├── tiny-generator/       # 代码生成器模块
│   ├── core/             # 生成器核心（模板引擎、规则链、代码写入器）
│   ├── entity/           # 生成器实体（GenTable、GenTableColumn）
│   ├── service/          # 生成器服务
│   └── controller/       # 生成器接口
├── tiny-admin/           # 主启动模块
│   ├── TinyAdminApplication
│   └── application.yml
├── tiny-ui/              # 前端项目
│   ├── src/              # 源代码
│   ├── config/           # 配置文件
│   └── mock/             # Mock 数据
└── docs/                 # 文档
    └── init.sql          # 数据库初始化脚本
```

**模块依赖关系**: tiny-admin -> tiny-system -> tiny-generator -> tiny-storage -> tiny-security -> tiny-core -> tiny-common

## 快速开始

### 环境要求
- JDK 21+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+
- Node.js 20+

### 后端启动

1. 创建数据库并执行初始化脚本
```bash
mysql -u root -p < docs/init.sql
```

2. 修改配置文件 `tiny-admin/src/main/resources/application.yml`
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tiny_admin
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
```

3. 编译打包
```bash
mvn clean install
```

4. 启动应用
```bash
cd tiny-admin
mvn spring-boot:run
```

- 后端访问地址：http://localhost:8081
- API 文档地址：http://localhost:8081/doc.html

### 前端启动

1. 进入前端目录
```bash
cd tiny-ui
```

2. 安装依赖
```bash
npm install
```

3. 启动开发服务器
```bash
npm run dev
```

- 前端访问地址：http://localhost:8000

### 默认账号
- 用户名：`admin`
- 密码：`admin123`

## API 接口

### 认证相关
| 方法 | 接口 | 说明 |
|-----|------|-----|
| POST | /auth/login | 用户登录 |
| POST | /auth/logout | 用户登出 |
| POST | /auth/register | 用户注册 |
| GET | /auth/getUserInfo | 获取当前用户信息 |
| GET | /auth/captcha | 获取验证码图片 |

### 系统管理
| 方法 | 接口 | 说明 |
|-----|------|-----|
| GET | /sys/user/page | 用户分页查询 |
| GET | /sys/role/page | 角色分页查询 |
| GET | /sys/menu/tree | 菜单树 |
| GET | /sys/dept/tree | 部门树 |

### 信息管理
| 方法 | 接口 | 说明 |
|-----|------|-----|
| POST | /info/notice/page | 通知公告分页查询 |
| GET | /info/notice/{noticeId} | 公告详情 |
| POST | /info/notice | 新增公告 |
| PUT | /info/notice | 修改公告 |
| DELETE | /info/notice/{noticeId} | 删除公告 |
| PUT | /info/notice/top | 置顶公告 |
| POST | /info/notice/read/{noticeId} | 标记已读 |
| GET | /info/notice/unread-count | 未读数量 |
| POST | /info/notice/read-all | 全部已读 |

### 日志管理
| 方法 | 接口 | 说明 |
|-----|------|-----|
| GET | /sys/operationLog/page | 操作日志分页查询 |
| GET | /sys/loginLog/page | 登录日志分页查询 |

### 文件存储
| 方法 | 接口 | 说明 |
|-----|------|-----|
| POST | /storage/file/upload | 文件上传 |
| GET | /storage/file/download/{id} | 文件下载 |
| GET | /storage/config/page | 存储配置列表 |
| POST | /storage/config | 创建存储配置 |

### 数据字典
| 方法 | 接口 | 说明 |
|-----|------|-----|
| GET | /sys/dictType/page | 字典类型分页查询 |
| GET | /sys/dictType/list | 字典类型列表 |
| GET | /sys/dictItem/list | 根据类型获取字典项列表 |
| POST | /sys/dictType | 创建字典类型 |
| POST | /sys/dictItem | 创建字典项 |

### 代码生成器
| 方法 | 接口 | 说明 |
|-----|------|-----|
| GET | /gen/table/page | 生成器表分页查询 |
| GET | /gen/table/db/list | 数据库表列表 |
| POST | /gen/table/import | 导入数据库表 |
| GET | /gen/table/preview/{tableId} | 预览生成代码 |
| GET | /gen/table/download/{tableId} | 下载生成代码 |
| POST | /gen/table/batchGenerate | 批量生成代码 |

### 系统参数配置
| 方法 | 接口 | 说明 |
|-----|------|-----|
| GET | /sys/config/page | 参数配置分页查询 |
| GET | /sys/config/{configId} | 根据ID获取参数 |
| GET | /sys/config/key/{configKey} | 根据键名获取参数 |
| POST | /sys/config | 创建参数配置 |
| PUT | /sys/config | 更新参数配置 |
| DELETE | /sys/config/{configId} | 删除参数配置 |
| DELETE | /sys/config/refreshCache | 刷新参数缓存 |

## 模块扩展

系统采用模块化设计，后期可以方便地添加新模块：

1. 在父 POM 中添加模块声明
2. 创建新模块并添加依赖
3. 实现业务功能
4. 在 `tiny-admin` 主模块中引入新模块依赖

## 技术特点

1. **模块化设计** - 清晰的模块划分，便于维护和扩展
2. **技术栈先进** - 使用最新版本的 Spring Boot 3、JDK 21、React 19
3. **安全优先** - BCrypt 密码加密，Redis Token 存储，数据权限控制
4. **开发友好** - 完善的 API 文档，丰富的工具库，参数校验
5. **生产就绪** - 日志系统、监控支持、支持分布式部署
6. **灵活存储** - 基于工厂模式的可插拔存储后端

## 后续规划

- [x] 通知公告模块
- [ ] 定时任务模块
- [ ] 工作流引擎

## 参与贡献

欢迎提交 Pull Request 参与贡献！

## 开源许可

本项目基于 MIT 许可证开源，详情请查看 [LICENSE](LICENSE) 文件。
