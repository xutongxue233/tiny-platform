<h1 align="center">Tiny Platform</h1>

<p align="center">
  <strong>A Modular Backend Management System</strong>
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
  <a href="./README_zh-CN.md">简体中文</a> | English
</p>

---

## Introduction

Tiny Platform is a modular backend management system built with **Spring Boot 3 + MyBatis Plus + Sa-Token + React + Ant Design Pro**. It features a multi-module architecture designed for easy maintenance and extensibility, with dedicated security and storage modules for enterprise-level functionality.

## Star History

<a href="https://star-history.com/#xutongxue233/tiny-platform&Date">
 <picture>
   <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/svg?repos=xutongxue233/tiny-platform&type=Date&theme=dark" />
   <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=xutongxue233/tiny-platform&type=Date" />
   <img alt="Star History Chart" src="https://api.star-history.com/svg?repos=xutongxue233/tiny-platform&type=Date" />
 </picture>
</a>

## Tech Stack

### Backend
| Technology | Version | Description |
|------------|---------|-------------|
| Spring Boot | 3.2.0 | Core framework |
| JDK | 21 | Java version |
| MyBatis Plus | 3.5.15 | ORM framework |
| Sa-Token | 1.37.0 | Authentication & Authorization |
| MySQL | 8.0 | Database |
| Redis | 6.0+ | Cache & Session |
| Knife4j | 4.4.0 | API documentation |
| Hutool | 5.8.23 | Utility library |
| Druid | 1.2.27 | Database connection pool |

### Frontend
| Technology | Version | Description |
|------------|---------|-------------|
| React | 19 | UI framework |
| Ant Design | 5 | UI component library |
| Ant Design Pro | - | Enterprise UI solution |
| UmiJS Max | - | React framework |
| TypeScript | - | Type-safe JavaScript |
| TailwindCSS | - | CSS framework |

## Features

### Authentication & Authorization
- Token-based authentication with Sa-Token
- 24-hour token validity
- Multi-device login support
- RBAC permission control
- Annotation-based authorization

### System Management
- User Management (CRUD, status control, password reset)
- User Registration (configurable registration, captcha verification)
- Role Management (role assignment, permission configuration)
- Menu Management (tree structure, routing, button permissions)
- Department Management (tree structure, data permission)
- Data Dictionary (dictionary type and item management, cache support)
- System Configuration (global parameters, cache refresh)

### Information Management
- Notice Announcements (publish, pin to top, status management)
- Read Status Tracking (read/unread, batch marking)
- Message Notifications (unread count, mark all as read)

### Message Center
- In-app Messages (system messages, personal messages)
- WebSocket Real-time Push
- Message List Management (pagination, batch delete)
- Unread Message Count with Real-time Updates

### Logging System
- Operation Logs (auto-record via AOP, annotation-based)
- Login Logs (login/logout tracking, IP geolocation)
- Online User Management

### Data Features
- Logical deletion
- Auto-fill (create time, update time)
- Pagination support
- Data permission control

### File Storage
- Multi-storage backend support (Local, MinIO, Aliyun OSS, AWS S3)
- Storage configuration management
- File upload/download with record tracking
- Unified storage abstraction layer

### Development Tools
- Code Generator (table import, code preview, batch generation)
- Customizable code templates (Entity, Mapper, Service, Controller, DTO, VO)
- Frontend code generation (React pages, API services)
- Generator configuration management

## Project Structure

```
tiny-platform/
├── tiny-common/          # Common module (annotations, constants, base entity)
│   ├── core/             # Core classes (BaseEntity, PageDTO)
│   ├── constant/         # Constants
│   └── annotation/       # Custom annotations
├── tiny-core/            # Core module (web infrastructure)
│   ├── config/           # MyBatis Plus, WebMvc config
│   ├── exception/        # Global exception handler
│   └── web/              # ResponseResult, WebUtil, TraceInterceptor
├── tiny-security/        # Security module (authentication & authorization)
│   ├── config/           # Sa-Token config
│   ├── aspect/           # Operation log aspect, Data scope aspect
│   ├── datascope/        # Data permission interceptor
│   └── context/          # Login user context
├── tiny-storage/         # Storage module (file management)
│   ├── service/storage/  # Storage implementations (Local, MinIO, OSS, S3)
│   ├── factory/          # Storage factory
│   └── controller/       # File upload/download APIs
├── tiny-system/          # System management module
│   ├── entity/           # Entities (User, Role, Menu, Dept, Dict, etc.)
│   ├── mapper/           # Data access layer
│   ├── service/          # Business logic layer
│   ├── controller/       # Controllers
│   ├── dto/              # Data transfer objects
│   └── vo/               # View objects
├── tiny-generator/       # Code generator module
│   ├── core/             # Generator core (template engine, rules, writer)
│   ├── entity/           # Generator entities (GenTable, GenTableColumn)
│   ├── service/          # Generator services
│   └── controller/       # Generator APIs
├── tiny-message/         # Message module
│   ├── entity/           # Message entities
│   ├── service/          # Message services
│   └── controller/       # Message APIs
├── tiny-websocket/       # WebSocket module
│   ├── config/           # WebSocket configuration
│   ├── handler/          # Message handlers
│   └── service/          # WebSocket services
├── tiny-admin/           # Main startup module
│   ├── TinyAdminApplication
│   └── application.yml
├── tiny-ui/              # Frontend project
│   ├── src/              # Source code
│   ├── config/           # Configuration
│   └── mock/             # Mock data
└── docs/                 # Documentation
    └── init.sql          # Database init script
```

**Module Dependencies**: tiny-admin -> tiny-system -> tiny-message -> tiny-websocket -> tiny-generator -> tiny-storage -> tiny-security -> tiny-core -> tiny-common

## Quick Start

### Prerequisites
- JDK 21+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+
- Node.js 20+

### Backend Setup

1. Create database and execute init script
```bash
mysql -u root -p < docs/init.sql
```

2. Modify configuration in `tiny-admin/src/main/resources/application.yml`
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

3. Build the project
```bash
mvn clean install
```

4. Run the application
```bash
cd tiny-admin
mvn spring-boot:run
```

- Backend URL: http://localhost:8081
- API Docs: http://localhost:8081/doc.html

### Frontend Setup

1. Navigate to frontend directory
```bash
cd tiny-ui
```

2. Install dependencies
```bash
npm install
```

3. Start development server
```bash
npm run dev
```

- Frontend URL: http://localhost:8000

### Default Credentials
- Username: `admin`
- Password: `admin123`

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /auth/login | User login |
| POST | /auth/logout | User logout |
| POST | /auth/register | User registration |
| GET | /auth/getUserInfo | Get current user info |
| GET | /auth/captcha | Get captcha image |

### System Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /sys/user/page | User pagination |
| GET | /sys/role/page | Role pagination |
| GET | /sys/menu/tree | Menu tree |
| GET | /sys/dept/tree | Department tree |

### Information Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /info/notice/page | Notice pagination |
| GET | /info/notice/{noticeId} | Get notice detail |
| POST | /info/notice | Create notice |
| PUT | /info/notice | Update notice |
| DELETE | /info/notice/{noticeId} | Delete notice |
| PUT | /info/notice/top | Pin notice to top |
| POST | /info/notice/read/{noticeId} | Mark as read |
| GET | /info/notice/unread-count | Get unread count |
| POST | /info/notice/read-all | Mark all as read |

### Logging
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /sys/operationLog/page | Operation logs |
| GET | /sys/loginLog/page | Login logs |

### File Storage
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /storage/file/upload | Upload file |
| GET | /storage/file/download/{id} | Download file |
| GET | /storage/config/page | Storage config list |
| POST | /storage/config | Create storage config |

### Data Dictionary
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /sys/dictType/page | Dictionary type pagination |
| GET | /sys/dictType/list | Dictionary type list |
| GET | /sys/dictItem/list | Dictionary item list by type |
| POST | /sys/dictType | Create dictionary type |
| POST | /sys/dictItem | Create dictionary item |

### Code Generator
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /gen/table/page | Generator table pagination |
| GET | /gen/table/db/list | Database table list |
| POST | /gen/table/import | Import database tables |
| GET | /gen/table/preview/{tableId} | Preview generated code |
| GET | /gen/table/download/{tableId} | Download generated code |
| POST | /gen/table/batchGenerate | Batch generate code |

### System Configuration
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /sys/config/page | Config pagination |
| GET | /sys/config/{configId} | Get config by ID |
| GET | /sys/config/key/{configKey} | Get config by key |
| POST | /sys/config | Create config |
| PUT | /sys/config | Update config |
| DELETE | /sys/config/{configId} | Delete config |
| DELETE | /sys/config/refreshCache | Refresh config cache |

### Message Center
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /msg/message/page | Message pagination |
| GET | /msg/message/{messageId} | Get message detail |
| POST | /msg/message | Send message |
| PUT | /msg/message/read/{messageId} | Mark message as read |
| PUT | /msg/message/read-all | Mark all as read |
| DELETE | /msg/message/{messageId} | Delete message |
| DELETE | /msg/message/batch | Batch delete messages |
| GET | /msg/message/unread-count | Get unread message count |

## Extension Guide

The system uses modular design for easy extension:

1. Add module declaration in parent POM
2. Create new module with dependencies
3. Implement business logic
4. Import the module in `tiny-admin`

## Technical Highlights

1. **Modular Design** - Clear module separation for maintainability
2. **Modern Stack** - Latest versions of Spring Boot 3, JDK 21, React 19
3. **Security First** - BCrypt password encryption, Redis token storage, data permission control
4. **Developer Friendly** - Comprehensive API docs, utility libraries, parameter validation
5. **Production Ready** - Logging, monitoring, distributed deployment support
6. **Flexible Storage** - Pluggable storage backends with factory pattern

## Roadmap

- [x] Notification Module
- [x] Message Center Module
- [ ] Scheduled Tasks Module
- [ ] Workflow Engine

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
