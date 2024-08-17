package com.easylinker.proxy.server.app.model.device;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by ruilin on 2018/11/16.
 */
public class RecordMap {
    @JSONField(ordinal = 1)
    private String month;
    @JSONField(ordinal = 2)
    private Double data;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month+"月";
    }

    public Double getData() {
        return data;
    }

    public void setData(Double data) {
        this.data = data;
    }
}
