CREATE TABLE IF NOT EXISTS knowledge_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    content_name VARCHAR(200) NOT NULL COMMENT '内容名称',
    content_category VARCHAR(50) NOT NULL COMMENT '内容分类',
    content_url VARCHAR(500) COMMENT '内容链接',
    content_summary TEXT COMMENT '内容总结',
    process_image VARCHAR(500) COMMENT '知识点流程图片',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '知识模型表';

-- 添加索引
CREATE INDEX idx_content_category ON knowledge_data(content_category);
CREATE INDEX idx_create_time ON knowledge_data(create_time); 