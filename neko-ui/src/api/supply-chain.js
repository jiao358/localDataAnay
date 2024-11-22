import request from '@/utils/request'

export function getSupplyChainProducts(params) {
  return request({
    url: '/system/supply-chain',
    method: 'get',
    params: {
      current: params.current,
      size: params.size,
      productName: params.productName || '',
      categoryCode: params.categoryCode || '',
      supplierCompany: params.supplierCompany || ''
    }
  })
}

export function createSupplyChainProduct(data) {
  return request({
    url: '/system/supply-chain',
    method: 'post',
    data
  })
}

export function updateSupplyChainProduct(data) {
  return request({
    url: `/system/supply-chain/${data.id}`,
    method: 'put',
    data
  })
}

export function deleteSupplyChainProduct(id) {
  return request({
    url: `/system/supply-chain/${id}`,
    method: 'delete'
  })
}

export function uploadImage(data) {
  return request({
    url: '/system/supply-chain/upload',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
 