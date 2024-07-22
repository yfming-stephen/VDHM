import request from '@/utils/fetch'

/**
 * 根据userId获取userName
 * @param {Number} userId 用户id
 */
export function getUserNameByUserId(userId) {
    return request.get('/promotion/getUserNameByUserId/'+userId)
}

/**
 * 进入
 * @param {Number} userId 用户id
 */
export function enter(userId){
    return request.get('/promotion/enter/'+userId)
}