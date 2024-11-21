import request from '@/utils/request'
import router from '@/router'

// 获取角色列表
export function getRoles(params) {
  return request({
    url: '/roles',
    method: 'get',
    params
  }).catch(error => {
    debugger;
    // 如果是401错误，跳转到登录页
    if (error.response && error.response.status === 401) {
      
      // router.push(`/login?redirect=${router.currentRoute.fullPath}`)
      router.push(`/login`)
    }
    return Promise.reject(error)
  })
}

// 创建角色
export function createRole(data) {
  return request({
    url: '/roles',
    method: 'post',
    data
  })
}

// 更新角色
export function updateRole(data) {
  return request({
    url: `/roles/${data.id}`,
    method: 'put',
    data
  })
}

// 更新角色状态
export function updateRoleStatus(id, status) {
  return request({
    url: `/roles/${id}/status`,
    method: 'put',
    params: { status }
  })
}

// 获取角色权限
export function getRolePermissions(roleId) {
  return request({
    url: `/roles/${roleId}/permissions`,
    method: 'get'
  })
}

// 更新角色权限
export function updateRolePermissions(roleId, permissionIds) {
  return request({
    url: `/roles/${roleId}/permissions`,
    method: 'put',
    data: { permissionIds }
  })
} 