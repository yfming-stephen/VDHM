package com.easylinker.proxy.server.app.config.mqttconfig.handler;

import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.config.mqttconfig.MqttMessageSender;
import com.easylinker.proxy.server.app.constants.mqtt.RealTimeType;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.DeviceData;
import com.easylinker.proxy.server.app.service.DeviceDataService;
import com.easylinker.proxy.server.app.service.DeviceService;
import com.easylinker.proxy.server.app.utils.HttpTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

/**
 * IN/CMD
 * 给客户端发送指定的指令，然后返回  在这里处理，暂时打印
 * send -> getInfo
 * reply-> name:rpi,date:2018......
 * 原理:  设备向IN方向echo进来数据,然后,转发到OUT,前端的JS mqtt订阅以后,就可以获取内容了
 */
@Component
public class ClientCmdReplyMessageHandler implements MessageHandler {
    Logger logger = LoggerFactory.getLogger(ClientCmdReplyMessageHandler.class);

    @Autowired
    DeviceService deviceService;
    @Autowired
    DeviceDataService deviceDataService;
    @Autowired
    MqttMessageSender mqttMessageSender;
    @Autowired
    HttpTool httpTool;
    @Value("${emq.api.host}")
    String apiHost;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = message.getHeaders().get("mqtt_topic").toString();
        //System.out.println("收到客户端回复命令:" + message.getPayload());


        try {
            //存入客户端回应消息
            if (topic.startsWith("CMD/IN/")) {
                Long openId = Long.parseLong(topic.split("/")[4]);
                JSONObject dataJson = JSONObject.parseObject(message.getPayload().toString());
                Device device = deviceService.findADevice(openId);
                if (device != null) {
                    //开始后传输数据
                    if (device.getAppUser() == null) {
                        logger.info("默认分组的设备，数据不记录!");
                    } else {
                        /**
                         * 这里AudioData是自定义数据格式
                         * 数据全部进 DeviceData
                         */
                        DeviceData deviceData = new DeviceData();
                        deviceData.setDevice(device);
                        deviceData.setData(dataJson.toString());
                        deviceData.setType("REPLY");
                        deviceDataService.save(deviceData);
                        logger.info("数据保存成功!");
                        JSONObject realTimeJson = new JSONObject();
                        realTimeJson.put("type", RealTimeType.DATA_RECEIVED);
                        realTimeJson.put("device", device.getId());
                        mqttMessageSender.sendRealTimePureMessage(realTimeJson);

                    }
                } else {
                    logger.info("设备不存在!");
                }
            }

            //消息转发
            JSONObject messageJson = new JSONObject();
            messageJson.put("topic", "OUT/REAL_TIME/" + message.getHeaders().get("mqtt_topic", String.class).split("/")[2]);
            messageJson.put("payload", message.getPayload().toString());
            messageJson.put("retain", false);
            messageJson.put("qos", 1);
            messageJson.put("client_id", "SERVER_PROXY");
            httpTool.postWithAuthorization(apiHost + "mqtt/publish", messageJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}