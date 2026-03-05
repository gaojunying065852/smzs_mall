# 商盟助手电商平台数据库文档

## 📋 文档概述

本文档包含了商盟助手电商平台的完整数据库设计说明，包括表结构、字段说明、索引设计以及系统初始化信息。

## 🏗️ 数据库架构

### 当前表结构设计

#### 1. 用户体系双表设计

##### 1.1 小程序客户表 (`app_customer`)
```sql
CREATE TABLE app_customer (
    id BIGSERIAL PRIMARY KEY,
    phone VARCHAR(20) UNIQUE,
    open_id VARCHAR(100) UNIQUE,
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    gender SMALLINT DEFAULT 0,
    birthday DATE,
    status SMALLINT DEFAULT 1,
    creater BIGINT,
    updater BIGINT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delete SMALLINT DEFAULT 0
);
```

**字段说明：**
- `phone`: 手机号（微信授权获取）
- `open_id`: 微信openId（唯一标识）
- `nickname`: 用户昵称
- `avatar`: 用户头像URL
- `gender`: 性别（0未知，1男，2女）
- `birthday`: 生日
- `status`: 状态（0禁用，1正常）

##### 1.2 管理后台用户表 (`admin_user`)
```sql
CREATE TABLE admin_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    avatar VARCHAR(255),
    role VARCHAR(20) DEFAULT 'admin',
    last_login_at TIMESTAMP,
    status SMALLINT DEFAULT 1,
    creater BIGINT,
    updater BIGINT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delete SMALLINT DEFAULT 0
);
```

**字段说明：**
- `username`: 登录用户名（通常是手机号）
- `password`: BCrypt加密后的密码
- `nickname`: 显示昵称
- `role`: 角色（admin/super_admin）
- `last_login_at`: 最后登录时间
- `status`: 状态（0禁用，1正常，2锁定）

#### 2. 业务数据表

##### 2.1 商品分类表 (`categories`)
```sql
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    icon VARCHAR(255),
    sort_order INTEGER DEFAULT 0,
    status SMALLINT DEFAULT 1,
    creater BIGINT,
    updater BIGINT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delete SMALLINT DEFAULT 0
);
```

##### 2.2 商品表 (`products`)
```sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    category_id BIGINT NOT NULL REFERENCES categories(id),
    price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2),
    stock INTEGER DEFAULT 0,
    images TEXT[],
    details JSONB,
    is_recommend BOOLEAN DEFAULT FALSE,
    is_hot BOOLEAN DEFAULT FALSE,
    status SMALLINT DEFAULT 1,
    creater BIGINT,
    updater BIGINT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delete SMALLINT DEFAULT 0
);
```

##### 2.3 收货地址表 (`addresses`)
```sql
CREATE TABLE addresses (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES app_customer(id) ON DELETE CASCADE,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    province VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    district VARCHAR(50) NOT NULL,
    detail_address VARCHAR(200) NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    creater BIGINT,
    updater BIGINT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delete SMALLINT DEFAULT 0
);
```

##### 2.4 订单相关表
```sql
-- 订单表
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_no VARCHAR(50) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL REFERENCES app_customer(id),
    address_id BIGINT NOT NULL REFERENCES addresses(id),
    total_amount DECIMAL(10,2) NOT NULL,
    status SMALLINT DEFAULT 0,
    payment_method SMALLINT,
    payment_time TIMESTAMP,
    delivery_time TIMESTAMP,
    finish_time TIMESTAMP,
    cancel_time TIMESTAMP,
    remark TEXT,
    creater BIGINT,
    updater BIGINT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delete SMALLINT DEFAULT 0
);

-- 订单项表
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id),
    quantity INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    creater BIGINT,
    updater BIGINT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delete SMALLINT DEFAULT 0
);
```

##### 2.5 辅助功能表
```sql
-- 验证码记录表
CREATE TABLE sms_codes (
    id BIGSERIAL PRIMARY KEY,
    phone VARCHAR(20) NOT NULL,
    code VARCHAR(10) NOT NULL,
    type SMALLINT DEFAULT 1,
    expire_time TIMESTAMP NOT NULL,
    is_used BOOLEAN DEFAULT FALSE,
    creater BIGINT,
    updater BIGINT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delete SMALLINT DEFAULT 0
);

-- 文件上传记录表
CREATE TABLE file_uploads (
    id BIGSERIAL PRIMARY KEY,
    original_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_size BIGINT,
    mime_type VARCHAR(100),
    user_id BIGINT REFERENCES app_customer(id),
    creater BIGINT,
    updater BIGINT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delete SMALLINT DEFAULT 0
);
```

## 🔐 系统默认账户信息

### 管理后台默认管理员账户

**账户信息：**
- **用户名**: `admin` 或 `13800138000`
- **密码**: `admin123`
- **昵称**: 超级管理员
- **角色**: super_admin
- **状态**: 正常(1)

**注意事项：**
⚠️ **重要安全提醒**: 
- 默认密码 `admin123` 仅为开发测试使用
- 生产环境部署后请立即修改默认密码
- 建议使用强密码策略（至少8位，包含大小写字母、数字和特殊字符）

### 小程序测试用户

**测试账户信息：**
- **openId**: `openid_test_001`
- **昵称**: 测试用户
- **头像**: https://example.com/avatar.jpg
- **状态**: 正常(1)

## 📊 数据库索引设计

```sql
-- 用户相关索引
CREATE INDEX idx_app_customer_phone ON app_customer(phone);
CREATE INDEX idx_app_customer_open_id ON app_customer(open_id);

-- 商品相关索引
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_recommend ON products(is_recommend) WHERE is_recommend = TRUE;
CREATE INDEX idx_products_hot ON products(is_hot) WHERE is_hot = TRUE;

-- 地址相关索引
CREATE INDEX idx_addresses_customer ON addresses(user_id);

-- 订单相关索引
CREATE INDEX idx_orders_customer ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_order_items_order ON order_items(order_id);

-- 其他索引
CREATE INDEX idx_sms_codes_phone_expire ON sms_codes(phone, expire_time) WHERE is_used = FALSE;
CREATE INDEX idx_file_uploads_customer ON file_uploads(user_id);
```

## 🔄 表结构变更历史

### 1. 用户表重命名
- **原表名**: `users` → **新表名**: `app_customer`
- **变更时间**: 2026-03-05
- **变更原因**: 明确区分小程序用户和管理后台用户
- **迁移脚本**: `rename_users_to_customers.sql`

### 2. 管理后台用户表重命名
- **原表名**: `admin_users` → **新表名**: `admin_user`
- **变更时间**: 2026-03-05
- **变更原因**: 统一使用单数命名规范
- **迁移脚本**: `rename_admin_users_to_admin_user.sql`

## 🛠️ 数据库初始化

### 初始分类数据
```sql
INSERT INTO categories (name, icon, sort_order, creater, updater) VALUES 
('电子产品', '/icons/electronics.png', 1, 1, 1),
('服装鞋帽', '/icons/clothing.png', 2, 1, 1),
('家居用品', '/icons/home.png', 3, 1, 1),
('美妆护肤', '/icons/beauty.png', 4, 1, 1),
('食品饮料', '/icons/food.png', 5, 1, 1);
```

### 管理员账户初始化
```sql
-- 通过应用程序初始化（推荐方式）
POST /api/admin/test/init-admin

-- 或者直接数据库插入（需要BCrypt加密）
INSERT INTO admin_user (username, password, nickname, role, status, creater, updater) VALUES 
('admin', '$2a$10$加密后的密码', '超级管理员', 'super_admin', 1, 1, 1);
```

## ⚙️ 数据库配置

### 连接信息
```yaml
# application.yml 配置示例
spring:
  datasource:
    url: jdbc:postgresql://主机:5432/数据库名
    username: 数据库用户名
    password: 数据库密码
    driver-class-name: org.postgresql.Driver
```

### 连接池配置
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
```

## 📈 性能优化建议

1. **定期维护**：建议每周执行 `VACUUM ANALYZE`
2. **索引监控**：定期检查索引使用情况，删除未使用的索引
3. **分区表**：对于大表（如订单表）建议按月分区
4. **读写分离**：高并发场景下建议配置主从复制
5. **连接池调优**：根据实际并发量调整连接池参数

## 🚨 安全注意事项

1. **密码安全**：所有密码必须使用BCrypt加密存储
2. **SQL注入防护**：使用参数化查询，避免拼接SQL
3. **权限控制**：严格控制数据库用户权限
4. **备份策略**：制定定期备份计划
5. **审计日志**：开启数据库审计功能

## 📞 技术支持

如有数据库相关问题，请联系：
- 数据库管理员
- 系统架构师
- 技术支持团队

---
*文档版本：v1.0*
*最后更新：2026-03-05*