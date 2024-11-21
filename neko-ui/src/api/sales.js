import request from '@/utils/request'
import router from '@/router'
import { removeToken } from '@/utils/auth'

// 获取销售订单列表
export function getSalesOrders(params) {
  return request({
    url: '/sales',
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

// 创建销售订单
export function createSalesOrder(data) {
  return request({
    url: '/sales',
    method: 'post',
    data
  })
}

// 更新销售订单
export function updateSalesOrder(data) {
  return request({
    url: `/sales/${data.id}`,
    method: 'put',
    data
  })
}

// 删除销售订单
export function deleteSalesOrder(id) {
  return request({
    url: `/sales/${id}`,
    method: 'delete'
  })
}

// 更新销售订单状态
export function updateSalesOrderStatus(id, status) {
  return request({
    url: `/sales/${id}/status`,
    method: 'put',
    params: { status }
  })
} 