-- 电商系统数据库表结构设计 (PostgreSQL)

-- 创建数据库
CREATE DATABASE smzs_mall;

-- 使用数据库
\c smzs_mall;

-- 1. 用户表
CREATE TABLE users (
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

COMMENT ON COLUMN users.phone IS '手机号';
COMMENT ON COLUMN users.open_id IS '微信openId';
COMMENT ON COLUMN users.nickname IS '昵称';
COMMENT ON COLUMN users.avatar IS '头像URL';
COMMENT ON COLUMN users.gender IS '性别：0未知，1男，2女';
COMMENT ON COLUMN users.birthday IS '生日';
COMMENT ON COLUMN users.status IS '状态：0禁用，1正常';
COMMENT ON COLUMN users.creater IS '创建人';
COMMENT ON COLUMN users.updater IS '修改人';
COMMENT ON COLUMN users.create_at IS '创建时间';
COMMENT ON COLUMN users.update_at IS '更新时间';
COMMENT ON COLUMN users.delete IS '逻辑删除：0正常，1删除';

-- 2. 分类表
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

COMMENT ON COLUMN categories.name IS '分类名称';
COMMENT ON COLUMN categories.icon IS '图标URL';
COMMENT ON COLUMN categories.sort_order IS '排序';
COMMENT ON COLUMN categories.status IS '状态：0隐藏，1显示';
COMMENT ON COLUMN categories.creater IS '创建人';
COMMENT ON COLUMN categories.updater IS '修改人';
COMMENT ON COLUMN categories.create_at IS '创建时间';
COMMENT ON COLUMN categories.update_at IS '更新时间';
COMMENT ON COLUMN categories.delete IS '逻辑删除：0正常，1删除';

-- 3. 商品表
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

COMMENT ON COLUMN products.title IS '商品标题';
COMMENT ON COLUMN products.description IS '商品描述';
COMMENT ON COLUMN products.price IS '价格';
COMMENT ON COLUMN products.original_price IS '原价';
COMMENT ON COLUMN products.stock IS '库存';
COMMENT ON COLUMN products.images IS '商品图片数组';
COMMENT ON COLUMN products.details IS '商品详情JSON';
COMMENT ON COLUMN products.is_recommend IS '是否推荐';
COMMENT ON COLUMN products.is_hot IS '是否热门';
COMMENT ON COLUMN products.status IS '状态：0下架，1上架';
COMMENT ON COLUMN products.creater IS '创建人';
COMMENT ON COLUMN products.updater IS '修改人';
COMMENT ON COLUMN products.create_at IS '创建时间';
COMMENT ON COLUMN products.update_at IS '更新时间';
COMMENT ON COLUMN products.delete IS '逻辑删除：0正常，1删除';

-- 4. 收货地址表
CREATE TABLE addresses (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
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

COMMENT ON COLUMN addresses.name IS '收货人姓名';
COMMENT ON COLUMN addresses.phone IS '联系电话';
COMMENT ON COLUMN addresses.province IS '省份';
COMMENT ON COLUMN addresses.city IS '城市';
COMMENT ON COLUMN addresses.district IS '区县';
COMMENT ON COLUMN addresses.detail_address IS '详细地址';
COMMENT ON COLUMN addresses.is_default IS '是否默认地址';
COMMENT ON COLUMN addresses.creater IS '创建人';
COMMENT ON COLUMN addresses.updater IS '修改人';
COMMENT ON COLUMN addresses.create_at IS '创建时间';
COMMENT ON COLUMN addresses.update_at IS '更新时间';
COMMENT ON COLUMN addresses.delete IS '逻辑删除：0正常，1删除';

-- 5. 订单表
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_no VARCHAR(50) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id),
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

COMMENT ON COLUMN orders.order_no IS '订单号';
COMMENT ON COLUMN orders.total_amount IS '订单总金额';
COMMENT ON COLUMN orders.status IS '订单状态：0待付款，1已付款，2已发货，3已完成，4已取消';
COMMENT ON COLUMN orders.payment_method IS '支付方式：1微信支付，2支付宝';
COMMENT ON COLUMN orders.payment_time IS '支付时间';
COMMENT ON COLUMN orders.delivery_time IS '发货时间';
COMMENT ON COLUMN orders.finish_time IS '完成时间';
COMMENT ON COLUMN orders.cancel_time IS '取消时间';
COMMENT ON COLUMN orders.remark IS '订单备注';
COMMENT ON COLUMN orders.creater IS '创建人';
COMMENT ON COLUMN orders.updater IS '修改人';
COMMENT ON COLUMN orders.create_at IS '创建时间';
COMMENT ON COLUMN orders.update_at IS '更新时间';
COMMENT ON COLUMN orders.delete IS '逻辑删除：0正常，1删除';

-- 6. 订单项表
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

COMMENT ON COLUMN order_items.quantity IS '购买数量';
COMMENT ON COLUMN order_items.price IS '单价';
COMMENT ON COLUMN order_items.total_price IS '小计';
COMMENT ON COLUMN order_items.creater IS '创建人';
COMMENT ON COLUMN order_items.updater IS '修改人';
COMMENT ON COLUMN order_items.create_at IS '创建时间';
COMMENT ON COLUMN order_items.update_at IS '更新时间';
COMMENT ON COLUMN order_items.delete IS '逻辑删除：0正常，1删除';

-- 7. 验证码记录表
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

COMMENT ON COLUMN sms_codes.phone IS '手机号';
COMMENT ON COLUMN sms_codes.code IS '验证码';
COMMENT ON COLUMN sms_codes.type IS '验证码类型：1登录';
COMMENT ON COLUMN sms_codes.expire_time IS '过期时间';
COMMENT ON COLUMN sms_codes.is_used IS '是否已使用';
COMMENT ON COLUMN sms_codes.creater IS '创建人';
COMMENT ON COLUMN sms_codes.updater IS '修改人';
COMMENT ON COLUMN sms_codes.create_at IS '创建时间';
COMMENT ON COLUMN sms_codes.update_at IS '更新时间';
COMMENT ON COLUMN sms_codes.delete IS '逻辑删除：0正常，1删除';

-- 8. 文件上传记录表
CREATE TABLE file_uploads (
    id BIGSERIAL PRIMARY KEY,
    original_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_size BIGINT,
    mime_type VARCHAR(100),
    user_id BIGINT REFERENCES users(id),
    creater BIGINT,
    updater BIGINT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delete SMALLINT DEFAULT 0
);

COMMENT ON COLUMN file_uploads.original_name IS '原始文件名';
COMMENT ON COLUMN file_uploads.file_path IS '文件路径';
COMMENT ON COLUMN file_uploads.file_size IS '文件大小(字节)';
COMMENT ON COLUMN file_uploads.mime_type IS 'MIME类型';
COMMENT ON COLUMN file_uploads.user_id IS '上传用户ID';
COMMENT ON COLUMN file_uploads.creater IS '创建人';
COMMENT ON COLUMN file_uploads.updater IS '修改人';
COMMENT ON COLUMN file_uploads.create_at IS '创建时间';
COMMENT ON COLUMN file_uploads.update_at IS '更新时间';
COMMENT ON COLUMN file_uploads.delete IS '逻辑删除：0正常，1删除';

-- 创建索引
CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_users_open_id ON users(open_id);
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_recommend ON products(is_recommend) WHERE is_recommend = TRUE;
CREATE INDEX idx_products_hot ON products(is_hot) WHERE is_hot = TRUE;
CREATE INDEX idx_addresses_user ON addresses(user_id);
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_sms_codes_phone_expire ON sms_codes(phone, expire_time) WHERE is_used = FALSE;
CREATE INDEX idx_file_uploads_user ON file_uploads(user_id);

-- 插入初始数据
-- 初始分类数据
INSERT INTO categories (name, icon, sort_order, creater, updater) VALUES 
('电子产品', '/icons/electronics.png', 1, 1, 1),
('服装鞋帽', '/icons/clothing.png', 2, 1, 1),
('家居用品', '/icons/home.png', 3, 1, 1),
('美妆护肤', '/icons/beauty.png', 4, 1, 1),
('食品饮料', '/icons/food.png', 5, 1, 1);

-- 初始管理员用户
INSERT INTO users (phone, nickname, status, creater, updater) VALUES 
('13800138000', '系统管理员', 1, 1, 1);

-- 初始微信测试用户
INSERT INTO users (open_id, nickname, avatar, status, creater, updater) VALUES 
('openid_test_001', '测试用户', 'https://example.com/avatar.jpg', 1, 1, 1);