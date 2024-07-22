package com.easylinker.proxy.server.app.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.dao.DeviceDriveRepository;
import com.easylinker.proxy.server.app.dao.DeviceGroupRepository;
import com.easylinker.proxy.server.app.dao.DrivingHealthRepository;
import com.easylinker.proxy.server.app.dao.DrivingRecordRepository;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.DeviceDrive;
import com.easylinker.proxy.server.app.model.device.DeviceGroup;
import com.easylinker.proxy.server.app.model.drive.DrivingHealth;
import com.easylinker.proxy.server.app.model.drive.DrivingRecord;
import com.easylinker.proxy.server.app.utils.MapDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * Created by ruilin on 2019/1/12.
 */
@Service
public class DriveService {
    @Autowired
    DrivingRecordRepository drivingRecordRepository;
    @Autowired
    DeviceDriveRepository deviceDriveRepository;
    @Autowired
    DeviceGroupRepository deviceGroupRepository;
    @Autowired
    DrivingHealthRepository drivingHealthRepository;

    public DeviceGroup getDeviceGroupByHash(String hash){
        DeviceDrive deviceDrive=deviceDriveRepository.findByDrivingRecordHash(hash);
        DeviceGroup deviceGroup=deviceGroupRepository.findTopById(deviceDrive.getDeviceGroup().getId());
        return deviceGroup;
    }


    public void saveRecord(DrivingRecord drivingRecord){
        drivingRecordRepository.save(drivingRecord);
    }
    public void saveHealth(DrivingHealth drivingHealth){
        drivingHealthRepository.save(drivingHealth);
    }

    /**
     * 获取 hash time
     * @param deviceGroup
     * @return
     */
    public JSONArray getAllDriveInfoByDeviceGroup(DeviceGroup deviceGroup){
        JSONArray data=new JSONArray();
        List<DeviceDrive> deviceDrives=deviceDriveRepository.findAllByDeviceGroup(deviceGroup);
        for (DeviceDrive deviceDrive :deviceDrives) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("hash",deviceDrive.getDrivingRecordHash());
            jsonObject.put("createTime",deviceDrive.getCreateTime());
            //还可以增加其他数据 待定
            data.add(jsonObject);
        }
        return data;
    }

    /**
     * 获取一次行程的各项数据信息TE
     * @param hash
     * @return
     */
    public JSONObject getAllDrivingRecordByHash(String hash){
        List<DrivingRecord> list=drivingRecordRepository.findAllByHash(hash);
        List<DrivingHealth> list1=drivingHealthRepository.findAllByHash(hash);

        JSONArray jsonArray=JSONArray.parseArray(JSON.toJSONString(list));
        JSONArray jsonArray1=JSONArray.parseArray(JSON.toJSONString(list1));

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("drivingRecord",jsonArray);
        jsonObject.put("drivingHealth",jsonArray1);
        return jsonObject;
    }


    /**
     * 根据获取行驶记录信息 需要时间
     * @param deviceGroup
     * @param startDate
     * @param endDate
     * @return
     */
    public JSONArray getAllInfoByDeviceGroupAndDate(DeviceGroup deviceGroup, Date startDate, Date endDate){
        JSONArray data=new JSONArray();
        List<DeviceDrive> deviceDrives=deviceDriveRepository.findAllByDeviceGroupAndAndCreateTimeBetween(deviceGroup,startDate,endDate);
        for (DeviceDrive deviceDrive :deviceDrives) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("hash",deviceDrive.getDrivingRecordHash());
            jsonObject.put("createTime",deviceDrive.getCreateTime());
            DrivingRecord record=drivingRecordRepository.findTopByHash(deviceDrive.getDrivingRecordHash());
            if (record==null){
                jsonObject.put("distance","0.0");
            }else {
                String lng = record.getGps().getLongitude();
                String lat = record.getGps().getLatitude();
                Double distance = 0.0;
                List<DrivingRecord> list = drivingRecordRepository.findAllByHash(deviceDrive.getDrivingRecordHash());
                for (DrivingRecord drivingRecord : list) {
                    distance += MapDistance.getDistanceDouble(lat, lng, drivingRecord.getGps().getLatitude(), drivingRecord.getGps().getLongitude());
                    lat = drivingRecord.getGps().getLatitude();
                    lng = drivingRecord.getGps().getLongitude();
                }
                String distanceStr = String.valueOf(distance);
                jsonObject.put("distance", distanceStr);
            }
            //还可以增加其他数据 待定
            data.add(jsonObject);
        }
        return data;
    }

    /**
     * 根据获取行驶记录信息 分页
     * @param deviceGroup
     * @param pageable
     * @return
     */
    public JSONObject getAllInfoByDeviceGroupAndPage(DeviceGroup deviceGroup, Pageable pageable){
        JSONArray data=new JSONArray();
        JSONObject pageJson = new JSONObject();
        Page<DeviceDrive> dataPage=deviceDriveRepository.findAllByDeviceGroup(deviceGroup,pageable);
        pageJson.put("page", dataPage.getNumber());
        pageJson.put("totalElements", dataPage.getTotalElements());
        pageJson.put("totalPages", dataPage.getTotalPages());
        pageJson.put("size", dataPage.getSize());
        pageJson.put("isLast", dataPage.isLast());
        pageJson.put("isFirst", dataPage.isFirst());
        for (DeviceDrive deviceDrive :dataPage.getContent()) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("hash",deviceDrive.getDrivingRecordHash());
            jsonObject.put("createTime",deviceDrive.getCreateTime());
            DrivingRecord record=drivingRecordRepository.findTopByHash(deviceDrive.getDrivingRecordHash());
            if (record==null){
                jsonObject.put("distance","0.0");
            }else {
                String lng = record.getGps().getLongitude();
                String lat = record.getGps().getLatitude();
                Double distance = 0.0;
                List<DrivingRecord> list = drivingRecordRepository.findAllByHash(deviceDrive.getDrivingRecordHash());
                for (DrivingRecord drivingRecord : list) {
                    distance += MapDistance.getDistanceDouble(lat, lng, drivingRecord.getGps().getLatitude(), drivingRecord.getGps().getLongitude());
                    lat = drivingRecord.getGps().getLatitude();
                    lng = drivingRecord.getGps().getLongitude();
                }
                String distanceStr = String.valueOf(distance);
                jsonObject.put("distance", distanceStr);
            }
            //还可以增加其他数据 待定
            data.add(jsonObject);
        }

        pageJson.put("data", data);
        return pageJson;
    }



    /**
     * 获取gps 根据hash
     * @param hash
     * @return
     */
    public JSONArray getGpsRecordByHash(String hash){
        List<DrivingRecord> drivingRecords=drivingRecordRepository.findAllByHash(hash);
        JSONArray data = new JSONArray();
        for (DrivingRecord drivingRecord:drivingRecords){
            if (drivingRecord!=null)
                data.add(drivingRecord.getGps());
        }
        return data;
    }


}
