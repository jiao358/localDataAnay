USE neko_db;

CREATE TABLE IF NOT EXISTS taobao_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    item_id VARCHAR(50) NOT NULL COMMENT '淘宝商品ID',
    item_price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    item_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    receiver_address VARCHAR(500) NOT NULL COMMENT '收货人地址',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    receiver_nickname VARCHAR(50) COMMENT '收货人淘宝昵称',
    trade_time DATETIME NOT NULL COMMENT '交易发生时间',
    phone VARCHAR(20) NOT NULL COMMENT '手机号',
    status TINYINT DEFAULT 1 COMMENT '状态：0-无效，1-有效',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '淘宝订单表';

-- 添加索引
CREATE INDEX idx_item_id ON taobao_order(item_id);
CREATE INDEX idx_phone ON taobao_order(phone);
CREATE INDEX idx_receiver_name ON taobao_order(receiver_name);
CREATE INDEX idx_trade_time ON taobao_order(trade_time);

-- 插入测试数据
INSERT INTO taobao_order (item_id, item_price, item_name, receiver_address, receiver_name, receiver_nickname, trade_time, phone)
VALUES 
('654321789', 299.99, 'Apple 20W 快充充电器', '浙江省杭州市西湖区文三路 123 号', '张三', '淘气小猫咪', '2024-01-15 14:30:00', '13800138001'),
('987654321', 89.90, '小米充电宝 10000mAh', '北京市朝阳区建国路 456 号', '李四', '科技达人', '2024-01-16 09:15:00', '13900139002'),
('123456789', 1999.00, 'Redmi K70 手机', '广东省深圳市南山区科技园 789 号', '王五', '数码控', '2024-01-17 16:45:00', '13700137003'); 