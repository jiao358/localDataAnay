import request from '@/utils/request'

export function uploadFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  
  return request({
    url: '/api/common/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function getImageUrl(path) {
  if (!path) return ''
  if (path.startsWith('http')) return path
  return process.env.VUE_APP_BASE_API + '/api/common/file/' + path
} 