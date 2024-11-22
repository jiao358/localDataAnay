-- 创建供应链商品表
CREATE TABLE supply_chain_product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    product_name VARCHAR(100) NOT NULL COMMENT '商品名称',
    category_code VARCHAR(50) NOT NULL COMMENT '商品类目编码',
    wholesale_price DECIMAL(10, 2) NOT NULL COMMENT '商品批发价',
    limit_price DECIMAL(10, 2) NOT NULL COMMENT '商品限售价',
    unit_code VARCHAR(50) NOT NULL COMMENT '商品单位编码',
    quantity INT NOT NULL DEFAULT 0 COMMENT '商品数量',
    supplier_company VARCHAR(200) NOT NULL COMMENT '供应商公司',
    supplier_contact VARCHAR(50) NOT NULL COMMENT '供应商联系人',
    supplier_phone VARCHAR(20) NOT NULL COMMENT '供应商联系电话',
    supplier_wechat VARCHAR(50) COMMENT '供应商微信',
    product_code VARCHAR(50) NOT NULL COMMENT '商品ID',
    main_image VARCHAR(255) NOT NULL COMMENT '商品主图URL',
    other_images TEXT COMMENT '商品其他图片URLs，JSON数组格式',
    material_url TEXT COMMENT '商品素材链接',
    description TEXT COMMENT '商品其他描述',
    batch_time DATETIME NOT NULL COMMENT '产品批次时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建者',
    update_by VARCHAR(50) COMMENT '更新者',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0未删除，1已删除',
    version INT NOT NULL DEFAULT 1 COMMENT '乐观锁版本号',
    UNIQUE KEY uk_product_code (product_code),
    KEY idx_category (category_code),
    KEY idx_supplier (supplier_company),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应链商品表';

-- 初始化字典数据：商品单位
INSERT INTO sys_dict_type (dict_name, dict_code, status, remark, create_time, update_time) 
VALUES ('商品单位', 'product_unit', 1, '商品单位字典', NOW(), NOW());

INSERT INTO sys_dict_data (dict_code, dict_label, dict_value, sort_order, status, remark, create_time, update_time) 
VALUES 
('product_unit', '箱', 'BOX', 1, 1, '商品单位-箱', NOW(), NOW()),
('product_unit', '个', 'PIECE', 2, 1, '商品单位-个', NOW(), NOW());

-- 初始化字典数据：商品类目
INSERT INTO sys_dict_type (dict_name, dict_code, status, remark, create_time, update_time) 
VALUES ('商品类目', 'product_category', 1, '商品类目字典', NOW(), NOW());

INSERT INTO sys_dict_data (dict_code, dict_label, dict_value, sort_order, status, remark, create_time, update_time) 
VALUES 
('product_category', '数码电子', 'DIGITAL', 1, 1, '商品类目-数码电子', NOW(), NOW()),
('product_category', '服装服饰', 'CLOTHING', 2, 1, '商品类目-服装服饰', NOW(), NOW()),
('product_category', '家居用品', 'HOME', 3, 1, '商品类目-家居用品', NOW(), NOW()),
('product_category', '食品饮料', 'FOOD', 4, 1, '商品类目-食品饮料', NOW(), NOW()),
('product_category', '美妆护肤', 'BEAUTY', 5, 1, '商品类目-美妆护肤', NOW(), NOW()),
('product_category', '母婴用品', 'BABY', 6, 1, '商品类目-母婴用品', NOW(), NOW()),
('product_category', '运动户外', 'SPORTS', 7, 1, '商品类目-运动户外', NOW(), NOW()),
('product_category', '图书音像', 'BOOKS', 8, 1, '商品类目-图书音像', NOW(), NOW()),
('product_category', '其他', 'OTHERS', 9, 1, '商品类目-其他', NOW(), NOW()); 