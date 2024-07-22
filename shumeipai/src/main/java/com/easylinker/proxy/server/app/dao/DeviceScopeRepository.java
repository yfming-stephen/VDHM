package com.easylinker.proxy.server.app.dao;

import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.DeviceScope;
import com.easylinker.proxy.server.app.model.user.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Ruilin on 2018/7/31.
 */
public interface DeviceScopeRepository extends JpaRepository<DeviceScope,Long> {
    List<DeviceScope> findByDevice(Device device);
    Page<DeviceScope> findAllByDevice_AppUser(AppUser appUser, Pageable pageable);
    DeviceScope findTopById(Long id);
}
