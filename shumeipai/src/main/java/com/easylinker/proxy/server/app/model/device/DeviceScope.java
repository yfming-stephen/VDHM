package com.easylinker.proxy.server.app.model.device;

import com.easylinker.proxy.server.app.model.base.BaseEntity;

import javax.persistence.*;
import java.util.Date;
/**
 * Created by Ruilin on 2018/7/31.
 */
// TODO: 2018/9/5 触发器 

/**
 * 设备数据范围设置
 */
@Entity
public class DeviceScope extends BaseEntity {
    @ManyToOne(targetEntity = Device.class, fetch = FetchType.LAZY)
    private Device device;

    //等写功能启动时使用
    //1 启用 0 不启用
    private Integer isLimitTime;
    @Temporal(value = TemporalType.TIME)
    private Date startTime;
    @Temporal(value = TemporalType.TIME)
    private Date endTime;

    //1 启用 0 不启用
    private Integer isLimitValue;
    private Double upperValue;
    private Double lowerValue;
    private Double upperValue1;
    private Double lowerValue1;

    //触发器名字
    private String name;
    //具体信息
    private String info;
    //触发payload
    private String payload;
    //是否启动 1 启用 0 不启用
    private Integer isStart;

    public Integer getIsStart() {
        return isStart;
    }

    public void setIsStart(Integer isStart) {
        this.isStart = isStart;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Integer getIsLimitTime() {
        return isLimitTime;
    }

    public void setIsLimitTime(Integer isLimitTime) {
        this.isLimitTime = isLimitTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getIsLimitValue() {
        return isLimitValue;
    }

    public void setIsLimitValue(Integer isLimitValue) {
        this.isLimitValue = isLimitValue;
    }

    public Double getUpperValue() {
        return upperValue;
    }

    public void setUpperValue(Double upperValue) {
        this.upperValue = upperValue;
    }

    public Double getUpperValue1() {
        return upperValue1;
    }

    public void setUpperValue1(Double upperValue) {
        this.upperValue1 = upperValue;
    }

    public Double getLowerValue() {
        return lowerValue;
    }

    public void setLowerValue(Double lowerValue) {
        this.lowerValue = lowerValue;
    }

    public Double getLowerValue1() {
        return lowerValue1;
    }

    public void setLowerValue1(Double lowerValue) {
        this.lowerValue1 = lowerValue;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
