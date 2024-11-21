import request from '@/utils/request'

// 获取知识点列表
export function getKnowledgeList(params) {
  return request({
    url: '/knowledge',
    method: 'get',
    params
  })
}

// 创建知识点
export function createKnowledge(data) {
  return request({
    url: '/knowledge',
    method: 'post',
    data
  })
}

// 更新知识点
export function updateKnowledge(data) {
  return request({
    url: `/knowledge/${data.id}`,
    method: 'put',
    data
  })
}

// 删除知识点
export function deleteKnowledge(id) {
  return request({
    url: `/knowledge/${id}`,
    method: 'delete'
  })
}

// 上传知识点流程图
export function uploadProcessImage(formData) {
  return request({
    url: '/knowledge/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
} 