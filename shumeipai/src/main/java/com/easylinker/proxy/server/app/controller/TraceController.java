package com.easylinker.proxy.server.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.constants.result.ReturnResult;
import com.easylinker.proxy.server.app.service.DeviceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ruilin on 2018/11/16.
 */
@RestController
@RequestMapping("/trace")
public class TraceController {
    @Autowired
    DeviceGroupService deviceGroupService;


    /**
     * 顾客溯源数据
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/getDeviceGroupeRecordDataByGroupId/{groupId}", method = RequestMethod.GET)
    public JSONObject getDeviceGroupeRecordDataByGroupId(@PathVariable Long groupId){
        return ReturnResult.returnDataMessage(1, "获取成功!", deviceGroupService.getDeviceGroupeRecordDataByGroupId(groupId));
    }
}
