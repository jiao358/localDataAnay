<template>
  <div class="navbar">
    <hamburger class="hamburger-container" />
    
    <breadcrumb class="breadcrumb-container" />

    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <i class="el-icon-user-solid avatar-icon"></i>
          <span class="username">{{ name }}</span>
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">
          <el-dropdown-item>
            <i class="el-icon-user"></i>个人信息
          </el-dropdown-item>
          <el-dropdown-item>
            <i class="el-icon-key"></i>修改密码
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <i class="el-icon-switch-button"></i>退出登录
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: rgba(28, 28, 30, 0.8);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 215, 0, 0.1);
  color: #FFD700;

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    padding: 0 15px;
    cursor: pointer;
    transition: background .3s;
    
    &:hover {
      background: rgba(255, 255, 255, 0.1);
    }
  }

  .breadcrumb-container {
    float: left;
    color: #FFD700;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;
    margin-right: 20px;
    
    .avatar-container {
      .avatar-wrapper {
        display: flex;
        align-items: center;
        padding: 0 8px;
        cursor: pointer;

        .avatar-icon {
          font-size: 20px;
          margin-right: 8px;
        }

        .username {
          margin-right: 4px;
        }

        .el-icon-caret-bottom {
          font-size: 12px;
        }
      }
    }
  }
}

:deep(.user-dropdown) {
  .el-dropdown-menu__item {
    i {
      margin-right: 8px;
    }
  }
}
</style> 