package com.easylinker.proxy.server.app.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.dao.DeviceDataRepository;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.DeviceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("DeviceDataService")
public class DeviceDataService {
    @Autowired
    DeviceDataRepository deviceDataRepository;

    public void save(DeviceData deviceData) {
        deviceDataRepository.save(deviceData);
    }

    public JSONArray getAllDeviceDataByDevice(Device device, Pageable pageable) {
        JSONArray data = new JSONArray();
        List<DeviceData> dataList = deviceDataRepository.findAllByDevice(device,pageable);
        for (DeviceData deviceData : dataList) {
            JSONObject dataJson = new JSONObject();
            dataJson.put("data", JSONObject.parse(deviceData.getData()));
            dataJson.put("create_time", deviceData.getCreateTime());
            dataJson.put("id", deviceData.getId());
            data.add(dataJson);
        }
        return data;
    }

    /**
     * 分页查询设备传输信息
     * @param pageable
     * @return
     */
    public JSONObject getAllDeviceDataByPage(Pageable pageable){
        Page<DeviceData> dataPage = deviceDataRepository.findAll(pageable);
        JSONArray pageArray = new JSONArray();
        for (DeviceData deviceData : dataPage.getContent()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("createTime", deviceData.getCreateTime().toString());
            jsonObject.put("data", deviceData.getData());
            jsonObject.put("deviceId", deviceData.getDevice().getId());
            if (deviceData.getType()!=null) {
                jsonObject.put("type", deviceData.getType());
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
