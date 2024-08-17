package com.easylinker.proxy.server.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.constants.result.ReturnResult;
import com.easylinker.proxy.server.app.model.device.Device;
import com.easylinker.proxy.server.app.model.device.DeviceData;
import com.easylinker.proxy.server.app.model.user.AppUser;
import com.easylinker.proxy.server.app.service.AppUserService;
import com.easylinker.proxy.server.app.service.DeviceDataService;
import com.easylinker.proxy.server.app.service.DeviceGroupService;
import com.easylinker.proxy.server.app.service.DeviceService;
import com.easylinker.proxy.server.app.utils.HttpTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 给HTTP使用
 */
@RestController
@RequestMapping("/api/v1")
public class HTTPApiController {

    @Value("${emq.api.host}")
    String apiHost;
    @Value("${emq.node.name}")
    String emqNodeName;
    /**
     * 通过HTTP接口发送数据
     * {
     * "topic"    : "test",
     * "payload"  : "hello",
     * "qos"      : 1,
     * "retain"   : false,
     * "client_id": "C_1492145414740"
     * }
     * curl -v --basic -u <user>:<passwd> -k http://localhost:8080/api/v2/nodes/emq@127.0.0.1/clients
     *
     * @return
     */

    @Autowired
    DeviceService deviceService;
    @Autowired
    AppUserService appUserService;
    @Autowired
    DeviceGroupService deviceGroupService;
    @Autowired
    HttpTool httpTool;
    @Autowired
    DeviceDataService deviceDataService;

    @Autowired
    RestTemplate restTemplate;

    /**
     * 注意：默认只接受JSON格式的CMD 其他格式直接返回结果
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/sendCmdToDevice", method = RequestMethod.POST)
    public JSONObject sendCmdToDevice(@RequestBody JSONObject body) {
        JSONObject cmd = new JSONObject();
        JSONObject payload = body.getJSONObject("payload");
        Long deviceId = body.getLongValue("deviceId");

        if (payload == null || deviceId == null) {
            return ReturnResult.returnTipMessage(0, "参数不全!");
        } else {
            Device device = deviceService.findADevice(deviceId);
            if (device != null && device.getAppUser() != null) {
                cmd.put("topic", device.getTopic().replace("IN", "OUT"));
                try {
                    cmd.put("payload", payload.toJSONString());//这里注意：payload必须是String类型的
                } catch (Exception e) {
                    return ReturnResult.returnTipMessage(0, "消息格式必须是JSON!");
                }
                cmd.put("qos", 1);
                cmd.put("retain", false);
                cmd.put("client_id", "easylinker_server");
                try {
                    //System.out.println(cmd);
                    httpTool.postWithAuthorization(apiHost + "mqtt/publish", cmd);

                    //保存记录
                    DeviceData deviceData = new DeviceData();
                    deviceData.setDevice(device);
                    deviceData.setData(payload.toString());
                    deviceData.setType("OUT");
                    deviceDataService.save(deviceData);

                } catch (Exception e) {
                    return ReturnResult.returnTipMessage(0, "发送失败!");
                }

                return ReturnResult.returnTipMessage(1, "发送成功!");
            } else {
                return ReturnResult.returnTipMessage(0, "设备不存在或者没有绑定!");
            }

        }
    }


    /**
     * 广播
     */
    @RequestMapping(value = "/sendCmdToGroup", method = RequestMethod.POST)
    public JSONObject sendCmdToGroup(@RequestBody JSONObject body) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JSONObject cmd = new JSONObject();
        JSONObject payload = body.getJSONObject("payload");
        Long groupId = body.getLongValue("groupId");
        cmd.put("topic", "OUT/DEVICE/" + appUser.getId() + "/" + groupId);
        try {
            cmd.put("payload", payload.toJSONString());//这里注意：payload必须是String类型的
        } catch (Exception e) {
            return ReturnResult.returnTipMessage(0, "消息格式必须是JSON!");
        }
        cmd.put("qos", 1);
        cmd.put("retain", false);
        cmd.put("client_id", "easylinker_server");
        try {
            httpTool.postWithAuthorization(apiHost + "mqtt/publish", cmd);
            //System.out.println("广播消息:" + httpTool.postWithAuthorization(apiHost + "mqtt/publish", cmd).toJSONString() + cmd);

        } catch (Exception e) {
            return ReturnResult.returnTipMessage(0, "发送失败!");
        }
        return ReturnResult.returnTipMessage(1, "发送成功!");

    }


    /**
     * 设备端通过HTTP POST数据进来
     *
     * @return
     */
    @RequestMapping(value = "/postDataWithHttp")
    public JSONObject receivePostFromClientWithHttp(@RequestBody JSONObject body) {
        Long deviceId = body.getLongValue("deviceId");
        JSONObject data = body.getJSONObject("data");
        if (data == null || deviceId == null) {
            return ReturnResult.returnTipMessage(0, "参数不全!");
        } else {
            Device device = deviceService.findADevice(deviceId);
            if (device != null) {
                if (device.getAppUser() != null) {
                    DeviceData deviceData = new DeviceData();
                    deviceData.setDevice(device);
                    deviceData.setData(data.toJSONString());
                    deviceDataService.save(deviceData);
                    return ReturnResult.returnTipMessage(1, "数据提交成功!");
                } else {
                    return ReturnResult.returnTipMessage(0, "设备没有绑定!");
                }


            } else {
                return ReturnResult.returnTipMessage(0, "设备不存在!");
            }
        }
    }

    /**
     * 获取当前EMQ的一些数据
     *
     * @return
     */
    @RequestMapping(value = "/getEmqInfo")
    public JSONObject getEmqInfo() {
        //api/v2/monitoring/nodes/emq@127.0.0.1
        JSONObject emqInfoJson = null;
        try {
            emqInfoJson = restTemplate.getForObject(apiHost + "monitoring/nodes/" + emqNodeName, JSONObject.class);
            return ReturnResult.returnDataMessage(1, "", emqInfoJson.getJSONObject("result"));
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            JSONObject result = new JSONObject();
            result.put("node_status", "STOP");
            return ReturnResult.returnDataMessage(0, "EMQ服务器启动失败!", result);
        }
    }
}
