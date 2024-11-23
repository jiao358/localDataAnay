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
        <span class="module-name">知识模型</span>
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
          v-model="listQuery.contentName"
          placeholder="内容名称"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
          clearable
        />
        <el-input
          v-model="listQuery.contentSummary"
          placeholder="内容总结"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
          clearable
        />
        <el-select
          v-model="listQuery.contentCategory"
          placeholder="内容分类"
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
        <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
          搜索
        </el-button>
        <el-button class="filter-item" type="info" icon="el-icon-delete" @click="clearSearch">
          清空搜索
        </el-button>
        <el-button
          class="filter-item"
          style="margin-left: 10px;"
          type="success"
          icon="el-icon-plus"
          @click="handleCreate"
        >
          新增知识点
        </el-button>
      </div>

      <!-- 知识点列表 -->
      <el-table
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
      >
        <el-table-column
          prop="contentName"
          label="内容名称"
          min-width="200"
          show-overflow-tooltip
        />
        <el-table-column
          prop="contentCategory"
          label="内容分类"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            {{ getDictLabel(categoryOptions, scope.row.contentCategory) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="contentUrl"
          label="内容链接"
          min-width="200"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <el-link 
              type="primary" 
              :href="scope.row.contentUrl" 
              target="_blank"
              :underline="false"
            >
              {{ scope.row.contentUrl }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column
          prop="contentSummary"
          label="内容总结"
          min-width="300"
          show-overflow-tooltip
        />
        <el-table-column
          prop="processImage"
          label="知识点流程"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            <el-image
              v-if="scope.row.processImage"
              style="width: 50px; height: 50px; cursor: pointer"
              :src="getImageUrl(scope.row.processImage)"
              fit="cover"
              :preview-src-list="[getImageUrl(scope.row.processImage)]"
            >
              <div slot="error" class="image-slot">
                <i class="el-icon-picture-outline"></i>
              </div>
            </el-image>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="200"
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

      <!-- 知识点表单对话框 -->
      <el-dialog 
        :title="dialogTitle" 
        :visible.sync="dialogVisible"
        width="700px"
      >
        <el-form
          ref="knowledgeForm"
          :model="knowledgeForm"
          :rules="rules"
          label-width="100px"
          label-position="right"
        >
          <el-form-item label="内容名称" prop="contentName">
            <el-input v-model="knowledgeForm.contentName" />
          </el-form-item>
          <el-form-item label="内容分类" prop="contentCategory">
            <el-select v-model="knowledgeForm.contentCategory" style="width: 100%">
              <el-option
                v-for="item in categoryOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="内容链接" prop="contentUrl">
            <el-input v-model="knowledgeForm.contentUrl" />
          </el-form-item>
          <el-form-item label="内容总结" prop="contentSummary">
            <el-input
              v-model="knowledgeForm.contentSummary"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
          <el-form-item label="知识点流程">
            <el-upload
              class="upload-demo"
              action="#"
              :http-request="handleUpload"
              :show-file-list="false"
              :before-upload="beforeUpload"
            >
              <img v-if="knowledgeForm.processImage" :src="knowledgeForm.processImage" class="preview-image">
              <el-button v-else icon="el-icon-plus" type="primary">上传图片</el-button>
              <div slot="tip" class="el-upload__tip">只能上传 jpg/png 文件，且不超过 5MB</div>
            </el-upload>
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
import { getKnowledgeList, createKnowledge, updateKnowledge, deleteKnowledge, uploadProcessImage } from '@/api/knowledge'
import { getDictData } from '@/api/dict'
import { getImageUrl } from '@/utils/request'

export default {
  name: 'KnowledgeManagement',
  data() {
    return {
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 10,
        contentName: '',
        contentCategory: '',
        contentSummary: ''
      },
      categoryOptions: [],
      dialogVisible: false,
      dialogTitle: '',
      submitLoading: false,
      knowledgeForm: {
        id: undefined,
        contentName: '',
        contentCategory: '',
        contentUrl: '',
        contentSummary: '',
        processImage: ''
      },
      rules: {
        contentName: [
          { required: true, message: '请输入内容名称', trigger: 'blur' },
          { min: 2, max: 200, message: '长度在 2 到 200 个字符', trigger: 'blur' }
        ],
        contentCategory: [
          { required: true, message: '请选择内容分类', trigger: 'change' }
        ],
        contentUrl: [
          { required: true, message: '请输入内容链接', trigger: 'blur' },
          { type: 'url', message: '请输入正确的URL地址', trigger: 'blur' }
        ],
        contentSummary: [
          { required: true, message: '请输入内容总结', trigger: 'blur' }
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
    this.loadDictData().then(() => {
      this.getList()
    })
  },
  methods: {
    async loadDictData() {
      try {
        const response = await getDictData('content_category')
        console.log('Dictionary response:', response)
        
        if (response && response.records) {
          this.categoryOptions = response.records
            .filter(item => item.status === 1)
            .map(item => ({
              label: item.dictLabel,
              value: item.dictValue
            }))
        } else if (Array.isArray(response)) {
          this.categoryOptions = response
            .filter(item => item.status === 1)
            .map(item => ({
              label: item.dictLabel,
              value: item.dictValue
            }))
        } else {
          console.warn('Unexpected dictionary response format:', response)
          this.categoryOptions = [
            { label: '电商运营操作', value: '1' },
            { label: 'AI商品图', value: '2' },
            { label: 'AI内容生成', value: '3' },
            { label: 'AI视频生成', value: '4' },
            { label: '剪辑技巧', value: '5' },
            { label: 'AI剪辑', value: '6' },
            { label: 'AI未分类', value: '7' },
            { label: '工具介绍', value: '8' },
            { label: '商业模式', value: '9' },
            { label: '赚钱赛道', value: '10' },
            { label: '菜谱', value: '11' },
            { label: '深度思考', value: '12' }
          ]
        }
        
        console.log('Processed category options:', this.categoryOptions)
      } catch (error) {
        console.error('Failed to load dict data:', error)
        this.$message.error('加载字典数据失败，使用默认值')
        this.categoryOptions = [
          { label: '电商运营操作', value: '1' },
          { label: 'AI商品图', value: '2' },
          { label: 'AI内容生成', value: '3' },
          { label: 'AI视频生成', value: '4' },
          { label: '剪辑技巧', value: '5' },
          { label: 'AI剪辑', value: '6' },
          { label: 'AI未分类', value: '7' },
          { label: '工具介绍', value: '8' },
          { label: '商业模式', value: '9' },
          { label: '赚钱赛道', value: '10' },
          { label: '菜谱', value: '11' },
          { label: '深度思考', value: '12' }
        ]
      }
    },
    async getList() {
      try {
        this.listLoading = true
        const response = await getKnowledgeList({
          page: this.listQuery.page,
          limit: this.listQuery.limit,
          contentName: this.listQuery.contentName,
          contentCategory: this.listQuery.contentCategory,
          contentSummary: this.listQuery.contentSummary
        })
        
        // 使用后端返回的分页数据
        if (response && response.records) {
          this.list = response.records
          this.total = response.total
        } else {
          this.list = []
          this.total = 0
        }
      } catch (error) {
        console.error('Failed to get knowledge list:', error)
        this.$message.error('获取知识点列表失败')
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
      this.knowledgeForm = {
        id: undefined,
        contentName: '',
        contentCategory: '',
        contentUrl: '',
        contentSummary: '',
        processImage: ''
      }
      if (this.$refs.knowledgeForm) {
        this.$refs.knowledgeForm.clearValidate()
      }
    },
    handleCreate() {
      this.dialogTitle = '新增知识点'
      this.dialogVisible = true
      this.resetForm()
    },
    handleUpdate(row) {
      this.dialogTitle = '编辑知识点'
      this.knowledgeForm = { ...row }
      this.dialogVisible = true
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该知识点吗？', '警告', {
          type: 'warning'
        })
        await deleteKnowledge(row.id)
        this.$message.success('删除成功')
        this.getList()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('Delete failed:', error)
          this.$message.error('删除失败')
        }
      }
    },
    async handleUpload({ file }) {
      try {
        const formData = new FormData()
        formData.append('file', file)

        const response = await uploadProcessImage(formData)
        console.log('Upload response:', response)

        if (response && response.url) {
          this.knowledgeForm.processImage = response.url
          this.$message.success('上传成功')
        } else {
          throw new Error('Invalid response format')
        }
      } catch (error) {
        console.error('Upload failed:', error)
        this.$message.error('上传失败：' + (error.message || '未知错误'))
      }
    },
    beforeUpload(file) {
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

      const allowedTypes = ['image/jpeg', 'image/png', 'image/gif']
      if (!allowedTypes.includes(file.type)) {
        this.$message.error('只能上传 JPG/PNG/GIF 格式的图片!')
        return false
      }

      return true
    },
    async submitForm() {
      try {
        await this.$refs.knowledgeForm.validate()
        
        this.submitLoading = true
        const action = this.knowledgeForm.id ? updateKnowledge : createKnowledge
        const actionText = this.knowledgeForm.id ? '编辑' : '新增'
        
        await action(this.knowledgeForm)
        this.$message.success(`${actionText}知识点成功`)
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
    getDictLabel(options, value) {
      const option = options.find(opt => opt.value === value)
      return option ? option.label : '-'
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
    getImageUrl,
    clearSearch() {
      this.listQuery.contentName = ''
      this.listQuery.contentCategory = ''
      this.handleFilter()
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
.upload-demo {
  .preview-image {
    width: 200px;
    height: 200px;
    object-fit: cover;
    border-radius: 4px;
    border: 1px solid #dcdfe6;
    cursor: pointer;
    
    &:hover {
      border-color: #409EFF;
    }
  }
  
  .el-upload__tip {
    color: #909399;
    font-size: 12px;
    margin-top: 5px;
  }
}

// 添加图片预览相关样式
:deep(.el-image) {
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  overflow: hidden;
  
  &:hover {
    border-color: #409EFF;
    box-shadow: 0 0 8px rgba(0, 0, 0, 0.1);
  }
}

:deep(.el-image-viewer__wrapper) {
  .el-image-viewer__close {
    color: #fff;
    
    &:hover {
      color: #409EFF;
    }
  }
  
  .el-image-viewer__btn {
    color: #fff;
    
    &:hover {
      color: #409EFF;
    }
    
    i {
      font-size: 24px;
    }
  }
  
  .el-image-viewer__mask {
    background: rgba(0, 0, 0, 0.9);
  }
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 20px;
}
</style> 