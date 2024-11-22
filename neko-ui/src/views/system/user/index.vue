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
      
      <!-- 添加中间区域显示模块名称 -->
      <div class="center-section">
        <span class="module-name">用户管理</span>
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
      <!-- 搜索和操作按钮 -->
      <div class="filter-container">
        <el-input
          v-model="listQuery.keyword"
          placeholder="用户名/姓名/邮箱"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
          搜索
        </el-button>
        <el-button
          class="filter-item"
          style="margin-left: 10px;"
          type="success"
          icon="el-icon-plus"
          @click="handleCreate"
        >
          新增用户
        </el-button>
      </div>

      <!-- 用户列表表格 -->
      <el-table
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
      >
        <el-table-column
          prop="id"
          label="ID"
          align="center"
          width="80"
          show-overflow-tooltip
        />
        <el-table-column
          prop="username"
          label="用户名"
          align="center"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="realName"
          label="姓名"
          align="center"
          min-width="120"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span>{{ scope.row.realName || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="email"
          label="邮箱"
          align="center"
          min-width="180"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span>{{ scope.row.email || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="phone"
          label="手机号"
          align="center"
          min-width="130"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span>{{ scope.row.phone || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          align="center"
          width="100"
        >
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          align="center"
          width="180"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span>{{ formatDateTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="250"
          fixed="right"
        >
          <template slot-scope="scope">
            <el-button 
              type="primary" 
              size="mini" 
              @click="handleUpdate(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              type="warning" 
              size="mini" 
              @click="handleRole(scope.row)"
            >
              角色
            </el-button>
            <el-button
              :type="scope.row.status === 1 ? 'danger' : 'success'"
              size="mini"
              @click="handleStatus(scope.row)"
            >
              {{ scope.row.status === 1 ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="listQuery.page"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="listQuery.limit"
          :total="total"
          background
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!-- 用户表单对话框 -->
      <el-dialog 
        :title="dialogTitle" 
        :visible.sync="dialogVisible"
        width="500px"
      >
        <el-form
          ref="userForm"
          :model="userForm"
          :rules="rules"
          label-width="80px"
          label-position="right"
        >
          <el-form-item label="用户名" prop="username">
            <el-input 
              v-model="userForm.username" 
              placeholder="请输入用户名"
              :disabled="!!userForm.id"
            />
          </el-form-item>
          <el-form-item label="姓名" prop="realName">
            <el-input 
              v-model="userForm.realName" 
              placeholder="请输入姓名"
            />
          </el-form-item>
          <el-form-item label="密码" prop="password" v-if="!userForm.id">
            <el-input 
              v-model="userForm.password" 
              type="password" 
              placeholder="请输入密码"
              show-password
            />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input 
              v-model="userForm.email" 
              placeholder="请输入邮箱"
            />
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input 
              v-model="userForm.phone" 
              placeholder="请输入手机号"
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-switch
              v-model="userForm.status"
              :active-value="1"
              :inactive-value="0"
              active-color="#13ce66"
              inactive-color="#ff4949"
            />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitLoading">
            确 定
          </el-button>
        </div>
      </el-dialog>

      <!-- 添加角色分配对话框 -->
      <el-dialog 
        title="分配角色" 
        :visible.sync="roleDialogVisible"
        width="500px"
      >
        <div v-loading="roleLoading">
          <el-checkbox-group v-model="selectedRoleIds">
            <el-checkbox 
              v-for="role in roleList" 
              :key="role.id" 
              :label="role.id"
            >
              {{ role.roleName }}
              <el-tooltip 
                v-if="role.description" 
                :content="role.description" 
                placement="top"
              >
                <i class="el-icon-info"></i>
              </el-tooltip>
            </el-checkbox>
          </el-checkbox-group>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="roleDialogVisible = false">取 消</el-button>
          <el-button 
            type="primary" 
            @click="saveUserRoles" 
            :loading="saveRoleLoading"
          >
            确 定
          </el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getUsers, createUser, updateUser, updateUserStatus, getUserRoles, updateUserRoles } from '@/api/user'
import { getRoles } from '@/api/role'

export default {
  name: 'UserManagement',
  data() {
    // 邮箱验证规则
    const validateEmail = (rule, value, callback) => {
      if (value && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
        callback(new Error('请输入正确的邮箱地址'))
      } else {
        callback()
      }
    }
    // 手机号验证规则
    const validatePhone = (rule, value, callback) => {
      if (value && !/^1[3-9]\d{9}$/.test(value)) {
        callback(new Error('请输入正确的手机号'))
      } else {
        callback()
      }
    }

    return {
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 10,
        keyword: ''
      },
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      userForm: {
        id: undefined,
        username: '',
        realName: '',
        password: '',
        email: '',
        phone: '',
        status: 1
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        realName: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
          { max: 20, message: '长度不能超过 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能小于 6 个字符', trigger: 'blur' }
        ],
        email: [
          { validator: validateEmail, trigger: 'blur' }
        ],
        phone: [
          { validator: validatePhone, trigger: 'blur' }
        ]
      },
      roleDialogVisible: false,
      roleLoading: false,
      saveRoleLoading: false,
      roleList: [],
      selectedRoleIds: [],
      currentUserId: null
    }
  },
  computed: {
    ...mapGetters([
      'username'
    ])
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      try {
        this.listLoading = true
        const response = await getUsers(this.listQuery)
        console.log('API Response:', response) // 添加日志

        // 处理响应数据
        if (response && response.records) {
          this.list = response.records
          this.total = response.total
        } else if (Array.isArray(response)) {
          this.list = response
          this.total = response.length
        } else {
          console.error('Unexpected response format:', response)
          this.list = []
          this.total = 0
        }
      } catch (error) {
        console.error('Failed to fetch users:', error)
        this.$message.error('获取用户列表失败')
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.listQuery.limit = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
      this.getList()
    },
    handleCreate() {
      this.dialogTitle = '新增用户'
      this.dialogVisible = true
      this.resetForm()
    },
    handleUpdate(row) {
      this.dialogTitle = '编辑用户'
      this.userForm = {
        ...row,
        password: '' // 编辑时不显示密码
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.userForm) {
          this.$refs.userForm.clearValidate()
        }
      })
    },
    handleRole(row) {
      this.currentUserId = row.id
      this.roleDialogVisible = true
      this.selectedRoleIds = []
      this.loadRoleData(row.id)
    },
    loadRoleData(userId) {
      this.roleLoading = true
      // 获取所有角色列表
      getRoles()
        .then(rolesResponse => {
          this.roleList = Array.isArray(rolesResponse) ? rolesResponse : (rolesResponse.records || [])
          // 获取用户当前角色
          return getUserRoles(userId)
        })
        .then(userRolesResponse => {
          this.selectedRoleIds = userRolesResponse.map(role => role.id)
        })
        .catch(error => {
          console.error('Failed to get roles:', error)
          this.$message.error('获取角色信息失败')
        })
        .finally(() => {
          this.roleLoading = false
        })
    },
    async handleStatus(row) {
      try {
        const statusText = row.status === 1 ? '禁用' : '启用'
        await this.$confirm(`确认${statusText}该用户吗？`, '提示', {
          type: 'warning'
        })
        
        const newStatus = row.status === 1 ? 0 : 1
        await updateUserStatus(row.id, newStatus)
        this.$message.success(`${statusText}成功`)
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('Status update failed:', error)
          this.$message.error('操作失败')
        }
      }
    },
    async handleLogout() {
      try {
        await this.$confirm('确认退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await this.$store.dispatch('user/logout')
        this.$router.push('/login')
      } catch (error) {
        if (error !== 'cancel') {
          console.error('Logout failed:', error)
        }
      }
    },
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      try {
        const date = new Date(dateTime)
        return date.toLocaleString('zh-CN', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit',
          hour12: false
        }).replace(/\//g, '-')
      } catch (e) {
        console.error('Date formatting error:', e)
        return dateTime
      }
    },
    // 重置表单
    resetForm() {
      this.userForm = {
        id: undefined,
        username: '',
        realName: '',
        password: '',
        email: '',
        phone: '',
        status: 1
      }
      if (this.$refs.userForm) {
        this.$refs.userForm.clearValidate()
      }
    },
    // 提交表单
    submitForm() {
      this.$refs.userForm.validate(async valid => {
        if (!valid) return
        
        try {
          this.submitLoading = true
          const isEdit = !!this.userForm.id
          const action = isEdit ? updateUser : createUser
          const actionText = isEdit ? '编辑' : '新增'
          
          await action(this.userForm)
          this.$message.success(`${actionText}用户成功`)
          this.dialogVisible = false
          this.getList()
        } catch (error) {
          console.error('Submit form failed:', error)
          const actionText = this.userForm.id ? '编辑' : '新增'
          this.$message.error(`${actionText}用户失败`)
        } finally {
          this.submitLoading = false
        }
      })
    },
    // ���存用户角色
    saveUserRoles() {
      if (!this.currentUserId) return
      
      this.saveRoleLoading = true
      updateUserRoles(this.currentUserId, this.selectedRoleIds)
        .then(() => {
          this.$message.success('角色分配成功')
          this.roleDialogVisible = false
          this.getList() // 刷新用户列表
        })
        .catch(error => {
          console.error('Failed to update user roles:', error)
          this.$message.error('角色分配失败')
        })
        .finally(() => {
          this.saveRoleLoading = false
        })
    },
    // 添加跳转到首页的方法
    goToDashboard() {
      this.$router.push('/dashboard')
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

  // 添加中间区域样式
  .center-section {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
    
    .module-name {
      color: #FFD700;
      font-size: 16px;
      font-weight: 500;
    }
  }
}

.main-content {
  padding: 24px;

  .filter-container {
    margin-bottom: 24px;
    background: rgba(28, 28, 30, 0.8);
    backdrop-filter: blur(20px);
    border-radius: 8px;
    padding: 20px;
    border: 1px solid rgba(255, 215, 0, 0.1);

    .filter-item {
      margin-right: 10px;
      &:last-child {
        margin-right: 0;
      }
    }
  }
}

// 修改表格样式
:deep(.el-table) {
  background-color: #ffffff !important;
  color: #000000;
  border: 1px solid #dcdfe6;
  border-radius: 4px;

  th {
    background-color: #f5f7fa !important;
    color: #000000;
    font-weight: bold;
    border-bottom: 1px solid #dcdfe6;
  }

  td {
    background-color: #ffffff !important;
    color: #000000;
    border-bottom: 1px solid #ebeef5;
  }

  .el-table__row:hover > td {
    background-color: #f5f7fa !important;
  }

  // 修改表格内的标签样式
  .el-tag {
    &.el-tag--success {
      color: #67c23a;
      background-color: #f0f9eb;
      border-color: #c2e7b0;
    }
    
    &.el-tag--danger {
      color: #f56c6c;
      background-color: #fef0f0;
      border-color: #fbc4c4;
    }
  }

  // 修改表格内的按钮样式
  .el-button {
    &.el-button--primary {
      color: #ffffff;
    }
    
    &.el-button--warning {
      color: #ffffff;
    }
    
    &.el-button--danger {
      color: #ffffff;
    }
    
    &.el-button--success {
      color: #ffffff;
    }
  }
}

// 修改分页器样式
.pagination-container {
  margin-top: 20px;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 4px;
  display: flex;
  justify-content: center; // 水平居中
  align-items: center; // 垂直居中

  :deep(.el-pagination) {
    padding: 0;
    font-weight: normal;
    color: #000000;

    .btn-prev,
    .btn-next,
    .el-pager li {
      background-color: #ffffff;
      color: #000000;
      border: 1px solid #dcdfe6;
      margin: 0 4px;

      &:hover {
        color: #409EFF;
        border-color: #409EFF;
      }

      &.active {
        background-color: #409EFF;
        color: #ffffff;
        border-color: #409EFF;
      }
    }

    .el-pagination__total,
    .el-pagination__sizes .el-input .el-input__inner,
    .el-pagination__jump {
      color: #000000;
      margin: 0 10px;
    }

    .el-pagination__sizes {
      margin: 0 10px;
    }

    .el-select .el-input {
      width: 110px;
      margin: 0 5px;
    }

    .el-pagination__jump {
      margin-left: 10px;
      .el-input__inner {
        text-align: center;
        width: 50px;
      }
    }
  }
}

// 修改搜索区域样式
.filter-container {
  background-color: #ffffff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .el-input {
    .el-input__inner {
      background-color: #ffffff;
      color: #000000;
    }
  }

  .el-button {
    &.el-button--primary {
      color: #ffffff;
    }
    
    &.el-button--success {
      color: #ffffff;
    }
  }
}

// 保持顶部导航栏的暗色主题
.top-navbar {
  background: rgba(28, 28, 30, 0.8);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 215, 0, 0.1);
  
  // ... 顶部导航栏的其他样式保持不变
}

// 修改对话框样式
:deep(.el-dialog) {
  background-color: #ffffff;
  
  .el-dialog__title {
    color: #000000;
  }
  
  .el-form-item__label {
    color: #000000;
  }
  
  .el-input__inner {
    background-color: #ffffff;
    border-color: #dcdfe6;
    color: #000000;
    
    &:focus {
      border-color: #409EFF;
    }
  }

  .el-dialog__footer {
    border-top: 1px solid #ebeef5;
  }
}

// 品牌和用户信息样式
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

// 响应式调整
@media screen and (max-width: 768px) {
  .top-navbar {
    padding: 0 16px;
    
    .brand-text {
      display: none;
    }
  }

  .main-content {
    padding: 16px;
  }
}

// 添加表单相关样式
.el-dialog {
  .el-form {
    padding: 20px;
  }
  
  .dialog-footer {
    text-align: right;
    padding: 20px;
    border-top: 1px solid #eee;
  }
}

// 暗色主题适配
.el-dialog {
  background-color: #2c2c2c;
  
  .el-dialog__title {
    color: #ffffff;
  }
  
  .el-form-item__label {
    color: #ffffff;
  }
  
  .el-input__inner {
    background-color: #363636;
    border-color: #4c4c4c;
    color: #ffffff;
    
    &:focus {
      border-color: #409EFF;
    }
  }
}

// 角色对话框样式
:deep(.el-dialog) {
  .el-checkbox-group {
    display: flex;
    flex-direction: column;
    gap: 10px;
    padding: 10px;
    max-height: 400px;
    overflow-y: auto;

    .el-checkbox {
      margin-left: 0;
      margin-right: 0;
      
      .el-icon-info {
        margin-left: 5px;
        color: #909399;
        cursor: help;
      }
    }
  }
}
</style> 