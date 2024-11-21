#!/bin/bash

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 配置变量
BACKEND_PORT=8081
FRONTEND_PORT=8082
MYSQL_PORT=3306
MYSQL_USER="root"
MYSQL_PASSWORD="root"
MYSQL_DATABASE="neko_db"

# 检查必要的命令
check_commands() {
    echo -e "${YELLOW}检查必要的命令...${NC}"
    commands=("java" "mvn" "npm" "mysql")
    for cmd in "${commands[@]}"; do
        if ! command -v $cmd &> /dev/null; then
            echo -e "${RED}错误: $cmd 未安装${NC}"
            exit 1
        fi
    done
    echo -e "${GREEN}所有必要的命令已安装${NC}"
}

# 检查端口占用
check_ports() {
    echo -e "${YELLOW}检查端口占用...${NC}"
    ports=($BACKEND_PORT $FRONTEND_PORT $MYSQL_PORT)
    for port in "${ports[@]}"; do
        if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null ; then
            echo -e "${RED}错误: 端口 $port 已被占用${NC}"
            exit 1
        fi
    done
    echo -e "${GREEN}所有端口可用${NC}"
}

# 初始化数据库
init_database() {
    echo -e "${YELLOW}初始化数据库...${NC}"
    mysql -u$MYSQL_USER -p$MYSQL_PASSWORD <<EOF
    CREATE DATABASE IF NOT EXISTS $MYSQL_DATABASE DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
    USE $MYSQL_DATABASE;
EOF
    
    # 执行所有SQL文件
    for sql_file in neko-server/src/main/resources/db/*.sql; do
        echo -e "${YELLOW}执行SQL文件: $sql_file${NC}"
        mysql -u$MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE < $sql_file
    done
    echo -e "${GREEN}数据库初始化完成${NC}"
}

# 构建后端
build_backend() {
    echo -e "${YELLOW}构建后端...${NC}"
    cd neko-server
    mvn clean package -DskipTests
    cd ..
    echo -e "${GREEN}后端构建完成${NC}"
}

# 构建前端
build_frontend() {
    echo -e "${YELLOW}构建前端...${NC}"
    cd neko-ui
    npm install
    npm run build
    cd ..
    echo -e "${GREEN}前端构建完成${NC}"
}

# 启动服务
start_services() {
    echo -e "${YELLOW}启动服务...${NC}"
    
    # 启动后端（修改这部分）
    cd neko-server
    nohup mvn spring-boot:run > ../neko-server.log 2>&1 &
    echo $! > ../backend.pid
    cd ..
    
    # 等待后端启动完成
    echo -e "${YELLOW}等待后端服务启动...${NC}"
    sleep 10
    
    # 检查后端是否成功启动
    if curl -s http://localhost:${BACKEND_PORT}/api/health > /dev/null; then
        echo -e "${GREEN}后端服务启动成功${NC}"
    else
        echo -e "${RED}后端服务启动失败，请检查 neko-server.log${NC}"
        tail -n 50 neko-server.log
        return 1
    fi
    
    # 启动前端
    cd neko-ui
    nohup npm run serve > ../neko-ui.log 2>&1 &
    echo $! > ../frontend.pid
    cd ..
    
    echo -e "${GREEN}服务启动完成${NC}"
    echo -e "${GREEN}后端日志: neko-server.log${NC}"
    echo -e "${GREEN}前端日志: neko-ui.log${NC}"
}

# 停止服务
stop_services() {
    echo -e "${YELLOW}停止服务...${NC}"
    
    # 停止后端（修改这部分）
    if [ -f backend.pid ]; then
        pid=$(cat backend.pid)
        # 查找 spring-boot 进程
        spring_pid=$(ps aux | grep spring-boot | grep -v grep | awk '{print $2}')
        if [ ! -z "$spring_pid" ]; then
            kill $spring_pid
        fi
        kill $pid 2>/dev/null
        rm backend.pid
    fi
    
    # 停止前端
    if [ -f frontend.pid ]; then
        kill $(cat frontend.pid) 2>/dev/null
        rm frontend.pid
    fi
    
    echo -e "${GREEN}服务已停止${NC}"
    
    # 确保所有相关进程都已停止
    pkill -f "spring-boot"
    pkill -f "npm run serve"
}

# 重启服务
restart_services() {
    stop_services
    start_services
}

# 检查服务状态
check_status() {
    echo -e "${YELLOW}检查服务状态...${NC}"
    
    # 检查后端状态（修改这部分）
    if curl -s http://localhost:${BACKEND_PORT}/api/health > /dev/null; then
        echo -e "${GREEN}后端服务运行中 (端口: ${BACKEND_PORT})${NC}"
    else
        echo -e "${RED}后端服务未运行${NC}"
    fi
    
    # 检查前端状态
    if curl -s http://localhost:${FRONTEND_PORT} > /dev/null; then
        echo -e "${GREEN}前端服务运行中 (端口: ${FRONTEND_PORT})${NC}"
    else
        echo -e "${RED}前端服务未运行${NC}"
    fi
    
    # 显示进程信息
    echo -e "\n${YELLOW}进程信息:${NC}"
    ps aux | grep -E "spring-boot|npm run serve" | grep -v grep
}

# 显示帮助信息
show_help() {
    echo "使用方法: $0 [选项]"
    echo "选项:"
    echo "  install    - 首次安装部署"
    echo "  start      - 启动服务"
    echo "  stop       - 停止服务"
    echo "  restart    - 重启服务"
    echo "  status     - 查看服务状态"
    echo "  help       - 显示帮助信息"
}

# 更新配置文件
update_configs() {
    echo -e "${YELLOW}更新配置文件...${NC}"
    
    # 更新后端数据库配置
    local BACKEND_CONFIG="neko-server/src/main/resources/application.yml"
    if [ -f "$BACKEND_CONFIG" ]; then
        echo -e "${YELLOW}更新后端配置: $BACKEND_CONFIG${NC}"
        # 使用临时文件进行替换
        sed -i.bak "
            s/url: jdbc:mysql:\/\/.*\/${MYSQL_DATABASE}/url: jdbc:mysql:\/\/localhost:${MYSQL_PORT}\/${MYSQL_DATABASE}/g;
            s/username: .*/username: ${MYSQL_USER}/g;
            s/password: .*/password: ${MYSQL_PASSWORD}/g;
            s/port: [0-9]*/port: ${BACKEND_PORT}/g
        " "$BACKEND_CONFIG"
        rm "${BACKEND_CONFIG}.bak"
    else
        echo -e "${RED}错误: 找不到后端配置文件${NC}"
        exit 1
    fi

    # 更新前端API配置
    local FRONTEND_ENV="neko-ui/.env.development"
    if [ -f "$FRONTEND_ENV" ]; then
        echo -e "${YELLOW}更新前端配置: $FRONTEND_ENV${NC}"
        echo "NODE_ENV = 'development'" > "$FRONTEND_ENV"
        echo "VUE_APP_BASE_API = 'http://localhost:${BACKEND_PORT}/api'" >> "$FRONTEND_ENV"
    else
        echo -e "${RED}错误: 找不到前端环境配置文件${NC}"
        exit 1
    fi

    # 更新前端开发服务器配置
    local FRONTEND_CONFIG="neko-ui/vue.config.js"
    if [ -f "$FRONTEND_CONFIG" ]; then
        echo -e "${YELLOW}更新前端开发服务器配置: $FRONTEND_CONFIG${NC}"
        # 使用临时文件进行替换
        sed -i.bak "
            s/port: [0-9]*/port: ${FRONTEND_PORT}/g;
            s/target: 'http:\/\/localhost:[0-9]*'/target: 'http:\/\/localhost:${BACKEND_PORT}'/g
        " "$FRONTEND_CONFIG"
        rm "${FRONTEND_CONFIG}.bak"
    else
        echo -e "${RED}错误: 找不到前端配置文件${NC}"
        exit 1
    fi

    echo -e "${GREEN}配置文件更新完成${NC}"
}

# 主函数
case "$1" in
    "install")
        check_commands
        check_ports
        update_configs
        init_database
        build_backend
        build_frontend
        start_services
        ;;
    "start")
        start_services
        ;;
    "stop")
        stop_services
        ;;
    "restart")
        stop_services
        update_configs
        start_services
        ;;
    "status")
        check_status
        ;;
    "help"|"")
        show_help
        ;;
    *)
        echo -e "${RED}未知选项: $1${NC}"
        show_help
        exit 1
        ;;
esac

exit 0 