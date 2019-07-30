### 商盟助手电商平台API接口文档

## 接口概览

| 模块 | 接口数量 | 完成状态 |
|------|----------|----------|
| 认证/用户 | 2 | ✅ 已完成 |
| 分类 | 1 | ✅ 已完成 |
| 商品 | 5 | ⬜ 部分完成 |
| 文件上传 | 1 | ⬜ 未开始 |
| 收货地址 | 5 | ⬜ 未开始 |
| 订单 | 4 | ⬜ 未开始 |

---

## 1. 认证/用户模块

### 1.1 微信授权登录
**接口**: `POST /api/auth/wx-login`  
**说明**: 微信授权登录，无需token

**请求参数**:
```json
{
  "code": "081VS8000bQrLC1xXX200oE000VS800k",
  "nickName": "张三",
  "avatarUrl": "https://thirdwx.qlogo.cn/mmopen/vi_32/xxx.jpg"
}
```

**参数说明**:
- `code`: 必填，wx.login()返回的临时code
- `nickName`: 选填，微信授权得到的昵称
- `avatarUrl`: 选填，微信授权得到的头像URL

**响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.xxx",
    "user": {
      "id": 1,
      "openId": "openid_xxx",
      "phone": null,
      "nickname": "张三",
      "avatar": "https://thirdwx.qlogo.cn/mmopen/vi_32/xxx.jpg",
      "gender": 0,
      "birthday": null,
      "status": 1,
      "createdAt": "2026-03-04T17:00:00",
      "updatedAt": "2026-03-04T17:00:00"
    }
  }
}
```

### 1.2 获取当前用户信息
**接口**: `GET /api/user/profile`  
**说明**: 获取当前登录用户的信息  
**需要认证**: 是

**请求头**:
```
User-ID: 1
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "phone": "13800138000",
    "nickname": "用户8000",
    "avatar": null,
    "gender": 0,
    "birthday": null,
    "status": 1,
    "createdAt": "2026-03-04T17:00:00",
    "updatedAt": "2026-03-04T17:00:00"
  }
}
```

---

## 2. 分类模块

### 2.1 分类列表
**接口**: `GET /api/categories`  
**说明**: 获取所有启用的分类列表

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
      "createdAt": "2026-03-04T17:00:00",
      "updatedAt": "2026-03-04T17:00:00"
    },
    {
      "id": 2,
      "name": "服装鞋帽",
      "icon": "/icons/clothing.png",
      "sortOrder": 2,
      "status": 1,
      "createdAt": "2026-03-04T17:00:00",
      "updatedAt": "2026-03-04T17:00:00"
    }
  ]
}
```

---

## 3. 商品模块（部分完成）

### 3.1 商品列表
**接口**: `GET /api/products`  
**说明**: 获取商品列表，支持按分类筛选

**请求参数**:
- `categoryId`: 分类ID（可选）
- `page`: 页码（可选，默认0）
- `size`: 每页大小（可选，默认10）

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "content": [
      {
        "id": 1,
        "title": "iPhone 15 Pro",
        "description": "最新款苹果手机",
        "category": {
          "id": 1,
          "name": "电子产品"
        },
        "price": 8999.00,
        "originalPrice": 9999.00,
        "stock": 100,
        "images": ["/images/iphone15.jpg"],
        "details": "{}",
        "recommend": false,
        "hot": true,
        "status": 1,
        "createdAt": "2026-03-04T17:00:00",
        "updatedAt": "2026-03-04T17:00:00"
      }
    ],
    "totalElements": 100,
    "totalPages": 10,
    "number": 0,
    "size": 10
  }
}
```

### 3.2 商品详情
**接口**: `GET /api/products/{id}`  
**说明**: 获取指定商品的详细信息

### 3.3 首页推荐
**接口**: `GET /api/products/recommend?limit=6`  
**说明**: 获取推荐商品列表

### 3.4 首页热门
**接口**: `GET /api/products/hot?limit=6`  
**说明**: 获取热门商品列表

---

## 错误响应格式

所有接口统一使用以下错误响应格式：

```json
{
  "code": 500,
  "message": "错误描述信息",
  "data": null
}
```

常见错误码：
- `200`: 成功
- `400`: 请求参数错误
- `401`: 未认证
- `403`: 权限不足
- `404`: 资源不存在
- `500`: 服务器内部错误

---

## 测试用例

### cURL测试命令

1. **发送验证码**:
```bash
curl -X POST http://localhost:8080/api/auth/sms \
  -H "Content-Type: application/json" \
  -d '{"phone":"13800138000"}'
```

2. **登录**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"phone":"13800138000","code":"123456"}'
```

3. **获取分类列表**:
```bash
curl http://localhost:8080/api/categories
```

4. **获取商品列表**:
```bash
curl "http://localhost:8080/api/products?page=0&size=10"
```

5. **获取推荐商品**:
```bash
curl "http://localhost:8080/api/products/recommend?limit=6"
```