package com.easylinker.proxy.server.app.dao;

import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.DeviceGroup;
import com.easylinker.proxy.server.app.model.user.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Device findTopById(Long id);
    List<Device> findAllByAppUser(AppUser appUser);

    Page<Device> findAllByAppUser(AppUser appUser, Pageable pageable);

    Page<Device> findAllByAppUserAndDeviceGroup(AppUser appUser, DeviceGroup deviceGroup, Pageable pageable);

    List<Device> findAllByDeviceGroupAndIsOnline(DeviceGroup deviceGroup, Boolean online);

    List<Device> findAllByAppUserAndIsOnline(AppUser appUser, Boolean online);

    Page<Device> findAllByAppUserAndIsOnline(AppUser appUser, Boolean online,Pageable pageable);

    List<Device> findAllByIsOnline(Boolean online);

    @Query(value = "select id from device where device_group_id=:deviceGroupId",nativeQuery=true)
    List<BigInteger> findAllByDeviceGroup_Id(@Param(value = "deviceGroupId")Long deviceGroupId);

    @Query("select id from Device ")
    List<Long> findAllId();

    /**
     * 条件查询
     *
     * @param keyWords
     * @return
     */

    @Query("select device from Device device where  device.deviceDescribe like %:keyWords%  or device.deviceName  like %:keyWords% ")
    List<Device> searchDevice(@Param(value = "keyWords") String keyWords);


    @Query("select device from Device device where ( device.deviceDescribe like %:keyWords%  or device.deviceName  like %:keyWords% ) and device.appUser = :appUser")
    List<Device> searchDeviceByAppUser(@Param(value = "keyWords") String keyWords,@Param(value = "appUser")AppUser appUser);


}
