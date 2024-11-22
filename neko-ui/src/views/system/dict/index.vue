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
        <span class="module-name">字典管理</span>
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
      <!-- 字典类型管理 -->
      <div class="dict-type-section">
        <div class="section-title">字典类型管理</div>
        <div class="filter-container">
          <el-input
            v-model="typeQuery.dictName"
            placeholder="字典名称"
            style="width: 200px;"
            class="filter-item"
            @keyup.enter.native="handleTypeFilter"
          />
          <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleTypeFilter">
            搜索
          </el-button>
          <el-button
            class="filter-item"
            style="margin-left: 10px;"
            type="success"
            icon="el-icon-plus"
            @click="handleTypeCreate"
          >
            新增类型
          </el-button>
        </div>

        <el-table
          ref="typeTable"
          v-loading="typeLoading"
          :data="typeList"
          border
          fit
          highlight-current-row
          style="width: 100%;"
          @row-click="handleTypeSelect"
        >
          <el-table-column
            prop="dictName"
            label="字典名称"
            min-width="120"
          />
          <el-table-column
            prop="dictCode"
            label="字典编码"
            min-width="120"
          />
          <el-table-column
            prop="status"
            label="状态"
            width="100"
            align="center"
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
            width="280"
          >
            <template slot-scope="scope">
              <el-button 
                type="primary" 
                size="mini" 
                @click.stop="handleTypeUpdate(scope.row)"
              >
                编辑
              </el-button>
              <el-button
                type="warning"
                size="mini"
                @click.stop="handleDictData(scope.row)"
              >
                管理内容
              </el-button>
              <el-button
                :type="scope.row.status === 1 ? 'danger' : 'success'"
                size="mini"
                @click.stop="handleTypeStatus(scope.row)"
              >
                {{ scope.row.status === 1 ? '禁用' : '启用' }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 字典数据管理 -->
      <div class="dict-data-section" v-if="currentDictCode">
        <div class="section-title">
          字典数据管理 - {{ currentDictName }}
          <el-button
            style="float: right;"
            type="success"
            size="small"
            icon="el-icon-plus"
            @click="handleDataCreate"
          >
            新增数据
          </el-button>
        </div>

        <el-table
          v-loading="dataLoading"
          :data="dataList"
          border
          fit
          highlight-current-row
          style="width: 100%;"
        >
          <el-table-column
            prop="dictLabel"
            label="字典标签"
            min-width="120"
          />
          <el-table-column
            prop="dictValue"
            label="字典值"
            min-width="120"
          />
          <el-table-column
            prop="sortOrder"
            label="排序"
            width="80"
            align="center"
          />
          <el-table-column
            prop="status"
            label="状态"
            width="100"
            align="center"
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
            width="200"
          >
            <template slot-scope="scope">
              <el-button 
                type="primary" 
                size="mini" 
                @click="handleDataUpdate(scope.row)"
              >
                编辑
              </el-button>
              <el-button
                :type="scope.row.status === 1 ? 'danger' : 'success'"
                size="mini"
                @click="handleDataStatus(scope.row)"
              >
                {{ scope.row.status === 1 ? '禁用' : '启用' }}
              </el-button>
              <el-button
                type="danger"
                size="mini"
                @click="handleDataDelete(scope.row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 添加底部操作栏 -->
        <div class="section-footer">
          <el-button 
            type="primary" 
            icon="el-icon-back"
            @click="handleBack"
          >
            返回
          </el-button>
        </div>
      </div>
    </div>

    <!-- 字典类型表单对话框 -->
    <el-dialog 
      :title="typeDialogTitle" 
      :visible.sync="typeDialogVisible"
      width="500px"
    >
      <el-form
        ref="typeForm"
        :model="typeForm"
        :rules="typeRules"
        label-width="80px"
        label-position="right"
      >
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="typeForm.dictName" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="字典编码" prop="dictCode">
          <el-input 
            v-model="typeForm.dictCode" 
            placeholder="请输入字典编码"
            :disabled="!!typeForm.id"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="typeForm.status"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="typeDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitTypeForm" :loading="typeSubmitLoading">
          确 定
        </el-button>
      </div>
    </el-dialog>

    <!-- 字典数据表单对话框 -->
    <el-dialog 
      :title="dataDialogTitle" 
      :visible.sync="dataDialogVisible"
      width="500px"
    >
      <el-form
        ref="dataForm"
        :model="dataForm"
        :rules="dataRules"
        label-width="80px"
        label-position="right"
      >
        <el-form-item label="字典标签" prop="dictLabel">
          <el-input v-model="dataForm.dictLabel" placeholder="请输入字典标签" />
        </el-form-item>
        <el-form-item label="字典值" prop="dictValue">
          <el-input v-model="dataForm.dictValue" placeholder="请输入字典值" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number 
            v-model="dataForm.sortOrder"
            :min="0"
            :max="999"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="dataForm.status"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dataDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitDataForm" :loading="dataSubmitLoading">
          确 定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { 
  getDictTypes, 
  getDictData, 
  createDictType,
  updateDictType,
  updateDictTypeStatus,
  createDictData,
  updateDictData,
  updateDictDataStatus
} from '@/api/dict'

export default {
  name: 'DictManagement',
  data() {
    return {
      // 字典类型相关数据
      typeList: [],
      typeLoading: false,
      typeQuery: {
        dictName: ''
      },
      typeDialogVisible: false,
      typeDialogTitle: '',
      typeSubmitLoading: false,
      typeForm: {
        id: undefined,
        dictName: '',
        dictCode: '',
        status: 1
      },
      typeRules: {
        dictName: [
          { required: true, message: '请输入字典名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        dictCode: [
          { required: true, message: '请输入字典编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ]
      },

      // 字典数据相关数据
      currentDictCode: '',
      currentDictName: '',
      dataList: [],
      dataLoading: false,
      dataDialogVisible: false,
      dataDialogTitle: '',
      dataSubmitLoading: false,
      dataForm: {
        id: undefined,
        dictCode: '',
        dictLabel: '',
        dictValue: '',
        sortOrder: 0,
        status: 1
      },
      dataRules: {
        dictLabel: [
          { required: true, message: '请输入字典标签', trigger: 'blur' },
          { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
        ],
        dictValue: [
          { required: true, message: '请输入字典值', trigger: 'blur' },
          { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
        ],
        sortOrder: [
          { required: true, message: '请输入排序', trigger: 'blur' }
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
    this.getTypeList()
  },
  methods: {
    // 字典类型相关方法
    async getTypeList() {
      try {
        this.typeLoading = true
        const response = await getDictTypes(this.typeQuery)
        this.typeList = Array.isArray(response) ? response : (response.records || [])
      } catch (error) {
        console.error('Failed to get dict types:', error)
        this.$message.error('获取字典类型失败')
      } finally {
        this.typeLoading = false
      }
    },
    handleTypeFilter() {
      this.getTypeList()
    },
    handleTypeCreate() {
      this.typeDialogTitle = '新增字典类型'
      this.typeDialogVisible = true
      this.resetTypeForm()
    },
    handleTypeUpdate(row) {
      this.typeDialogTitle = '编辑字典类型'
      this.typeForm = { ...row }
      this.typeDialogVisible = true
    },
    async handleTypeStatus(row) {
      const statusText = row.status === 1 ? '禁用' : '启用'
      try {
        await this.$confirm(`确认${statusText}该字典类型吗？`, '提示', {
          type: 'warning'
        })
        await updateDictTypeStatus(row.id, row.status === 1 ? 0 : 1)
        this.$message.success(`${statusText}成功`)
        this.getTypeList()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('Failed to update type status:', error)
          this.$message.error('操作失败')
        }
      }
    },
    resetTypeForm() {
      this.typeForm = {
        id: undefined,
        dictName: '',
        dictCode: '',
        status: 1
      }
      if (this.$refs.typeForm) {
        this.$refs.typeForm.clearValidate()
      }
    },
    async submitTypeForm() {
      await this.$refs.typeForm.validate()
      
      try {
        this.typeSubmitLoading = true
        const isEdit = !!this.typeForm.id
        const action = isEdit ? updateDictType : createDictType
        const operationType = isEdit ? '编辑' : '新增'
        
        await action(this.typeForm)
        this.$message.success(`${operationType}字典类型成功`)
        this.typeDialogVisible = false
        this.getTypeList()
      } catch (error) {
        console.error('Failed to save dict type:', error)
        this.$message.error('保存失败')
      } finally {
        this.typeSubmitLoading = false
      }
    },

    // 字典数据相关方法
    handleTypeSelect(row) {
      this.currentDictCode = row.dictCode
      this.currentDictName = row.dictName
      this.getDataList()
    },
    async getDataList() {
      if (!this.currentDictCode) return
      
      try {
        this.dataLoading = true
        const response = await getDictData(this.currentDictCode)
        this.dataList = Array.isArray(response) ? response : (response.records || [])
      } catch (error) {
        console.error('Failed to get dict data:', error)
        this.$message.error('获取字典数据失败')
      } finally {
        this.dataLoading = false
      }
    },
    handleDataCreate() {
      this.dataDialogTitle = '新增字典数据'
      this.dataDialogVisible = true
      this.resetDataForm()
    },
    handleDataUpdate(row) {
      this.dataDialogTitle = '编辑字典数据'
      this.dataForm = { ...row }
      this.dataDialogVisible = true
    },
    async handleDataStatus(row) {
      const statusText = row.status === 1 ? '禁用' : '启用'
      try {
        await this.$confirm(`确认${statusText}该字典数据吗？`, '提示', {
          type: 'warning'
        })
        await updateDictDataStatus(row.id, row.status === 1 ? 0 : 1)
        this.$message.success(`${statusText}成功`)
        this.getDataList()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('Failed to update data status:', error)
          this.$message.error('操作失败')
        }
      }
    },
    handleDataDelete(rowData) {
      this.$confirm('确认删除该字典数据吗？此操作不可恢复！', '警告', {
        type: 'warning'
      })
        .then(() => {
          // TODO: 调用删除接口，传入 rowData.id
          console.log('Deleting dict data:', rowData.id)
          this.$message.success('删除成功')
          this.getDataList()
        })
        .catch(() => {})
    },
    resetDataForm() {
      this.dataForm = {
        id: undefined,
        dictCode: this.currentDictCode,
        dictLabel: '',
        dictValue: '',
        sortOrder: 0,
        status: 1
      }
      if (this.$refs.dataForm) {
        this.$refs.dataForm.clearValidate()
      }
    },
    async submitDataForm() {
      await this.$refs.dataForm.validate()
      
      try {
        this.dataSubmitLoading = true
        const isEdit = !!this.dataForm.id
        const action = isEdit ? updateDictData : createDictData
        const operationType = isEdit ? '编辑' : '新增'
        
        await action(this.dataForm)
        this.$message.success(`${operationType}字典数据成功`)
        this.dataDialogVisible = false
        this.getDataList()
      } catch (error) {
        console.error('Failed to save dict data:', error)
        this.$message.error('保存失败')
      } finally {
        this.dataSubmitLoading = false
      }
    },

    // 通用方法
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

    // 处理字典内容管理
    handleDictData(row) {
      // 方式一：在当前页面显示字典内容
      this.currentDictCode = row.dictCode
      this.currentDictName = row.dictName
      this.getDataList()

      // 滚动到字典内容区域
      this.$nextTick(() => {
        const dataSection = document.querySelector('.dict-data-section')
        if (dataSection) {
          dataSection.scrollIntoView({ behavior: 'smooth' })
        }
      })

      // 高亮显示选中的行
      this.$refs.typeTable.setCurrentRow(row)
    },

    // 处理返回操作
    handleBack() {
      this.currentDictCode = ''
      this.currentDictName = ''
      this.dataList = []
      // 清除表格选中状态
      if (this.$refs.typeTable) {
        this.$refs.typeTable.setCurrentRow(null)
      }
      // 滚动到顶部
      window.scrollTo({ top: 0, behavior: 'smooth' })
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
  display: flex;
  gap: 24px;

  .dict-type-section,
  .dict-data-section {
    flex: 1;
    background-color: #ffffff;
    border-radius: 4px;
    padding: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }

  .section-title {
    font-size: 18px;
    font-weight: 500;
    color: #000000;
    margin-bottom: 20px;
    padding-bottom: 10px;
    border-bottom: 1px solid #ebeef5;
  }
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

  .el-table__row {
    cursor: pointer;
    
    &:hover > td {
      background-color: #f5f7fa !important;
    }

    &.current-row > td {
      background-color: #ecf5ff !important;
    }
  }
}

// 搜索区域样式
.filter-container {
  margin-bottom: 20px;

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

  .el-dialog__footer {
    border-top: 1px solid #ebeef5;
    padding-top: 15px;
  }
}

// 响应式布局
@media screen and (max-width: 1200px) {
  .main-content {
    flex-direction: column;
  }
}

// 状态标签样式
:deep(.el-tag) {
  &.el-tag--success {
    background-color: #f0f9eb;
    border-color: #e1f3d8;
    color: #67c23a;
  }
  
  &.el-tag--danger {
    background-color: #fef0f0;
    border-color: #fde2e2;
    color: #f56c6c;
  }
}

// 按钮组样式
:deep(.el-button) {
  &.el-button--mini {
    padding: 7px 12px;
  }
  
  & + .el-button {
    margin-left: 6px;
  }
}

// 表单样式
:deep(.el-form) {
  .el-form-item {
    margin-bottom: 20px;
  }

  .el-input-number {
    width: 100%;
  }
}

// 用户下拉菜单样式
.user-dropdown {
  cursor: pointer;
  
  .user-info {
    display: flex;
    align-items: center;
    color: #ffffff;
    
    .avatar-icon {
      font-size: 20px;
      margin-right: 8px;
    }
    
    .username {
      margin-right: 4px;
    }
  }
}

:deep(.user-dropdown-menu) {
  .el-dropdown-menu__item {
    i {
      margin-right: 8px;
    }
  }
}

// 添加选中行的样式
:deep(.el-table) {
  .el-table__row {
    &.current-row > td {
      background-color: #ecf5ff !important;
    }
  }
}

// 优化按钮组样式
:deep(.el-button--mini) {
  padding: 7px 12px;
  
  & + .el-button--mini {
    margin-left: 6px;
  }
}

// 添加滚动过渡效果
.dict-data-section {
  scroll-margin-top: 20px;
  transition: all 0.3s ease;
}

.dict-data-section {
  .section-footer {
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;
    text-align: center;

    .el-button {
      padding: 12px 25px;
      font-size: 14px;
      
      i {
        margin-right: 5px;
      }
    }
  }
}

// 优化过渡效果
.dict-data-section {
  transition: all 0.3s ease;
  
  &-enter-active, &-leave-active {
    transition: opacity 0.3s ease;
  }
  
  &-enter, &-leave-to {
    opacity: 0;
  }
}
</style> 