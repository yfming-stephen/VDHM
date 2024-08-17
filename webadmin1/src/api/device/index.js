import request from '@/utils/fetch'

/**
 * 获取指定设备详细信息
 * @param DeviceId
 * @returns {*}
 */
export function getDeviceDetails(DeviceId) {
    return request.get('/device/getDeviceDetail/'+DeviceId)
}

/**
 * 获取指定设备日志
 * @param DeviceId
 * @returns {*}
 */
export function getDeviceLog(DeviceId) {
    return request.get('/device/getAllLogByPage/'+DeviceId+'/0/10')
}

/**
 * 分页获取全部日志信息
 * @param {Number} cur_page 页码
 * @param {Number} page_size 页数据量
 */
export function getAllLog(cur_page,page_size) {
    return request.get('/device/getAllLog/'+cur_page+'/'+page_size)
}

/**
 * cmd接口
 * @param {JSON} data
 */
export function sendCmdToDevice(data) {
    return request.post('/api/v1/sendCmdToDevice',data)
}

/**
 * 获取设备推流地址
 * @param {Number} deviceId 
 */
export function getPushLiveUrlByDevice(deviceId) {
    return request.get('/device/getPushLiveUrlByDevice/'+deviceId)
}


/**
 * 获取设备直播地址
 * @param {Number} deviceId 
 */
export function getLiveUrlByDevice(deviceId) {
    return request.get('/device/getLiveUrlByDevice/'+deviceId)
}

// /**
//  * 获取设备限制范围配置
//  * @param {Number} deviceId 
//  */
// export function getDeviceScope(deviceId) {
//     return request.get('/device/getDeviceScope/'+deviceId)
// }

// /**
//  * 保存设备限制范围配置
//  * @param {JSON} data
//  */
// export function updateValueLimit(data) {
//     return request.post('/device/updateValueLimit',data)
// }


/************************
 * 触发器 START
 * Trigger (DeviceScope)
 ************************/

/**
 * 分页获取用户的设备Trigger
 * @param {Number} cur_page 页码
 * @param {Number} page_size 页数据量
 */
export function getTriggers(cur_page,page_size) {
    return request.get('/device/getTriggers/'+cur_page+'/'+page_size)
}


/**
 * 删除指定触发器
 * @param {Number} deviceScopeId
 */
export function delTrigger(deviceScopeId) {
    return request.get('/device/delTrigger/'+deviceScopeId)
}


/**
 * 修改触发器配置信息
 * @param {JSON} data 
 */
export function updateTriggerSetting(data) {
    return request.post('/device/updateTriggerSetting',data)
}

/**
 * 开启/关闭触发器状态
 * @param {Number} deviceScopeId
 */
export function updateTriggerStatus(deviceScopeId) {
    return request.get('/device/updateTriggerStatus/'+deviceScopeId)
}


/**
 * 增加触发器
 * @param {JSON} data 
 */
export function addTrigger(data) {
    return request.post('/device/addTrigger',data)
}

/************************
 * 触发器 END
 * Trigger (DeviceScope)
 ************************/