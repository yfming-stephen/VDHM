import request from '@/utils/fetch'

/**
 * 分页获取商家信息
 * @param {Number} cur_page 页码
 * @param {Number} page_size 页数据量
 */
export function getAllDeviceGroupeRecordDataByPage(cur_page,page_size) {
    return request.get('/enterprise/getAllDeviceGroupeRecordDataByPage/'+cur_page+'/'+page_size)
}