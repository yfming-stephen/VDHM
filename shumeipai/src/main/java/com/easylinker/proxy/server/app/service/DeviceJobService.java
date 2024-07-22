package com.easylinker.proxy.server.app.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.dao.DeviceJobRepository;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.DeviceJob;
import com.easylinker.proxy.server.app.model.user.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DeviceJobService {
    @Autowired
    DeviceJobRepository deviceJobRepository;

    public void save(DeviceJob scheduleJob) {
        deviceJobRepository.save(scheduleJob);
    }

    /**
     * 根据设备的ID查询任务
     *
     * @param device
     * @return
     */

    public DeviceJob findAJobByDevice(Device device) {
        return deviceJobRepository.findTopByDevice(device);

    }

    /**
     * 删除设备绑定的JOB
     *
     * @param deviceJob
     */

    public void delete(DeviceJob deviceJob) {
        deviceJobRepository.delete(deviceJob);

    }

    /**
     * 根据用户查找
     *
     * @param appUser
     * @param pageable
     * @return
     */

    public JSONObject getAllJobByAppUser(AppUser appUser, Pageable pageable) {
        Page<DeviceJob> dataPage = deviceJobRepository.findAllByAppUser(appUser, pageable);

        JSONArray data = new JSONArray();
        JSONObject pageJson = new JSONObject();
        pageJson.put("page", dataPage.getNumber());
        pageJson.put("total", dataPage.getTotalPages());
        pageJson.put("size", dataPage.getSize());
        pageJson.put("isLast", dataPage.isLast());
        pageJson.put("isFirst", dataPage.isFirst());
        pageJson.put("totalPages", dataPage.getTotalPages());
        pageJson.put("totalElements", dataPage.getTotalElements());
        for (DeviceJob deviceJob : dataPage.getContent()) {
            JSONObject deviceJson = new JSONObject();
            deviceJson.put("id", deviceJob.getId());
            deviceJson.put("cron", deviceJob.getCronExpression());
            deviceJson.put("device", deviceJob.getDevice().getId());
            deviceJson.put("jobJson", deviceJob.getJobJson());
            deviceJson.put("state",deviceJob.getState());
            data.add(deviceJson);
        }

        pageJson.put("data", data);
        return pageJson;
    }
}
