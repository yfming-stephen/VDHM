package com.easylinker.proxy.server.app.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.config.mqttconfig.MqttMessageSender;
import com.easylinker.proxy.server.app.constants.result.ReturnResult;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.DeviceScope;
import com.easylinker.proxy.server.app.model.user.AppUser;
import com.easylinker.proxy.server.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 设备操作业务逻辑
 */
@RestController
@RequestMapping("/device")
@PreAuthorize(value = "hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
public class DeviceController {
    @Autowired
    DeviceService deviceService;
    @Autowired
    DeviceDataService deviceDataService;
    @Autowired
    LocationService locationService;

    @Autowired
    MqttMessageSender mqttMessageSender;

    @Autowired
    DeviceOnAndOffLineLogService deviceOnAndOffLineLogService;

    @Autowired
    DeviceScopeService deviceScopeService;
    @Autowired
    DeviceRecordService deviceRecordService;

    @Autowired
    DeviceWarningLogService deviceWarningLogService;

    /**
     * 获取单个设备的细节
     *
     * @return
     */
    @RequestMapping(value = "/getDeviceDetail/{deviceId}", method = RequestMethod.GET)
    public JSONObject getDeviceDetail(@PathVariable Long deviceId) {

        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Device device = deviceService.findADevice(deviceId);
        if (device != null && device.getAppUser().getId().longValue() == appUser.getId().longValue()) {

            return ReturnResult.returnDataMessage(1, "查询成功!", deviceService.getDeviceDetail(deviceId));
        } else {
            return ReturnResult.returnTipMessage(0, "设备不存在!");
        }


    }

    /**
     * 获取设备产生的数据
     *
     * @return
     */
    @RequestMapping(value = "/getDeviceData/{deviceId}/{page}/{size}", method = RequestMethod.GET)
    public JSONObject getDeviceData(@PathVariable Long deviceId, @PathVariable int page, @PathVariable int size) {
        Device device = deviceService.findADevice(deviceId);
        if (device == null) {
            return ReturnResult.returnTipMessage(0, "设备不存在!");
        }

        if (device.getAppUser() == null) {
            return ReturnResult.returnTipMessage(0, "设备未绑定!");
        }
        if (device != null) {
            JSONArray data = deviceDataService.getAllDeviceDataByDevice(device, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime")));

            return ReturnResult.returnDataMessage(1, "查询成功!", data);

        } else {
            return ReturnResult.returnTipMessage(0, "设备不存在!");
        }

    }


    /**
     * 分页获取设备日志
     */
    @RequestMapping(value = "/getAllLogByPage/{deviceId}/{page}/{size}", method = RequestMethod.GET)
    public JSONObject getAllLogByPage(@PathVariable Long deviceId, @PathVariable int page, @PathVariable int size) {
        Device device = deviceService.findADevice(deviceId);
        if (device == null) {
            return ReturnResult.returnTipMessage(0, "设备不存在!");
        }

        if (device.getAppUser() == null) {
            return ReturnResult.returnTipMessage(0, "设备未绑定!");
        }
        if (device != null) {

            return ReturnResult.returnDataMessage(1, "查询成功!",
                    deviceOnAndOffLineLogService.getAllLogByPage(
                            device, PageRequest.of(page, size,
                                    Sort.by(Sort.Direction.DESC,
                                            "id"))));

        } else {
            return ReturnResult.returnTipMessage(0, "设备不存在!");
        }
    }

    /**
     * 分页获取所有设备日志
     */
    @RequestMapping(value = "/getAllLog/{page}/{size}", method = RequestMethod.GET)
    public JSONObject getAllLog(@PathVariable int page, @PathVariable int size) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (GrantedAuthority authority : appUser.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                return ReturnResult.returnDataMessage(1, "查询成功!", deviceOnAndOffLineLogService.getAllLog(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"))));
            }
        }
        return ReturnResult.returnDataMessage(1, "查询成功!", deviceOnAndOffLineLogService.getAllLogByUser(appUser, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"))));
    }


    /**
     * 获取当日数据
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/getDeviceRecordByToday/{deviceId}", method = RequestMethod.GET)
    public JSONObject getDeviceRecordByToday(@PathVariable Long deviceId) {
        Device device = deviceService.findADevice(deviceId);
        if (device == null) {
            return ReturnResult.returnTipMessage(0, "设备不存在!");
        }

        if (device.getAppUser() == null) {
            return ReturnResult.returnTipMessage(0, "设备未绑定!");
        }
        if (device != null) {
            JSONArray data = deviceRecordService.getDeviceRecordByToday(device);
            return ReturnResult.returnDataMessage(1, "查询成功!", data);

        } else {
            return ReturnResult.returnTipMessage(0, "设备不存在!");
        }
    }

    /**
     * 根据当前用户获取全部设备当日数据
     *
     * @return
     */
    @RequestMapping(value = "/getAllDeviceRecordByUser", method = RequestMethod.GET)
    public JSONObject getAllDeviceRecordByUser() {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (appUser == null) {
            return ReturnResult.returnTipMessage(0, "用户不存在!");
        } else {
            JSONArray data = deviceRecordService.getAllDeviceRecordByUser(appUser);
            return ReturnResult.returnDataMessage(1, "查询成功!", data);
        }
    }


    /**
     * 根据用户获取设备全部位置
     * @return
     */
    @RequestMapping(value = "/getAllDeviceLocationByUser", method = RequestMethod.GET)
    public JSONObject getAllDeviceLocationByUser() {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (appUser == null) {
            return ReturnResult.returnTipMessage(0, "用户不存在!");
        } else {
            JSONArray data = locationService.getAllDevicesLocation(appUser);
            return ReturnResult.returnDataMessage(1, "查询成功!", data);
        }
    }



    @RequestMapping(value = "/getADeviceLocation/{deviceId}", method = RequestMethod.GET)
    public JSONObject getADeviceLocation(@PathVariable Long deviceId) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (appUser == null) {
            return ReturnResult.returnTipMessage(0, "用户不存在!");
        } else {
            JSONArray data = locationService.getADeviceLocation(deviceId);
            return ReturnResult.returnDataMessage(1, "查询成功!", data);
        }
    }

    /**
     * 增加Trigger上下限
     *
     * @param deviceBody
     * @return
     */
    @RequestMapping("/addTrigger")
    public JSONObject addTrigger(@RequestBody JSONObject deviceBody) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long deviceId = deviceBody.getLong("deviceId");
        //Integer isLimitValue = deviceBody.getInteger("isLimitValue");
        Double upperValue = deviceBody.getDouble("upperValue");
        Double lowerValue = deviceBody.getDouble("lowerValue");
        Double upperValue1 = deviceBody.getDouble("upperValue1");
        Double lowerValue1 = deviceBody.getDouble("lowerValue1");
        String name = deviceBody.getString("name");
        String info = deviceBody.getString("info");
        String payload = deviceBody.getString("payload");
        Device device = deviceService.findADevice(deviceId);
        if (deviceId == null || upperValue == null || lowerValue == null || lowerValue1 == null || upperValue1 == null) {
            return ReturnResult.returnTipMessage(0, "参数不全!");
        }
        if (device == null) {
            return ReturnResult.returnTipMessage(0, "设备不存在!");
        }
        if (device.getAppUser() == null) {
            return ReturnResult.returnTipMessage(0, "设备未绑定!");
        }
        if (device != null && device.getAppUser().getId().longValue() == appUser.getId().longValue()) {
            DeviceScope deviceScope = new DeviceScope();
            deviceScope.setDevice(device);
            deviceScope.setIsStart(1);
            deviceScope.setIsLimitValue(1);
            deviceScope.setLowerValue(lowerValue);
            deviceScope.setUpperValue(upperValue);
            deviceScope.setLowerValue1(lowerValue1);
            deviceScope.setUpperValue1(upperValue1);
            deviceScope.setName(name);
            deviceScope.setInfo(info);
            deviceScope.setPayload(payload);
            deviceScopeService.save(deviceScope);
            try {
                mqttMessageSender.sendDeviceScopeMessage(device.getTopic(), device);
            } catch (Exception e) {
                return ReturnResult.returnTipMessage(0, "配置失败!");
            }
            return ReturnResult.returnTipMessage(1, "配置成功!");
        } else {
            return ReturnResult.returnTipMessage(0, "设备不存在!");
        }
    }

    /**
     * 开启/关闭触发器状态
     *
     * @param deviceScopeId
     * @return
     */
    @RequestMapping(value = "/updateTriggerStatus/{deviceScopeId}", method = RequestMethod.GET)
    public JSONObject updateTriggerStatus(@PathVariable Long deviceScopeId) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (appUser != null) {
            try {
                DeviceScope deviceScope = deviceScopeService.findById(deviceScopeId);
                deviceScopeService.updateTriggerStatus(deviceScopeId);
                mqttMessageSender.sendDeviceScopeMessage(deviceScope.getDevice().getTopic(), deviceScope.getDevice());
            } catch (Exception e) {
                return ReturnResult.returnTipMessage(0, "更新状态失败!");
            }
            return ReturnResult.returnTipMessage(1, "更新状态成功!");

        }
        return ReturnResult.returnTipMessage(0, "更新状态失败!");
    }

    /**
     * 修改触发器配置信息
     *
     * @param deviceBody
     * @return
     */
    @RequestMapping(value = "/updateTriggerSetting", method = RequestMethod.POST)
    public JSONObject updateTriggerSetting(@RequestBody JSONObject deviceBody) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (appUser != null) {
            Long deviceScopeId = deviceBody.getLong("deviceScopeId");
            Long deviceId = deviceBody.getLong("deviceId");
            //Integer isLimitValue = deviceBody.getInteger("isLimitValue");
            Double upperValue = deviceBody.getDouble("upperValue");
            Double lowerValue = deviceBody.getDouble("lowerValue");
            Double upperValue1 = deviceBody.getDouble("upperValue1");
            Double lowerValue1 = deviceBody.getDouble("lowerValue1");
            String name = deviceBody.getString("name");
            String info = deviceBody.getString("info");
            String payload = deviceBody.getString("payload");
            if (deviceScopeId == null || upperValue == null || lowerValue == null ||upperValue1 == null || lowerValue1 == null) {
                return ReturnResult.returnTipMessage(0, "参数不全!");
            }
            DeviceScope deviceScope = deviceScopeService.findById(deviceScopeId);
            Device device = deviceService.findADevice(deviceId);
            //deviceScope.setIsLimitValue(isLimitValue);
            deviceScope.setLowerValue(lowerValue);
            deviceScope.setUpperValue(upperValue);
            deviceScope.setLowerValue1(lowerValue1);
            deviceScope.setUpperValue1(upperValue1);
            deviceScope.setDevice(device);
            deviceScope.setName(name);
            deviceScope.setInfo(info);
            deviceScope.setPayload(payload);
            deviceScopeService.save(deviceScope);
            try {
                mqttMessageSender.sendDeviceScopeMessage(deviceScope.getDevice().getTopic(), deviceScope.getDevice());
            } catch (Exception e) {
                return ReturnResult.returnTipMessage(0, "修改失败!");
            }
            return ReturnResult.returnTipMessage(1, "修改成功!");
        }
        return ReturnResult.returnTipMessage(0, "修改失败!");

    }

    /**
     * 删除指定触发器
     *
     * @param deviceScopeId
     * @return
     */
    @RequestMapping(value = "/delTrigger/{deviceScopeId}", method = RequestMethod.GET)
    public JSONObject delTrigger(@PathVariable Long deviceScopeId) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (appUser != null) {
            try {
                DeviceScope deviceScope=deviceScopeService.findById(deviceScopeId);
                deviceScope.setIsStart(0);
                mqttMessageSender.sendDeviceScopeMessage(deviceScope.getDevice().getTopic(), deviceScope.getDevice());
                deviceScopeService.delTrigger(deviceScope);
            } catch (Exception e) {
                return ReturnResult.returnTipMessage(0, "删除失败!");
            }
            return ReturnResult.returnTipMessage(1, "删除成功!");

        }
        return ReturnResult.returnTipMessage(0, "删除失败!");
    }


    /**
     * 分页获取设备Trigger
     *
     * @return
     */
    @RequestMapping(value = "/getTriggers/{page}/{size}", method = RequestMethod.GET)
    public JSONObject getTriggers(@PathVariable int page, @PathVariable int size) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (appUser != null) {
            return ReturnResult.returnDataMessage(1, "获取成功!", deviceScopeService.getAllDeviceScopeByUserAndPage(appUser, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"))));
        }
        return ReturnResult.returnTipMessage(0, "获取失败!");
    }


    /**
     * 分页获取设备的报警信息
     *
     * @param deviceId
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/getDeviceWarningLog/{deviceId}/{page}/{size}", method = RequestMethod.GET)
    public JSONObject getDeviceWarningLog(@PathVariable Long deviceId, @PathVariable int page, @PathVariable int size) {
        Device device = deviceService.findADevice(deviceId);
        if (device == null) {
            return ReturnResult.returnTipMessage(0, "设备不存在!");
        }

        if (device.getAppUser() == null) {
            return ReturnResult.returnTipMessage(0, "设备未绑定!");
        }
        if (device != null) {
            JSONObject data = deviceWarningLogService.getAllLogByDeviceAndPage(device, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime")));
            return ReturnResult.returnDataMessage(1, "查询成功!", data);

        } else {
            return ReturnResult.returnTipMessage(0, "设备不存在!");
        }

    }


    /**
     * 获取播流地址
     * flv
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/getLiveUrlByDevice/{deviceId}", method = RequestMethod.GET)
    public JSONObject getLiveUrlByDevice(@PathVariable Long deviceId) {
        Device device = deviceService.findADevice(deviceId);
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (device != null && device.getAppUser().getId().longValue() == appUser.getId().longValue()) {
            return ReturnResult.returnDataMessage(1, "获取成功!", deviceService.getLiveUrl(device.getSecretKey(),"flv"));
        }
        return ReturnResult.returnTipMessage(0, "获取播放地址失败!");
    }

    /**
     * 获取播流地址
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/getM3u8LiveUrlByDevice/{deviceId}", method = RequestMethod.GET)
    public JSONObject getM3u8LiveUrlByDevice(@PathVariable Long deviceId) {
        Device device = deviceService.findADevice(deviceId);
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (device != null && device.getAppUser().getId().longValue() == appUser.getId().longValue()) {
            return ReturnResult.returnDataMessage(1, "获取成功!", deviceService.getLiveUrl(device.getSecretKey(),"m3u8"));
        }
        return ReturnResult.returnTipMessage(0, "获取播放地址失败!");
    }

    /**
     * 获取推流地址
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/getPushLiveUrlByDevice/{deviceId}", method = RequestMethod.GET)
    public JSONObject getPushLiveUrlByDevice(@PathVariable Long deviceId) {
        Device device = deviceService.findADevice(deviceId);
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (device != null && device.getAppUser().getId().longValue() == appUser.getId().longValue()) {
            return ReturnResult.returnDataMessage(1, "获取成功!", deviceService.getPushLiveUrl(device.getSecretKey()));
        }
        return ReturnResult.returnTipMessage(0, "获取推流地址失败!");
    }


}
