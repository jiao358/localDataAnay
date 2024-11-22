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
        <span class="module-name">角色管理</span>
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
          placeholder="角色名称/编码"
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
          新增角色
        </el-button>
      </div>

      <!-- 角色列表表格 -->
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
          prop="roleName"
          label="角色名称"
          align="center"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="roleCode"
          label="角色编码"
          align="center"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="description"
          label="描述"
          align="center"
          min-width="180"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span>{{ scope.row.description || '-' }}</span>
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
          width="280"
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
              @click="handlePermission(scope.row)"
            >
              权限
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

      <!-- 角色表单对话框 -->
      <el-dialog 
        :title="dialogTitle" 
        :visible.sync="dialogVisible"
        width="500px"
      >
        <el-form
          ref="roleForm"
          :model="roleForm"
          :rules="rules"
          label-width="80px"
          label-position="right"
        >
          <el-form-item label="角色名称" prop="roleName">
            <el-input 
              v-model="roleForm.roleName" 
              placeholder="请输入角色名称"
            />
          </el-form-item>
          <el-form-item label="角色编码" prop="roleCode">
            <el-input 
              v-model="roleForm.roleCode" 
              placeholder="请输入角色编码"
              :disabled="!!roleForm.id"
            />
          </el-form-item>
          <el-form-item label="描述" prop="description">
            <el-input 
              v-model="roleForm.description" 
              type="textarea"
              :rows="3"
              placeholder="请输入描述"
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-switch
              v-model="roleForm.status"
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

      <!-- 权限分配对话框 -->
      <el-dialog 
        title="分配权限" 
        :visible.sync="permissionDialogVisible"
        width="600px"
      >
        <div v-loading="permissionLoading">
          <el-tree
            ref="permissionTree"
            :data="permissionTreeData"
            :props="defaultProps"
            show-checkbox
            node-key="id"
            :default-checked-keys="selectedPermissionIds"
          >
            <span slot-scope="{ data }" class="custom-tree-node">
              <span>{{ data.permissionName }}</span>
              <el-tag 
                size="mini" 
                :type="getPermissionTypeTag(data.permissionType)"
                style="margin-left: 8px;"
              >
                {{ getPermissionTypeText(data.permissionType) }}
              </el-tag>
            </span>
          </el-tree>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="permissionDialogVisible = false">取 消</el-button>
          <el-button 
            type="primary" 
            @click="saveRolePermissions" 
            :loading="savePermissionLoading"
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
import { getRoles, createRole, updateRole, updateRoleStatus } from '@/api/role'
import { getRolePermissions, updateRolePermissions } from '@/api/role'
import { getPermissions } from '@/api/permission'

export default {
  name: 'RoleManagement',
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
      roleForm: {
        id: undefined,
        roleName: '',
        roleCode: '',
        description: '',
        status: 1
      },
      rules: {
        roleName: [
          { required: true, message: '请输入角色名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        roleCode: [
          { required: true, message: '请输入角色编码', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ]
      },
      permissionDialogVisible: false,
      permissionLoading: false,
      savePermissionLoading: false,
      permissionTreeData: [],
      selectedPermissionIds: [],
      currentRoleId: null,
      defaultProps: {
        children: 'children',
        label: 'permissionName'
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
    async getList() {
      try {
        this.listLoading = true
        const response = await getRoles(this.listQuery)
        console.log('API Response:', response)

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
        console.error('Failed to fetch roles:', error)
        this.$message.error('获取角色列表失败')
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
    resetForm() {
      this.roleForm = {
        id: undefined,
        roleName: '',
        roleCode: '',
        description: '',
        status: 1
      }
      if (this.$refs.roleForm) {
        this.$refs.roleForm.clearValidate()
      }
    },
    handleCreate() {
      this.dialogTitle = '新增角色'
      this.dialogVisible = true
      this.resetForm()
    },
    handleUpdate(row) {
      this.dialogTitle = '编辑角色'
      this.roleForm = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.roleForm) {
          this.$refs.roleForm.clearValidate()
        }
      })
    },
    handlePermission(row) {
      this.currentRoleId = row.id
      this.permissionDialogVisible = true
      this.loadPermissionData(row.id)
    },
    async handleStatus(row) {
      try {
        const statusText = row.status === 1 ? '禁用' : '启用'
        await this.$confirm(`确认${statusText}该角色吗？`, '提示', {
          type: 'warning'
        })
        
        const newStatus = row.status === 1 ? 0 : 1
        await updateRoleStatus(row.id, newStatus)
        this.$message.success(`${statusText}成功`)
        await this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('Status update failed:', error)
          this.$message.error('操作失败')
        }
      }
    },
    async submitForm() {
      this.$refs.roleForm.validate(async valid => {
        if (!valid) return
        
        try {
          this.submitLoading = true
          const isEdit = !!this.roleForm.id
          const action = isEdit ? updateRole : createRole
          const actionText = isEdit ? '编辑' : '新增'
          
          await action(this.roleForm)
          this.$message.success(`${actionText}角色成功`)
          this.dialogVisible = false
          this.getList()
        } catch (error) {
          console.error('Submit form failed:', error)
          const actionText = this.roleForm.id ? '编辑' : '新增'
          this.$message.error(`${actionText}角色失败`)
        } finally {
          this.submitLoading = false
        }
      })
    },
    goToDashboard() {
      this.$router.push('/dashboard')
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
    // 加载权限数据
    async loadPermissionData(roleId) {
      try {
        this.permissionLoading = true
        // 获取所有权限列表
        const permissionsResponse = await getPermissions()
        const permissions = Array.isArray(permissionsResponse) 
          ? permissionsResponse 
          : (permissionsResponse.records || [])
        
        // 构建树形结构
        this.permissionTreeData = this.buildPermissionTree(permissions)
        
        // 获取角色当前权限
        const rolePermissionsResponse = await getRolePermissions(roleId)
        this.selectedPermissionIds = rolePermissionsResponse.map(p => p.id)
      } catch (error) {
        console.error('Failed to get permissions:', error)
        this.$message.error('获取权限信息失败')
      } finally {
        this.permissionLoading = false
      }
    },
    // 构建权限树
    buildPermissionTree(permissions) {
      const permissionMap = {}
      const result = []

      // 创建映射
      permissions.forEach(permission => {
        permissionMap[permission.id] = { ...permission, children: [] }
      })

      // 构建树形结构
      permissions.forEach(permission => {
        const node = permissionMap[permission.id]
        if (permission.parentId) {
          const parent = permissionMap[permission.parentId]
          if (parent) {
            parent.children.push(node)
          }
        } else {
          result.push(node)
        }
      })

      return result
    },
    // 保存角色权限
    async saveRolePermissions() {
      if (!this.currentRoleId) return

      try {
        this.savePermissionLoading = true
        const checkedKeys = this.$refs.permissionTree.getCheckedKeys()
        const halfCheckedKeys = this.$refs.permissionTree.getHalfCheckedKeys()
        const allSelectedKeys = [...checkedKeys, ...halfCheckedKeys]
        
        await updateRolePermissions(this.currentRoleId, allSelectedKeys)
        this.$message.success('权限分配成功')
        this.permissionDialogVisible = false
      } catch (error) {
        console.error('Failed to update role permissions:', error)
        this.$message.error('权限分配失败')
      } finally {
        this.savePermissionLoading = false
      }
    },
    // 获取权限类型文本
    getPermissionTypeText(type) {
      const types = {
        1: '菜单',
        2: '按钮',
        3: '接口'
      }
      return types[type] || '未知'
    },
    // 获取权限类型标签样式
    getPermissionTypeTag(type) {
      const types = {
        1: 'primary',
        2: 'success',
        3: 'warning'
      }
      return types[type] || 'info'
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

// 权限树样式
.custom-tree-node {
  display: flex;
  align-items: center;
  font-size: 14px;
}

:deep(.el-tree) {
  background-color: transparent;
  color: #000000;
  
  .el-tree-node__content {
    height: 32px;
    
    &:hover {
      background-color: #f5f7fa;
    }
  }
}

:deep(.el-tree-node.is-current > .el-tree-node__content) {
  background-color: #f0f7ff;
}
</style> 