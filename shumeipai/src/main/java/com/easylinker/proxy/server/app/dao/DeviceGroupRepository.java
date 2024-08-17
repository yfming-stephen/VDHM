package com.easylinker.proxy.server.app.dao;

import com.easylinker.proxy.server.app.model.device.DeviceGroup;
import com.easylinker.proxy.server.app.model.user.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceGroupRepository extends JpaRepository<DeviceGroup, Long> {
    List<DeviceGroup> findAllByAppUser(AppUser appUser);

    DeviceGroup findTopByGroupName(String name);

    DeviceGroup findTopById(Long id);
    @Override
    Page<DeviceGroup> findAll(Pageable pageable);

    Page<DeviceGroup> findAllByAppUser(AppUser appUser, Pageable pageable);

    @Query("select id from DeviceGroup ")
    List<Long> findAllId();

}
