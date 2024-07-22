package com.easylinker.proxy.server.app.dao;

import com.easylinker.proxy.server.app.model.device.DeviceDrive;
import com.easylinker.proxy.server.app.model.device.DeviceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by ruilin on 2019/1/12.
 */
public interface DeviceDriveRepository extends JpaRepository<DeviceDrive, Long> {
    DeviceDrive findByDrivingRecordHash(String hash);
    List<DeviceDrive> findAllByDeviceGroup(DeviceGroup deviceGroup);
    List<DeviceDrive> findAll();
    Page<DeviceDrive> findAllByDeviceGroup(DeviceGroup deviceGroup,Pageable pageable);
    List<DeviceDrive> findAllByDeviceGroupAndAndCreateTimeBetween(DeviceGroup deviceGroup, Date startDate,Date endDate);
}
