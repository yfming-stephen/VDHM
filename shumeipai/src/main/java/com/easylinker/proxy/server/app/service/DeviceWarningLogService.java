package com.easylinker.proxy.server.app.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.dao.DeviceWarningLogRepository;
import com.easylinker.proxy.server.app.model.daily.DeviceWarningLog;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.user.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by Ruilin on 2018/7/31.
 */
@Service("DeviceWarningLogService")
public class DeviceWarningLogService {
    @Autowired
    DeviceWarningLogRepository deviceWarningLogRepository;

    public void save(DeviceWarningLog deviceWarningLog) {
        deviceWarningLogRepository.save(deviceWarningLog);
    }

    /**
     * 根据device获取报警日志
     * @param device
     * @param pageable
     * @return
     */
    public JSONObject getAllLogByDeviceAndPage(Device device, Pageable pageable) {
        Page<DeviceWarningLog> dataPage = deviceWarningLogRepository.findAllByDevice(device, pageable);
        JSONArray pageArray = new JSONArray();
        for (DeviceWarningLog deviceWarningLog : dataPage.getContent()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("realValue", deviceWarningLog.getRealValue());
            jsonObject.put("info", deviceWarningLog.getInfo());
            jsonObject.put("date", deviceWarningLog.getCreateTime());
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
     * 根据appUser获取报警日志
     * @param appUser
     * @param pageable
     * @return
     */
    public JSONObject getAllLogByAppUserAndPage(AppUser appUser, Pageable pageable) {
        Page<DeviceWarningLog> dataPage = deviceWarningLogRepository.findAllByDevice_AppUser(appUser,pageable);
        JSONArray pageArray = new JSONArray();
        for (DeviceWarningLog deviceWarningLog : dataPage.getContent()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("realValue", deviceWarningLog.getRealValue());
            jsonObject.put("info", deviceWarningLog.getInfo());
            jsonObject.put("date", deviceWarningLog.getCreateTime());
            jsonObject.put("deviceId",deviceWarningLog.getDevice().getId());
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
