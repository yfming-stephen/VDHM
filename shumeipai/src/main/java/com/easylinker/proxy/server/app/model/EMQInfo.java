package com.easylinker.proxy.server.app.model;

import com.easylinker.proxy.server.app.model.base.BaseEntity;

import javax.persistence.Entity;

/**
 * EMQ的服务器信息存储点
 */

@Entity
public class EMQInfo extends BaseEntity {
    private String name;
    private String version;
    private String runTime;
    private String nodeName;
    private Boolean isConnected = false;

    public Boolean getConnected() {
        return isConnected;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}
