package com.easylinker.proxy.server.app.config.resttemplateconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Rest 框架配置
 */
@Configuration
public class RestTemplateConfig {
    @Value("${emq.api.user}")
    String username;
    @Value("${emq.api.password}")
    String password;


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.basicAuthorization(username, password)
                .setConnectTimeout(1000)
                .setReadTimeout(1000)
                .build();
    }
}
