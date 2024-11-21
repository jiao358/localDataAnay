# Neko Admin System

## 系统配置说明

### 后端配置
- 配置文件路径：`neko-server/src/main/resources/application.yml`
- 主要配置项：
  ```yaml
  server:
    port: 8081  # 后端服务端口

  spring:
    datasource:
      url: jdbc:mysql://localhost:3306/neko_db  # 数据库连接
      username: root  # 数据库用户名
      password: root  # 数据库密码
  ```

### 前端配置
- 开发环境配置：`neko-ui/.env.development`
  ```
  NODE_ENV = 'development'
  VUE_APP_BASE_API = 'http://localhost:8081/api'  # API基础路径
  ```

- Vue配置文件：`neko-ui/vue.config.js`
  ```javascript
  devServer: {
    port: 8082,  # 前端开发服务器端口
    proxy: {
      '/api': {
        target: 'http://localhost:8081'  # API代理目标
      }
    }
  }
  ```

### 资源路径配置
- 图片上传路径：`http://localhost:8081/uploads/`
- 静态资源路径：`http://localhost:8081/static/`
- API接口路径：`http://localhost:8081/api/`

### 数据库配置
- 数据库名：neko_db
- 字符集：utf8mb4
- 排序规则：utf8mb4_general_ci

### 开发工具建议
- IDE：IntelliJ IDEA / VSCode
- 数据库工具：Navicat / MySQL Workbench
- API测试：Postman

### 启动说明
1. 后端启动：
   ```bash
   cd neko-server
   mvn spring-boot:run
   ```

2. 前端启动：
   ```bash
   cd neko-ui
   npm install
   npm run serve
   ```

### 访问地址
- 前端页面：http://localhost:8082
- 后端接口：http://localhost:8081/api
- 接口文档：http://localhost:8081/swagger-ui.html

### 注意事项
1. 确保 MySQL 服务已启动
2. 确保后端 8081 端口未被占用
3. 确保前端 8082 端口未被占用
4. 确保已执行数据库初始化脚本
