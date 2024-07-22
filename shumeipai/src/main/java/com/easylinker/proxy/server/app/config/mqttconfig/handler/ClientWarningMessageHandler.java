package com.easylinker.proxy.server.app.config.mqttconfig.handler;

import com.easylinker.proxy.server.app.model.daily.DeviceWarningLog;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.DeviceScope;
import com.easylinker.proxy.server.app.model.user.AppUser;
import com.easylinker.proxy.server.app.service.AppUserService;
import com.easylinker.proxy.server.app.service.DeviceScopeService;
import com.easylinker.proxy.server.app.service.DeviceService;
import com.easylinker.proxy.server.app.service.DeviceWarningLogService;
import com.easylinker.proxy.server.app.utils.RedisUtils;
import com.easylinker.proxy.server.app.utils.WarningUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

/**
 * Created by Ruilin on 2018/7/31.
 */

/**
 * 报警
 */
@Component
public class ClientWarningMessageHandler implements MessageHandler {
    Logger logger = LoggerFactory.getLogger(ClientWarningMessageHandler.class);

    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceScopeService deviceScopeService;

    @Autowired
    DeviceWarningLogService deviceWarningLogService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    WarningUtils warningUtils;

    @Autowired
    AppUserService appUserService;
    @Value("${emq.api.host}")
    String apiHost;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {

        String topic = message.getHeaders().get("mqtt_topic").toString();
        try {
            if (topic.startsWith("WARNING/DEVICE/")) {
                Long deviceId = Long.parseLong(topic.split("/")[4]);
                String triggerId = topic.split("/")[5];
                Double record = Double.parseDouble(message.getPayload().toString());
                Device device = deviceService.findADevice(deviceId);
                DeviceScope trigger=deviceScopeService.findById(Long.parseLong(triggerId));
                String key="warning"+triggerId;
                AppUser appUser=appUserService.getAUserById(device.getAppUser().getId());

                if (device != null) {
                    //开始后传输数据
                    if (device.getAppUser() == null) {
                        logger.info("默认分组的设备，数据不记录!");
                    } else {

                        if (redisUtils.hasKey(key)){
                            redisUtils.incr(key,1);
                            Integer value=Integer.valueOf(redisUtils.get(key).toString());
                            if (value==null) {
                                logger.info("Redis数据读取出错!");
                            }
                            //0.5秒发送一次 如果五分钟内频率多达2/3则再发一次短信
                            if (value==14000){
                                DeviceWarningLog deviceWarningLog = new DeviceWarningLog();
                                deviceWarningLog.setDevice(device);
                                deviceWarningLog.setType(device.getDeviceType());
                                deviceWarningLog.setInfo("数据超出规定范围");
                                deviceWarningLog.setRealValue(record);
                                deviceWarningLogService.save(deviceWarningLog);
                                redisUtils.set(key,"1");
                                if (warningUtils.warningByMessage(appUser.getPhone(),deviceId,device.getDeviceName(),trigger.getName(),record)==1){
                                    logger.info("短信发送成功");

                                }
                                else {
                                    if (appUser.getAlternatePhone()==null) {
                                        warningUtils.warningByMessage(appUser.getPhone(),deviceId,device.getDeviceName(),trigger.getName(),record);
                                    } else {
                                        warningUtils.warningByMessage(appUser.getAlternatePhone(),deviceId,device.getDeviceName(),trigger.getName(),record);
                                    }
                                    logger.info("短信发送至备用手机");
                                }
                                if (warningUtils.warningByVoice(appUser.getPhone(),deviceId,device.getDeviceName(),trigger.getName(),record)==1){
                                    logger.info("电话拨打成功");
                                }
                                else {
                                    if (appUser.getAlternatePhone()==null) {
                                        warningUtils.warningByVoice(appUser.getPhone(),deviceId,device.getDeviceName(),trigger.getName(),record);
                                    } else {
                                        warningUtils.warningByVoice(appUser.getAlternatePhone(),deviceId,device.getDeviceName(),trigger.getName(),record);
                                    }
                                    logger.info("电话拨打至备用手机");
                                }
                            }
                        }else{
                            redisUtils.set(key,"1",21600);//十分钟删除
                            DeviceWarningLog deviceWarningLog = new DeviceWarningLog();
                            deviceWarningLog.setDevice(device);
                            deviceWarningLog.setType(device.getDeviceType());
                            deviceWarningLog.setInfo("数据超出规定范围");
                            deviceWarningLog.setRealValue(record);
                            deviceWarningLogService.save(deviceWarningLog);
                            if (warningUtils.warningByMessage(appUser.getPhone(),deviceId,device.getDeviceName(),trigger.getName(),record)==1){
                                logger.info("短信发送成功");

                            }
                            else {
                                if (appUser.getAlternatePhone()==null) {
                                    warningUtils.warningByMessage(appUser.getPhone(),deviceId,device.getDeviceName(),trigger.getName(),record);
                                } else {
                                    warningUtils.warningByMessage(appUser.getAlternatePhone(),deviceId,device.getDeviceName(),trigger.getName(),record);
                                }
                                logger.info("短信发送至备用手机");
                            }
                            if (warningUtils.warningByVoice(appUser.getPhone(),deviceId,device.getDeviceName(),trigger.getName(),record)==1){
                                logger.info("电话拨打成功");
                            }
                            else {
                                if (appUser.getAlternatePhone()==null) {
                                    warningUtils.warningByVoice(appUser.getPhone(),deviceId,device.getDeviceName(),trigger.getName(),record);
                                } else {
                                    warningUtils.warningByVoice(appUser.getAlternatePhone(),deviceId,device.getDeviceName(),trigger.getName(),record);
                                }
                                logger.info("电话拨打至备用手机");
                            }

                        }
                        
//                        logger.info("报警数据保存成功!");
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
