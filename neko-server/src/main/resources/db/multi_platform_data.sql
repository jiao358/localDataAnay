CREATE TABLE IF NOT EXISTS multi_platform_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    account_name VARCHAR(100) NOT NULL COMMENT '账号名称',
    account_password VARCHAR(200) NOT NULL COMMENT '账号密码',
    security_info TEXT COMMENT '账号密保内容',
    payment_password VARCHAR(200) COMMENT '支付密码',
    platform_type VARCHAR(20) NOT NULL COMMENT '平台类型',
    platform_url VARCHAR(500) COMMENT '链接地址',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_account_name (account_name)
) COMMENT '多平台账号表';

-- 添加索引
CREATE INDEX idx_platform_type ON multi_platform_data(platform_type);
CREATE INDEX idx_status ON multi_platform_data(status);

-- 插入测试数据
INSERT INTO multi_platform_data (
    account_name, account_password, security_info, 
    payment_password, platform_type, platform_url
) VALUES 
('taobao_test', 'encrypted_pwd_1', '密保问题1：答案1；密保问题2：答案2', 
 'pay_pwd_1', '1', 'https://www.taobao.com'),
('weibo_test', 'encrypted_pwd_2', '邮箱：test@example.com；手机：13800138000', 
 NULL, '3', 'https://www.weibo.com'),
('douyin_test', 'encrypted_pwd_3', '绑定手机：13900139000', 
 'pay_pwd_3', '4', 'https://www.douyin.com'); 