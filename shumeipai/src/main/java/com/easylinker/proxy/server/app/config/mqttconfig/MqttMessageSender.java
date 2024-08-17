package com.easylinker.proxy.server.app.config.mqttconfig;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.DeviceScope;
import com.easylinker.proxy.server.app.service.DeviceScopeService;
import com.easylinker.proxy.server.app.utils.HttpTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息辅助发送
 */
@Component
public class MqttMessageSender {
    @Autowired
    HttpTool httpTool;
    @Value("${emq.api.host}")
    String apiHost;
    @Autowired
    DeviceScopeService deviceScopeService;
    /**
     * 发送实时消息
     *
     * @param device
     * @param message
     * @throws Exception
     */

    public void sendRealTimeDeviceMessage(Device device, String message) throws Exception {
        JSONObject messageJson = new JSONObject();
        messageJson.put("topic", "OUT/REAL_TIME");
        messageJson.put("payload", message);//这里注意：payload必须是String类型的
        messageJson.put("qos", 1);
        messageJson.put("retain", false);
        messageJson.put("client_id", "SERVER_PROXY");
        httpTool.postWithAuthorization(apiHost + "mqtt/publish", messageJson);
    }

    /**
     *
     * @param message
     * @throws Exception
     */
    public void sendRealTimePureMessage(JSONObject message) throws Exception {
        JSONObject messageJson = new JSONObject();
        messageJson.put("topic", "OUT/REAL_TIME");
        messageJson.put("payload", message.toJSONString());
        messageJson.put("retain", false);
        messageJson.put("qos", 1);
        messageJson.put("client_id", "SERVER_PROXY");
        httpTool.postWithAuthorization(apiHost + "mqtt/publish", messageJson);
    }


    public void sendDriveHashMessage(String userId,String groupId,String hash) throws Exception {
        JSONObject messageJson = new JSONObject();
        messageJson.put("topic", "OUT/DEVICE/"+userId+"/"+groupId+"/DRIVE/HASH");
        messageJson.put("payload", hash);
        messageJson.put("retain", false);
        messageJson.put("qos", 1);
        messageJson.put("client_id", "SERVER_PROXY");
        httpTool.postWithAuthorization(apiHost + "mqtt/publish", messageJson);
    }


    /**
     * 发送设备配置范围信息 //Trigger
     * @param topic
     * @param device
     * @throws Exception
     */
    public void sendDeviceScopeMessage(String topic,Device device) throws Exception {
        JSONObject messageJson = new JSONObject();
        JSONObject payload = new JSONObject();
        JSONArray data = new JSONArray();
        List<DeviceScope> deviceScopes= deviceScopeService.findAllByDevice(device);
        for (DeviceScope deviceScope:deviceScopes) {
            JSONObject trigger = new JSONObject();
            JSONObject scope = new JSONObject();
            scope.put("isLimitValue",deviceScope.getIsLimitValue());
            scope.put("upperValue",deviceScope.getUpperValue());
            scope.put("lowerValue",deviceScope.getLowerValue());
            scope.put("upperValue1",deviceScope.getUpperValue1());
            scope.put("lowerValue1",deviceScope.getLowerValue1());
            trigger.put("deviceScope",scope);
            trigger.put("payload",deviceScope.getPayload());
            trigger.put("id",deviceScope.getId());
            trigger.put("isStart",deviceScope.getIsStart());
            trigger.put("name",deviceScope.getName());
            data.add(trigger);
        }
        payload.put("triggers",data);
        messageJson.put("topic", topic.replace("IN", "OUT"));
        messageJson.put("payload", payload.toJSONString());
        messageJson.put("retain", false);
        messageJson.put("qos", 1);
        messageJson.put("client_id", "SERVER_PROXY");
        httpTool.postWithAuthorization(apiHost + "mqtt/publish", messageJson);
    }

}
