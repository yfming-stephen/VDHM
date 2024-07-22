import request from '@/utils/fetch'
/**
 * 用户退出接口
 * @param {Number} id 
 */
export function logout(id) {
    return request.get('/logOut')
}

/**
 * 用户登陆接口
 * @param {JSON} data
 * @param {String} data.loginParam 用户名
 * @param {String} data.password 密码
 */
export function login(data) {
    return request.post('/userLogin', data)
}

/** 
 * 获取当前用户信息
*/
export function getCurrentUserInfo() {
    return request.get('/front/getCurrentUserInfo')
}
/** 
 * 查询当前的系统状态
*/
export function getCurrentState() {
    return request.get('/user/getCurrentState')
}

/**
 * 更新用户信息
 * @param {JSON} data 
 */
export function updateUserInfo(data) {
    return request.post('/user/updateUserInfo',data)
}
