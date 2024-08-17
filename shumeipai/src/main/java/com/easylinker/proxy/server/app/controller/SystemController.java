package com.easylinker.proxy.server.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.constants.result.ReturnResult;
import com.sun.management.OperatingSystemMXBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.Properties;

/**
 * 系统信息获取
 */
@RestController
@RequestMapping(value = "/system")
public class SystemController {
    /**
     * 获取当前服务器的一些状态
     *
     * @return
     */
    @RequestMapping(value = "/getSystemInfo", method = RequestMethod.GET)
    public JSONObject getSystemInfo() {
        JSONObject systemProperty = new JSONObject();
        OperatingSystemMXBean mem = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        Properties sysProperty = System.getProperties(); //系统属性
        systemProperty.put("java_version", sysProperty.getProperty("java.version"));
        systemProperty.put("java_vendor", sysProperty.getProperty("java.vendor"));
        systemProperty.put("java_home", sysProperty.getProperty("java.home").replace("\\","_"));
        systemProperty.put("java_vm_version", sysProperty.getProperty("java.vm.version"));
        systemProperty.put("os_name", sysProperty.getProperty("os.name"));
        systemProperty.put("os_arch", sysProperty.getProperty("os.arch"));
        systemProperty.put("os_version", sysProperty.getProperty("os.version"));
        systemProperty.put("sun_cpu_isalist", sysProperty.getProperty("sun.cpu.isalist"));
        systemProperty.put("total_ram", mem.getTotalPhysicalMemorySize() / 1024 / 1024 );
        systemProperty.put("available_ram", mem.getFreePhysicalMemorySize() / 1024 / 1024 );
        systemProperty.put("totalMemory", mem.getFreePhysicalMemorySize() / 1024 / 1024 );
        systemProperty.put("freeMemory", Runtime.getRuntime().freeMemory() / 1024 );
        systemProperty.put("maxMemory", Runtime.getRuntime().maxMemory() / 1024 );
        systemProperty.put("time", new Date());


        return ReturnResult.returnDataMessage(1, "获取成功!", systemProperty);
    }



}
