package com.easylinker.proxy.server.app.model.device;

import com.easylinker.proxy.server.app.model.base.BaseEntity;
import com.easylinker.proxy.server.app.model.user.AppUser;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class DeviceJob extends BaseEntity {
    private String jobName = "DEVICE_SCHEDULE_JOB";    //任务名
    private String jobGroup = "JOB_GROUP";    //任务组
    private String cronExpression;    //cron表达式
    private String jobDescription = "DEVICE_JOB";    //描述
    private String jobJson;
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getJobJson() {
        return jobJson;
    }

    public void setJobJson(String jobJson) {
        this.jobJson = jobJson;
    }

    @OneToOne(targetEntity = Device.class, fetch = FetchType.LAZY)
    private Device device;

    @ManyToOne(targetEntity = AppUser.class, fetch = FetchType.LAZY)

    private AppUser appUser;

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }


    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
