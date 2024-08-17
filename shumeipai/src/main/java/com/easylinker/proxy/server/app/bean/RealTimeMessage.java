package com.easylinker.proxy.server.app.bean;

import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.constants.mqtt.RealTimeType;

import java.io.Serializable;

/**
 * 实时数据模型
 */
public class RealTimeMessage implements Serializable{
    private JSONObject message;
    private RealTimeType type;

    RealTimeMessage(){

    }


    public RealTimeMessage(JSONObject message, RealTimeType type) {
        this.message = message;
        this.type = type;
    }

    public JSONObject getMessage() {
        return message;
    }

    public void setMessage(JSONObject message) {
        this.message = message;
    }

    public RealTimeType getType() {
        return type;
    }

    public void setType(RealTimeType type) {
        this.type = type;
    }
}
