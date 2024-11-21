import request from '@/utils/request'

// 获取订单列表
export function getOrders(params) {
  return request({
    url: '/orders',
    method: 'get',
    params
  })
}

// 创建订单
export function createOrder(data) {
  return request({
    url: '/orders',
    method: 'post',
    data
  })
}

// 更新订单
export function updateOrder(data) {
  return request({
    url: `/orders/${data.id}`,
    method: 'put',
    data
  })
}

// 更新订单状态
export function updateOrderStatus(id, status) {
  return request({
    url: `/orders/${id}/status`,
    method: 'put',
    params: { status }
  })
} 