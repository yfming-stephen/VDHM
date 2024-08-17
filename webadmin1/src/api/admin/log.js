import request from '@/utils/fetch'

/**
 * 管理员查看所有DailyLog
 * @param {Number} cur_page 页码
 * @param {Number} page_size 页数据量
 */
export function getAllDailyLogByPage(cur_page,page_size) {
    return request.get('/admin/getAllDailyLogByPage/'+cur_page+'/'+page_size)
}

/**
 * 管理员查看所有DeviceData
 * @param {Number} cur_page 页码
 * @param {Number} page_size 页数据量
 */
export function getAllDeviceDataByPage(cur_page,page_size) {
    return request.get('/admin/getAllDeviceDataByPage/'+cur_page+'/'+page_size)
}