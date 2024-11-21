import request from '@/utils/request'

// 获取平台账号列表
export function getPlatforms(params) {
  return request({
    url: '/platforms',
    method: 'get',
    params
  })
}

// 获取平台类型字典
export function getPlatformTypes() {
  return request({
    url: '/platforms/types',
    method: 'get'
  })
}

// 创建平台账号
export function createPlatform(data) {
  return request({
    url: '/platforms',
    method: 'post',
    data
  })
}

// 更新平台账号
export function updatePlatform(data) {
  return request({
    url: `/platforms/${data.id}`,
    method: 'put',
    data
  })
}

// 更新平台账号状态
export function updatePlatformStatus(id, status) {
  return request({
    url: `/platforms/${id}/status`,
    method: 'put',
    params: { status }
  })
}

// 删除平台账号
export function deletePlatform(id) {
  return request({
    url: `/platforms/${id}`,
    method: 'delete'
  })
} 