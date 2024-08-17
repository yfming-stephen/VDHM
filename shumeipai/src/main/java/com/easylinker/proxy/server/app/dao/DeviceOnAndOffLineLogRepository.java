package com.easylinker.proxy.server.app.dao;

import com.easylinker.proxy.server.app.model.daily.DeviceOnAndOffLineLog;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.user.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceOnAndOffLineLogRepository extends JpaRepository<DeviceOnAndOffLineLog, Long> {
    Page<DeviceOnAndOffLineLog> findAllByDevice(Device device, Pageable pageable);
    Page<DeviceOnAndOffLineLog> findAllByDevice_AppUser(AppUser appUser, Pageable pageable);
    Page<DeviceOnAndOffLineLog> findAll(Pageable pageable);

}
