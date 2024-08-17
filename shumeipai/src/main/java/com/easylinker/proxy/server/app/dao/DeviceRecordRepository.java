package com.easylinker.proxy.server.app.dao;

import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.DeviceRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Ruilin on 2018/7/30.
 */

public interface DeviceRecordRepository extends JpaRepository<DeviceRecord, Long> {

    Page<DeviceRecord> findAllByDevice(Device device,Pageable pageable);
    @Override
    Page<DeviceRecord> findAll(Pageable pageable);

    @Query(value="select month(create_time) as month, avg(data) as data from device_record where device_id=:deviceId group by month(create_time) limit 0,12",nativeQuery=true)
    List<Map<String,Double>> getMonthDataByDevice(@Param(value = "deviceId") Long deviceId);
}
