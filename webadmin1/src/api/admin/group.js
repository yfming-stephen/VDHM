import request from '@/utils/fetch'

export function delAGroup(id) {
    return request.delete('/admin/deleteGroup/'+id)
}
export function getAllGroupsByPage(cur_page,page_size) {
    return request.get('/admin/getAllGroupByPage/'+cur_page+'/'+page_size)
}

