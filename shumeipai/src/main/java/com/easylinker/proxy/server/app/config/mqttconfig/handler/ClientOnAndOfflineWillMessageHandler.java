package com.easylinker.proxy.server.app.config.mqttconfig.handler;

import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.config.mqttconfig.MqttMessageSender;
import com.easylinker.proxy.server.app.constants.mqtt.RealTimeType;
import com.easylinker.proxy.server.app.model.daily.DeviceOnAndOffLineLog;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.service.DeviceGroupService;
import com.easylinker.proxy.server.app.service.DeviceOnAndOffLineLogService;
import com.easylinker.proxy.server.app.service.DeviceService;
import com.easylinker.proxy.server.app.utils.HttpTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 设备上下线控制
 */

//clintId == groupId 组登陆成功后设备全部登陆 反之同理
@Component
public class ClientOnAndOfflineWillMessageHandler implements MessageHandler {
    Logger logger = LoggerFactory.getLogger(ClientOnAndOfflineWillMessageHandler.class);
    @Autowired
    DeviceService deviceService;
    @Value("${emq.username}")
    private String EMQ_USERNAME;

    @Autowired
    MqttMessageSender mqttMessageSender;
    @Autowired
    HttpTool httpTool;
    @Value("${emq.api.host}")
    String apiHost;
    //emq.websocket.username
    @Value("${emq.websocket.username}")
    String WEBSOCKET_USERNAME;
    @Autowired
    DeviceOnAndOffLineLogService deviceOnAndOffLineLogService;
    @Autowired
    DeviceGroupService deviceGroupService;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        JSONObject mqttMessage;
        MessageHeaders header = message.getHeaders();
        String state = header.get("mqtt_topic").toString().
                substring(header.get("mqtt_topic").toString().lastIndexOf("/") + 1);
        System.out.println(header.get("mqtt_topic").toString());
        if ("connected".equals(state)) {
            try {
                mqttMessage = (JSONObject) JSONObject.parse(message.getPayload().toString());
                String username = mqttMessage.getString("username");
                if (username.equals(EMQ_USERNAME) || username.equals(WEBSOCKET_USERNAME)) {
                    logger.info("内部组件[" + username + "]连接成功!");
                } else {
                    try {
                        List<Long> deviceIds=deviceGroupService.getAllDeviceIdByGroupId(Long.parseLong(username));
                        for (Long id:deviceIds) {
                            Device device = deviceService.findADevice(id);
                            if (device != null) {
                                device.setOnline(true);
                                deviceService.save(device);
                                logger.info("设备:[" + device.getDeviceName() + "]上线");
                                JSONObject realTimeJson = new JSONObject();
                                realTimeJson.put("type", RealTimeType.ONLINE);
                                realTimeJson.put("device", device.getId());
                                mqttMessageSender.sendRealTimePureMessage(realTimeJson);
                                mqttMessageSender.sendDeviceScopeMessage(device.getTopic(),device);
                                //生成日志
                                DeviceOnAndOffLineLog deviceOnAndOffLineLog = new DeviceOnAndOffLineLog();
                                deviceOnAndOffLineLog.setDate(new Date());
                                deviceOnAndOffLineLog.setDevice(device);
                                deviceOnAndOffLineLog.setEvent("CONNECT");
                                deviceOnAndOffLineLogService.save(deviceOnAndOffLineLog);

                            }
                        }
                    } catch (Exception e) {
                        //todo
                        logger.error(e.getMessage());
                    }


                }
            } catch (Exception e) {
                logger.error("解析消息时出现了格式错误!");
            }


        } else if ("disconnected".equals(state)) {
            try {
                mqttMessage = (JSONObject) JSONObject.parse(message.getPayload().toString());
                String username = mqttMessage.getString("username");
                if (username.equals(EMQ_USERNAME) || username.equals(WEBSOCKET_USERNAME)) {
                    logger.info("内部组件[" + username + "]断开连接!");
                } else {
                    List<Long> deviceIds=deviceGroupService.getAllDeviceIdByGroupId(Long.parseLong(username));
                    for (Long id:deviceIds) {
                        Device device = deviceService.findADevice(id);
                        if (device != null) {
                            device.setOnline(false);
                            device.setLastActiveDate(new Date());
                            deviceService.save(device);
                            logger.info("设备:[" + device.getDeviceName() + "]下线");
                            JSONObject realTimeJson = new JSONObject();
                            realTimeJson.put("type", RealTimeType.OFFLINE);
                            realTimeJson.put("device", device.getId());
                            mqttMessageSender.sendRealTimePureMessage(realTimeJson);
                            //生成日志
                            DeviceOnAndOffLineLog deviceOnAndOffLineLog = new DeviceOnAndOffLineLog();
                            deviceOnAndOffLineLog.setDate(new Date());
                            deviceOnAndOffLineLog.setDevice(device);
                            deviceOnAndOffLineLog.setEvent("DISCONNECT");
                            deviceOnAndOffLineLogService.save(deviceOnAndOffLineLog);

                        }
                    }
                }
            } catch (Exception e) {
                logger.error("解析消息时出现了格式错误!");
            }

        }
    }
}
