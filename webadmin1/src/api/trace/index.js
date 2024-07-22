import request from '@/utils/fetch'

/**
 * 获取系统参数
 */
export function getTraceByGroup(groupId) {
    return request.get('/trace/getDeviceGroupeRecordDataByGroupId/'+groupId)
}

