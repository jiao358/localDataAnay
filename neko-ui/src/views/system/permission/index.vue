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
          v-model="listQuery.keyword"
          placeholder="权限名称/编码"
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
          新增权限
        </el-button>
      </div>

      <!-- 权限列表表格 -->
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
          prop="permissionName"
          label="权限名称"
          align="center"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="permissionCode"
          label="权限编码"
          align="center"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="permissionType"
          label="权限类型"
          align="center"
          width="100"
        >
          <template slot-scope="scope">
            <el-tag :type="getPermissionTypeTag(scope.row.permissionType)">
              {{ getPermissionTypeText(scope.row.permissionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="path"
          label="路由路径"
          align="center"
          min-width="120"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span>{{ scope.row.path || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="component"
          label="前端组件"
          align="center"
          min-width="120"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span>{{ scope.row.component || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="icon"
          label="图标"
          align="center"
          width="80"
        >
          <template slot-scope="scope">
            <i v-if="scope.row.icon" :class="scope.row.icon"></i>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="sortOrder"
          label="排序"
          align="center"
          width="80"
        />
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
          width="180"
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

      <!-- 权限表单对话框 -->
      <el-dialog 
        :title="dialogTitle" 
        :visible.sync="dialogVisible"
        width="500px"
      >
        <el-form
          ref="permissionForm"
          :model="permissionForm"
          :rules="rules"
          label-width="100px"
          label-position="right"
        >
          <el-form-item label="权限名称" prop="permissionName">
            <el-input 
              v-model="permissionForm.permissionName" 
              placeholder="请输入权限名称"
            />
          </el-form-item>
          <el-form-item label="权限编码" prop="permissionCode">
            <el-input 
              v-model="permissionForm.permissionCode" 
              placeholder="请输入权限编码"
              :disabled="!!permissionForm.id"
            />
          </el-form-item>
          <el-form-item label="权限类型" prop="permissionType">
            <el-select v-model="permissionForm.permissionType" placeholder="请选择权限类型">
              <el-option label="菜单" :value="1" />
              <el-option label="按钮" :value="2" />
              <el-option label="接口" :value="3" />
            </el-select>
          </el-form-item>
          <el-form-item label="父权限" prop="parentId">
            <el-select v-model="permissionForm.parentId" placeholder="请选择父权限">
              <el-option label="无" :value="null" />
              <el-option
                v-for="item in menuOptions"
                :key="item.id"
                :label="item.permissionName"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="路由路径" prop="path">
            <el-input 
              v-model="permissionForm.path" 
              placeholder="请输入路由路径"
            />
          </el-form-item>
          <el-form-item label="前端组件" prop="component">
            <el-input 
              v-model="permissionForm.component" 
              placeholder="请输入前端组件"
            />
          </el-form-item>
          <el-form-item label="图标" prop="icon">
            <el-input 
              v-model="permissionForm.icon" 
              placeholder="请输入图标类名"
            >
              <i 
                v-if="permissionForm.icon"
                slot="suffix" 
                :class="permissionForm.icon"
              ></i>
            </el-input>
          </el-form-item>
          <el-form-item label="排序" prop="sortOrder">
            <el-input-number 
              v-model="permissionForm.sortOrder" 
              :min="0"
              :max="999"
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-switch
              v-model="permissionForm.status"
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
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getPermissions, createPermission, updatePermission, updatePermissionStatus } from '@/api/permission'

export default {
  name: 'PermissionManagement',
  data() {
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
      menuOptions: [],
      permissionForm: {
        id: undefined,
        permissionName: '',
        permissionCode: '',
        permissionType: 1,
        parentId: null,
        path: '',
        component: '',
        icon: '',
        sortOrder: 0,
        status: 1
      },
      rules: {
        permissionName: [
          { required: true, message: '请输入权限名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        permissionCode: [
          { required: true, message: '请输入权限编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        permissionType: [
          { required: true, message: '请选择权限类型', trigger: 'change' }
        ]
      }
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
    getList() {
      this.listLoading = true
      getPermissions(this.listQuery)
        .then(response => {
          if (Array.isArray(response)) {
            this.list = response
            this.total = response.length
          } else if (response && response.records) {
            this.list = response.records
            this.total = response.total
          }
          // 更新菜单选项
          this.menuOptions = this.list.filter(item => item.permissionType === 1)
        })
        .catch(error => {
          console.error('Failed to get permissions:', error)
          this.$message.error('获取权限列表失败')
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
      this.permissionForm = {
        id: undefined,
        permissionName: '',
        permissionCode: '',
        permissionType: 1,
        parentId: null,
        path: '',
        component: '',
        icon: '',
        sortOrder: 0,
        status: 1
      }
      if (this.$refs.permissionForm) {
        this.$refs.permissionForm.clearValidate()
      }
    },
    handleCreate() {
      this.dialogTitle = '新增权限'
      this.dialogVisible = true
      this.resetForm()
    },
    handleUpdate(row) {
      this.dialogTitle = '编辑权限'
      this.permissionForm = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.permissionForm) {
          this.$refs.permissionForm.clearValidate()
        }
      })
    },
    handleStatus(row) {
      const statusText = row.status === 1 ? '禁用' : '启用'
      this.$confirm(`确认${statusText}该权限吗？`, '提示', {
        type: 'warning'
      })
        .then(() => {
          const newStatus = row.status === 1 ? 0 : 1
          return updatePermissionStatus(row.id, newStatus)
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
      this.$refs.permissionForm.validate(valid => {
        if (!valid) return
        
        this.submitLoading = true
        const action = this.permissionForm.id ? updatePermission : createPermission
        const actionText = this.permissionForm.id ? '编辑' : '新增'
        
        action(this.permissionForm)
          .then(() => {
            this.$message.success(`${actionText}权限成功`)
            this.dialogVisible = false
            this.getList()
          })
          .catch(error => {
            console.error('Submit form failed:', error)
            this.$message.error(`${actionText}权限失败`)
          })
          .finally(() => {
            this.submitLoading = false
          })
      })
    },
    getPermissionTypeText(type) {
      const types = {
        1: '菜单',
        2: '按钮',
        3: '接口'
      }
      return types[type] || '未知'
    },
    getPermissionTypeTag(type) {
      const types = {
        1: 'primary',
        2: 'success',
        3: 'warning'
      }
      return types[type] || 'info'
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
    }
  }
}
</script>

<style lang="scss" scoped>
// 使用与用户管理相同的样式
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