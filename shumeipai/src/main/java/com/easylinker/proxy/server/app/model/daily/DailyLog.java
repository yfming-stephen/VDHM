package com.easylinker.proxy.server.app.model.daily;

import com.alibaba.fastjson.annotation.JSONField;
import com.easylinker.proxy.server.app.constants.DailyLogType;
import com.easylinker.proxy.server.app.model.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

/**
 * 日常日志
 */
@Entity
public class DailyLog extends BaseEntity {
    private Long whosId;
    private String event;
    private String who;
    private String operate;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date date;

    @Enumerated(value = EnumType.STRING)
    private DailyLogType dailyLogType;

    public DailyLogType getDailyLogType() {
        return dailyLogType;
    }

    public void setDailyLogType(DailyLogType dailyLogType) {
        this.dailyLogType = dailyLogType;
    }


    public Long getWhosId() {
        return whosId;
    }

    public void setWhosId(Long whosId) {
        this.whosId = whosId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
}
