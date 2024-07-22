package com.easylinker.proxy.server.app.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.constants.result.ReturnResult;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.DeviceGroup;
import com.easylinker.proxy.server.app.model.device.Location;
import com.easylinker.proxy.server.app.model.user.AppUser;
import com.easylinker.proxy.server.app.service.*;
import com.easylinker.proxy.server.app.utils.Image2Base64Tool;
import com.easylinker.proxy.server.app.utils.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 普通管理员业务逻辑控制器
 */
@RestController
@RequestMapping("/admin")
@PreAuthorize(value = "hasRole('ROLE_ADMIN') AND hasRole('ROLE_USER')")
public class AdminController {
    private static final String REG_1_Z = "^[\\u4e00-\\u9fff\\w]{6,10}$";
    @Autowired
    LocationService locationService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    DeviceGroupService deviceGroupService;
    @Autowired
    DeviceDataService deviceDataService;
    @Autowired
    AppUserService appUserService;
    @Autowired
    DailyLogService dailyLogService;

    /**
     * 管理员增加一个设备
     * 默认增加的设备是没有用户的，后面用户购买设备扫码的时候 再绑定用户
     *
     * @param deviceBody 包含设备信息的JSON
     * @return
     */

    @RequestMapping("/addADevice")
    public JSONObject addADevice(@RequestBody JSONObject deviceBody) {
        String deviceName = deviceBody.getString("deviceName");
        String deviceDescribe = deviceBody.getString("deviceDescribe");
        String deviceNamePrefix = deviceBody.getString("deviceNamePrefix");
        String latitude = deviceBody.getString("latitude");
        String longitude = deviceBody.getString("longitude");
        String locationDescribe = deviceBody.getString("locationDescribe");
        String groupName = deviceBody.getString("groupName");


        if (deviceDescribe == null || deviceName == null || groupName == null || latitude == null || longitude == null || locationDescribe == null || deviceNamePrefix == null) {
            return ReturnResult.returnTipMessage(0, "参数不全!");


        } else if (!groupName.matches(REG_1_Z)) {
            return ReturnResult.returnTipMessage(0, "设备组名字不下6位!");
        } else if (!deviceNamePrefix.matches(REG_1_Z)) {
            return ReturnResult.returnTipMessage(0, "设备名称前缀不下6位!");
        } else {

            Device device = new Device();
            //DeviceGroup deviceGroup = new DeviceGroup();
//            deviceGroup.setComment("默认分组");
//            deviceGroup.setGroupName(groupName);
//            deviceGroupService.save(deviceGroup);//保存分组
            device.setDeviceGroup(null);
            device.setAppUser(null);
            device.setLastActiveDate(new Date());
            device.setDeviceName(deviceNamePrefix + "_" + deviceName);
            device.setDeviceDescribe("Device_" + deviceDescribe);
            device.setClientId(device.getId().toString());
            //设置ACL  默认值
            device.setTopic("IN/DEVICE/DEFAULT_USER/" + "DEFAULT_GROUP" + "/" + device.getId());
            device.setBarCode(Image2Base64Tool.imageToBase64String(QRCodeGenerator.string2BarCode(device.getId().toString())));
            device.setOpenId(device.getId().toString());
            Location location = new Location();
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setLocationDescribe(locationDescribe);
            //先保存位置
            locationService.save(location);
            device.setLocation(location);
            deviceService.save(device);
            return ReturnResult.returnTipMessage(1, "设备创建成功!");
        }
    }

    /**
     * 批量增加设备
     * 批量增加设备不需要指定专门的名称 前缀，自动生成.
     *
     * @param deviceBody
     * @return
     */
    @RequestMapping("/batchAddADevice")
    public JSONObject batchAddADevice(@RequestBody JSONObject deviceBody) {

        int deviceSum = deviceBody.getIntValue("deviceSum");
        String deviceNamePrefix = deviceBody.getString("deviceNamePrefix");
        String latitude = deviceBody.getString("latitude");
        String longitude = deviceBody.getString("longitude");
        String locationDescribe = deviceBody.getString("locationDescribe");
        String groupName = deviceBody.getString("groupName");

        if (groupName == null || latitude == null || longitude == null || locationDescribe == null || deviceNamePrefix == null) {
            return ReturnResult.returnTipMessage(0, "参数不全，请检查参数后提交!");
        } else if (deviceSum <= 0) {
            return ReturnResult.returnTipMessage(0, "至少选择创建一个设备!");


        } else if (!groupName.matches(REG_1_Z)) {
            return ReturnResult.returnTipMessage(0, "设备组名不下6位!");

        } else if (!deviceNamePrefix.matches(REG_1_Z)) {
            return ReturnResult.returnTipMessage(0, "设备名称不下6位!");
        } else {
//            //DeviceGroup deviceGroup = new DeviceGroup();
//            deviceGroup.setAppUser(null);
//            deviceGroup.setComment("默认分组");
//            deviceGroup.setGroupName(groupName);
//            deviceGroupService.save(deviceGroup);//所有新增加的设备，分组一样,保存分组

            for (int i = 0; i < deviceSum; i++) {
                Location location = new Location();
                location.setLatitude(latitude);
                location.setLongitude(longitude);
                location.setLocationDescribe(locationDescribe);
                locationService.save(location);//先保存位置
                Device device = new Device();
                device.setDeviceGroup(null);
                device.setLastActiveDate(new Date());
                device.setDeviceName(deviceNamePrefix + "_Auto_" + i);
                device.setDeviceDescribe("Product_" + i);
                device.setClientId(device.getId().toString());
                //设置ACL  默认值

                device.setTopic("IN/DEVICE/DEFAULT_USER/" + "DEFAULT_GROUP" + "/" + device.getId());
                device.setBarCode(Image2Base64Tool.imageToBase64String(QRCodeGenerator.string2BarCode(device.getId().toString())));
                device.setOpenId(device.getId().toString());
                device.setLocation(location);
                deviceService.save(device);
            }
            return ReturnResult.returnTipMessage(1, "批量添加设备成功!");
        }

    }

    /**
     * 删除设备
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public JSONObject delete(@PathVariable("id") Long id) {
        Device device = deviceService.findADevice(id);
        if (device != null) {
            deviceService.delete(device);
            return ReturnResult.returnTipMessage(1, "设备删除成功!");

        } else {
            return ReturnResult.returnTipMessage(0, "设备不存在!");
        }
    }

    /**
     * 管理员查看所有设备
     *
     * @return
     */
    @RequestMapping(value = "/getAllDevices/{page}/{size}", method = RequestMethod.GET)
    public JSONObject getAllDevices(@PathVariable int page, @PathVariable int size) {
        return ReturnResult.returnDataMessage(1, "查询成功!",
                deviceService.getAllDevices(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"))));

    }

    /**
     * 管理员绑定设备建议换成V2
     * <p>
     * /**
     * 管理员绑定设备新版接口
     *
     * @return
     */
    @RequestMapping(value = "/bindDevicesToUser", method = RequestMethod.POST)
    public JSONObject bindDevicesToUser(@RequestBody JSONObject body) {
        Long userId = body.getLongValue("userId");
        String groupName = body.getString("groupName");
        JSONArray deviceIdArray;
        try {
            deviceIdArray = body.getJSONArray("deviceIdArray");
        } catch (Exception e) {
            return ReturnResult.returnTipMessage(1, "deviceIdArray应该为数组!");

        }
        if (userId == null || groupName == null || deviceIdArray == null) {
            return ReturnResult.returnTipMessage(0, "参数不全!");
        }
//        if (!groupName.matches(REG_1_Z)) {
//            return ReturnResult.returnTipMessage(0, "设备组名不下6位!");
//        }
        else {
            int total = deviceIdArray.size();
            int successCount = 0;
            AppUser appUser = appUserService.findAAppUser(userId);
            if (appUser != null) {
                //更新ACL
                DeviceGroup newGroup = new DeviceGroup();
                newGroup.setGroupName("G" + appUser.getId());
                newGroup.setAppUser(appUser);
                newGroup.setComment(new Date().toString().replace(" ", "_"));
                deviceGroupService.save(newGroup);
                for (Object o : deviceIdArray) {
                    Device device = deviceService.findADevice((Long.parseLong(o.toString())));
                    if (device != null && device.getAppUser() == null) {
                        successCount += 1;
                        device.setAppUser(appUser);
                        device.setDeviceGroup(newGroup);
                        device.setSecretKey(appUser.getId() + "-" + device.getDeviceGroup().getId() + "-" + device.getId());
                        device.setTopic("IN/DEVICE/" + appUser.getId() + "/" + newGroup.getId() + "/" + device.getId());
                        deviceService.save(device);
                    }

                }
                return ReturnResult.returnTipMessage(1, "结果:总数[" + total + "]成功[" + successCount + "]失败[" + (total - successCount) + "],如果有失败，可能原因:部分设备ID不存在!");


            } else {
                return ReturnResult.returnTipMessage(0, "用户不存在!");

            }

        }

    }


    /**
     * 查看当前所有的用户
     *
     * @return
     */
    @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)

    public JSONObject getAllUsers() {

        return ReturnResult.returnDataMessage(1, "获取成功!", appUserService.getAllUsers());

    }

    /**
     * 页码当前所有的用户
     *
     * @return
     */
    @RequestMapping(value = "/getAllUsersByPage/{page}/{size}", method = RequestMethod.GET)

    public JSONObject getAllUsersByPage(@PathVariable int page, @PathVariable int size) {

        return ReturnResult.returnDataMessage(1, "获取成功!", appUserService.getAllUsersByPage(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"))));

    }


    /**
     * 获取单个设备的细节
     *
     * @return
     */
    @RequestMapping(value = "/getDeviceDetail/{deviceId}", method = RequestMethod.GET)
    public JSONObject getDeviceDetail(@PathVariable Long deviceId) {
        Device device = deviceService.findADevice(deviceId);
        if (device != null) {
            return ReturnResult.returnDataMessage(1, "查询成功!",deviceService.getDeviceDetail(deviceId));
        } else {
            return ReturnResult.returnTipMessage(0, "设备不存在!");
        }
    }


    /**
     * 分页获取分组
     */
    @RequestMapping(value = "/getAllGroupByPage/{page}/{size}", method = RequestMethod.GET)
    public JSONObject getAllGroupByPage(@PathVariable int page, @PathVariable int size) {
        return ReturnResult.returnDataMessage(1, "获取成功!",
                deviceGroupService.getAllDeviceGroupByPage(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"))));

    }

    /**
     * 系统实时数据
     * 1 当前在线设备数目
     * 2 用户量
     * 3 绑定数
     * 4 设备总数
     * 5 离线
     * 6 当前消息
     *
     * @return
     */
    @RequestMapping(value = "/getRealTimeData", method = RequestMethod.GET)
    public JSONObject getRealTimeData() {
        JSONObject data = new JSONObject();
        data.put("totalDevice", deviceService.findAllDevice().size());
        data.put("online", deviceService.findAllOnlineDevice());
        data.put("userCount", appUserService.getAllUsers().size());
        return ReturnResult.returnDataMessage(1, "查询成功!", data);
    }


    @RequestMapping(value = "/deleteGroup/{id}", method = RequestMethod.DELETE)
    public JSONObject deleteGroup(@PathVariable("id") Long id) {
        DeviceGroup deviceGroup=deviceGroupService.findADeviceGroupById(id);
        if (deviceGroup != null) {
            deviceGroupService.delete(deviceGroupService.findADeviceGroupById(id));
            return ReturnResult.returnTipMessage(1, "群组删除成功!");
        } else {
            return ReturnResult.returnTipMessage(0, "该群组不存在!");
        }
    }
    /**
     * 关键字搜索
     */
    @RequestMapping(value = "/search/{keyWords}", method = RequestMethod.POST)
    public JSONObject search(@PathVariable String keyWords) {

        return ReturnResult.returnDataMessage(1, "查询成功!", deviceService.search(keyWords));
    }



    /////////////////LOG//////////////////

    /**
     * 分页查询用户日志
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/getAllDailyLogByPage/{page}/{size}", method = RequestMethod.GET)
    public JSONObject getAllDailyLogByPage(@PathVariable int page, @PathVariable int size) {
System.out.println(ReturnResult.returnDataMessage(1, "获取成功!", dailyLogService.getAllDailyLogByPage(
        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime")))));
        return ReturnResult.returnDataMessage(1, "获取成功!", dailyLogService.getAllDailyLogByPage(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"))));

    }


    /**
     * 分页查询用户日志
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/getAllDeviceDataByPage/{page}/{size}", method = RequestMethod.GET)
    public JSONObject getAllDeviceDataByPage(@PathVariable int page, @PathVariable int size) {

        return ReturnResult.returnDataMessage(1, "获取成功!", deviceDataService.getAllDeviceDataByPage(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"))));

    }

}
