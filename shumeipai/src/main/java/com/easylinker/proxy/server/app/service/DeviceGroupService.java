package com.easylinker.proxy.server.app.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.easylinker.proxy.server.app.dao.DeviceGroupRepository;
import com.easylinker.proxy.server.app.dao.DeviceRecordRepository;
import com.easylinker.proxy.server.app.dao.DeviceRepository;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.DeviceGroup;
import com.easylinker.proxy.server.app.model.device.RecordMap;
import com.easylinker.proxy.server.app.model.user.AppUser;
import io.swagger.models.auth.In;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 设备组service
 */
@Service("DeviceGroupService")
public class DeviceGroupService {
    @Autowired
    DeviceGroupRepository deviceGroupRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    DeviceRecordRepository deviceRecordRepository;

    @Autowired
    AppUserService appUserService;


    public void save(DeviceGroup deviceGroup) {
        deviceGroupRepository.save(deviceGroup);
    }

    public JSONObject getADeviceGroupByName(String name) {
        DeviceGroup deviceGroup = deviceGroupRepository.findTopByGroupName(name);
        if (deviceGroup != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", deviceGroup.getGroupName());
            jsonObject.put("comment", deviceGroup.getComment());
            return jsonObject;
        } else {
            return null;
        }

    }


    public JSONArray getAllByAppUser(AppUser appUser) {
        JSONArray data = new JSONArray();
        List<DeviceGroup> deviceGroupList = deviceGroupRepository.findAllByAppUser(appUser);
        for (DeviceGroup group : deviceGroupList) {
            JSONObject dataJson = new JSONObject();
            dataJson.put("id", group.getId());
            dataJson.put("name", group.getGroupName());
            dataJson.put("comment", group.getComment());
            dataJson.put("user", group.getAppUser().getId());
            data.add(dataJson);
        }
        return data;
    }


    public void delete(DeviceGroup deviceGroup) {
        deviceGroupRepository.delete(deviceGroup);
    }


    public DeviceGroup findADeviceGroupById(Long id) {
        return deviceGroupRepository.findTopById(id);
    }


    public DeviceGroup findADeviceGroupByName(String groupName) {
        return deviceGroupRepository.findTopByGroupName(groupName);
    }

    /**
     * 分页获取分组
     *
     * @param pageable
     * @return
     */
    public JSONObject getAllDeviceGroupByPage(AppUser appUser, Pageable pageable) {
        JSONObject pageJson = new JSONObject();
        JSONArray data = new JSONArray();
        Page<DeviceGroup> dataPage = deviceGroupRepository.findAllByAppUser(appUser, pageable);
        for (DeviceGroup group : dataPage.getContent()) {
            JSONObject groupJson = new JSONObject();
            groupJson.put("name", group.getGroupName());
            groupJson.put("user", group.getAppUser().getId());
            groupJson.put("comment", group.getComment());
            groupJson.put("id", group.getId());
            groupJson.put("create_time", group.getCreateTime().toString());

            data.add(groupJson);
        }
        pageJson.put("page", dataPage.getNumber());
        pageJson.put("totalElements", dataPage.getTotalElements());
        pageJson.put("totalPages", dataPage.getTotalPages());
        pageJson.put("size", dataPage.getSize());
        pageJson.put("isLast", dataPage.isLast());
        pageJson.put("data", data);
        pageJson.put("isFirst", dataPage.isFirst());
        return pageJson;

    }

    /**
     * 分页获取所有分组
     *
     * @param pageable
     * @return
     */

    public JSONArray getAllDeviceGroupByPage(Pageable pageable) {
        JSONArray data = new JSONArray();
        Page<DeviceGroup> page = deviceGroupRepository.findAll(pageable);
        for (DeviceGroup group : page.getContent()) {
            JSONObject dataJson = new JSONObject();
            dataJson.put("id", group.getId());
            dataJson.put("user", group.getAppUser().getUsername());
            dataJson.put("name", group.getGroupName());
            dataJson.put("comment", group.getComment());
            data.add(dataJson);
        }
        return data;
    }

    /**
     * 分页获取设备组12月内平均数据
     * @param pageable
     * @return
     */
    public JSONArray getAllDeviceGroupeRecordDataByPage(Pageable pageable){
        JSONArray data= new JSONArray();
        Page<DeviceGroup> page = deviceGroupRepository.findAll(pageable);
        for (DeviceGroup group : page.getContent()) {
            JSONObject dataJson = new JSONObject();
            JSONArray deviceRecordDataArray=new JSONArray();
            AppUser appUser=appUserService.getAUserById(group.getAppUser().getId());
            List<BigInteger> deviceIdList=deviceRepository.findAllByDeviceGroup_Id(group.getId());

            for (BigInteger id:deviceIdList){
                JSONObject deviceRecordData=new JSONObject();
                Device device=deviceRepository.findTopById(Long.parseLong(id.toString()));
                JSONArray monthData=JSONArray.parseArray(JSON.toJSONString(deviceRecordRepository.getMonthDataByDevice(Long.parseLong(id.toString()))));
                deviceRecordData.put("monthData",monthData);
                deviceRecordData.put("deviceId",id.toString());
                deviceRecordData.put("deviceType",device.getDeviceType());
                deviceRecordDataArray.add(deviceRecordData);
            }
            dataJson.put("deviceRecordData",deviceRecordDataArray);
            dataJson.put("id", group.getId());
            dataJson.put("userName", appUser.getUsername());
            dataJson.put("userPhone",appUser.getPhone());
            dataJson.put("name", group.getGroupName());
            dataJson.put("comment", group.getComment());
            data.add(dataJson);
        }
        return data;
    }


    /**
     * 根据群组ID获取设备组12月内平均数据
     * @param groupId
     * @return
     */
    public JSONObject getDeviceGroupeRecordDataByGroupId(Long groupId){
        JSONArray deviceRecordDataArray=new JSONArray();
        JSONObject data=new JSONObject();
        JSONArray columns=new JSONArray();
        columns.add("month");
        columns.add("data");

        DeviceGroup group=deviceGroupRepository.findTopById(groupId);
        data.put("id", group.getId());
        data.put("name", group.getGroupName());
        data.put("comment", group.getComment());

        List<BigInteger> deviceIdList=deviceRepository.findAllByDeviceGroup_Id(groupId);

        for (BigInteger id:deviceIdList){
            JSONObject deviceRecordData=new JSONObject();
            Device device=deviceRepository.findTopById(Long.parseLong(id.toString()));
            List<Map<String,Double>> list=deviceRecordRepository.getMonthDataByDevice(Long.parseLong(id.toString()));
            JSONArray monthData=new JSONArray();
            for (Map map:list) {
                RecordMap recordMap=new RecordMap();
                recordMap.setMonth(String.valueOf( map.get("month")));
                recordMap.setData((Double) map.get("data"));
                monthData.add(recordMap);
            }
            deviceRecordData.put("rows",monthData);//为前端展示方便
            deviceRecordData.put("columns",columns);
            deviceRecordData.put("deviceName",device.getDeviceName());
            deviceRecordData.put("deviceType",device.getDeviceType());
            deviceRecordDataArray.add(deviceRecordData);
        }

        data.put("data",deviceRecordDataArray);
        return data;
    }


    /**
     * 通过组id获取设备id
     * @param groupId
     * @return
     */
    public List<Long> getAllDeviceIdByGroupId(Long groupId){
        List<BigInteger> deviceIdList=deviceRepository.findAllByDeviceGroup_Id(groupId);
        List<Long> deviceIds=new ArrayList<>();
        for (BigInteger id:
             deviceIdList) {
            deviceIds.add(Long.parseLong(id.toString()));

        }
        return deviceIds;
    }

    /**
     * 获取所有组
     * @return
     */
    public List<Long> getAllDeviceGroupId(){
        return deviceGroupRepository.findAllId();
    }



}
