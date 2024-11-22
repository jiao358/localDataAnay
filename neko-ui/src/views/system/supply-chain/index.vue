<template>
  <div class="app-container">
    <div class="top-navbar">
      <div class="left-section">
        <div class="brand" @click="goToDashboard">
          <i class="el-icon-platform-eleme brand-icon"></i>
          <span class="brand-text">Neko Admin</span>
        </div>
      </div>
      
      <div class="center-section">
        <span class="module-name">供应链商品管理</span>
      </div>

      <div class="right-section">
        <el-dropdown trigger="click" class="user-dropdown">
          <div class="user-info">
            <i class="el-icon-user-solid avatar-icon"></i>
            <span class="username">{{ username }}</span>
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

    <div class="main-content">
      <!-- 搜索区域 -->
      <div class="filter-container">
        <el-input
          v-model="listQuery.productName"
          placeholder="商品名称"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-select
          v-model="listQuery.categoryCode"
          placeholder="商品类目"
          clearable
          class="filter-item"
          style="width: 150px"
        >
          <el-option
            v-for="item in categoryOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-input
          v-model="listQuery.supplierCompany"
          placeholder="供应商公司"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-input-number
          v-model="listQuery.minLimitPrice"
          placeholder="最低限售价"
          :min="0"
          :precision="2"
          class="filter-item"
          style="width: 130px;"
        />
        <el-input-number
          v-model="listQuery.maxLimitPrice"
          placeholder="最高限售价"
          :min="0"
          :precision="2"
          class="filter-item"
          style="width: 130px;"
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
          新增商品
        </el-button>
      </div>

      <!-- 表格区域 -->
      <el-table
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
      >
        <el-table-column
          prop="productName"
          label="商品名称"
          min-width="150"
          show-overflow-tooltip
        />
        <el-table-column
          prop="categoryCode"
          label="商品类目"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            {{ getDictLabel(categoryOptions, scope.row.categoryCode) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="wholesalePrice"
          label="批发价"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            ¥{{ scope.row.wholesalePrice.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="limitPrice"
          label="限售价"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            ¥{{ scope.row.limitPrice.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="quantity"
          label="数量"
          width="80"
          align="center"
        />
        <el-table-column
          prop="unitCode"
          label="单位"
          width="80"
          align="center"
        >
          <template slot-scope="scope">
            {{ getDictLabel(unitOptions, scope.row.unitCode) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="mainImage"
          label="主图"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            <el-image
              v-if="scope.row.mainImage"
              style="width: 50px; height: 50px"
              :src="scope.row.mainImage"
              fit="cover"
              :preview-src-list="[scope.row.mainImage]"
            />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="supplierCompany"
          label="供应商公司"
          min-width="150"
          show-overflow-tooltip
        />
        <el-table-column
          prop="supplierContact"
          label="联系人"
          width="100"
          align="center"
        />
        <el-table-column
          prop="supplierPhone"
          label="联系电话"
          width="120"
          align="center"
        />
        <el-table-column
          label="操作"
          align="center"
          width="230"
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
              @click="handleDelete(scope.row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="listQuery.current"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="listQuery.size"
          :total="total"
          background
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!-- 表单对话框 -->
      <el-dialog 
        :title="dialogTitle" 
        :visible.sync="dialogVisible"
        width="650px"
      >
        <el-form
          ref="form"
          :model="form"
          :rules="rules"
          label-width="100px"
          label-position="right"
        >
          <el-form-item label="商品名称" prop="productName">
            <el-input v-model="form.productName" />
          </el-form-item>
          <el-form-item label="商品类目" prop="categoryCode">
            <el-select v-model="form.categoryCode" style="width: 100%">
              <el-option
                v-for="item in categoryOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="批发价" prop="wholesalePrice">
            <el-input-number 
              v-model="form.wholesalePrice"
              :precision="2"
              :min="0"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="限售价" prop="limitPrice">
            <el-input-number 
              v-model="form.limitPrice"
              :precision="2"
              :min="0"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="商品数量" prop="quantity">
            <el-input-number 
              v-model="form.quantity"
              :min="0"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="商品单位" prop="unitCode">
            <el-select v-model="form.unitCode" style="width: 100%">
              <el-option
                v-for="item in unitOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="供应商公司" prop="supplierCompany">
            <el-input v-model="form.supplierCompany" />
          </el-form-item>
          <el-form-item label="联系人" prop="supplierContact">
            <el-input v-model="form.supplierContact" />
          </el-form-item>
          <el-form-item label="联系电话" prop="supplierPhone">
            <el-input v-model="form.supplierPhone" />
          </el-form-item>
          <el-form-item label="微信" prop="supplierWechat">
            <el-input v-model="form.supplierWechat" />
          </el-form-item>
          <el-form-item label="商品主图">
            <el-upload
              class="avatar-uploader"
              action="#"
              :http-request="handleMainImageUpload"
              :show-file-list="false"
              :before-upload="beforeImageUpload"
            >
              <img v-if="form.mainImage" :src="form.mainImage" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
          <el-form-item label="其他图片">
            <el-upload
              action="#"
              :http-request="handleOtherImageUpload"
              :file-list="otherImagesList"
              :before-upload="beforeImageUpload"
              :limit="10"
              list-type="picture-card"
              @remove="handleRemoveImage"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
          </el-form-item>
          <el-form-item label="素材链接" prop="materialUrl">
            <el-input v-model="form.materialUrl" type="textarea" :rows="2" />
          </el-form-item>
          <el-form-item label="其他描述" prop="description">
            <el-input v-model="form.description" type="textarea" :rows="3" />
          </el-form-item>
          <el-form-item label="批次时间" prop="batchTime">
            <el-date-picker
              v-model="form.batchTime"
              type="datetime"
              placeholder="选择批次时间"
              style="width: 100%"
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
import { getDictData } from '@/api/dict'
import { 
  getSupplyChainProducts, 
  createSupplyChainProduct, 
  updateSupplyChainProduct, 
  deleteSupplyChainProduct,
  uploadImage 
} from '@/api/supply-chain'

export default {
  name: 'SupplyChainProduct',
  computed: {
    ...mapGetters([
      'username'
    ]),
    dialogTitle() {
      return this.form.id ? '编辑商品' : '新增商品'
    }
  },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        current: 1,
        size: 10,
        productName: '',
        categoryCode: '',
        supplierCompany: '',
        minLimitPrice: undefined,
        maxLimitPrice: undefined
      },
      categoryOptions: [],
      unitOptions: [],
      dialogVisible: false,
      submitLoading: false,
      form: {
        id: undefined,
        productName: '',
        categoryCode: '',
        wholesalePrice: 0,
        limitPrice: 0,
        unitCode: '',
        quantity: 0,
        supplierCompany: '',
        supplierContact: '',
        supplierPhone: '',
        supplierWechat: '',
        mainImage: '',
        otherImages: [],
        materialUrl: '',
        description: '',
        batchTime: null
      },
      otherImagesList: [],
      rules: {
        productName: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
        categoryCode: [{ required: true, message: '请选择商品类目', trigger: 'change' }],
        wholesalePrice: [{ required: true, message: '请输入批发价', trigger: 'blur' }],
        limitPrice: [{ required: true, message: '请输入限售价', trigger: 'blur' }],
        unitCode: [{ required: true, message: '请选择商品单位', trigger: 'change' }],
        quantity: [{ required: true, message: '请输入商品数量', trigger: 'blur' }],
        supplierCompany: [{ required: true, message: '请输入供应商公司', trigger: 'blur' }],
        supplierContact: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
        supplierPhone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ],
        batchTime: [{ required: true, message: '请选择批次时间', trigger: 'change' }]
      }
    }
  },
  created() {
    this.loadDictData()
    this.getList()
  },
  methods: {
    async loadDictData() {
      try {
        const [categoryResponse, unitResponse] = await Promise.all([
          getDictData('product_category'),
          getDictData('product_unit')
        ])
        this.categoryOptions = this.formatDictOptions(categoryResponse)
        this.unitOptions = this.formatDictOptions(unitResponse)
      } catch (error) {
        console.error('Failed to load dict data:', error)
        this.$message.error('加载字典数据失败')
      }
    },
    formatDictOptions(response) {
      if (response && response.records) {
        return response.records
          .filter(item => item.status === 1)
          .map(item => ({
            label: item.dictLabel,
            value: item.dictValue
          }))
      }
      return []
    },
    getDictLabel(options, value) {
      const option = options.find(opt => opt.value === value)
      return option ? option.label : '-'
    },
    async getList() {
      try {
        this.listLoading = true
        const response = await getSupplyChainProducts(this.listQuery)
        console.log('Response:', response)
        
        if (response.data) {
          this.list = response.data.records || []
          this.total = response.data.total || 0
        } else {
          this.list = []
          this.total = 0
        }
      } catch (error) {
        console.error('Failed to get products:', error)
        this.$message.error('获取商品列表失败')
      } finally {
        this.listLoading = false
      }
    },
    handleFilter() {
      this.listQuery.current = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.listQuery.size = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.listQuery.current = val
      this.getList()
    },
    resetForm() {
      this.form = {
        id: undefined,
        productName: '',
        categoryCode: '',
        wholesalePrice: 0,
        limitPrice: 0,
        unitCode: '',
        quantity: 0,
        supplierCompany: '',
        supplierContact: '',
        supplierPhone: '',
        supplierWechat: '',
        mainImage: '',
        otherImages: [],
        materialUrl: '',
        description: '',
        batchTime: null
      }
      this.otherImagesList = []
      if (this.$refs.form) {
        this.$refs.form.clearValidate()
      }
    },
    handleCreate() {
      this.resetForm()
      this.dialogVisible = true
    },
    handleUpdate(row) {
      this.form = { ...row }
      this.otherImagesList = (row.otherImages || []).map(url => ({
        url,
        name: url.split('/').pop()
      }))
      this.dialogVisible = true
    },
    async handleDelete(id) {
      try {
        await this.$confirm('确认删除该商品吗？此操作不可恢复！', '警告', {
          type: 'warning'
        })
        await deleteSupplyChainProduct(id)
        this.$message.success('删除成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('Failed to delete:', error)
          this.$message.error('删除失败')
        }
      }
    },
    beforeImageUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt5M = file.size / 1024 / 1024 < 5

      if (!isImage) {
        this.$message.error('只能上传图片文件!')
        return false
      }
      if (!isLt5M) {
        this.$message.error('图片大小不能超过 5MB!')
        return false
      }
      return true
    },
    async handleMainImageUpload({ file }) {
      try {
        const formData = new FormData()
        formData.append('file', file)
        const response = await uploadImage(formData)
        
        if (response.code === 200 && response.data) {
          this.form.mainImage = response.data.url
          this.$message.success('上传成功')
        } else {
          throw new Error(response.message || '上传失败')
        }
      } catch (error) {
        console.error('Upload failed:', error)
        this.$message.error('上传失败：' + (error.message || '未知错误'))
      }
    },
    async handleOtherImageUpload({ file }) {
      try {
        const formData = new FormData()
        formData.append('file', file)
        const response = await uploadImage(formData)
        
        if (response.code === 200 && response.data) {
          this.form.otherImages = [...(this.form.otherImages || []), response.data.url]
          this.otherImagesList.push({
            url: response.data.url,
            name: file.name
          })
          this.$message.success('上传成功')
        } else {
          throw new Error(response.message || '上传失败')
        }
      } catch (error) {
        console.error('Upload failed:', error)
        this.$message.error('上传失败：' + (error.message || '未知错误'))
      }
    },
    handleRemoveImage(file) {
      const index = this.otherImagesList.findIndex(item => item.url === file.url)
      if (index > -1) {
        this.otherImagesList.splice(index, 1)
        this.form.otherImages.splice(index, 1)
      }
    },
    async submitForm() {
      try {
        await this.$refs.form.validate()
        this.submitLoading = true
        
        if (this.form.id) {
          await updateSupplyChainProduct(this.form)
          this.$message.success('更新成功')
        } else {
          await createSupplyChainProduct(this.form)
          this.$message.success('创建成功')
        }
        
        this.dialogVisible = false
        this.getList()
      } catch (error) {
        if (error !== false) {
          console.error('Submit form failed:', error)
          this.$message.error('保存失败')
        }
      } finally {
        this.submitLoading = false
      }
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

// 图片上传样式
.avatar-uploader {
  :deep(.el-upload) {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    
    &:hover {
      border-color: #409EFF;
    }
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 120px;
    height: 120px;
    line-height: 120px;
    text-align: center;
  }

  .avatar {
    width: 120px;
    height: 120px;
    display: block;
    object-fit: cover;
  }
}

:deep(.el-upload--picture-card) {
  width: 100px;
  height: 100px;
  line-height: 100px;
}

:deep(.el-upload-list--picture-card .el-upload-list__item) {
  width: 100px;
  height: 100px;
}
</style>
