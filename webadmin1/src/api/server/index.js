import request from '@/utils/fetch'

/**
 * 获取系统参数
 */
export function getSystemInfo() {
    return request.get('/system/getSystemInfo')
}

/**
 * 获取EMQ节点的信息
 */
export function getEmqInfo() {
    return request.get('/api/v1/getEmqInfo')
}