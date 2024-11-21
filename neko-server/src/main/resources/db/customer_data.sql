CREATE TABLE IF NOT EXISTS customer_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    customer_name VARCHAR(50) NOT NULL COMMENT '客户姓名',
    channel VARCHAR(20) NOT NULL COMMENT '客户渠道',
    contact VARCHAR(50) NOT NULL COMMENT '联系方式',
    address VARCHAR(500) COMMENT '客户地址',
    communication_count INT DEFAULT 0 COMMENT '沟通次数',
    last_intention VARCHAR(20) COMMENT '上次沟通意愿',
    value_level VARCHAR(20) COMMENT '预估价值等级',
    item_category VARCHAR(100) COMMENT '淘宝商品类目',
    status TINYINT DEFAULT 1 COMMENT '状态：0-无效，1-有效',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '客户数据表';

-- 添加索引
CREATE INDEX idx_customer_name ON customer_data(customer_name);
CREATE INDEX idx_channel ON customer_data(channel);
CREATE INDEX idx_contact ON customer_data(contact);
CREATE INDEX idx_value_level ON customer_data(value_level);

-- 插入测试数据
INSERT INTO customer_data (
    customer_name, channel, contact, address, 
    communication_count, last_intention, value_level, item_category
) VALUES 
('张三', '淘宝', '13800138001', '浙江省杭州市西湖区', 2, '愿意', '300到500元', '数码配件'),
('李四', '抖音', '13900139002', '北京市朝阳区', 1, '中立', '100到300元', '美妆护肤'),
('王五', '小红书', '13700137003', '广东省深圳市南山区', 3, '推迟意愿', '500元以上', '奢侈品'); 