import request from '@/utils/fetch'

/**
 * 根据用户获取设备全部位置
 */
export function getAllDeviceLocationByUser() {
    return request.get('/device/getAllDeviceLocationByUser')
}


export function  getAdeviceLocation(DeviceId) {
    return request.get('/device/getADeviceLocation/'+DeviceId);
}
