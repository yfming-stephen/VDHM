import request from '@/utils/fetch'


export function getAllUsers(cur_page,page_size) {
    return request.get('/admin/getAllUsersByPage/'+cur_page+'/'+page_size)
}
