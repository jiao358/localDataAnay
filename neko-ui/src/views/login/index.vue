<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-form">
        <div class="title-container">
          <img src="@/assets/logo.png" class="logo" alt="logo">
          <h3 class="title">Neko Admin</h3>
          <p class="subtitle">欢迎回来 (｡･ω･｡)</p>
        </div>

        <div class="form-item">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            type="text"
            class="input-with-icon"
          >
            <i slot="prefix" class="el-icon-user"></i>
          </el-input>
        </div>

        <div class="form-item">
          <el-input
            v-model="loginForm.password"
            placeholder="密码"
            :type="passwordType"
            class="input-with-icon"
            @keyup.enter.native="handleLogin"
          >
            <i slot="prefix" class="el-icon-lock"></i>
            <i
              slot="suffix"
              class="password-eye"
              :class="passwordType === 'password' ? 'el-icon-view' : 'el-icon-hide'"
              @click="showPwd"
            ></i>
          </el-input>
        </div>

        <el-button
          :loading="loading"
          type="primary"
          class="login-button"
          @click="handleLogin"
        >
          {{ loading ? '登录中...' : '登录' }}
        </el-button>

        <div class="tips">
          <span>默认账号: admin</span>
          <span>默认密码: 123456</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LoginPage',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      passwordType: 'password',
      loading: false
    }
  },
  methods: {
    showPwd() {
      this.passwordType = this.passwordType === 'password' ? '' : 'password'
    },
    handleLogin() {
      if (!this.loginForm.username || !this.loginForm.password) {
        this.$message.error('请输入用户名和密码 (｡ŏ_ŏ)')
        return
      }
      this.loading = true
      this.$store.dispatch('user/login', this.loginForm)
        .then(() => {
          this.$router.push('/')
          this.loading = false
        })
        .catch(() => {
          this.$message.error('登录失败，请检查用户名和密码 (´;ω;｀)')
          this.loading = false
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1e1e1e 0%, #2d3436 100%);

  .login-box {
    width: 420px;
    padding: 40px;
    background: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);
    border-radius: 16px;
    box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
    border: 1px solid rgba(255, 255, 255, 0.18);

    .login-form {
      .title-container {
        text-align: center;
        margin-bottom: 40px;

        .logo {
          width: 64px;
          height: 64px;
          margin-bottom: 16px;
        }

        .title {
          font-size: 28px;
          color: #ffffff;
          margin-bottom: 8px;
          font-weight: 500;
        }

        .subtitle {
          color: #a0aec0;
          font-size: 16px;
          margin: 0;
        }
      }

      .form-item {
        margin-bottom: 24px;

        .el-input {
          .el-input__inner {
            height: 50px;
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 8px;
            color: #ffffff;
            font-size: 16px;
            padding-left: 45px;

            &:focus {
              border-color: #409EFF;
              background: rgba(255, 255, 255, 0.1);
            }

            &::placeholder {
              color: #718096;
            }
          }

          .el-input__prefix {
            left: 15px;
            color: #718096;
            font-size: 18px;
          }

          .password-eye {
            color: #718096;
            font-size: 18px;
            cursor: pointer;
            margin-right: 10px;

            &:hover {
              color: #ffffff;
            }
          }
        }
      }

      .login-button {
        width: 100%;
        height: 50px;
        border-radius: 8px;
        font-size: 16px;
        font-weight: 500;
        background: linear-gradient(45deg, #3498db, #2980b9);
        border: none;
        margin-bottom: 20px;
        transition: transform 0.3s ease;

        &:hover {
          transform: translateY(-2px);
          background: linear-gradient(45deg, #2980b9, #3498db);
        }

        &:active {
          transform: translateY(0);
        }
      }

      .tips {
        text-align: center;
        color: #718096;
        font-size: 14px;

        span {
          margin: 0 10px;
          opacity: 0.8;

          &:hover {
            opacity: 1;
          }
        }
      }
    }
  }
}

// 自定义 Element UI 组件样式
::v-deep .el-message {
  background: rgba(0, 0, 0, 0.8) !important;
  border: 1px solid rgba(255, 255, 255, 0.1) !important;
  backdrop-filter: blur(10px);
  
  .el-message__content {
    color: #ffffff !important;
  }
}
</style> 