package com.easylinker.proxy.server.app.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.constants.result.ReturnResult;
import com.easylinker.proxy.server.app.model.user.AppUser;
import com.easylinker.proxy.server.app.service.DeviceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 企业管理控制器 :ROLE_ENTERPRISE
 * 企业用户可以自己创建设备
 *
 */
@RestController
@PreAuthorize(value = "hasRole('ROLE_ENTERPRISE') OR hasRole('ROLE_ADMIN')")
@RequestMapping("/enterprise")
public class EnterpriseUserController {
    @Autowired
    DeviceGroupService deviceGroupService;
    //分页获取设备组12月内平均数据
    @RequestMapping(value = "/getAllDeviceGroupeRecordDataByPage/{page}/{size}", method = RequestMethod.GET)
    public JSONObject DeviceGroupeRecordDataByPage(@PathVariable int page, @PathVariable int size){
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (appUser != null) {
            try {
                JSONArray data=deviceGroupService.getAllDeviceGroupeRecordDataByPage(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime")));
                return ReturnResult.returnDataMessage(1, "获取成功!", data);
            } catch (Exception e) {
                return ReturnResult.returnTipMessage(0, "获取失败!");
            }
        }
        return ReturnResult.returnTipMessage(0, "获取失败!");
    }
}
