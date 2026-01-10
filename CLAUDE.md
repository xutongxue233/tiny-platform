# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Tiny Platform是一个模块化的企业级后台管理系统，基于Spring Boot 3 + React 19构建。

## Tech Stack

**后端**: Spring Boot 3.2.0, Java 21, MyBatis Plus 3.5.15, Sa-Token 1.37.0, MySQL 8.0, Redis, Knife4j 4.4.0

**前端**: React 19, Ant Design 5, UmiJS Max 4.3.24, TypeScript 5.6.3, TailwindCSS 3

## Module Structure

```
tiny-platform/
├── tiny-common/   # 基础层：注解、常量、工具类、异常处理、基础实体
├── tiny-core/     # 核心层：Sa-Token配置、数据权限拦截器、登录用户工具
├── tiny-system/   # 业务层：用户/角色/菜单/部门管理、日志系统
├── tiny-admin/    # 启动层：Spring Boot主应用、Knife4j配置
├── tiny-ui/       # 前端：React应用
└── docs/          # 文档：数据库脚本、前端组件使用文档
```

依赖关系: tiny-admin -> tiny-system -> tiny-core -> tiny-common

## Build & Run Commands

### 后端

```bash
# 构建所有模块
mvn clean install

# 运行应用
cd tiny-admin && mvn spring-boot:run

# 打包
mvn clean package
java -jar tiny-admin/target/tiny-admin-1.0.0.jar
```

### 前端

```bash
cd tiny-ui

npm install          # 安装依赖
npm run dev          # 开发模式(带mock)
npm start:dev        # 开发模式(连接真实后端)
npm run build        # 生产构建
npm run lint         # 代码检查
npm run tsc          # TypeScript类型检查
npm run test         # 运行测试
```

### 数据库初始化

```bash
mysql -u root -p < docs/init.sql
```

## Access URLs

- 后端API: http://localhost:8081/api
- API文档: http://localhost:8081/api/doc.html
- Druid监控: http://localhost:8081/api/druid (admin/admin123)
- 前端: http://localhost:8000

## Architecture Patterns

### 认证授权
- Sa-Token实现Token认证，存储于Redis
- 权限控制使用`@SaCheckPermission`和`@SaCheckRole`注解
- 默认所有接口需登录，白名单配置在`SaTokenConfig.java`

### 数据权限
- 通过`@DataScope`注解标记需要数据权限控制的Mapper方法
- `DataScopeInterceptor`自动注入SQL Where条件
- 5种权限范围：全部数据(1)、自定义部门(2)、本部门(3)、本部门及以下(4)、仅本人(5)

### 操作日志
- 使用`@OperationLog`注解标记需要记录的Controller方法
- `OperationLogAspect`切面自动拦截并记录
- 支持配置排除敏感参数、是否保存请求/响应数据

### 统一响应
- 所有API返回`ResponseResult<T>`格式
- 包含: code, message, data, timestamp
- 全局异常处理器统一转换异常为响应格式

### 分层规范
- Controller: 处理HTTP请求，参数校验
- Service: 业务逻辑
- Mapper: 数据访问(MyBatis Plus)
- DTO: 输入参数对象
- VO: 响应数据对象
- Entity: 数据库实体

## Coding Conventions

### 类命名后缀规范
| 层级 | 后缀 | 示例 |
|------|------|------|
| Controller | Controller | SysUserController |
| Service接口 | Service | SysUserService |
| Service实现 | ServiceImpl | SysUserServiceImpl |
| Mapper | Mapper | SysUserMapper |
| 实体类 | 无特定后缀 | SysUser |
| DTO | DTO/Query | SysUserDTO, SysUserQuery |
| VO | VO | SysUserVO |

### 实体类规范
- 所有数据库实体类必须继承`BaseEntity`
- `BaseEntity`提供通用字段: createBy, createTime, updateBy, updateTime, delFlag, remark
- 这些字段由MyBatis Plus自动填充，无需手动设置

```java
// 正确示例
@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long userId;
    private String username;
    // 业务字段...
}
```

### 数据库约定
- 逻辑删除字段: del_flag (0正常, 1删除)
- 审计字段: create_by, create_time, update_by, update_time (自动填充)
- 树形结构使用ancestors字段存储祖级路径
- 主键使用自增ID，字段名格式为`表名_id`(如user_id, role_id)

## Key Files

- `tiny-core/src/.../config/SaTokenConfig.java` - 认证拦截器配置
- `tiny-core/src/.../datascope/DataScopeInterceptor.java` - 数据权限核心实现
- `tiny-system/src/.../aspect/OperationLogAspect.java` - 操作日志切面
- `tiny-admin/src/main/resources/application.yml` - 主配置文件
- `tiny-ui/config/routes.ts` - 前端路由配置
- `tiny-ui/src/access.ts` - 前端权限控制
- `docs/init.sql` - 数据库初始化脚本
- `docs/llms-full.txt` - Ant Design组件语义化描述文档，包含64个组件的详细说明，用于理解组件结构和样式定制



