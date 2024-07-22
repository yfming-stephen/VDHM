import request from '@/utils/fetch'

export function getChart(id) {
    return request.get('/device/getDeviceRecordByToday/'+id)
}
