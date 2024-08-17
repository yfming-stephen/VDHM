package com.easylinker.proxy.server.app;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.service.DeviceGroupService;
import com.easylinker.proxy.server.app.service.DeviceService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@EnableWebSecurity
@EnableAsync
@EnableCaching
@EnableScheduling
@EnableTransactionManagement
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication
public class EasyLinkerApplication implements CommandLineRunner {
    Logger logger = LoggerFactory.getLogger(EasyLinkerApplication.class);
    @Autowired
    RestTemplate restTemplate;
    @Value("${emq.api.user}")
    String username;
    @Value("${emq.api.password}")
    String password;
    @Value("${emq.node.name}")
    String emqNodeName;
    @Value("${emq.api.host}")
    String apiHost;
    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceGroupService deviceGroupService;

    @Override
    public void run(String... args) throws Exception {
        /**
         * 重启以后会有一些操作
         * 1 更新设备的状态
         * 2 刷新EMQ的状态
         */
        getEmqInfo();
        checkOnlineDevice();
        checkOfflineDevice();
        logger.info("EasyLinker启动成功!");

    }

    public void getEmqInfo() {
        /**
         {
         "code": 0,
         "result": {
         "version": "2.3.5",
         "sysdescr": "Erlang MQTT Broker",
         "uptime": "6 minutes, 33 seconds",
         "datetime": "2018-07-05 11:05:25",
         "otp_release": "R19/8.3",
         "node_status": "Running"
         }
         }
         */
        //api/v2/nodes/emq@127.0.0.1/clients
        //api/v2/management/nodes/emq@127.0.0.1
        try {

            JSONObject emqInfoJson = restTemplate.getForObject(apiHost + "management/nodes/" + emqNodeName, JSONObject.class);
            logger.info("┏━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
            logger.info("┃EMQ版本：" + emqInfoJson.getJSONObject("result").getString("version"));
            logger.info("┃EMQ描述：" + emqInfoJson.getJSONObject("result").getString("sysdescr"));
            logger.info("┃EMQ开启时间：" + emqInfoJson.getJSONObject("result").getString("uptime"));
            logger.info("┃状态：" + emqInfoJson.getJSONObject("result").getString("node_status"));
            logger.info("┗━━━━━━━━━━━━━━━━━━━━━━━━━━┛");


        } catch (Exception e) {
            if (e instanceof ResourceAccessException) {
                logger.info("EMQ没有启动,服务器信息获取失败！");
            }
        }
    }

    /**
     * 检查时候存在EasyLinker关闭了但是EMQ上面任然连接了设备导致没有及时刷新数据的情况
     * 作用是更新EasyLinker掉线后来连接的设备的状态
     */
    HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("UTF-8")));

            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

    public void checkOnlineDevice() {
        try {
//            JSONObject clientsJson = restTemplate.exchange (apiHost + "nodes/" + emqNodeName + "/clients",
//                    HttpMethod.POST,
//                    new HttpEntity<T>(createHeaders(username, password)),
//                    JSONObject.class);
            JSONObject clientsJson = restTemplate.getForObject(apiHost + "nodes/" + emqNodeName + "/clients", JSONObject.class);


            JSONArray clientsArray = clientsJson.getJSONObject("result").getJSONArray("objects");
            if (clientsArray.size() > 4) {
                logger.info("开始检测脱离EMQ的设备,开始恢复状态!");
                //需要考虑另一种情况: 设备电线了,但是没有改变状态
                //虽然数据库显示 isOnline=true
                //但是 EMQ并没有在线的 全部吧数据库更新一遍
                for (Object o : clientsArray) {
                    JSONObject clientJson = (JSONObject) o;
                    //排除监控器
                    if (isNumeric(clientJson.getString("client_id"))) {
                        logger.info("更新设备[{}]的状态", clientJson.getString("client_id"));
                        Long deviceGroupId = clientJson.getLongValue("client_id");
                        List<Long> deviceIds = deviceGroupService.getAllDeviceIdByGroupId(deviceGroupId);
                        for (Long id : deviceIds) {
                            Device device = deviceService.findADevice(id);
                            if (device != null) {
                                device.setOnline(true);
                            }
                            deviceService.save(device);//重启以后更新状态
                        }
                    }
                }

            }
            logger.info("恢复状态完毕!");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("恢复设备状态的时候出现问题!请重启EasyLinker!", e.getLocalizedMessage());
        }

    }

    /**
     * .
     * 同理 EMQ突然关了的情况
     */
    public void checkOfflineDevice() {
        try {
            JSONObject clientsJson = restTemplate.getForObject(apiHost + "nodes/" + emqNodeName + "/clients", JSONObject.class);
            JSONArray clientsArray = clientsJson.getJSONObject("result").getJSONArray("objects");
            logger.info("开始检测脱离EMQ控制的设备");
            List<Long> emqDeviceIdList = new ArrayList<>();
            for (Object o : clientsArray) {
                JSONObject clientJson = (JSONObject) o;
                if (isNumeric(clientJson.getString("client_id"))) {
                    emqDeviceIdList.add(clientJson.getLongValue("client_id"));
                }
            }
            /**
             * 数据库里面把所有的设备拿出来对比是否成僵尸
             */

            List<Long> idList = deviceGroupService.getAllDeviceGroupId();

            for (Long groupId : idList) {
                if (!emqDeviceIdList.contains(groupId)) {//如果数据库里面的ID不在EMQ连接的上面,那说明就掉线跑飞了
                    List<Long> deviceIds = deviceGroupService.getAllDeviceIdByGroupId(groupId);
                    for (Long id : deviceIds) {
                        Device device = deviceService.findADevice(id);
                        if (device != null) {
                            if (device.isOnline()) {
                                device.setOnline(false);
                                deviceService.save(device);
                                logger.info("设备[{}]脱离状态，恢复完毕", device.getClientId());
                            }
                        }
                    }
                }
            }

            logger.info("恢复状态完毕!");
        } catch (
                Exception e)

        {
            e.printStackTrace();
            logger.error("恢复EMQ状态的时候出现问题!请重启EasyLinker!", e.getLocalizedMessage());
        }

    }


    @SuppressWarnings("AlibabaAvoidPatternCompileInMethod")
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
