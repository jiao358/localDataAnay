import request from '@/utils/request'
import router from '@/router'
import { removeToken } from '@/utils/auth'

// 获取字典类型列表
export function getDictTypes(params) {
  return request({
    url: '/dict/types',
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

// 获取字典数据列表
export function getDictData(dictCode) {
  return request({
    url: '/dict/data',
    method: 'get',
    params: { dictCode }
  }).catch(error => {
    if (error.response && error.response.status === 401) {
      removeToken()
      router.push('/login')
    }
    return Promise.reject(error)
  })
}

// 创建字典类型
export function createDictType(data) {
  return request({
    url: '/dict/types',
    method: 'post',
    data
  })
}

// 更新字典类型
export function updateDictType(data) {
  return request({
    url: `/dict/types/${data.id}`,
    method: 'put',
    data
  })
}

// 更新字典类型状态
export function updateDictTypeStatus(id, status) {
  return request({
    url: `/dict/types/${id}/status`,
    method: 'put',
    params: { status }
  })
}

// 创建字典数据
export function createDictData(data) {
  return request({
    url: '/dict/data',
    method: 'post',
    data
  })
}

// 更新字典数据
export function updateDictData(data) {
  return request({
    url: `/dict/data/${data.id}`,
    method: 'put',
    data
  })
}

// 更新字典数据状态
export function updateDictDataStatus(id, status) {
  return request({
    url: `/dict/data/${id}/status`,
    method: 'put',
    params: { status }
  })
}

// 获取字典数据Map
export function getDictDataMap(dictCode) {
  return request({
    url: '/dict/data/map',
    method: 'get',
    params: { dictCode }
  })
} 