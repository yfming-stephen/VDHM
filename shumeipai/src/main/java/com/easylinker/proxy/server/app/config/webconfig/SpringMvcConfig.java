package com.easylinker.proxy.server.app.config.webconfig;

import com.easylinker.proxy.server.app.interceptor.AccessLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by wwhai on 2018/3/14.
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
    @Autowired
    AccessLogInterceptor accessLogInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLogInterceptor);
    }


}
