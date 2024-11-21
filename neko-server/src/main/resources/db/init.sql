-- 创建数据库
CREATE DATABASE IF NOT EXISTS neko_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE neko_db;

-- 创建用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT '系统用户表';

-- 插入初始管理员用户
INSERT INTO sys_user (username, password, real_name, email, status)
VALUES ('admin', '123456', '系统管理员', 'admin@example.com', 1);

-- 插入测试用户数据
INSERT INTO sys_user (username, password, real_name, email, phone, status)
VALUES 
('test1', '123456', '测试用户1', 'test1@example.com', '13800000001', 1),
('test2', '123456', '测试用户2', 'test2@example.com', '13800000002', 1),
('test3', '123456', '测试用户3', 'test3@example.com', '13800000003', 0);

-- 创建角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
    description VARCHAR(200) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_role_code (role_code)
) COMMENT '系统角色表';

-- 创建权限表
CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(50) NOT NULL COMMENT '权限编码',
    permission_type TINYINT NOT NULL COMMENT '权限类型：1-菜单，2-按钮，3-接口',
    parent_id BIGINT COMMENT '父权限ID',
    path VARCHAR(200) COMMENT '路由路径',
    component VARCHAR(200) COMMENT '前端组件',
    icon VARCHAR(50) COMMENT '图标',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_permission_code (permission_code)
) COMMENT '系统权限表';

-- 创建用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    UNIQUE KEY uk_user_role (user_id, role_id)
) COMMENT '用户角色关联表';

-- 创建角色权限关联表
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_role_permission (role_id, permission_id)
) COMMENT '角色权限关联表';

-- 插入初始角色数据
INSERT INTO sys_role (role_name, role_code, description, status)
VALUES ('超级管理员', 'ROLE_ADMIN', '系统超级管理员', 1);

-- 插入初始权限数据
INSERT INTO sys_permission (permission_name, permission_code, permission_type, path, component, icon, sort_order)
VALUES 
('系统管理', 'system', 1, '/system', 'Layout', 'setting', 1),
('用户管理', 'system:user', 1, '/system/user', 'system/user/index', 'user', 1),
('角色管理', 'system:role', 1, '/system/role', 'system/role/index', 'peoples', 2),
('权限管理', 'system:permission', 1, '/system/permission', 'system/permission/index', 'tree-table', 3);

-- 为超级管理员分配所有权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission;

-- 为admin用户分配超级管理员角色
INSERT INTO sys_user_role (user_id, role_id)
VALUES (1, 1); 