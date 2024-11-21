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
      <!-- 搜索和操作按钮 -->
      <div class="filter-container">
        <el-input
          v-model="listQuery.accountName"
          placeholder="账号名称"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-select
          v-model="listQuery.platformType"
          placeholder="平台类型"
          clearable
          class="filter-item"
          style="width: 150px"
        >
          <el-option
            v-for="(name, code) in platformTypes"
            :key="code"
            :label="name"
            :value="code"
          />
        </el-select>
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
          新增账号
        </el-button>
      </div>

      <!-- 平台账号列表表格 -->
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
          prop="accountName"
          label="账号名称"
          align="center"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="platformTypeName"
          label="平台类型"
          align="center"
          width="120"
        />
        <el-table-column
          prop="platformUrl"
          label="链接地址"
          align="center"
          min-width="200"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <el-link 
              type="primary" 
              :href="scope.row.platformUrl" 
              target="_blank"
              :underline="false"
            >
              {{ scope.row.platformUrl }}
            </el-link>
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
          label="操作"
          align="center"
          width="300"
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
              :type="scope.row.status === 1 ? 'danger' : 'success'"
              size="mini"
              @click="handleStatus(scope.row)"
            >
              {{ scope.row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button
              type="danger"
              size="mini"
              @click="handleDelete(scope.row)"
            >
              删除
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

      <!-- 平台账号表单对话框 -->
      <el-dialog 
        :title="dialogTitle" 
        :visible.sync="dialogVisible"
        width="500px"
      >
        <el-form
          ref="platformForm"
          :model="platformForm"
          :rules="rules"
          label-width="100px"
          label-position="right"
        >
          <el-form-item label="账号名称" prop="accountName">
            <el-input v-model="platformForm.accountName" />
          </el-form-item>
          <el-form-item label="账号密码" prop="accountPassword">
            <el-input 
              v-model="platformForm.accountPassword" 
              type="password"
              show-password
            />
          </el-form-item>
          <el-form-item label="密保内容" prop="securityInfo">
            <el-input 
              v-model="platformForm.securityInfo"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
          <el-form-item label="支付密码" prop="paymentPassword">
            <el-input 
              v-model="platformForm.paymentPassword"
              type="password"
              show-password
            />
          </el-form-item>
          <el-form-item label="平台类型" prop="platformType">
            <el-select v-model="platformForm.platformType" style="width: 100%">
              <el-option
                v-for="(name, code) in platformTypes"
                :key="code"
                :label="name"
                :value="code"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="链接地址" prop="platformUrl">
            <el-input v-model="platformForm.platformUrl" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitLoading">
            确 定
          </el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getPlatforms, getPlatformTypes, createPlatform, updatePlatform, updatePlatformStatus, deletePlatform } from '@/api/platform'

export default {
  name: 'PlatformManagement',
  data() {
    return {
      list: [],
      total: 0,
      listLoading: false,
      platformTypes: {},
      listQuery: {
        page: 1,
        limit: 10,
        accountName: '',
        platformType: ''
      },
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      platformForm: {
        id: undefined,
        accountName: '',
        accountPassword: '',
        securityInfo: '',
        paymentPassword: '',
        platformType: '',
        platformUrl: '',
        status: 1
      },
      rules: {
        accountName: [{ required: true, message: '请输入账号名称', trigger: 'blur' }],
        accountPassword: [{ required: true, message: '请输入账号密码', trigger: 'blur' }],
        platformType: [{ required: true, message: '请选择平台类型', trigger: 'change' }],
        platformUrl: [{ required: true, message: '请输入链接地址', trigger: 'blur' }]
      }
    }
  },
  computed: {
    ...mapGetters([
      'username'
    ])
  },
  created() {
    this.getPlatformTypes()
    this.getList()
  },
  methods: {
    getPlatformTypes() {
      getPlatformTypes()
        .then(response => {
          this.platformTypes = response
        })
        .catch(error => {
          console.error('Failed to get platform types:', error)
          this.$message.error('获取平台类型失败')
        })
    },
    getList() {
      this.listLoading = true
      getPlatforms(this.listQuery)
        .then(response => {
          if (Array.isArray(response)) {
            this.list = response
            this.total = response.length
          } else if (response && response.records) {
            this.list = response.records
            this.total = response.total
          }
        })
        .catch(error => {
          console.error('Failed to get platforms:', error)
          this.$message.error('获取平台账号列表失败')
        })
        .finally(() => {
          this.listLoading = false
        })
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
    resetForm() {
      this.platformForm = {
        id: undefined,
        accountName: '',
        accountPassword: '',
        securityInfo: '',
        paymentPassword: '',
        platformType: '',
        platformUrl: '',
        status: 1
      }
      if (this.$refs.platformForm) {
        this.$refs.platformForm.clearValidate()
      }
    },
    handleCreate() {
      this.dialogTitle = '新增平台账号'
      this.dialogVisible = true
      this.resetForm()
    },
    handleUpdate(row) {
      this.dialogTitle = '编辑平台账号'
      this.platformForm = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.platformForm) {
          this.$refs.platformForm.clearValidate()
        }
      })
    },
    handleStatus(row) {
      const statusText = row.status === 1 ? '禁用' : '启用'
      this.$confirm(`确认${statusText}该平台账号吗？`, '提示', {
        type: 'warning'
      })
        .then(() => {
          const newStatus = row.status === 1 ? 0 : 1
          return updatePlatformStatus(row.id, newStatus)
        })
        .then(() => {
          this.$message.success(`${statusText}成功`)
          this.getList()
        })
        .catch(error => {
          if (error !== 'cancel') {
            console.error('Failed to update status:', error)
            this.$message.error('操作失败')
          }
        })
    },
    submitForm() {
      this.$refs.platformForm.validate(valid => {
        if (!valid) return
        
        this.submitLoading = true
        const action = this.platformForm.id ? updatePlatform : createPlatform
        const actionText = this.platformForm.id ? '编辑' : '新增'
        
        action(this.platformForm)
          .then(() => {
            this.$message.success(`${actionText}平台账号成功`)
            this.dialogVisible = false
            this.getList()
          })
          .catch(error => {
            console.error('Submit form failed:', error)
            this.$message.error(`${actionText}平台账号失败`)
          })
          .finally(() => {
            this.submitLoading = false
          })
      })
    },
    goToDashboard() {
      this.$router.push('/dashboard')
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
    },
    handleDelete(row) {
      this.$confirm('确认删除该平台账号吗？此操作不可恢复！', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          return deletePlatform(row.id)
        })
        .then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
        .catch(error => {
          if (error !== 'cancel') {
            console.error('Failed to delete platform:', error)
            this.$message.error('删除失败')
          }
        })
    }
  }
}
</script>

<style lang="scss" scoped>
// 使用与其他页面相同的样式
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

// 表格样式
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
}

// 分页样式
.pagination-container {
  margin-top: 20px;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 4px;
  display: flex;
  justify-content: center;
  align-items: center;
}

// 搜索区域样式
.filter-container {
  background-color: #ffffff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .filter-item {
    margin-right: 10px;
    margin-bottom: 10px;
  }
}

// 对话框样式
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
}
</style> 