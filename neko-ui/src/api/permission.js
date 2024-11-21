import request from '@/utils/request'
import router from '@/router'
import { removeToken } from '@/utils/auth'

// 获取权限列表
export function getPermissions(params) {
  return request({
    url: '/permissions',
    method: 'get',
    params
  }).catch(error => {
    if (error.response && error.response.status === 401) {
      removeToken()
      router.push('/login')
    }
    return Promise.reject(error)
  })
}

// 创建权限
export function createPermission(data) {
  return request({
    url: '/permissions',
    method: 'post',
    data
  })
}

// 更新权限
export function updatePermission(data) {
  return request({
    url: `/permissions/${data.id}`,
    method: 'put',
    data
  })
}

// 更新权限状态
export function updatePermissionStatus(id, status) {
  return request({
    url: `/permissions/${id}/status`,
    method: 'put',
    params: { status }
  })
}

// 获取角色的权限
export function getRolePermissions(roleId) {
  return request({
    url: `/roles/${roleId}/permissions`,
    method: 'get'
  })
}

// 更新角色的权限
export function updateRolePermissions(roleId, permissionIds) {
  return request({
    url: `/roles/${roleId}/permissions`,
    method: 'put',
    data: { permissionIds }
  })
} 