package com.easylinker.proxy.server.app.interfaces;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.web.servlet.filter.ApplicationContextHeaderFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 插件安装器
 * 注意：插件本质上是一个SpringMVC的拦截器
 */
@Component
public class PluginInstaller extends ApplicationContextHeaderFilter implements WebMvcConfigurer {
    private Log logger = LogFactory.getLog(PluginInstaller.class);

    public PluginInstaller(org.springframework.context.ApplicationContext context) {
        super(context);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        for (InterceptorPlugin interceptorPlugin : LoadPlugin()) {
            registry.addInterceptor(interceptorPlugin);
            logger.info(String.format("插件[%s]加载成功", interceptorPlugin.getName()));
        }

    }

    /**
     * 解析配置文件
     *
     * @return
     */

    public List<InterceptorPlugin> LoadPlugin() {
        List<InterceptorPlugin> interceptorPluginList = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(this.getClass().getResourceAsStream("/plugin.json")));
            StringBuffer pluginConfigJsonStringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                pluginConfigJsonStringBuffer.append(line);
            }
            bufferedReader.close();
            JSONObject pluginConfigJson = JSONObject.parseObject(pluginConfigJsonStringBuffer.toString());
            JSONArray pluginsJsonArray = pluginConfigJson.getJSONArray("plugins");
            for (int i = 0; i < pluginsJsonArray.size(); i++) {

                Class<InterceptorPlugin> demoPluginClass = (Class<InterceptorPlugin>) Class.forName(((JSONObject) pluginsJsonArray.get(i)).getString("package"));
                InterceptorPlugin interceptorPlugin = demoPluginClass.newInstance();
                interceptorPlugin.setDesc(((JSONObject) pluginsJsonArray.get(i)).getString("desc"));
                interceptorPlugin.setVersion(((JSONObject) pluginsJsonArray.get(i)).getString("version"));
                interceptorPlugin.setPluginPackage(((JSONObject) pluginsJsonArray.get(i)).getString("package"));
                interceptorPlugin.setAuthor(((JSONObject) pluginsJsonArray.get(i)).getString("author"));
                interceptorPlugin.setName(((JSONObject) pluginsJsonArray.get(i)).getString("name"));
                interceptorPluginList.add(interceptorPlugin);
            }

        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("加载插件的时候出错:"+e.getMessage());
        }
        return interceptorPluginList;
    }


}
