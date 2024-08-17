package com.easylinker.proxy.server.app.config.mqttconfig.handler;

import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.config.mqttconfig.MqttMessageSender;
import com.easylinker.proxy.server.app.constants.mqtt.RealTimeType;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.DeviceRecord;
import com.easylinker.proxy.server.app.service.DeviceRecordService;
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
 * Created by Ruilin on 2018/7/30.
 */
@Component
public class ClientHourRecordMessageHandler implements MessageHandler {
    Logger logger = LoggerFactory.getLogger(ClientHourRecordMessageHandler.class);

    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceRecordService deviceRecordService;
    @Autowired
    MqttMessageSender mqttMessageSender;
    @Autowired
    HttpTool httpTool;
    @Value("${emq.api.host}")
    String apiHost;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String topic = message.getHeaders().get("mqtt_topic").toString();
        try {
            if (topic.startsWith("IN/DEVICE/")&&topic.endsWith("HOUR")) {
                Long openId = Long.parseLong(topic.split("/")[4]);
                String type = topic.split("/")[5];
                String record=message.getPayload().toString();
                Device device = deviceService.findADevice(openId);
                if (device != null) {
                    //开始后传输数据
                    if (device.getAppUser() == null) {
                        logger.info("默认分组的设备，数据不记录!");
                    } else {
                        DeviceRecord deviceRecord=new DeviceRecord();

                        deviceRecord.setDevice(device);
                        deviceRecord.setData(record);
                        deviceRecord.setType(type);
                        deviceRecordService.save(deviceRecord);

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
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("数据格式出错!");

        }

    }

}
