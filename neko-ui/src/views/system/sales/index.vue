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
          placeholder="客户名称"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-select
          v-model="listQuery.itemCategory"
          placeholder="商品类目"
          clearable
          class="filter-item"
          style="width: 150px"
          :loading="dictLoading"
        >
          <el-option
            v-for="item in categoryOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-select
          v-model="listQuery.tradePlatform"
          placeholder="交易平台"
          clearable
          class="filter-item"
          style="width: 150px"
          :loading="dictLoading"
        >
          <el-option
            v-for="item in platformOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-input
          v-model="listQuery.salesPerson"
          placeholder="销售人员"
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
          新增订单
        </el-button>
        <el-button
          class="filter-item"
          type="info"
          icon="el-icon-refresh"
          circle
          :loading="dictLoading"
          @click="refreshDictData"
        />
        <el-dropdown class="filter-item" @command="handleExport">
          <el-button type="primary" icon="el-icon-download">
            导出Excel<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="selected">导出选中数据</el-dropdown-item>
            <el-dropdown-item command="all">导出全部数据</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        
        <el-upload
          class="filter-item"
          :show-file-list="false"
          accept=".xlsx, .xls"
          :before-upload="handleImport"
        >
          <el-button type="success" icon="el-icon-upload2">
            导入Excel
          </el-button>
        </el-upload>
      </div>

      <!-- 销售订单列表表格 -->
      <el-table
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        @selection-change="handleSelectionChange"
      >
        <template slot="empty">
          <div class="empty-text">
            {{ listLoading ? '加载中...' : '暂无数据' }}
          </div>
        </template>

        <el-table-column
          type="selection"
          width="55"
          align="center"
        />

        <el-table-column
          prop="customerName"
          label="客户名称"
          align="center"
          min-width="120"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            {{ scope.row.customerName || '-' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="customerGender"
          label="性别"
          align="center"
          width="80"
        />
        <el-table-column
          prop="customerAge"
          label="年龄"
          align="center"
          width="80"
        />
        <el-table-column
          prop="itemCategory"
          label="商品类目"
          align="center"
          min-width="120"
        >
          <template slot-scope="scope">
            {{ getDictLabel(categoryOptions, scope.row.itemCategory) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="itemName"
          label="商品名称"
          align="center"
          min-width="180"
        />
        <el-table-column
          prop="itemPrice"
          label="商品价格"
          align="center"
          width="120"
        >
          <template slot-scope="scope">
            ¥{{ scope.row.itemPrice ? Number(scope.row.itemPrice).toFixed(2) : '0.00' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="itemQuantity"
          label="数量"
          align="center"
          width="80"
        />
        <el-table-column
          prop="itemSku"
          label="SKU"
          align="center"
          min-width="120"
        />
        <el-table-column
          prop="tradePlatform"
          label="交易平台"
          align="center"
          min-width="120"
        >
          <template slot-scope="scope">
            {{ getDictLabel(platformOptions, scope.row.tradePlatform) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="salesPerson"
          label="销售人员"
          align="center"
          min-width="120"
        />
        <el-table-column
          prop="orderTime"
          label="订单时间"
          align="center"
          width="180"
        >
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.orderTime) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="deliveryTime"
          label="快递时间"
          align="center"
          width="180"
        >
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.deliveryTime) }}
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

      <!-- 销售订单表单对话框 -->
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
          <el-form-item label="客户名称" prop="customerName">
            <el-input v-model="orderForm.customerName" />
          </el-form-item>
          <el-form-item label="性别" prop="customerGender">
            <el-radio-group v-model="orderForm.customerGender">
              <el-radio label="男">男</el-radio>
              <el-radio label="女">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="年龄" prop="customerAge">
            <el-input-number 
              v-model="orderForm.customerAge"
              :min="1"
              :max="150"
            />
          </el-form-item>
          <el-form-item label="商品类目" prop="itemCategory">
            <el-select 
              v-model="orderForm.itemCategory" 
              placeholder="请选择商品类目"
              style="width: 100%"
            >
              <el-option
                v-for="item in categoryOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="商品名称" prop="itemName">
            <el-input v-model="orderForm.itemName" />
          </el-form-item>
          <el-form-item label="商品价格" prop="itemPrice">
            <el-input-number 
              v-model="orderForm.itemPrice"
              :precision="2"
              :min="0"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="商品数量" prop="itemQuantity">
            <el-input-number 
              v-model="orderForm.itemQuantity"
              :min="1"
            />
          </el-form-item>
          <el-form-item label="商品SKU" prop="itemSku">
            <el-input v-model="orderForm.itemSku" />
          </el-form-item>
          <el-form-item label="快递地址" prop="deliveryAddress">
            <el-input 
              v-model="orderForm.deliveryAddress"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
          <el-form-item label="联系电话" prop="contactPhone">
            <el-input v-model="orderForm.contactPhone" />
          </el-form-item>
          <el-form-item label="交易平台" prop="tradePlatform">
            <el-select 
              v-model="orderForm.tradePlatform" 
              placeholder="请选择交易平台"
              style="width: 100%"
            >
              <el-option
                v-for="item in platformOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="销售人员" prop="salesPerson">
            <el-input v-model="orderForm.salesPerson" />
          </el-form-item>
          <el-form-item label="订单时间" prop="orderTime">
            <el-date-picker
              v-model="orderForm.orderTime"
              type="datetime"
              placeholder="选择订单时间"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="快递时间" prop="deliveryTime">
            <el-date-picker
              v-model="orderForm.deliveryTime"
              type="datetime"
              placeholder="选择快递时间"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="快递编号" prop="trackingNumber">
            <el-input v-model="orderForm.trackingNumber" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitLoading">
            确 定
          </el-button>
        </div>
      </el-dialog>

      <!-- 添加导入模板下载按钮 -->
      <div class="import-help">
        <el-link type="primary" @click="downloadTemplate">下载导入模板</el-link>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getSalesOrders, createSalesOrder, updateSalesOrder } from '@/api/sales'
import { getDictData } from '@/api/dict'
import { exportExcel, importExcel } from '@/utils/excel'

export default {
  name: 'SalesManagement',
  data() {
    return {
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 10,
        customerName: '',
        itemCategory: '',
        tradePlatform: '',
        salesPerson: ''
      },
      categoryOptions: [],
      platformOptions: [],
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      orderForm: {
        id: undefined,
        customerName: '',
        customerGender: '男',
        customerAge: undefined,
        itemCategory: '',
        itemName: '',
        itemPrice: 0,
        itemQuantity: 1,
        itemSku: '',
        deliveryAddress: '',
        contactPhone: '',
        tradePlatform: '',
        salesPerson: '',
        orderTime: null,
        deliveryTime: null,
        trackingNumber: ''
      },
      rules: {
        customerName: [
          { required: true, message: '请输入客户名称', trigger: 'blur' }
        ],
        customerGender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        itemCategory: [
          { required: true, message: '请选择商品类目', trigger: 'change' }
        ],
        itemName: [
          { required: true, message: '请输入商品名称', trigger: 'blur' }
        ],
        itemPrice: [
          { required: true, message: '请输入商品价格', trigger: 'blur' }
        ],
        itemSku: [
          { required: true, message: '请输入商品SKU', trigger: 'blur' }
        ],
        deliveryAddress: [
          { required: true, message: '请输入快递地址', trigger: 'blur' }
        ],
        contactPhone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ],
        tradePlatform: [
          { required: true, message: '请选择交易平台', trigger: 'change' }
        ],
        salesPerson: [
          { required: true, message: '请输入销售人员', trigger: 'blur' }
        ],
        orderTime: [
          { required: true, message: '请选择订单时间', trigger: 'change' }
        ]
      },
      dictLoading: false,
      selectedRows: []
    }
  },
  computed: {
    ...mapGetters([
      'username'
    ])
  },
  created() {
    this.getList()
    this.loadDictData()
  },
  watch: {
    '$route': {
      handler() {
        this.getList()
      },
      immediate: true
    }
  },
  methods: {
    async loadDictData() {
      try {
        this.dictLoading = true
        const [categoryResponse, platformResponse] = await Promise.all([
          getDictData('item_category'),
          getDictData('trade_platform')
        ])

        if (categoryResponse && Array.isArray(categoryResponse.records)) {
          this.categoryOptions = categoryResponse.records
            .filter(item => item.status === 1)
            .map(item => ({
              value: item.dictValue,
              label: item.dictLabel
            }))
        }

        if (platformResponse && Array.isArray(platformResponse.records)) {
          this.platformOptions = platformResponse.records
            .filter(item => item.status === 1)
            .map(item => ({
              value: item.dictValue,
              label: item.dictLabel
            }))
        }
      } catch (error) {
        console.error('Failed to load dict data:', error)
        this.$message.error('加载字典数据失败')
      } finally {
        this.dictLoading = false
      }
    },
    async getList() {
      try {
        this.listLoading = true
        console.log('Request params:', this.listQuery)
        
        const response = await getSalesOrders(this.listQuery)
        console.log('API Response:', response)

        if (response && response.records) {
          this.list = response.records
          this.total = response.total
        } else if (Array.isArray(response)) {
          this.list = response
          this.total = response.length
        } else if (response && Array.isArray(response.data)) {
          this.list = response.data
          this.total = response.total || response.data.length
        } else {
          console.warn('Unexpected response format:', response)
          this.list = []
          this.total = 0
        }

        console.log('Processed list:', this.list)
        console.log('Total:', this.total)

      } catch (error) {
        console.error('Failed to get sales orders:', error)
        this.$message.error('获取销售订单列表失败')
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
      this.orderForm = {
        id: undefined,
        customerName: '',
        customerGender: '男',
        customerAge: undefined,
        itemCategory: '',
        itemName: '',
        itemPrice: 0,
        itemQuantity: 1,
        itemSku: '',
        deliveryAddress: '',
        contactPhone: '',
        tradePlatform: '',
        salesPerson: '',
        orderTime: null,
        deliveryTime: null,
        trackingNumber: ''
      }
      if (this.$refs.orderForm) {
        this.$refs.orderForm.clearValidate()
      }
    },
    handleCreate() {
      this.dialogTitle = '新增销售订单'
      this.dialogVisible = true
      this.resetForm()
    },
    handleUpdate(row) {
      this.dialogTitle = '编辑销售订单'
      this.orderForm = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.orderForm) {
          this.$refs.orderForm.clearValidate()
        }
      })
    },
    handleDelete(record) {
      this.$confirm('确认删除该销售订单吗？此操作不可恢复！', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          // TODO: 调用删除接口，传入 record.id
          console.log('Deleting sales order:', record.id)
          this.$message.success('删除成功')
          this.getList()
        })
        .catch(() => {})
    },
    submitForm() {
      this.$refs.orderForm.validate(async valid => {
        if (!valid) return
        
        try {
          this.submitLoading = true
          const isEdit = !!this.orderForm.id
          const action = isEdit ? updateSalesOrder : createSalesOrder
          const operationType = isEdit ? '编辑' : '新增'
          
          await action(this.orderForm)
          this.$message.success(`${operationType}销售订单成功`)
          this.dialogVisible = false
          this.getList()
        } catch (error) {
          console.error('Submit form failed:', error)
          const operationType = this.orderForm.id ? '编辑' : '新增'
          this.$message.error(`${operationType}销售订单失败`)
        } finally {
          this.submitLoading = false
        }
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
    },
    async refreshDictData() {
      await this.getDictData()
      this.getList()
    },
    getDictLabel(options, value) {
      const option = options.find(opt => opt.value === value)
      return option ? option.label : '-'
    },
    // 处理表格选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    // 处理导出
    async handleExport(command) {
      try {
        let exportData = []
        
        if (command === 'selected') {
          if (this.selectedRows.length === 0) {
            this.$message.warning('请至少选择一条数据')
            return
          }
          exportData = this.selectedRows
          this.$message.info(`正在导出 ${exportData.length} 条数据...`)
        } else if (command === 'all') {
          this.$message.info('正在导出全部数据...')
          const response = await getSalesOrders({
            ...this.listQuery,
            limit: 999999
          })
          
          if (response && response.records) {
            exportData = response.records
          } else if (Array.isArray(response)) {
            exportData = response
          }
        }

        if (exportData.length === 0) {
          this.$message.warning('没有可导出的数据')
          return
        }

        // 转换数据格式
        const formattedData = exportData.map(item => {
          // 确保所有字段都有值，避免undefined
          return {
            '客户名称': item.customerName || '',
            '性别': item.customerGender || '',
            '年龄': item.customerAge?.toString() || '',
            '商品类目': this.getDictLabel(this.categoryOptions, item.itemCategory) || '',
            '商品名称': item.itemName || '',
            '商品价格': item.itemPrice ? `¥${Number(item.itemPrice).toFixed(2)}` : '¥0.00',
            '数量': item.itemQuantity?.toString() || '0',
            'SKU': item.itemSku || '',
            '交易平台': this.getDictLabel(this.platformOptions, item.tradePlatform) || '',
            '销售人员': item.salesPerson || '',
            '订单时间': item.orderTime ? this.formatDateTime(item.orderTime) : '',
            '快递时间': item.deliveryTime ? this.formatDateTime(item.deliveryTime) : '',
            '快递编号': item.trackingNumber || ''
          }
        })

        // 生成文件名
        const timestamp = this.formatFileDateTime(new Date())
        const filename = `销售订单_${timestamp}.xlsx`

        // 导出Excel
        await exportExcel(formattedData, filename)
        this.$message.success(`成功导出 ${formattedData.length} 条数据`)
      } catch (error) {
        console.error('Export failed:', error)
        this.$message.error('导出失败：' + (error.message || '未知错误'))
      }
    },
    // 格式化文件名日期时间
    formatFileDateTime(date) {
      if (!date) return ''
      const d = new Date(date)
      const pad = (n) => n.toString().padStart(2, '0')
      return `${d.getFullYear()}${pad(d.getMonth() + 1)}${pad(d.getDate())}_${pad(d.getHours())}${pad(d.getMinutes())}${pad(d.getSeconds())}`
    },
    // 处理导入
    async handleImport(file) {
      try {
        this.$message.info('正在导入...')
        
        // 读取Excel文件
        const data = await importExcel(file)
        
        // 转换数据格式
        const importData = data.map(row => ({
          customerName: row['客户名称'],
          customerGender: row['性别'],
          customerAge: Number(row['年龄']),
          itemCategory: this.getCategoryValue(row['商品类目']),
          itemName: row['商品名称'],
          itemPrice: Number(row['商品价格']),
          itemQuantity: Number(row['数量']),
          itemSku: row['SKU'],
          tradePlatform: this.getPlatformValue(row['交易平台']),
          salesPerson: row['销售人员'],
          orderTime: row['订单时间'],
          deliveryTime: row['快递时间'],
          trackingNumber: row['快递编号']
        }))

        // TODO: 调用批量导入API
        console.log('Import data:', importData)
        
        this.$message.success(`成功导入 ${importData.length} 条数据`)
        this.getList() // 刷新列表
        
        return false // 阻止自动上传
      } catch (error) {
        console.error('Import failed:', error)
        this.$message.error('导入失败')
        return false
      }
    },

    // 下载导入模板
    downloadTemplate() {
      const template = [{
        '客户名称': '示例客户',
        '性别': '男',
        '年龄': '25',
        '商品类目': '数码电子',
        '商品名称': 'iPhone 15',
        '商品价格': '8999',
        '数量': '1',
        'SKU': 'IP15-256G-BLACK',
        '交易平台': '淘宝',
        '销售人员': '张三',
        '订单时间': '2024-01-20 14:30:00',
        '快递时间': '2024-01-21 10:00:00',
        '快递编号': 'SF1234567890'
      }]
      
      exportExcel(template, '销售订单导入模板.xlsx')
    },

    // 根据字典标签获取字典值
    getCategoryValue(label) {
      const option = this.categoryOptions.find(opt => opt.label === label)
      return option ? option.value : ''
    },

    getPlatformValue(label) {
      const option = this.platformOptions.find(opt => opt.label === label)
      return option ? option.value : ''
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

.empty-text {
  color: #909399;
  font-size: 14px;
  padding: 20px 0;
}

.debug-info {
  margin-top: 20px;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
  font-family: monospace;
  font-size: 12px;
  color: #666;
  white-space: pre-wrap;
}

.filter-container {
  .el-upload {
    display: inline-block;
    margin-left: 10px;
  }
}

.import-help {
  margin-top: 10px;
  text-align: right;
  padding: 0 20px;
}

.filter-container {
  .el-dropdown {
    margin-right: 10px;
    
    .el-button {
      display: flex;
      align-items: center;
      
      .el-icon--right {
        margin-left: 5px;
      }
    }
  }
}
</style> 