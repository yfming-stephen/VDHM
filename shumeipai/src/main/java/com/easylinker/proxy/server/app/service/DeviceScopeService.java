package com.easylinker.proxy.server.app.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.dao.DeviceScopeRepository;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.DeviceScope;
import com.easylinker.proxy.server.app.model.user.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Ruilin on 2018/7/31.
 */
@Service("DeviceScopeService")
public class DeviceScopeService {
    @Autowired
    DeviceScopeRepository deviceScopeRepository;

    public void save(DeviceScope deviceScope) {
        deviceScopeRepository.save(deviceScope);
    }

    public JSONObject findByDevice(Device device){
        JSONArray data = new JSONArray();
        List<DeviceScope> deviceScope=deviceScopeRepository.findByDevice(device);
        JSONObject json=new JSONObject();

        for (DeviceScope trigger:deviceScope) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("deviceId",device.getId());
            jsonObject.put("deviceScopeId",trigger.getId());
            jsonObject.put("isLimitValue",trigger.getIsLimitValue());
            jsonObject.put("upperValue",trigger.getUpperValue());
            jsonObject.put("lowerValue",trigger.getLowerValue());
            jsonObject.put("upperValue1",trigger.getUpperValue1());
            jsonObject.put("lowerValue1",trigger.getLowerValue1());
            jsonObject.put("name",trigger.getName());
            jsonObject.put("info",trigger.getInfo());
            jsonObject.put("payload",trigger.getPayload());
            jsonObject.put("isStart",trigger.getIsStart());
            data.add(jsonObject);
        }
        json.put("data",data);

        return json;
    }

    public  DeviceScope findById(Long deviceScopeId){
        return deviceScopeRepository.findTopById(deviceScopeId);
    }


    public List<DeviceScope> findAllByDevice(Device device){
        return deviceScopeRepository.findByDevice(device);
    }

    public JSONObject getAllDeviceScopeByUserAndPage(AppUser appUser, Pageable pageable) {
        Page<DeviceScope> dataPage = deviceScopeRepository.findAllByDevice_AppUser(appUser,pageable);
        JSONArray pageArray = new JSONArray();
        for (DeviceScope trigger : dataPage.getContent()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceScopeId",trigger.getId());
            jsonObject.put("deviceId",trigger.getDevice().getId());
            jsonObject.put("isLimitValue",trigger.getIsLimitValue());
            jsonObject.put("upperValue",trigger.getUpperValue());
            jsonObject.put("lowerValue",trigger.getLowerValue());
            jsonObject.put("upperValue1",trigger.getUpperValue1());
            jsonObject.put("lowerValue1",trigger.getLowerValue1());
            jsonObject.put("name",trigger.getName());
            jsonObject.put("info",trigger.getInfo());
            jsonObject.put("payload",trigger.getPayload());

            if (trigger.getIsStart()==null||trigger.getIsStart()==0) {
                jsonObject.put("isStart",false);
            } else {
                jsonObject.put("isStart",true);
            }
            pageArray.add(jsonObject);
        }

        JSONObject pageJson = new JSONObject();
        pageJson.put("page", dataPage.getNumber());
        pageJson.put("total", dataPage.getTotalPages());
        pageJson.put("size", dataPage.getSize());
        pageJson.put("isLast", dataPage.isLast());
        pageJson.put("totalPages", dataPage.getTotalPages());
        pageJson.put("isFirst", dataPage.isFirst());
        pageJson.put("totalElements", dataPage.getTotalElements());
        pageJson.put("data", pageArray);
        return pageJson;
    }

    public void updateTriggerStatus(Long deviceScopeId){
        DeviceScope deviceScope=deviceScopeRepository.findTopById(deviceScopeId);
        if (deviceScope.getIsStart()==1){
            deviceScope.setIsStart(0);
        }else{
            deviceScope.setIsStart(1);
        }
        deviceScopeRepository.save(deviceScope);
    }

    public void delTrigger(DeviceScope deviceScope){
        deviceScopeRepository.delete(deviceScope);
    }


}
