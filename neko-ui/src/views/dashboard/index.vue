<template>
  <div class="app-container">
    <!-- 顶部导航栏 -->
    <div class="top-navbar">
      <div class="left-section">
        <div class="brand" @click="goToDashboard">
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

    <!-- 主内容区域 -->
    <div class="main-content">
      <div class="welcome-section">
        <div class="dashboard-text">Welcome to Neko Admin</div>
        <div class="dashboard-subtitle">优雅、简约、高效的后台管理系统</div>
      </div>
      
      <el-row :gutter="24" class="panel-group">
        <!-- 用户管理 -->
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

        <!-- 角色管理 -->
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

        <!-- 权限管理 -->
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

        <!-- 字典管理 -->
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <router-link to="/system/dict">
            <div class="panel-item">
              <div class="panel-content">
                <i class="el-icon-collection panel-icon"></i>
                <div class="panel-info">
                  <div class="panel-title">字典管理</div>
                  <div class="panel-desc">Dictionary Management</div>
                </div>
              </div>
            </div>
          </router-link>
        </el-col>

        <!-- 淘宝交易快照 -->
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <router-link to="/system/order">
            <div class="panel-item">
              <div class="panel-content">
                <i class="el-icon-shopping-cart-full panel-icon"></i>
                <div class="panel-info">
                  <div class="panel-title">淘宝交易快照</div>
                  <div class="panel-desc">Taobao Trade Snapshot</div>
                </div>
              </div>
            </div>
          </router-link>
        </el-col>

        <!-- 销售订单 -->
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <router-link to="/system/sales">
            <div class="panel-item">
              <div class="panel-content">
                <i class="el-icon-shopping-bag-1 panel-icon"></i>
                <div class="panel-info">
                  <div class="panel-title">销售订单</div>
                  <div class="panel-desc">Sales Order</div>
                </div>
              </div>
            </div>
          </router-link>
        </el-col>

        <!-- 平台账号管理 -->
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <router-link to="/system/platform">
            <div class="panel-item">
              <div class="panel-content">
                <i class="el-icon-link panel-icon"></i>
                <div class="panel-info">
                  <div class="panel-title">平台账号</div>
                  <div class="panel-desc">Platform Management</div>
                </div>
              </div>
            </div>
          </router-link>
        </el-col>

        <!-- 知识模型管理 -->
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <router-link to="/system/knowledge">
            <div class="panel-item">
              <div class="panel-content">
                <i class="el-icon-reading panel-icon"></i>
                <div class="panel-info">
                  <div class="panel-title">知识模型</div>
                  <div class="panel-desc">Knowledge Management</div>
                </div>
              </div>
            </div>
          </router-link>
        </el-col>

        <!-- 供应链商品 -->
        <el-col :xs="24" :sm="12" :md="8" :lg="6">
          <router-link to="/system/supply-chain">
            <div class="panel-item">
              <div class="panel-content">
                <i class="el-icon-goods panel-icon"></i>
                <div class="panel-info">
                  <div class="panel-title">供应链商品</div>
                  <div class="panel-desc">Supply Chain Products</div>
                </div>
              </div>
            </div>
          </router-link>
        </el-col>
      </el-row>
    </div>
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
    goToDashboard() {
      if (this.$route.path !== '/dashboard') {
        this.$router.push('/dashboard')
      }
    },
    handleLogout() {
      this.$confirm('确认退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          return this.$store.dispatch('user/logout')
        })
        .then(() => {
          this.$router.push('/login')
        })
        .catch(error => {
          if (error !== 'cancel') {
            console.error('Logout failed:', error)
          }
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  min-height: 100vh;
  background-color: #000000;
}

.top-navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  height: 64px;
  background: rgba(28, 28, 30, 0.8);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 215, 0, 0.1);
}

.brand {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: all 0.3s ease;
  
  &:hover {
    background: rgba(255, 255, 255, 0.1);
    
    .brand-icon {
      transform: scale(1.1);
    }
  }
  
  .brand-icon {
    font-size: 32px;
    margin-right: 12px;
    background: linear-gradient(to right, #FFD700, #FFA500);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    transition: transform 0.3s ease;
  }

  .brand-text {
    font-size: 20px;
    font-weight: 500;
    background: linear-gradient(to right, #FFD700, #FFA500);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }
}

.main-content {
  padding: 24px;
}

.welcome-section {
  text-align: center;
  padding: 40px 0;
  margin-bottom: 40px;
  
  .dashboard-text {
    font-size: 30px;
    font-weight: bold;
    color: #FFD700;
    margin-bottom: 10px;
  }
  
  .dashboard-subtitle {
    font-size: 16px;
    color: #FFA500;
    opacity: 0.8;
  }
}

.panel-group {
  .panel-item {
    background: rgba(255, 255, 255, 0.1);
    border-radius: 8px;
    margin-bottom: 24px;
    transition: all 0.3s ease;
    cursor: pointer;
    
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 5px 15px rgba(255, 215, 0, 0.1);
      background: rgba(255, 255, 255, 0.15);
      
      .panel-icon {
        transform: scale(1.1);
      }
    }
    
    .panel-content {
      padding: 24px;
      display: flex;
      align-items: center;
      
      .panel-icon {
        font-size: 36px;
        margin-right: 16px;
        color: #FFD700;
        transition: transform 0.3s ease;
      }
      
      .panel-info {
        .panel-title {
          font-size: 18px;
          font-weight: 500;
          color: #FFD700;
          margin-bottom: 4px;
        }
        
        .panel-desc {
          font-size: 14px;
          color: #FFA500;
          opacity: 0.8;
        }
      }
    }
  }
}

.user-dropdown {
  cursor: pointer;
  
  .user-info {
    display: flex;
    align-items: center;
    color: #ffffff;
    
    .avatar-icon {
      font-size: 20px;
      margin-right: 8px;
    }
    
    .username {
      margin-right: 4px;
    }
  }
}

:deep(.user-dropdown-menu) {
  .el-dropdown-menu__item {
    i {
      margin-right: 8px;
    }
  }
}
</style> 