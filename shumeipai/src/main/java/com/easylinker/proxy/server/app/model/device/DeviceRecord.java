package com.easylinker.proxy.server.app.model.device;

import com.easylinker.proxy.server.app.model.base.BaseEntity;

import javax.persistence.*;

/**
 * Created by Ruilin on 2018/7/30.
 */

/**
 * 设备温度/湿度记录
 */
@Entity
public class DeviceRecord extends BaseEntity {
    @Lob
    @Column(columnDefinition = "TEXT")
    private String data;
    @ManyToOne(targetEntity = Device.class, fetch = FetchType.LAZY)
    private Device device;
    private String type;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

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
}
