package com.easylinker.proxy.server.app.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.dao.DeviceRepository;
import com.easylinker.proxy.server.app.dao.LocationRepository;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.Location;
import com.easylinker.proxy.server.app.model.user.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("LocationService")
public class LocationService {
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    DeviceService deviceService;
    /**
     * 获取一个设备的位置
     *
     * @param device
     * @return
     */

    public Location getALocationByDevice(Device device) {
        return locationRepository.findTopByDevice(device);

    }

    /**
     * 保存位置
     *
     * @param location
     */

    public void save(Location location) {
        locationRepository.save(location);
    }

    /**
     * 根据用户获取全部设备位置
     * @param appUser
     * @return
     */
    public JSONArray getAllDevicesLocation(AppUser appUser){
        JSONArray data = new JSONArray();
        List<Device> devices=deviceRepository.findAllByAppUser(appUser);
        for (Device device:devices){
            JSONObject deviceJson = new JSONObject();
            deviceJson.put("name",device.getDeviceName());
            Location locationData= getALocationByDevice(device);
            JSONArray location = new JSONArray();
            if (locationData!=null) {
                location.add(Double.parseDouble(locationData.getLongitude()));
                location.add(Double.parseDouble(locationData.getLatitude()));
            }
            deviceJson.put("value",location);
            data.add(deviceJson);
        }
        return data;
    }

    public JSONArray getADeviceLocation(long deviceId){
        JSONArray data = new JSONArray();
        Device device=deviceService.findADevice(deviceId);
            JSONObject deviceJson = new JSONObject();
            deviceJson.put("name",device.getDeviceName());
            Location locationData= getALocationByDevice(device);
            JSONArray location = new JSONArray();
            if (locationData!=null) {
                location.add(Double.parseDouble(locationData.getLongitude()));
                location.add(Double.parseDouble(locationData.getLatitude()));
            }
            deviceJson.put("value",location);
            data.add(deviceJson);

        return data;
    }




}
