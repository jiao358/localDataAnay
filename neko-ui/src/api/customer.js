import request from '@/utils/request'

// 获取客户列表
export function getCustomers(params) {
  return request({
    url: '/customers',
    method: 'get',
    params
  })
}

// 创建客户
export function createCustomer(data) {
  return request({
    url: '/customers',
    method: 'post',
    data
  })
}

// 更新客户
export function updateCustomer(data) {
  return request({
    url: `/customers/${data.id}`,
    method: 'put',
    data
  })
}

// 更新客户状态
export function updateCustomerStatus(id, status) {
  return request({
    url: `/customers/${id}/status`,
    method: 'put',
    params: { status }
  })
}

// 增加沟通次数
export function incrementCommunication(id) {
  return request({
    url: `/customers/${id}/communication`,
    method: 'put'
  })
} 