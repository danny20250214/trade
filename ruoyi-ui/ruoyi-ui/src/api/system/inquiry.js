import request from '@/utils/request'

// 查询产品咨询记录列表
export function listInquiry(query) {
  return request({
    url: '/system/inquiry/list',
    method: 'get',
    params: query
  })
}

// 删除产品咨询记录
export function delInquiry(id) {
  return request({
    url: '/system/inquiry/' + id,
    method: 'delete'
  })
}

// 修改产品咨询记录
export function updateInquiry(data) {
  return request({
    url: '/system/inquiry',
    method: 'put',
    data: data
  })
}
