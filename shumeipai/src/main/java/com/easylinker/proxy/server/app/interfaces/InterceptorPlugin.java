package com.easylinker.proxy.server.app.interfaces;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 插件编写的时候，需要实现这个接口
 */
public class InterceptorPlugin implements HandlerInterceptor {
    private String name;
    private String version;
    private String pluginPackage;
    private String desc;
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPluginPackage() {
        return pluginPackage;
    }

    public void setPluginPackage(String pluginPackage) {
        this.pluginPackage = pluginPackage;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
