# Tiny Platform 模块移除指南

本文档帮助使用者根据实际需求精简系统，移除不必要的模块。

## 模块分类总览

| 模块 | 类型 | 功能 | 可否移除 |
|------|------|------|----------|
| tiny-common | 基础层 | 注解、常量、工具类 | 不可移除 |
| tiny-core | 核心层 | MyBatis Plus、全局异常 | 不可移除 |
| tiny-security | 安全层 | Sa-Token认证、数据权限 | 不可移除 |
| tiny-system | 业务层 | 用户/角色/菜单/部门 | 不可移除(核心) |
| tiny-admin | 启动层 | Spring Boot入口 | 不可移除 |
| tiny-storage | 存储层 | 文件上传、云存储 | 可移除 |
| tiny-generator | 工具层 | 代码生成器 | 可移除 |

### tiny-system 内部可选模块

| 子模块 | 功能 | 可否移除 | 影响范围 |
|--------|------|----------|----------|
| 用户管理 | 用户CRUD | 不可移除 | 认证基础 |
| 角色管理 | 角色权限 | 不可移除 | 权限基础 |
| 菜单管理 | 菜单配置 | 不可移除 | 动态菜单 |
| 部门管理 | 部门树 | 可移除 | 数据权限失效 |
| 字典管理 | 数据字典 | 可移除 | 下拉选项需硬编码 |
| 操作日志 | 操作记录 | 可移除 | 无操作审计 |
| 登录日志 | 登录记录 | 可移除 | 无登录审计 |
| 在线用户 | 用户监控 | 可移除 | 无法踢人下线 |

---

## 一、移除 tiny-storage 模块

### 1.1 影响说明
- 无法使用文件上传功能
- 无存储配置管理
- 移除云存储SDK依赖(OSS/MinIO/S3/COS)

### 1.2 后端移除步骤

#### 步骤1: 修改 tiny-admin/pom.xml
删除以下依赖:
```xml
<!-- 删除此依赖 -->
<dependency>
    <groupId>com.tiny</groupId>
    <artifactId>tiny-storage</artifactId>
</dependency>
```

#### 步骤2: 修改根目录 pom.xml
在 `<modules>` 中删除:
```xml
<module>tiny-storage</module>
```

#### 步骤3: 删除模块目录
```bash
rm -rf tiny-storage/
```

### 1.3 数据库清理SQL
```sql
-- 删除存储相关表
DROP TABLE IF EXISTS sys_file_record;
DROP TABLE IF EXISTS sys_storage_config;

-- 删除存储管理菜单 (menu_id: 35-45)
DELETE FROM sys_menu WHERE menu_id IN (35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45);

-- 清理角色菜单关联
DELETE FROM sys_role_menu WHERE menu_id IN (35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45);
```

### 1.4 前端移除步骤

#### 步骤1: 删除页面目录
```bash
rm -rf tiny-ui/src/pages/storage/
rm -rf tiny-ui/src/pages/system/storage/
```

#### 步骤2: 删除API服务文件
```bash
rm -rf tiny-ui/src/services/storage/
```

#### 步骤3: 修改路由配置 tiny-ui/config/routes.ts
删除以下路由:
```typescript
// 删除此路由配置
{
  path: '/storage',
  hideInMenu: true,
  routes: [
    { path: '/storage', redirect: '/storage/file' },
    { path: '/storage/file', component: './storage/file/index' },
    { path: '/storage/config', component: './system/storage/index' },
  ],
},
// 删除 /system 下的 storage 路由
{ path: '/system/storage', component: './system/storage/index' },
```

---

## 二、移除 tiny-generator 模块

### 2.1 影响说明
- 无法使用代码生成功能
- 无法自动生成CRUD代码
- 移除FreeMarker模板引擎依赖

### 2.2 后端移除步骤

#### 步骤1: 修改 tiny-admin/pom.xml
删除以下依赖:
```xml
<!-- 删除此依赖 -->
<dependency>
    <groupId>com.tiny</groupId>
    <artifactId>tiny-generator</artifactId>
</dependency>
```

#### 步骤2: 修改根目录 pom.xml
在 `<modules>` 中删除:
```xml
<module>tiny-generator</module>
```

#### 步骤3: 删除模块目录
```bash
rm -rf tiny-generator/
```

### 2.3 数据库清理SQL
```sql
-- 删除代码生成相关表
DROP TABLE IF EXISTS gen_table_column;
DROP TABLE IF EXISTS gen_table;
DROP TABLE IF EXISTS gen_config;
DROP TABLE IF EXISTS gen_type_mapping;

-- 删除开发工具菜单 (menu_id: 55-63)
DELETE FROM sys_menu WHERE menu_id IN (55, 56, 57, 58, 59, 60, 61, 62, 63);

-- 清理角色菜单关联
DELETE FROM sys_role_menu WHERE menu_id IN (55, 56, 57, 58, 59, 60, 61, 62, 63);
```

### 2.4 前端移除步骤

#### 步骤1: 删除页面目录
```bash
rm -rf tiny-ui/src/pages/tool/
```

#### 步骤2: 删除API服务文件
```bash
rm -rf tiny-ui/src/services/tool/
```

#### 步骤3: 修改路由配置 tiny-ui/config/routes.ts
删除以下路由:
```typescript
// 删除此路由配置
{
  path: '/tool',
  hideInMenu: true,
  routes: [
    { path: '/tool', redirect: '/tool/gen' },
    { path: '/tool/gen', component: './tool/gen/index' },
    { path: '/tool/gen/edit/:tableId', component: './tool/gen/edit' },
    { path: '/tool/genConfig', component: './tool/genConfig/index' },
  ],
},
```

---

## 三、移除字典管理模块

### 3.1 影响说明
- 无法动态配置下拉选项
- 需要在代码中硬编码枚举值
- 前端字典组件不可用

### 3.2 后端移除步骤

#### 步骤1: 删除后端文件
```bash
# 删除Controller
rm tiny-system/src/main/java/com/tiny/system/controller/SysDictTypeController.java
rm tiny-system/src/main/java/com/tiny/system/controller/SysDictItemController.java

# 删除Service
rm tiny-system/src/main/java/com/tiny/system/service/SysDictTypeService.java
rm tiny-system/src/main/java/com/tiny/system/service/SysDictItemService.java
rm tiny-system/src/main/java/com/tiny/system/service/impl/SysDictTypeServiceImpl.java
rm tiny-system/src/main/java/com/tiny/system/service/impl/SysDictItemServiceImpl.java

# 删除Mapper
rm tiny-system/src/main/java/com/tiny/system/mapper/SysDictTypeMapper.java
rm tiny-system/src/main/java/com/tiny/system/mapper/SysDictItemMapper.java
rm tiny-system/src/main/resources/mapper/SysDictTypeMapper.xml
rm tiny-system/src/main/resources/mapper/SysDictItemMapper.xml

# 删除Entity
rm tiny-system/src/main/java/com/tiny/system/entity/SysDictType.java
rm tiny-system/src/main/java/com/tiny/system/entity/SysDictItem.java

# 删除DTO/VO
rm tiny-system/src/main/java/com/tiny/system/dto/SysDictTypeDTO.java
rm tiny-system/src/main/java/com/tiny/system/dto/SysDictItemDTO.java
rm tiny-system/src/main/java/com/tiny/system/vo/SysDictTypeVO.java
rm tiny-system/src/main/java/com/tiny/system/vo/SysDictItemVO.java
```

### 3.3 数据库清理SQL
```sql
-- 删除字典相关表
DROP TABLE IF EXISTS sys_dict_item;
DROP TABLE IF EXISTS sys_dict_type;

-- 删除字典管理菜单 (menu_id: 64-68)
DELETE FROM sys_menu WHERE menu_id IN (64, 65, 66, 67, 68);

-- 清理角色菜单关联
DELETE FROM sys_role_menu WHERE menu_id IN (64, 65, 66, 67, 68);
```

### 3.4 前端移除步骤
```bash
# 删除页面
rm -rf tiny-ui/src/pages/system/dict/

# 删除API服务
rm tiny-ui/src/services/system/dict.ts
rm tiny-ui/src/services/system/dictItem.ts
```

修改路由配置，删除:
```typescript
{ path: '/system/dict', component: './system/dict/index' },
```

---

## 四、移除系统监控模块(日志+在线用户)

### 4.1 影响说明
- 无操作日志记录
- 无登录日志记录
- 无法查看/踢出在线用户

### 4.2 移除操作日志

#### 后端文件删除
```bash
# 删除Controller
rm tiny-system/src/main/java/com/tiny/system/controller/SysOperationLogController.java

# 删除Service
rm tiny-system/src/main/java/com/tiny/system/service/SysOperationLogService.java
rm tiny-system/src/main/java/com/tiny/system/service/impl/SysOperationLogServiceImpl.java

# 删除Mapper
rm tiny-system/src/main/java/com/tiny/system/mapper/SysOperationLogMapper.java
rm tiny-system/src/main/resources/mapper/SysOperationLogMapper.xml

# 删除Entity
rm tiny-system/src/main/java/com/tiny/system/entity/SysOperationLog.java

# 删除切面(重要:需要保留空实现或删除@OperationLog注解使用处)
rm tiny-security/src/main/java/com/tiny/security/aspect/OperationLogAspect.java
```

#### 数据库清理SQL
```sql
-- 删除操作日志表
DROP TABLE IF EXISTS sys_operation_log;

-- 删除操作日志菜单 (menu_id: 24, 27, 28)
DELETE FROM sys_menu WHERE menu_id IN (24, 27, 28);
DELETE FROM sys_role_menu WHERE menu_id IN (24, 27, 28);
```

### 4.3 移除登录日志

#### 后端文件删除
```bash
# 删除Controller
rm tiny-system/src/main/java/com/tiny/system/controller/SysLoginLogController.java

# 删除Service
rm tiny-system/src/main/java/com/tiny/system/service/SysLoginLogService.java
rm tiny-system/src/main/java/com/tiny/system/service/impl/SysLoginLogServiceImpl.java

# 删除Mapper
rm tiny-system/src/main/java/com/tiny/system/mapper/SysLoginLogMapper.java
rm tiny-system/src/main/resources/mapper/SysLoginLogMapper.xml

# 删除Entity
rm tiny-system/src/main/java/com/tiny/system/entity/SysLoginLog.java
```

需要修改 `AuthServiceImpl.java`，删除登录日志记录代码。

#### 数据库清理SQL
```sql
-- 删除登录日志表
DROP TABLE IF EXISTS sys_login_log;

-- 删除登录日志菜单 (menu_id: 23, 25, 26)
DELETE FROM sys_menu WHERE menu_id IN (23, 25, 26);
DELETE FROM sys_role_menu WHERE menu_id IN (23, 25, 26);
```

### 4.4 移除在线用户管理

#### 后端文件删除
```bash
# 删除Controller
rm tiny-system/src/main/java/com/tiny/system/controller/OnlineUserController.java

# 删除Service
rm tiny-system/src/main/java/com/tiny/system/service/OnlineUserService.java
rm tiny-system/src/main/java/com/tiny/system/service/impl/OnlineUserServiceImpl.java
```

#### 数据库清理SQL
```sql
-- 删除在线用户菜单 (menu_id: 29, 30, 31, 32)
DELETE FROM sys_menu WHERE menu_id IN (29, 30, 31, 32);
DELETE FROM sys_role_menu WHERE menu_id IN (29, 30, 31, 32);
```

### 4.5 移除整个监控模块

如果三个子模块都移除，还需删除监控目录菜单:
```sql
-- 删除系统监控目录 (menu_id: 22)
DELETE FROM sys_menu WHERE menu_id = 22;
DELETE FROM sys_role_menu WHERE menu_id = 22;
```

#### 前端移除
```bash
# 删除整个监控页面目录
rm -rf tiny-ui/src/pages/monitor/

# 删除API服务
rm -rf tiny-ui/src/services/monitor/
```

修改路由配置，删除:
```typescript
{
  path: '/monitor',
  hideInMenu: true,
  routes: [
    { path: '/monitor', redirect: '/monitor/loginLog' },
    { path: '/monitor/loginLog', component: './monitor/loginLog/index' },
    { path: '/monitor/operationLog', component: './monitor/operationLog/index' },
    { path: '/monitor/onlineUser', component: './monitor/onlineUser/index' },
  ],
},
```

---

## 五、移除部门管理模块

### 5.1 影响说明
- 无部门组织结构
- 数据权限功能失效(无法按部门过滤数据)
- 用户无法关联部门

### 5.2 后端移除步骤

#### 后端文件删除
```bash
# 删除Controller
rm tiny-system/src/main/java/com/tiny/system/controller/SysDeptController.java

# 删除Service
rm tiny-system/src/main/java/com/tiny/system/service/SysDeptService.java
rm tiny-system/src/main/java/com/tiny/system/service/impl/SysDeptServiceImpl.java

# 删除Mapper
rm tiny-system/src/main/java/com/tiny/system/mapper/SysDeptMapper.java
rm tiny-system/src/main/resources/mapper/SysDeptMapper.xml

# 删除Entity
rm tiny-system/src/main/java/com/tiny/system/entity/SysDept.java
```

需要修改:
1. `SysUser.java` - 删除 `deptId` 字段或设为可空
2. `SysUserServiceImpl.java` - 删除部门关联逻辑
3. `DataScopeInterceptor.java` - 禁用数据权限或简化实现

### 5.3 数据库清理SQL
```sql
-- 删除部门相关表
DROP TABLE IF EXISTS sys_role_dept;
DROP TABLE IF EXISTS sys_dept;

-- 修改用户表，删除部门字段(可选)
ALTER TABLE sys_user DROP COLUMN dept_id;

-- 删除部门管理菜单 (menu_id: 17-21)
DELETE FROM sys_menu WHERE menu_id IN (17, 18, 19, 20, 21);
DELETE FROM sys_role_menu WHERE menu_id IN (17, 18, 19, 20, 21);
```

### 5.4 前端移除步骤
```bash
# 删除页面
rm -rf tiny-ui/src/pages/system/dept/

# 删除API服务
rm tiny-ui/src/services/system/dept.ts
```

修改路由配置，删除:
```typescript
{ path: '/system/dept', component: './system/dept/index' },
```

修改用户管理页面，移除部门树选择组件。

---

## 六、精简方案推荐

### 方案A: 最小化系统(仅保留核心认证)
移除: tiny-storage, tiny-generator, 字典, 部门, 所有日志
保留: 用户, 角色, 菜单

**适用场景**: 简单的后台管理，无需文件、无需组织架构

```sql
-- 方案A 一键清理SQL
DROP TABLE IF EXISTS sys_file_record;
DROP TABLE IF EXISTS sys_storage_config;
DROP TABLE IF EXISTS gen_table_column;
DROP TABLE IF EXISTS gen_table;
DROP TABLE IF EXISTS gen_config;
DROP TABLE IF EXISTS gen_type_mapping;
DROP TABLE IF EXISTS sys_dict_item;
DROP TABLE IF EXISTS sys_dict_type;
DROP TABLE IF EXISTS sys_operation_log;
DROP TABLE IF EXISTS sys_login_log;
DROP TABLE IF EXISTS sys_role_dept;
DROP TABLE IF EXISTS sys_dept;

DELETE FROM sys_menu WHERE menu_id IN (
  17,18,19,20,21,  -- 部门
  22,23,24,25,26,27,28,29,30,31,32,  -- 监控
  35,36,37,38,39,40,41,42,43,44,45,  -- 存储
  55,56,57,58,59,60,61,62,63,  -- 代码生成
  64,65,66,67,68  -- 字典
);

DELETE FROM sys_role_menu WHERE menu_id IN (
  17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,
  35,36,37,38,39,40,41,42,43,44,45,55,56,57,58,59,60,61,62,63,
  64,65,66,67,68
);
```

### 方案B: 标准系统(保留组织+字典)
移除: tiny-storage, tiny-generator, 操作日志, 在线用户
保留: 用户, 角色, 菜单, 部门, 字典, 登录日志

**适用场景**: 标准企业后台，需要组织架构和数据字典

```sql
-- 方案B 一键清理SQL
DROP TABLE IF EXISTS sys_file_record;
DROP TABLE IF EXISTS sys_storage_config;
DROP TABLE IF EXISTS gen_table_column;
DROP TABLE IF EXISTS gen_table;
DROP TABLE IF EXISTS gen_config;
DROP TABLE IF EXISTS gen_type_mapping;
DROP TABLE IF EXISTS sys_operation_log;

DELETE FROM sys_menu WHERE menu_id IN (
  24,27,28,  -- 操作日志
  29,30,31,32,  -- 在线用户
  35,36,37,38,39,40,41,42,43,44,45,  -- 存储
  55,56,57,58,59,60,61,62,63  -- 代码生成
);

DELETE FROM sys_role_menu WHERE menu_id IN (
  24,27,28,29,30,31,32,
  35,36,37,38,39,40,41,42,43,44,45,
  55,56,57,58,59,60,61,62,63
);
```

### 方案C: 开发系统(保留代码生成)
移除: tiny-storage, 操作日志
保留: 所有核心 + 代码生成 + 字典

**适用场景**: 快速开发项目，需要代码生成器加速开发

```sql
-- 方案C 一键清理SQL
DROP TABLE IF EXISTS sys_file_record;
DROP TABLE IF EXISTS sys_storage_config;
DROP TABLE IF EXISTS sys_operation_log;

DELETE FROM sys_menu WHERE menu_id IN (
  24,27,28,  -- 操作日志
  35,36,37,38,39,40,41,42,43,44,45  -- 存储
);

DELETE FROM sys_role_menu WHERE menu_id IN (
  24,27,28,35,36,37,38,39,40,41,42,43,44,45
);
```

---

## 七、移除后验证清单

完成移除后，请验证以下功能:

- [ ] 后端能正常启动 (`mvn spring-boot:run`)
- [ ] 前端能正常编译 (`npm run build`)
- [ ] 登录功能正常
- [ ] 用户管理正常
- [ ] 角色管理正常
- [ ] 菜单管理正常(已删除的菜单不再显示)
- [ ] 权限控制正常
- [ ] API文档能正常访问 (`/api/doc.html`)

---

## 八、回滚方案

如需恢复已删除的模块:

1. 从Git历史恢复代码文件
2. 执行 `docs/init.sql` 中对应模块的建表语句和数据
3. 重新在 `pom.xml` 中添加模块依赖
4. 重启后端服务
5. 在前端路由中恢复对应页面配置