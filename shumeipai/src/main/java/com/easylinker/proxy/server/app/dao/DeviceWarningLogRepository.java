package com.easylinker.proxy.server.app.dao;

import com.easylinker.proxy.server.app.model.daily.DeviceWarningLog;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.user.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Created by Ruilin on 2018/7/31.
 */
public interface DeviceWarningLogRepository extends JpaRepository<DeviceWarningLog,Long> {
    Page<DeviceWarningLog> findAllByDevice(Device device,Pageable pageable);
    Page<DeviceWarningLog> findAllByDevice_AppUser(AppUser appUser, Pageable pageable);
}