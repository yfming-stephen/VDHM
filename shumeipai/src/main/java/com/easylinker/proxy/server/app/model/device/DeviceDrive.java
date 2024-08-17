package com.easylinker.proxy.server.app.model.device;

import com.easylinker.proxy.server.app.model.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * Created by ruilin on 2019/1/12.
 */
@Entity
public class DeviceDrive extends BaseEntity{
    @ManyToOne(targetEntity = DeviceGroup.class, fetch = FetchType.LAZY)
    private DeviceGroup deviceGroup;
    private String drivingRecordHash;

    public DeviceGroup getDeviceGroup() {
        return deviceGroup;
    }

    public void setDeviceGroup(DeviceGroup deviceGroup) {
        this.deviceGroup = deviceGroup;
    }

    public String getDrivingRecordHash() {
        return drivingRecordHash;
    }

    public void setDrivingRecordHash(String drivingRecordHash) {
        this.drivingRecordHash = drivingRecordHash;
    }
    public DeviceDrive(){

    }
    public DeviceDrive(DeviceGroup deviceGroup, String drivingRecordHash) {
        this.deviceGroup = deviceGroup;
        this.drivingRecordHash = drivingRecordHash;
    }

    @Override
    public String toString() {
        return "DeviceDrive{" +
                "deviceGroup=" + deviceGroup +
                ", drivingRecordHash='" + drivingRecordHash + '\'' +
                '}';
    }
}
