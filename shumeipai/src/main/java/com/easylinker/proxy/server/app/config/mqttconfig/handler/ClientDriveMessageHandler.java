package com.easylinker.proxy.server.app.config.mqttconfig.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.easylinker.proxy.server.app.config.mqttconfig.MqttMessageSender;
import com.easylinker.proxy.server.app.model.device.DeviceGroup;
import com.easylinker.proxy.server.app.model.drive.DrivingHealth;
import com.easylinker.proxy.server.app.model.drive.DrivingRecord;
import com.easylinker.proxy.server.app.service.DeviceGroupService;
import com.easylinker.proxy.server.app.service.DriveService;
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
 * Created by ruilin on 2019/1/12.
 */
@Component
public class ClientDriveMessageHandler implements MessageHandler {
    Logger logger = LoggerFactory.getLogger(ClientDriveMessageHandler.class);

    @Autowired
    DeviceGroupService deviceGroupService;

    @Autowired
    DriveService driveService;
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
            if (topic.startsWith("IN/DEVICE/")&&topic.endsWith("/DRIVE/TE/MIN")) {
                Long id = Long.parseLong(topic.split("/")[3]);
                String record=message.getPayload().toString();
                DeviceGroup deviceGroup=deviceGroupService.findADeviceGroupById(id);
                if (deviceGroup != null) {
                    //开始后传输数据
                    if (deviceGroup.getAppUser() == null) {
                        logger.info("默认分组的设备，数据不记录!");
                    } else {
                        DrivingRecord drivingRecord=JSONObject.parseObject(record,new TypeReference<DrivingRecord>(){});
                        driveService.saveRecord(drivingRecord);
                    }
                } else {
                    logger.info("设备不存在!");
                }

            }
            if (topic.startsWith("IN/DEVICE/")&&topic.endsWith("/DRIVE/PR/MIN")) {
                Long id = Long.parseLong(topic.split("/")[3]);
                String record=message.getPayload().toString();
                DeviceGroup deviceGroup=deviceGroupService.findADeviceGroupById(id);
                if (deviceGroup != null) {
                    //开始后传输数据
                    if (deviceGroup.getAppUser() == null) {
                        logger.info("默认分组的设备，数据不记录!");
                    } else {
                        DrivingHealth drivingHealth=JSONObject.parseObject(record,new TypeReference<DrivingHealth>(){});
                        driveService.saveHealth(drivingHealth);
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
