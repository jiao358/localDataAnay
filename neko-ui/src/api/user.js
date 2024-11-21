import request from '@/utils/request'
import router from '@/router'
import { removeToken } from '@/utils/auth'

// 用户登录
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

// 获取用户列表
export function getUsers(params) {
  return request({
    url: '/users',
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

// 创建用户
export function createUser(data) {
  return request({
    url: '/users',
    method: 'post',
    data
  })
}

// 更新用户
export function updateUser(id, data) {
  return request({
    url: `/users/${id}`,
    method: 'put',
    data
  })
}

// 更新用户状态
export function updateUserStatus(id, status) {
  return request({
    url: `/users/${id}/status`,
    method: 'put',
    params: { status }
  })
}

// 获取用户信息
export function getInfo() {
  return request({
    url: '/users/info',
    method: 'get'
  })
}

// 用户登出
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

// 获取用户角色
export function getUserRoles(userId) {
  return request({
    url: `/users/${userId}/roles`,
    method: 'get'
  })
}

// 更新用户角色
export function updateUserRoles(userId, roleIds) {
  return request({
    url: `/users/${userId}/roles`,
    method: 'put',
    data: { roleIds }
  })
} 