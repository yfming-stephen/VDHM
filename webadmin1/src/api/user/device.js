import request from '@/utils/fetch'

/**
 * 用户查看自己的设备
 * @param {Number} cur_page 页码
 * @param {Number} page_size 页数据量
 */
export function getAllDevices(cur_page,page_size) {
    return request.get('/user/getAllDevices/'+cur_page+'/'+page_size)
}

/**
 * 用户增加设备
 * @param {JSON} data
 */
export function createADevice(data) {
    return request.post('/user/createADevice',data)
}
/**
 * 用户删除指定设备
 * @param {Number} id 设备id
 */
export function delADevice(id) {
    return request.delete('/user/delete/'+id)
}

/**
 * 用户查看自己的设备状况
 */
export function getCurrentState() {
    return request.get('/user/getCurrentState')
}

/**
根据用户查询设备
 */
export function searchByAppUser(keyWords) {
    return request.post('/user/searchByAppUser/'+keyWords)
}

/**
管理员用户修改设备
 */
export function changeDeviceGroup(data) {
    return request.post('/user/changeDeviceGroup',data)
}

/**
 * 添加定时任务
 */
export function addJob(data) {
    return request.post('/user/addJob',data)
}
export function getAllJob(cur_page,page_size) {
    return request.get('/user/getAllJobByAppUser/'+cur_page+'/'+page_size)
}

export function delAJob(id) {
    return request.delete('/user/deleteJob/'+id)
}

export function StopAJob(id) {
    return request.get('/user/stopJob/' + id)
}

/**
 * 获取当前用户所有在线设备信息
 * @param {Number} cur_page 页码
 * @param {Number} page_size 页数据量
 */
export function getAllOnlineDevicesByAppUser(cur_page,page_size) {
    return request.get('/user/getAllOnlineDevicesByAppUser/' + cur_page + '/' + page_size)
}
