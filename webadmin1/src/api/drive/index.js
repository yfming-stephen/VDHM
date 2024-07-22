import request from '@/utils/fetch'

/**
 * getDriveInfoByGroupIdAndPage
 * @param groupId 组ID
 * @param {Number} cur_page 页码
 * @param {Number} page_size 页数据量
 */
export function getDriveInfoByGroupIdAndPage(groupId,cur_page,page_size) {
    return request.get('/drive/getDriveInfoByGroupIdAndPage/'+groupId+'/'+cur_page+'/'+page_size)
}


/**
 * 根据hash获取全部记录
 * @param {*} hash 
 */
export function getRecordByHash(hash) {
    return request.get('/drive/getRecordByHash/'+hash)
}


/**
 * 根据hash 单独获取GPS记录
 * @param {*} hash 
 */
export function getGpsByHash(hash) {
    return request.get('/drive/getGpsByHash/'+hash)
}