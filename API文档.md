# 商盟助手电商平台 - 小程序接口文档

## 基础信息
- **服务端口**: 8080
- **基础路径**: `/api`
- **统一响应格式**: `Result<T>`
- **跨域支持**: 已启用

## 接口概览

| 模块 | 接口数量 | 完成状态 |
|------|----------|----------|
| 认证/用户 | 2 | ✅ 已完成 |
| 分类 | 1 | ✅ 已完成 |
| 商品 | 0 | ⬜ 未实现 |
| 文件上传 | 0 | ⬜ 未实现 |
| 收货地址 | 0 | ⬜ 未实现 |
| 订单 | 0 | ⬜ 未实现 |

---

## 统一响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "错误描述信息",
  "data": null
}
```

### 响应字段说明
| 字段名 | 类型 | 说明 |
|--------|------|------|
| code | Integer | 状态码 |
| message | String | 响应消息 |
| data | Object/Array | 响应数据 |

### 常见错误码
| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 500 | 服务器内部错误 |

---

## 1. 认证/用户模块

### 1.1 微信授权登录
**接口**: `POST /api/auth/wx-login`  
**说明**: 微信小程序授权登录接口

**请求参数**:
```json
{
  "code": "微信登录凭证code",
  "nickName": "用户昵称（可选）",
  "avatarUrl": "用户头像URL（可选）"
}
```

**参数说明**:
| 参数名 | 必填 | 类型 | 说明 |
|--------|------|------|------|
| code | 是 | String | wx.login()返回的临时code |
| nickName | 否 | String | 微信授权得到的昵称 |
| avatarUrl | 否 | String | 微信授权得到的头像URL |

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "Bearer 1712345678901",
    "user": {
      "id": 1,
      "phone": null,
      "openId": "openid_xxxx",
      "nickname": "用户昵称",
      "avatar": "头像URL",
      "gender": 0,
      "birthday": null,
      "status": 1,
      "createAt": "2026-03-05T09:30:00",
      "updateAt": "2026-03-05T09:30:00"
    }
  }
}
```

**错误响应**:
```json
{
  "code": 500,
  "message": "code不能为空",
  "data": null
}
```

### 1.2 获取当前用户信息
**接口**: `GET /api/user/profile`  
**说明**: 获取当前登录用户的详细信息  
**需要认证**: 是

**请求头**:
```
User-ID: 用户ID
```

**请求参数**: 无

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "phone": "13800138000",
    "openId": "openid_test_001",
    "nickname": "测试用户",
    "avatar": "https://example.com/avatar.jpg",
    "gender": 0,
    "birthday": null,
    "status": 1,
    "createAt": "2026-03-04T17:00:00",
    "updateAt": "2026-03-04T17:00:00"
  }
}
```

**错误响应**:
```json
{
  "code": 500,
  "message": "未登录",
  "data": null
}
```

---

## 2. 分类模块

### 2.1 分类列表
**接口**: `GET /api/categories`  
**说明**: 获取所有启用的分类列表

**请求参数**: 无

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "电子产品",
      "icon": "/icons/electronics.png",
      "sortOrder": 1,
      "status": 1,
      "createAt": "2026-03-04T17:00:00",
      "updateAt": "2026-03-04T17:00:00"
    },
    {
      "id": 2,
      "name": "服装鞋帽",
      "icon": "/icons/clothing.png",
      "sortOrder": 2,
      "status": 1,
      "createAt": "2026-03-04T17:00:00",
      "updateAt": "2026-03-04T17:00:00"
    }
  ]
}
```

**错误响应**:
```json
{
  "code": 500,
  "message": "获取分类列表失败：具体错误信息",
  "data": null
}
```

---

## 实体类结构说明

### MiniProgramCustomer 小程序客户实体
对应数据库表：`app_customer`
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 主键ID |
| phone | String | 手机号（唯一） |
| openId | String | 微信openId（唯一） |
| nickname | String | 用户昵称 |
| avatar | String | 头像URL |
| gender | Integer | 性别（0未知，1男，2女） |
| birthday | LocalDateTime | 生日 |
| status | Integer | 状态（0禁用，1正常） |
| createAt | LocalDateTime | 创建时间 |
| updateAt | LocalDateTime | 更新时间 |

### AdminUser 管理后台用户实体
对应数据库表：`admin_user`
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | Long | 主键ID |
| username | String | 登录用户名（唯一） |
| password | String | 加密密码 |
| nickname | String | 显示昵称 |
| avatar | String | 头像URL |
| role | String | 角色（admin/super_admin） |
| lastLoginAt | LocalDateTime | 最后登录时间 |
| status | Integer | 状态（0禁用，1正常，2锁定） |
| createAt | LocalDateTime | 创建时间 |
| updateAt | LocalDateTime | 更新时间 |

---

## 注意事项
1. 所有接口均支持跨域访问
2. 用户认证接口返回的token为模拟token，实际使用时应替换为JWT
3. 用户信息获取需要在请求头中传递`User-ID`
4. 分类列表默认只返回status=1（启用）的分类，按sortOrder升序排列
5. 项目采用多模块结构，当前文档仅涵盖小程序接口模块(app)
6. 管理后台接口位于独立模块(manage)，端口为8081

## 测试用例

### 小程序接口测试命令

1. **微信登录**:
```bash
curl -X POST http://localhost:8080/api/auth/wx-login \
  -H "Content-Type: application/json" \
  -d '{"code":"test_code","nickName":"测试用户","avatarUrl":"https://example.com/avatar.jpg"}'
```

2. **获取用户信息**:
```bash
curl -H "User-ID: 1" http://localhost:8080/api/user/profile
```

3. **获取分类列表**:
```bash
curl http://localhost:8080/api/categories
```

### 管理后台接口测试命令

1. **管理员登录**:
```bash
curl -X POST http://localhost:8081/api/admin/login \
  -H "Content-Type: application/json" \
  -d '{"username":"13800138000","password":"admin123"}'
```

2. **获取管理员信息**:
```bash
curl -H "Authorization: Bearer your_jwt_token" http://localhost:8081/api/admin/profile
```

3. **退出登录**:
```bash
curl -X POST http://localhost:8081/api/admin/logout
```

## 管理后台接口详细说明

### 管理员登录接口
**接口**: `POST /api/admin/login`  
**端口**: 8081
**认证方式**: 账号密码 → JWT Token

**请求参数**:
```json
{
  "username": "管理员手机号",
  "password": "登录密码"
}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "JWT令牌",
    "userInfo": {
      "id": 1,
      "phone": "13800138000",
      "nickname": "系统管理员",
      "role": "admin",
      "isAdmin": true
    }
  }
}
```

### 管理员信息接口
**接口**: `GET /api/admin/profile`  
**认证方式**: Bearer Token

**请求头**:
```
Authorization: Bearer your_jwt_token
```

### 退出登录接口
**接口**: `POST /api/admin/logout`  
**说明**: 客户端清除本地token即可