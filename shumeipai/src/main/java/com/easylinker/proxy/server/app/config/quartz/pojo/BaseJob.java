package com.easylinker.proxy.server.app.config.quartz.pojo;

import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.service.DeviceService;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Base64;
import java.util.Properties;

/**
 * job父类，包含一个抽象函方法，将实现推迟到具体的子类
 */
@Component
public abstract class BaseJob implements Job, Serializable {
    @Autowired
    DeviceService deviceService;

    private OkHttpClient client = new OkHttpClient();

    /**
     * 发送http post请求
     *
     * @param url
     * @param data 提交的参数为key=value&key1=value1的形式
     * @return
     */
    public JSONObject postWithAuthorization(String url, JSONObject data) throws Exception {

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data.toJSONString());
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));

        Request request = new Request.Builder()
                .addHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((properties.get("emq.api.user") + ":" + properties.get("emq.api.password")).getBytes()))
                .addHeader("Content-Length", "22")
                .addHeader("Content-Type", "application/json")
                .addHeader("Server", "MochiWeb/1.0 (Any of you quaids got a smint?)")
                .url(url)
                .post(body)
                .build();
        System.out.println("自动"+data);
        return JSONObject.parseObject(client.newCall(request).execute().body().string());


    }

    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    public abstract void action(JobExecutionContext context);

    /**
     * 统计时间
     *
     * @param context
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext context) {
        this.action(context);
    }
}
