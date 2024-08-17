package com.easylinker.proxy.server.app.model.daily;

import com.easylinker.proxy.server.app.model.base.BaseEntity;
import com.easylinker.proxy.server.app.model.device.Device;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * Created by Ruilin on 2018/7/31.
 */
@Entity
public class DeviceWarningLog extends BaseEntity{
    @ManyToOne(targetEntity = Device.class, fetch = FetchType.LAZY)
    private Device device;
    private String type;
    private Double realValue;
    private String info;
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getRealValue() {
        return realValue;
    }

    public void setRealValue(Double realValue) {
        this.realValue = realValue;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
