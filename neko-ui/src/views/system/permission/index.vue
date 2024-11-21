<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.keyword"
        placeholder="权限名称"
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
        type="primary"
        icon="el-icon-plus"
        @click="handleCreate"
      >
        添加
      </el-button>
    </div>

    <el-table
      :data="list"
      style="width: 100%;margin-top:30px;"
      row-key="id"
      border
      default-expand-all
      :tree-props="{children: 'children'}"
    >
      <el-table-column label="权限名称" prop="permissionName" />
      <el-table-column label="权限编码" prop="permissionCode" />
      <el-table-column label="权限类型" prop="permissionType">
        <template slot-scope="{row}">
          {{ row.permissionType | permissionTypeFilter }}
        </template>
      </el-table-column>
      <el-table-column label="路由路径" prop="path" />
      <el-table-column label="组件" prop="component" />
      <el-table-column label="图标" prop="icon">
        <template slot-scope="{row}">
          <i :class="row.icon"></i>
        </template>
      </el-table-column>
      <el-table-column label="排序" prop="sortOrder" width="80" />
      <el-table-column label="状态" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusFilter">
            {{ row.status | statusTextFilter }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button type="danger" size="mini" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'PermissionManagement',
  filters: {
    permissionTypeFilter(type) {
      const typeMap = {
        1: '菜单',
        2: '按钮',
        3: '接口'
      }
      return typeMap[type]
    },
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'danger'
      }
      return statusMap[status]
    },
    statusTextFilter(status) {
      const statusMap = {
        1: '启用',
        0: '禁用'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: [],
      listQuery: {
        keyword: ''
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      // TODO: 实现获取权限列表的方法
    },
    handleFilter() {
      this.getList()
    },
    handleCreate() {
      // TODO: 实现创建权限的方法
      console.log('创建权限')
    },
    handleUpdate(row) {
      // TODO: 实现更新权限的方法
      console.log('更新权限', row.id)
    },
    handleDelete(row) {
      // TODO: 实现删除权限的方法
      console.log('删除权限', row.id)
    }
  }
}
</script> 