package com.easylinker.proxy.server.app.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 默认首页控制器
 * Created by wwhai on 2018/3/14.
 */
@Controller
public class IndexController {
    /**
     * 默认程序运行起来加载的主页
     *
     * @return
     */
    @RequestMapping({"/"})
    public String index() {
        return "index";
    }
}
