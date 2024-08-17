package com.easylinker.proxy.server.app.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.dao.DeviceRecordRepository;
import com.easylinker.proxy.server.app.dao.DeviceRepository;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.DeviceRecord;
import com.easylinker.proxy.server.app.model.user.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Ruilin on 2018/7/30.
 */
@Service("DeviceRecordService")
public class DeviceRecordService {
    @Autowired
    DeviceRecordRepository deviceRecordRepository;
    @Autowired
    DeviceRepository deviceRepository;

    public void save(DeviceRecord deviceRecord) {
        deviceRecordRepository.save(deviceRecord);
    }

    public JSONArray getDeviceRecordByToday(Device device) {
        Page<DeviceRecord> dataPage = deviceRecordRepository.findAllByDevice(device, PageRequest.of(0, 24, Sort.by(Sort.Direction.ASC, "createTime")));
        JSONArray pageArray = new JSONArray();
        for (DeviceRecord deviceRecord : dataPage.getContent()) {
            JSONObject jsonObject = new JSONObject();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            String date = sdf.format(deviceRecord.getCreateTime());
            jsonObject.put("date", date);
            jsonObject.put("data", Double.parseDouble(deviceRecord.getData()));
            jsonObject.put("type", deviceRecord.getType());
            pageArray.add(jsonObject);
        }
        return pageArray;
    }

    public JSONArray getAllDeviceRecordByUser(AppUser appUser) {
        List<Device> deviceList = deviceRepository.findAllByAppUser(appUser);
        JSONArray data = new JSONArray();
        for (Device device : deviceList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data",getDeviceRecordByToday(device));
            jsonObject.put("deviceId",device.getId());
            data.add(jsonObject);
        }
        return data;
    }

}
