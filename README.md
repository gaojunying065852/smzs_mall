# 商盟助手电商平台后端

这是一个基于Spring Boot的多模块电商平台后端项目，包含小程序接口和管理后台两套独立的服务。

## 项目架构

### 模块划分

```
smzs-mall-parent/                  # 父项目
├── smzs-mall-common/             # 公共模块
│   ├── entity/                   # 实体类
│   ├── repository/               # 数据访问层
│   ├── config/                   # 配置类
│   ├── util/                     # 工具类
│   └── common/                   # 通用类
├── smzs-mall-app/                # 小程序接口模块
│   ├── controller/               # 小程序控制器
│   ├── service/                  # 小程序业务服务
│   └── SmzsMallAppApplication.java # 小程序启动类
└── smzs-mall-manage/             # 管理后台模块
    ├── controller/               # 管理后台控制器
    ├── service/                  # 管理后台业务服务
    └── SmzsMallManageApplication.java # 管理后台启动类
```

## 技术栈

- Spring Boot 2.7.0
- Spring Data JPA
- PostgreSQL
- Spring Security
- JWT
- Maven

## 模块功能说明

### smzs-mall-app (小程序接口模块)
面向微信小程序用户提供服务

**核心功能**:
- ✅ 用户认证接口
- ✅ 商品浏览接口
- ✅ 分类查询接口
- ⬜ 购物车接口
- ⬜ 订单管理接口
- ⬜ 收货地址接口
- ⬜ 支付接口

### smzs-mall-manage (管理后台模块)
面向运营管理人员提供服务

**核心功能**:
- ⬜ 商品管理接口
- ⬜ 订单管理接口
- ⬜ 用户管理接口
- ⬜ 数据统计接口
- ⬜ 系统配置接口

## 数据库设计

数据库表结构位于 `database/schema.sql` 文件中，包含以下主要表：

- `users` - 用户表
- `categories` - 分类表
- `products` - 商品表
- `addresses` - 收货地址表
- `orders` - 订单表
- `order_items` - 订单项表
- `sms_codes` - 验证码记录表
- `file_uploads` - 文件上传记录表

## 快速开始

### 1. 环境准备
- JDK 1.8+
- PostgreSQL 12+
- Maven 3.6+
- Docker & Docker Compose (推荐)

**Windows**:
```cmd
deploy.bat
```

### 3. 数据库配置
```sql
-- 创建数据库
CREATE DATABASE smzs_mall;

-- 执行数据库脚本
psql -d smzs_mall -f database/schema.sql
```

### 4. 修改配置文件
编辑各模块下的 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/smzs_mall
    username: your_username
    password: your_password
```

## 服务端口配置

| 服务 | 端口 | 说明 |
|------|------|------|
| 小程序服务 | 8080 | 面向微信小程序用户 |
| 管理后台服务 | 8081 | 面向运营管理人员 |
| 数据库 | 5432 | PostgreSQL |
| Redis | 6379 | 缓存服务 |
| Nginx | 80/443 | 反向代理 |

## Docker部署

项目支持Docker一键部署：

```bash
# 构建并启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

## 微服务化演进

项目已为微服务化做好准备，详细路线图请参考 [docs/微服务化路线图.md](docs/微服务化路线图.md)

### 当前架构优势
- ✅ 模块化设计，职责清晰
- ✅ 独立部署，互不影响
- ✅ 易于水平扩展
- ✅ 为微服务拆分奠定基础

## API测试示例

### 小程序接口测试

**1. 发送验证码**:
```bash
curl -X POST http://localhost:8080/api/auth/sms \
  -H "Content-Type: application/json" \
  -d '{"phone":"13800138000"}'
```

**2. 登录**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"phone":"13800138000","code":"123456"}'
```

**3. 获取分类列表**:
```bash
curl http://localhost:8080/api/categories
```

### 管理后台接口测试
(待开发)

## 项目结构说明

### 公共模块 (smzs-mall-common)
包含所有共享的代码和配置：
- 实体类定义
- 数据访问层
- 安全配置
- 工具类
- 通用响应类

### 小程序模块 (smzs-mall-app)
专注于用户端功能：
- 用户认证接口
- 商品浏览接口
- 订单相关接口

### 管理后台模块 (smzs-mall-manage)
专注于运营管理功能：
- 商品管理接口
- 订单管理接口
- 数据统计接口

## 开发进度

### 小程序模块
✅ 已完成：基础框架搭建、用户认证、分类管理
⬜ 待完成：商品管理、购物车、订单管理、地址管理

### 管理后台模块
⬜ 待开发：商品管理、订单管理、用户管理、数据统计

## 注意事项

1. 当前版本为简化版，部分功能仅实现基本框架
2. 短信验证码发送为模拟实现
3. JWT Token生成为简化版本
4. 实际部署时需要配置真实的短信服务商和支付接口
5. Docker部署时确保Docker和Docker Compose已正确安装
6. 生产环境建议使用HTTPS和域名访问

## 许可证

MIT License