# Tiny Platform - 模块化中后台管理系统

## 项目介绍

Tiny Platform 是一个基于 Spring Boot 3 + MyBatis Plus + Sa-Token + React + Ant Design Pro 构建的模块化中后台管理系统。采用多模块设计，便于后期维护和功能扩展。

## 技术栈

### 后端
- Spring Boot 3.2.0
- JDK 21
- MyBatis Plus 3.5.15
- Sa-Token 1.37.0（权限认证框架）
- MySQL 8.0
- Redis
- Knife4j 4.4.0（API文档）
- Hutool 5.8.23
- Fastjson2 2.0.43

### 前端
- React 19
- Ant Design 5
- Ant Design Pro Components
- UmiJS Max
- TypeScript
- TailwindCSS

## 项目结构

```
tiny-platform/
├── tiny-common/                # 公共模块
│   ├── core/                   # 核心类
│   │   ├── model/              # 基础实体、统一响应
│   │   └── page/               # 分页对象
│   ├── constant/               # 常量定义
│   ├── exception/              # 异常处理
│   └── config/                 # 通用配置（MyBatis Plus等）
├── tiny-core/                  # 核心模块
│   ├── security/               # 登录用户信息
│   ├── config/                 # Sa-Token配置、Redis配置
│   └── utils/                  # 登录用户工具类
├── tiny-system/                # 系统管理模块
│   ├── entity/                 # 实体类（用户、角色、菜单等）
│   ├── mapper/                 # 数据访问层
│   ├── service/                # 业务逻辑层
│   ├── controller/             # 控制器层
│   ├── dto/                    # 数据传输对象
│   └── vo/                     # 视图对象
├── tiny-admin/                 # 主启动模块
│   ├── TinyAdminApplication    # 启动类
│   └── application.yml         # 配置文件
├── tiny-ui/                    # 前端项目
│   ├── src/                    # 源代码
│   ├── config/                 # 配置文件
│   └── mock/                   # Mock数据
└── docs/                       # 文档
    └── init.sql                # 数据库初始化脚本
```

## 模块说明

### 1. tiny-common（公共模块）
提供系统公共功能：
- 统一响应封装（ResponseResult）
- 统一异常处理（BusinessException、GlobalExceptionHandler）
- 基础实体类（BaseEntity）
- 分页对象（PageQuery、PageResult）
- 常量定义（CommonConstants）
- MyBatis Plus配置

### 2. tiny-core（核心模块）
提供系统核心功能：
- Sa-Token权限认证配置
- Redis配置
- 登录用户信息封装（LoginUser）
- 登录用户工具类（LoginUserUtil）
- Sa-Token异常处理

### 3. tiny-system（系统管理模块）
实现系统管理功能：
- 用户管理
- 角色管理
- 菜单管理
- 用户认证（登录、登出）
- RBAC权限控制

### 4. tiny-admin（主启动模块）
- 应用启动入口
- 配置文件管理
- 整合所有子模块

### 5. tiny-ui（前端项目）
基于 Ant Design Pro 的前端项目：
- 用户登录/登出
- 系统管理页面
- 权限控制

## 核心功能

### 1. 认证授权
- 基于Sa-Token的认证体系
- Token有效期24小时
- 支持同一账号多地登录
- 基于RBAC的权限控制
- 注解式权限校验

### 2. 系统管理
- 用户管理（增删改查、状态管理）
- 角色管理（角色分配、权限配置）
- 菜单管理（菜单树、路由配置、按钮权限）

### 3. 数据权限
- 逻辑删除
- 自动填充（创建时间、更新时间等）
- 分页查询

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

后端访问地址：http://localhost:8081

API文档地址：http://localhost:8081/doc.html

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

前端访问地址：http://localhost:8000

### 默认账号
- 用户名：admin
- 密码：admin123

## API接口

### 认证相关
- POST /auth/login - 用户登录
- POST /auth/logout - 用户登出
- GET /auth/getUserInfo - 获取当前用户信息

## 扩展模块

系统采用模块化设计，后期可以方便地添加新模块：

### 扩展步骤
1. 在父POM中添加模块声明
2. 创建新模块并添加依赖
3. 实现业务功能
4. 在主模块中引入新模块依赖

## 技术特点

1. **模块化设计**：清晰的模块划分，便于维护和扩展
2. **权限框架**：使用Sa-Token框架，简化权限管理
3. **优先使用框架**：优先使用MyBatis Plus、Hutool等成熟工具，减少重复造轮子
4. **统一封装**：统一的响应格式、异常处理、分页对象
5. **代码规范**：遵循阿里巴巴Java开发规范

## 注意事项

1. 密码使用BCrypt加密存储
2. Token存储在Redis中，支持分布式部署
3. 所有表都支持逻辑删除
4. 建议使用MyBatis Plus的Lambda方式编写查询，避免硬编码字段名
5. 工具类优先使用Hutool、Commons-Lang3等成熟工具库

## 后续规划

- [ ] 用户管理完整CRUD
- [ ] 角色管理完整CRUD
- [ ] 菜单管理完整CRUD
- [ ] 操作日志模块
- [ ] 定时任务模块
- [ ] 代码生成器

## 许可证

MIT License
