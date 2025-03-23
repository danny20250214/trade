import request from '@/utils/request'

// 通用上传请求
export function upload(data) {
  return request({
    url: '/common/upload',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 通用下载请求
export function download(fileName) {
  return request({
    url: '/common/download?fileName=' + encodeURI(fileName),
    method: 'get',
    responseType: 'blob'
  })
} 