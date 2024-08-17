package com.easylinker.proxy.server.app.model.daily;

import com.easylinker.proxy.server.app.model.base.BaseEntity;
import com.easylinker.proxy.server.app.model.device.Device;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class DeviceOnAndOffLineLog extends BaseEntity {
    private String event;
    private Date date;
    @ManyToOne(targetEntity = Device.class, fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Device device;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
