<template>
  <div class="dashboard-container">
    <!-- 顶部导航栏 -->
    <div class="top-navbar">
      <div class="left-section">
        <div class="brand">
          <i class="el-icon-platform-eleme brand-icon"></i>
          <span class="brand-text">Neko Admin</span>
        </div>
      </div>
      <div class="right-section">
        <el-dropdown trigger="click" class="user-dropdown">
          <div class="user-info">
            <i class="el-icon-user-solid avatar-icon"></i>
            <span class="username">{{ username || 'Admin' }}</span>
            <i class="el-icon-caret-bottom"></i>
          </div>
          <el-dropdown-menu slot="dropdown" class="user-dropdown-menu">
            <el-dropdown-item>
              <i class="el-icon-user"></i>个人信息
            </el-dropdown-item>
            <el-dropdown-item>
              <i class="el-icon-key"></i>修改密码
            </el-dropdown-item>
            <el-dropdown-item divided @click.native="handleLogout">
              <i class="el-icon-switch-button"></i>退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="dashboard-text">Welcome to Neko Admin</div>
      <div class="dashboard-subtitle">优雅、简约、高效的后台管理系统</div>
    </div>
    
    <!-- 功能模块区域 -->
    <el-row :gutter="24" class="panel-group">
      <el-col :xs="24" :sm="12" :md="8" :lg="6">
        <router-link to="/system/user">
          <div class="panel-item">
            <div class="panel-content">
              <i class="el-icon-user panel-icon"></i>
              <div class="panel-info">
                <div class="panel-title">用户管理</div>
                <div class="panel-desc">User Management</div>
              </div>
            </div>
          </div>
        </router-link>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="8" :lg="6">
        <router-link to="/system/role">
          <div class="panel-item">
            <div class="panel-content">
              <i class="el-icon-s-custom panel-icon"></i>
              <div class="panel-info">
                <div class="panel-title">角色管理</div>
                <div class="panel-desc">Role Management</div>
              </div>
            </div>
          </div>
        </router-link>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="8" :lg="6">
        <router-link to="/system/permission">
          <div class="panel-item">
            <div class="panel-content">
              <i class="el-icon-key panel-icon"></i>
              <div class="panel-info">
                <div class="panel-title">权限管理</div>
                <div class="panel-desc">Permission Management</div>
              </div>
            </div>
          </div>
        </router-link>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'DashboardPage',
  computed: {
    ...mapGetters([
      'username'
    ])
  },
  methods: {
    handleLogout() {
      this.$confirm('确认退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('user/logout').then(() => {
          this.$router.push('/login')
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  min-height: 100vh;
  background-color: #000000;
  color: #ffffff;
  overflow: hidden;
}

// 顶部导航栏样式
.top-navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  height: 64px;
  background: rgba(28, 28, 30, 0.8);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 215, 0, 0.1);

  .brand {
    display: flex;
    align-items: center;
    
    .brand-icon {
      font-size: 32px;
      margin-right: 12px;
      background: linear-gradient(to right, #FFD700, #FFA500);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }

    .brand-text {
      font-size: 20px;
      font-weight: 500;
      background: linear-gradient(to right, #FFD700, #FFA500);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
  }

  .user-dropdown {
    .user-info {
      display: flex;
      align-items: center;
      cursor: pointer;
      padding: 8px 12px;
      border-radius: 8px;
      transition: all 0.3s ease;

      &:hover {
        background: rgba(255, 255, 255, 0.1);
      }

      .avatar-icon {
        font-size: 24px;
        color: #FFD700;
        margin-right: 8px;
      }

      .username {
        color: #ffffff;
        margin-right: 8px;
      }

      .el-icon-caret-bottom {
        color: #8E8E93;
        font-size: 12px;
      }
    }
  }
}

// 欢迎区域样式
.welcome-section {
  text-align: center;
  padding: 60px 0;
  opacity: 0;
  animation: fadeInUp 1s ease forwards;
  
  .dashboard-text {
    font-size: 48px;
    font-weight: 600;
    background: linear-gradient(to right, #FFD700, #FFA500);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    margin-bottom: 16px;
    letter-spacing: -0.5px;
  }
  
  .dashboard-subtitle {
    font-size: 20px;
    color: #8E8E93;
    font-weight: 300;
  }
}

// 功能模块区域样式
.panel-group {
  padding: 0 24px;
  
  .panel-item {
    background: rgba(28, 28, 30, 0.8);
    border-radius: 16px;
    backdrop-filter: blur(20px);
    padding: 24px;
    margin-bottom: 24px;
    cursor: pointer;
    border: 1px solid rgba(255, 215, 0, 0.1);
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    opacity: 0;
    animation: fadeInUp 1s ease forwards;
    animation-delay: calc(var(--index, 0) * 0.1s);

    &:hover {
      transform: translateY(-8px);
      background: rgba(44, 44, 46, 0.8);
      border-color: rgba(255, 215, 0, 0.3);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);

      .panel-icon {
        color: #FFD700;
        transform: scale(1.1);
      }

      .panel-title {
        background: linear-gradient(to right, #FFD700, #FFA500);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
      }
    }

    .panel-content {
      display: flex;
      align-items: center;
    }

    .panel-icon {
      font-size: 36px;
      color: #ffffff;
      margin-right: 20px;
      transition: all 0.4s ease;
    }

    .panel-info {
      flex: 1;
    }

    .panel-title {
      font-size: 20px;
      font-weight: 500;
      margin-bottom: 8px;
      color: #ffffff;
      transition: all 0.4s ease;
    }

    .panel-desc {
      font-size: 14px;
      color: #8E8E93;
      font-weight: 300;
    }
  }
}

// 动画
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 为每个面板设置不同的动画延迟
.panel-group {
  .el-col {
    &:nth-child(1) .panel-item { --index: 1; }
    &:nth-child(2) .panel-item { --index: 2; }
    &:nth-child(3) .panel-item { --index: 3; }
  }
}

// 下拉菜单样式覆盖
:deep(.el-dropdown-menu) {
  background: rgba(28, 28, 30, 0.95);
  border: 1px solid rgba(255, 215, 0, 0.1);
  backdrop-filter: blur(20px);
  
  .el-dropdown-menu__item {
    color: #ffffff;
    
    i {
      margin-right: 8px;
    }
    
    &:hover {
      background: rgba(255, 215, 0, 0.1);
      color: #FFD700;
    }
    
    &.is-disabled {
      color: #8E8E93;
    }
  }
}

// 响应式调整
@media screen and (max-width: 768px) {
  .top-navbar {
    padding: 0 16px;
    
    .brand-text {
      display: none;
    }
  }

  .welcome-section {
    padding: 40px 0;
    
    .dashboard-text {
      font-size: 32px;
    }
    
    .dashboard-subtitle {
      font-size: 16px;
    }
  }

  .panel-group {
    padding: 0 16px;
  }
}
</style> 