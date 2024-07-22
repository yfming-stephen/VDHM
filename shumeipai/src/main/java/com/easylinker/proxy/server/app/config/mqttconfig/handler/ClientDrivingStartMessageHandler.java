package com.easylinker.proxy.server.app.config.mqttconfig.handler;

import com.easylinker.proxy.server.app.config.mqttconfig.MqttMessageSender;
import com.easylinker.proxy.server.app.dao.DeviceDriveRepository;
import com.easylinker.proxy.server.app.model.device.DeviceDrive;
import com.easylinker.proxy.server.app.model.device.DeviceGroup;
import com.easylinker.proxy.server.app.service.DeviceGroupService;
import com.easylinker.proxy.server.app.utils.HttpTool;
import com.easylinker.proxy.server.app.utils.MD5Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by ruilin on 2019/1/12.
 */
@Component
public class ClientDrivingStartMessageHandler implements MessageHandler {
    Logger logger = LoggerFactory.getLogger(ClientDrivingStartMessageHandler.class);

    @Autowired
    DeviceGroupService deviceGroupService;

    @Autowired
    DeviceDriveRepository deviceDriveRepository;
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
            if (topic.startsWith("IN/DEVICE/")&&topic.endsWith("/DRIVE/START")) {
                String userId=topic.split("/")[2];
                Long id = Long.parseLong(topic.split("/")[3]);
                String idMd5=message.getPayload().toString();
                DeviceGroup deviceGroup=deviceGroupService.findADeviceGroupById(id);
                if (deviceGroup != null) {
                    //开始后传输数据
                    if (deviceGroup.getAppUser() == null) {
                        logger.info("默认分组的设备，数据不记录!");
                    } else {
                        if (MD5Generator.EncodingMD5(String.valueOf(id)).equals(idMd5)){
                            String hash=MD5Generator.EncodingMD5(String.valueOf(id)+new Date().getTime());
                            mqttMessageSender.sendDriveHashMessage(userId,String.valueOf(id),hash);
                            deviceDriveRepository.save(new DeviceDrive(deviceGroup,hash));
                        }
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
