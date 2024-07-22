package com.easylinker.proxy.server.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.constants.result.ReturnResult;
import com.easylinker.proxy.server.app.model.device.DeviceGroup;
import com.easylinker.proxy.server.app.model.user.AppUser;
import com.easylinker.proxy.server.app.service.DeviceGroupService;
import com.easylinker.proxy.server.app.service.DriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by ruilin on 2019/1/12.
 */
@RestController
@RequestMapping("/drive")
@PreAuthorize(value = "hasRole('ROLE_USER')")
public class DriveController {
    @Autowired
    DriveService driveService;
    @Autowired
    DeviceGroupService deviceGroupService;

    @RequestMapping(value = "/getGpsByHash/{hash}", method = RequestMethod.GET)
    public JSONObject getGpsByHash(@PathVariable String hash) {
        DeviceGroup deviceGroup= driveService.getDeviceGroupByHash(hash);
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (deviceGroup != null && deviceGroup.getAppUser().getId().longValue() == appUser.getId().longValue()) {
            return ReturnResult.returnDataMessage(1, "获取成功!", driveService.getGpsRecordByHash(hash));
        }
        return ReturnResult.returnTipMessage(0, "获取数据失败!");
    }


    @RequestMapping(value = "/getRecordByHash/{hash}", method = RequestMethod.GET)
    public JSONObject getRecordByHash(@PathVariable String hash) {
        DeviceGroup deviceGroup= driveService.getDeviceGroupByHash(hash);
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (deviceGroup != null && deviceGroup.getAppUser().getId().longValue() == appUser.getId().longValue()) {
            return ReturnResult.returnDataMessage(1, "获取成功!", driveService.getAllDrivingRecordByHash(hash));
        }
        return ReturnResult.returnTipMessage(0, "获取数据失败!");
    }

    @RequestMapping(value = "/getDriveInfoByGroupId/{groupId}", method = RequestMethod.GET)
    public JSONObject getDriveInfoByGroupId(@PathVariable Long groupId) {
        DeviceGroup deviceGroup=deviceGroupService.findADeviceGroupById(groupId);
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (deviceGroup != null && deviceGroup.getAppUser().getId().longValue() == appUser.getId().longValue()) {
            return ReturnResult.returnDataMessage(1, "获取成功!", driveService.getAllDriveInfoByDeviceGroup(deviceGroup));
        }
        return ReturnResult.returnTipMessage(0, "获取数据失败!");
    }

    @RequestMapping(value = "/getDriveInfoByGroupIdAndPage/{groupId}/{page}/{size}", method = RequestMethod.GET)
    public JSONObject getDriveInfoByGroupIdAndPage(@PathVariable Long groupId,@PathVariable int page, @PathVariable int size) {
        DeviceGroup deviceGroup=deviceGroupService.findADeviceGroupById(groupId);
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (deviceGroup != null && deviceGroup.getAppUser().getId().longValue() == appUser.getId().longValue()) {
            return ReturnResult.returnDataMessage(1, "获取成功!", driveService.getAllInfoByDeviceGroupAndPage(deviceGroup, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"))));
        }
        return ReturnResult.returnTipMessage(0, "获取数据失败!");
    }

    @RequestMapping(value = "/getDriveInfo", method = RequestMethod.POST)
    public JSONObject getDriveInfo(@RequestBody JSONObject driveBody) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (appUser != null) {
            Long groupId=driveBody.getLong("groupId");
            Date startDate=driveBody.getDate("startDate");
            System.out.println(startDate.toString());
            Date endDate=driveBody.getDate("endDate");
            DeviceGroup deviceGroup = deviceGroupService.findADeviceGroupById(groupId);
            if (deviceGroup != null && deviceGroup.getAppUser().getId().longValue() == appUser.getId().longValue()) {
                return ReturnResult.returnDataMessage(1, "获取成功!", driveService.getAllInfoByDeviceGroupAndDate(deviceGroup,startDate,endDate));
            }
        }
        return ReturnResult.returnTipMessage(0, "获取数据失败!");
    }

}
