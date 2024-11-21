CREATE TABLE IF NOT EXISTS order_sales (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    customer_name VARCHAR(50) NOT NULL COMMENT '客户名称',
    customer_gender VARCHAR(10) NOT NULL COMMENT '客户性别',
    customer_age INT COMMENT '客户年龄',
    item_category VARCHAR(50) NOT NULL COMMENT '商品类目',
    item_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    item_price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    item_quantity INT NOT NULL COMMENT '商品数量',
    item_sku VARCHAR(100) NOT NULL COMMENT '商品SKU',
    delivery_address VARCHAR(500) NOT NULL COMMENT '快递地址',
    contact_phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    trade_platform VARCHAR(50) NOT NULL COMMENT '交易平台',
    sales_person VARCHAR(50) NOT NULL COMMENT '销售人员名称',
    order_time DATETIME NOT NULL COMMENT '订单时间',
    delivery_time DATETIME COMMENT '快递时间',
    tracking_number VARCHAR(100) COMMENT '快递邮寄编号',
    status TINYINT DEFAULT 1 COMMENT '状态：0-无效，1-有效',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '销售订单表';

-- 添加索引
CREATE INDEX idx_customer_name ON order_sales(customer_name);
CREATE INDEX idx_item_category ON order_sales(item_category);
CREATE INDEX idx_trade_platform ON order_sales(trade_platform);
CREATE INDEX idx_sales_person ON order_sales(sales_person);
CREATE INDEX idx_order_time ON order_sales(order_time);

-- 插入测试数据
INSERT INTO order_sales (
    customer_name, customer_gender, customer_age, 
    item_category, item_name, item_price, item_quantity, item_sku,
    delivery_address, contact_phone, trade_platform, sales_person,
    order_time, delivery_time, tracking_number
) VALUES 
('张三', '男', 25, '1', 'iPhone 15 Pro Max', 9999.00, 1, 'IP15PM-256G-BLACK',
 '浙江省杭州市西湖区文三路 123 号', '13800138001', '1', '李四',
 '2024-01-15 14:30:00', '2024-01-16 10:00:00', 'SF1234567890'),
('王五', '女', 30, '3', '兰蔻小黑瓶', 1299.00, 2, 'LANCOME-50ML',
 '北京市朝阳区建国路 456 号', '13900139002', '5', '赵六',
 '2024-01-16 09:15:00', '2024-01-17 11:30:00', 'YT9876543210'); 