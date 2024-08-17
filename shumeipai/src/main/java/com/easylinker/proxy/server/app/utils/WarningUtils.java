package com.easylinker.proxy.server.app.utils;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruilin on 2018/9/16.
 */
@Component
public class WarningUtils {
    Logger logger = LoggerFactory.getLogger(WarningUtils.class);


    @Value("${ali.appcode}")
    String appCode;

    @Value("${ali.tpl.voiceid}")
    String voiceId;
    @Value("${ali.tpl.messageid}")
    String messageId;

    public int warningByVoice(String phone, Long id, String name, String triggerName, Double value) {
        String host = "http://mbyytz.market.alicloudapi.com";
        String path = "/mobai_voicenotifysms";
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appCode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("phone", phone);
        querys.put("param",  "triggername:" + triggerName +",name:" + name +  ",value:" + value);
        querys.put("templateId", voiceId);
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            logger.info("语音发送成功 "  + "triggername:" + triggerName +",name:" + name + ",value:" + value);
//            System.out.println(response.toString());

//            获取response的body
//            System.out.println(EntityUtils.toString(response.getEntity()));
            String str=EntityUtils.toString(response.getEntity());
            JsonObject jsonObject=  new JsonParser().parse(str).getAsJsonObject();
            String returnCode=jsonObject.get("return_code").getAsString();
            if ("00000".equals(returnCode)){
                return 1;
            }
            else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int warningByMessage(String phone, Long id, String name, String triggerName, Double value) {
        String host = "http://mobai130.market.alicloudapi.com";
        String path = "/mobai_longSms";
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appCode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("param", "id:" + id + ",triggername:" + triggerName + ",name:" + name +  ",value:" + value);
        querys.put("phone", phone);
        querys.put("templateId", messageId);
        Map<String, String> bodys = new HashMap<String, String>();

                
        try {

            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
//            System.out.println(response.toString());
//            JSONObject jsonObject= JSON.parseObject(EntityUtils.toString(response.getEntity()));
            String str=EntityUtils.toString(response.getEntity());
            JsonObject jsonObject=  new JsonParser().parse(str).getAsJsonObject();
            String returnCode=jsonObject.get("return_code").getAsString();
            if ("00000".equals(returnCode)){
                return 1;
            }
            else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }


}
