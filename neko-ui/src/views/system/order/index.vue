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
          v-model="listQuery.itemId"
          placeholder="商品ID"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-input
          v-model="listQuery.phone"
          placeholder="手机号"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-input
          v-model="listQuery.receiverName"
          placeholder="收货人姓名"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-input-number
          v-model="listQuery.minPrice"
          placeholder="最低价格"
          :min="0"
          :precision="2"
          :controls="false"
          class="filter-item"
          style="width: 130px;"
          clearable
        />
        <el-input-number
          v-model="listQuery.maxPrice"
          placeholder="最高价格"
          :min="0"
          :precision="2"
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
          新增订单
        </el-button>
      </div>

      <!-- 订单列表表格 -->
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
          prop="itemId"
          label="商品ID"
          align="center"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="itemName"
          label="商品名称"
          align="center"
          min-width="200"
          show-overflow-tooltip
        />
        <el-table-column
          prop="itemPrice"
          label="商品价格"
          align="center"
          width="120"
        >
          <template slot-scope="scope">
            ¥{{ scope.row.itemPrice.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="receiverName"
          label="收货人"
          align="center"
          min-width="100"
          show-overflow-tooltip
        />
        <el-table-column
          prop="receiverNickname"
          label="淘宝昵称"
          align="center"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="phone"
          label="手机号"
          align="center"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="receiverAddress"
          label="收货地址"
          align="center"
          min-width="300"
          show-overflow-tooltip
        />
        <el-table-column
          prop="tradeTime"
          label="交易时间"
          align="center"
          width="180"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.tradeTime) }}
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
              {{ scope.row.status === 1 ? '有效' : '无效' }}
            </el-tag>
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
              {{ scope.row.status === 1 ? '作废' : '恢复' }}
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

      <!-- 订单表单对话框 -->
      <el-dialog 
        :title="dialogTitle" 
        :visible.sync="dialogVisible"
        width="600px"
      >
        <el-form
          ref="orderForm"
          :model="orderForm"
          :rules="rules"
          label-width="100px"
          label-position="right"
        >
          <el-form-item label="商品ID" prop="itemId">
            <el-input v-model="orderForm.itemId" />
          </el-form-item>
          <el-form-item label="商品名称" prop="itemName">
            <el-input v-model="orderForm.itemName" />
          </el-form-item>
          <el-form-item label="商品价格" prop="itemPrice">
            <el-input-number 
              v-model="orderForm.itemPrice"
              :precision="2"
              :min="0"
              style="width: 100%;"
            />
          </el-form-item>
          <el-form-item label="收货人" prop="receiverName">
            <el-input v-model="orderForm.receiverName" />
          </el-form-item>
          <el-form-item label="淘宝昵称" prop="receiverNickname">
            <el-input v-model="orderForm.receiverNickname" />
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="orderForm.phone" />
          </el-form-item>
          <el-form-item label="收货地址" prop="receiverAddress">
            <el-input 
              v-model="orderForm.receiverAddress"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
          <el-form-item label="交易时间" prop="tradeTime">
            <el-date-picker
              v-model="orderForm.tradeTime"
              type="datetime"
              placeholder="选择交易时间"
              style="width: 100%;"
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-switch
              v-model="orderForm.status"
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
import { getOrders, createOrder, updateOrder, updateOrderStatus } from '@/api/order'

export default {
  name: 'OrderManagement',
  data() {
    return {
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 10,
        itemId: '',
        phone: '',
        receiverName: '',
        minPrice: undefined,
        maxPrice: undefined
      },
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      orderForm: {
        id: undefined,
        itemId: '',
        itemName: '',
        itemPrice: 0,
        receiverName: '',
        receiverNickname: '',
        phone: '',
        receiverAddress: '',
        tradeTime: null,
        status: 1
      },
      rules: {
        itemId: [{ required: true, message: '请输入商品ID', trigger: 'blur' }],
        itemName: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        itemPrice: [{ required: true, message: '请输入商品价格', trigger: 'blur' }],
        receiverName: [{ required: true, message: '请输入收货人', trigger: 'blur' }],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ],
        receiverAddress: [{ required: true, message: '请输入收货地址', trigger: 'blur' }],
        tradeTime: [{ required: true, message: '请选择交易时间', trigger: 'change' }]
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
      getOrders(this.listQuery)
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
          console.error('Failed to get orders:', error)
          this.$message.error('获取订单列表失败')
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
      this.orderForm = {
        id: undefined,
        itemId: '',
        itemName: '',
        itemPrice: 0,
        receiverName: '',
        receiverNickname: '',
        phone: '',
        receiverAddress: '',
        tradeTime: null,
        status: 1
      }
      if (this.$refs.orderForm) {
        this.$refs.orderForm.clearValidate()
      }
    },
    handleCreate() {
      this.dialogTitle = '新增订单'
      this.dialogVisible = true
      this.resetForm()
    },
    handleUpdate(row) {
      this.dialogTitle = '编辑订单'
      this.orderForm = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.orderForm) {
          this.$refs.orderForm.clearValidate()
        }
      })
    },
    handleStatus(row) {
      const statusText = row.status === 1 ? '作废' : '恢复'
      this.$confirm(`确认${statusText}该订单吗？`, '提示', {
        type: 'warning'
      })
        .then(() => {
          const newStatus = row.status === 1 ? 0 : 1
          return updateOrderStatus(row.id, newStatus)
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
      this.$refs.orderForm.validate(valid => {
        if (!valid) return
        
        this.submitLoading = true
        const action = this.orderForm.id ? updateOrder : createOrder
        const actionText = this.orderForm.id ? '编辑' : '新增'
        
        action(this.orderForm)
          .then(() => {
            this.$message.success(`${actionText}订单成功`)
            this.dialogVisible = false
            this.getList()
          })
          .catch(error => {
            console.error('Submit form failed:', error)
            this.$message.error(`${actionText}订单失败`)
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

// 添加价格输入框的样式
:deep(.el-input-number) {
  .el-input__inner {
    text-align: left;
    padding-left: 15px;
  }
}
</style> 