package com.easylinker.proxy.server.app.model.daily;

import com.easylinker.proxy.server.app.model.base.BaseEntity;

import javax.persistence.Entity;

/**
 * 访问请求日志
 */
@Entity
public class AccessLog extends BaseEntity {
    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
