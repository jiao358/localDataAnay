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
          v-model="listQuery.customerName"
          placeholder="客户姓名"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-select
          v-model="listQuery.channel"
          placeholder="客户渠道"
          clearable
          class="filter-item"
          style="width: 150px"
        >
          <el-option
            v-for="item in channelOptions"
            :key="item"
            :label="item"
            :value="item"
          />
        </el-select>
        <el-input
          v-model="listQuery.contact"
          placeholder="联系方式"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-select
          v-model="listQuery.valueLevel"
          placeholder="价值等级"
          clearable
          class="filter-item"
          style="width: 150px"
        >
          <el-option
            v-for="item in valueLevelOptions"
            :key="item"
            :label="item"
            :value="item"
          />
        </el-select>
        <el-input-number
          v-model="listQuery.minCommunication"
          placeholder="最少沟通次数"
          :min="0"
          :controls="false"
          class="filter-item"
          style="width: 130px;"
          clearable
        />
        <el-input-number
          v-model="listQuery.maxCommunication"
          placeholder="最多沟通次数"
          :min="0"
          :controls="false"
          class="filter-item"
          style="width: 130px;"
          clearable
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
          新增客户
        </el-button>
      </div>

      <!-- 客户列表表格 -->
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
          prop="customerName"
          label="客户姓名"
          align="center"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="channel"
          label="客户渠道"
          align="center"
          width="120"
        />
        <el-table-column
          prop="contact"
          label="联系方式"
          align="center"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="address"
          label="客户地址"
          align="center"
          min-width="200"
          show-overflow-tooltip
        />
        <el-table-column
          prop="communicationCount"
          label="沟通次数"
          align="center"
          width="100"
        />
        <el-table-column
          prop="lastIntention"
          label="沟通意愿"
          align="center"
          width="120"
        >
          <template slot-scope="scope">
            <el-tag :type="getIntentionTagType(scope.row.lastIntention)">
              {{ scope.row.lastIntention }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="valueLevel"
          label="价值等级"
          align="center"
          width="120"
        >
          <template slot-scope="scope">
            <el-tag :type="getValueLevelTagType(scope.row.valueLevel)">
              {{ scope.row.valueLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="itemCategory"
          label="商品类目"
          align="center"
          min-width="120"
          show-overflow-tooltip
        />
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
              type="success" 
              size="mini" 
              @click="handleCommunication(scope.row)"
            >
              沟通
            </el-button>
            <el-button
              :type="scope.row.status === 1 ? 'danger' : 'info'"
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

      <!-- 客户表单对话框 -->
      <el-dialog 
        :title="dialogTitle" 
        :visible.sync="dialogVisible"
        width="500px"
      >
        <el-form
          ref="customerForm"
          :model="customerForm"
          :rules="rules"
          label-width="100px"
          label-position="right"
        >
          <el-form-item label="客户姓名" prop="customerName">
            <el-input v-model="customerForm.customerName" />
          </el-form-item>
          <el-form-item label="客户渠道" prop="channel">
            <el-select v-model="customerForm.channel" style="width: 100%">
              <el-option
                v-for="item in channelOptions"
                :key="item"
                :label="item"
                :value="item"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="联系方式" prop="contact">
            <el-input v-model="customerForm.contact" />
          </el-form-item>
          <el-form-item label="客户地址" prop="address">
            <el-input 
              v-model="customerForm.address"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
          <el-form-item label="沟通意愿" prop="lastIntention">
            <el-select v-model="customerForm.lastIntention" style="width: 100%">
              <el-option
                v-for="item in intentionOptions"
                :key="item"
                :label="item"
                :value="item"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="价值等级" prop="valueLevel">
            <el-select v-model="customerForm.valueLevel" style="width: 100%">
              <el-option
                v-for="item in valueLevelOptions"
                :key="item"
                :label="item"
                :value="item"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="商品类目" prop="itemCategory">
            <el-input v-model="customerForm.itemCategory" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitLoading">
            确 定
          </el-button>
        </div>
      </el-dialog>

      <!-- 沟通记录对话框 -->
      <el-dialog 
        title="客户沟通" 
        :visible.sync="communicationDialogVisible"
        width="400px"
      >
        <el-form
          ref="communicationForm"
          :model="communicationForm"
          label-width="100px"
          label-position="right"
        >
          <el-form-item label="沟通意愿" prop="intention">
            <el-select v-model="communicationForm.intention" style="width: 100%">
              <el-option
                v-for="item in intentionOptions"
                :key="item"
                :label="item"
                :value="item"
              />
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="communicationDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveCommunication" :loading="communicationLoading">
            确 定
          </el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getCustomers, createCustomer, updateCustomer, updateCustomerStatus, incrementCommunication } from '@/api/customer'

export default {
  name: 'CustomerManagement',
  data() {
    return {
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 10,
        customerName: '',
        channel: '',
        contact: '',
        valueLevel: '',
        minCommunication: undefined,
        maxCommunication: undefined
      },
      channelOptions: ['淘宝', '抖音', '快手', '小红书', '线下', '微信', '推特', '外网'],
      intentionOptions: ['愿意', '不愿意', '中立', '推迟意愿'],
      valueLevelOptions: ['100元以下', '100到300元', '300到500元', '500元以上'],
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      customerForm: {
        id: undefined,
        customerName: '',
        channel: '',
        contact: '',
        address: '',
        lastIntention: '',
        valueLevel: '',
        itemCategory: '',
        status: 1
      },
      communicationDialogVisible: false,
      communicationLoading: false,
      communicationForm: {
        intention: ''
      },
      currentCustomerId: null,
      rules: {
        customerName: [{ required: true, message: '请输入客户姓名', trigger: 'blur' }],
        channel: [{ required: true, message: '请选择客户渠道', trigger: 'change' }],
        contact: [{ required: true, message: '请输入联系方式', trigger: 'blur' }],
        valueLevel: [{ required: true, message: '请选择价值等级', trigger: 'change' }]
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
      getCustomers(this.listQuery)
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
          console.error('Failed to get customers:', error)
          this.$message.error('获取客户列表失败')
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
      this.customerForm = {
        id: undefined,
        customerName: '',
        channel: '',
        contact: '',
        address: '',
        lastIntention: '',
        valueLevel: '',
        itemCategory: '',
        status: 1
      }
      if (this.$refs.customerForm) {
        this.$refs.customerForm.clearValidate()
      }
    },
    handleCreate() {
      this.dialogTitle = '新增客户'
      this.dialogVisible = true
      this.resetForm()
    },
    handleUpdate(row) {
      this.dialogTitle = '编辑客户'
      this.customerForm = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.customerForm) {
          this.$refs.customerForm.clearValidate()
        }
      })
    },
    handleCommunication(row) {
      this.currentCustomerId = row.id
      this.communicationForm.intention = row.lastIntention
      this.communicationDialogVisible = true
    },
    handleStatus(row) {
      const statusText = row.status === 1 ? '禁用' : '启用'
      this.$confirm(`确认${statusText}该客户吗？`, '提示', {
        type: 'warning'
      })
        .then(() => {
          const newStatus = row.status === 1 ? 0 : 1
          return updateCustomerStatus(row.id, newStatus)
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
      this.$refs.customerForm.validate(valid => {
        if (!valid) return
        
        this.submitLoading = true
        const action = this.customerForm.id ? updateCustomer : createCustomer
        const actionText = this.customerForm.id ? '编辑' : '新增'
        
        action(this.customerForm)
          .then(() => {
            this.$message.success(`${actionText}客户成功`)
            this.dialogVisible = false
            this.getList()
          })
          .catch(error => {
            console.error('Submit form failed:', error)
            this.$message.error(`${actionText}客户失败`)
          })
          .finally(() => {
            this.submitLoading = false
          })
      })
    },
    saveCommunication() {
      if (!this.currentCustomerId) return
      
      this.communicationLoading = true
      const customer = {
        id: this.currentCustomerId,
        lastIntention: this.communicationForm.intention
      }
      
      Promise.all([
        updateCustomer(customer),
        incrementCommunication(this.currentCustomerId)
      ])
        .then(() => {
          this.$message.success('沟通记录已更新')
          this.communicationDialogVisible = false
          this.getList()
        })
        .catch(error => {
          console.error('Failed to save communication:', error)
          this.$message.error('保存沟通记录失败')
        })
        .finally(() => {
          this.communicationLoading = false
        })
    },
    getIntentionTagType(intention) {
      const types = {
        '愿意': 'success',
        '不愿意': 'danger',
        '中立': 'info',
        '推迟意愿': 'warning'
      }
      return types[intention] || 'info'
    },
    getValueLevelTagType(level) {
      const types = {
        '100元以下': 'info',
        '100到300元': 'success',
        '300到500元': 'warning',
        '500元以上': 'danger'
      }
      return types[level] || 'info'
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

// 搜索区域��式
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

// 添加数字输入框的样式
:deep(.el-input-number) {
  .el-input__inner {
    text-align: left;
    padding-left: 15px;
  }
}
</style> 