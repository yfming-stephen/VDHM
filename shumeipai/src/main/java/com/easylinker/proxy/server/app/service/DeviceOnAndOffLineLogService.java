package com.easylinker.proxy.server.app.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.dao.DeviceOnAndOffLineLogRepository;
import com.easylinker.proxy.server.app.model.daily.DeviceOnAndOffLineLog;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.user.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DeviceOnAndOffLineLogService {
    @Autowired
    DeviceOnAndOffLineLogRepository deviceOnAndOffLineLogRepository;

    public void save(DeviceOnAndOffLineLog deviceOnAndOffLineLog) {
        deviceOnAndOffLineLogRepository.save(deviceOnAndOffLineLog);

    }

    /**
     * 获取日志
     *
     * @param pageable
     * @return
     */
    public JSONObject getAllLogByPage(Device device, Pageable pageable) {
        Page<DeviceOnAndOffLineLog> dataPage = deviceOnAndOffLineLogRepository.findAllByDevice(device, pageable);
        JSONArray pageArray = new JSONArray();
        for (DeviceOnAndOffLineLog deviceOnAndOffLineLog : dataPage.getContent()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", deviceOnAndOffLineLog.getDate().toString());
            jsonObject.put("deviceId", deviceOnAndOffLineLog.getDevice().getId());
            jsonObject.put("event", deviceOnAndOffLineLog.getEvent());

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

    /**
     * 获取所有的日志
     */
    public JSONObject getAllLog(Pageable pageable) {
        Page<DeviceOnAndOffLineLog> dataPage = deviceOnAndOffLineLogRepository.findAll(pageable);
        JSONArray pageArray = new JSONArray();
        for (DeviceOnAndOffLineLog deviceOnAndOffLineLog : dataPage.getContent()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", deviceOnAndOffLineLog.getDate().toString());
            jsonObject.put("event", deviceOnAndOffLineLog.getEvent());
            if (deviceOnAndOffLineLog.getDevice()!=null){
            jsonObject.put("deviceId", deviceOnAndOffLineLog.getDevice().getId());
            }else{
                jsonObject.put("deviceId", "(HAS BEEN DELETED)");
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

    public JSONObject getAllLogByUser(AppUser appUser, Pageable pageable) {
        Page<DeviceOnAndOffLineLog> dataPage = deviceOnAndOffLineLogRepository.findAllByDevice_AppUser(appUser,pageable);
        JSONArray pageArray = new JSONArray();
        for (DeviceOnAndOffLineLog deviceOnAndOffLineLog : dataPage.getContent()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", deviceOnAndOffLineLog.getDate());
            jsonObject.put("event", deviceOnAndOffLineLog.getEvent());
            if (deviceOnAndOffLineLog.getDevice()!=null){
                jsonObject.put("deviceId", deviceOnAndOffLineLog.getDevice().getId());
            }else{
                jsonObject.put("deviceId", "(HAS BEEN DELETED)");
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
}
