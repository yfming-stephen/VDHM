import request from '@/utils/fetch'

/**
 * 管理员查看所有设备
 * @param {Number} cur_page 页码
 * @param {Number} page_size 页数据量
 */
export function getAllDevices(cur_page,page_size) {
    return request.get('/admin/getAllDevices/'+cur_page+'/'+page_size)
}
/**
 * 获取设备信息
 * @param {Number} DeviceId 设备id
 */
export function getDeviceDetail(DeviceId) {
    return request.get('/admin/getDeviceDetail/'+DeviceId)
}

/**
 * 删除指定设备
 * @param {Number} id 设备id
 */
export function delADevice(id) {
    return request.delete('/admin/delete/'+id)
}

/**
 * 搜索设备
 * @param {*} keyword 
 */
export function selectDevice(keyword) {
    return request.post('/admin/search/'+keyword)
}