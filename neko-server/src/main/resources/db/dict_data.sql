CREATE TABLE IF NOT EXISTS sys_dict_type (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    dict_name VARCHAR(100) NOT NULL COMMENT '字典名称',
    dict_code VARCHAR(100) NOT NULL COMMENT '字典编码',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_dict_code (dict_code)
) COMMENT '字典类型表';

CREATE TABLE IF NOT EXISTS sys_dict_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    dict_code VARCHAR(100) NOT NULL COMMENT '字典编码',
    dict_label VARCHAR(100) NOT NULL COMMENT '字典标签',
    dict_value VARCHAR(100) NOT NULL COMMENT '字典值',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '字典数据表';

-- 插入字典类型
INSERT INTO sys_dict_type (dict_name, dict_code, remark) VALUES 
('商品类目', 'item_category', '商品分类'),
('交易平台', 'trade_platform', '交易平台类型');


-- 插入字典数据
INSERT INTO sys_dict_data (dict_code, dict_label, dict_value, sort_order) VALUES 
-- 商品类目
('item_category', '数码电子', '1', 1),
('item_category', '服装鞋帽', '2', 2),
('item_category', '美妆护肤', '3', 3),
('item_category', '家居用品', '4', 4),
('item_category', '食品饮料', '5', 5),
-- 交易平台
('trade_platform', '淘宝', '1', 1),
('trade_platform', '京东', '2', 2),
('trade_platform', '拼多多', '3', 3),
('trade_platform', '抖音', '4', 4),
('trade_platform', '小红书', '5', 5);

INSERT INTO sys_dict_type (dict_name, dict_code, remark) VALUES
    ('内容分类', 'content_category', '知识模型内容分类');

-- 插入内容分类字典数据
INSERT INTO sys_dict_data (dict_code, dict_label, dict_value, sort_order) VALUES
                                                                              ('content_category', '电商运营操作', '1', 1),
                                                                              ('content_category', 'AI商品图', '2', 2),
                                                                              ('content_category', 'AI内容生成', '3', 3),
                                                                              ('content_category', 'AI视频生成', '4', 4),
                                                                              ('content_category', '剪辑技巧', '5', 5),
                                                                              ('content_category', 'AI剪辑', '6', 6),
                                                                              ('content_category', 'AI未分类', '7', 7),
                                                                              ('content_category', '工具介绍', '8', 8),
                                                                              ('content_category', '商业模式', '9', 9),
                                                                              ('content_category', '赚钱赛道', '10', 10),
                                                                              ('content_category', '菜谱', '11', 11),
                                                                              ('content_category', '深度思考', '12', 12);