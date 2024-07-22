package com.easylinker.proxy.server.app.config.securityconfig.filter;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.server.ui.LoginPageGeneratingWebFilter;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 把认证时 ，提交的JSON ，封装成了一个类
 * 默认包含了  用户名  密码  验证码
 */
public class LoginParameterCatcher {
    private static final Logger logger = LoggerFactory.getLogger(LoginPageGeneratingWebFilter.class);


    public static final String LOGIN_PARAM = "loginParam";
    public static final String PASSWORD_PARAM = "password";
    public static final String CODE_PARAM="code";

    private String loginparam;
    private String password;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginParameterCatcher(HttpServletRequest httpServletRequest) {
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new BufferedReader(
                            new InputStreamReader(httpServletRequest.getInputStream())));
            if ("POST".equals(httpServletRequest.getMethod())) {
                String tempLine = "";
                StringBuffer jsonStringBuffer = new StringBuffer();
                while ((tempLine = bufferedReader.readLine()) != null) {
                    jsonStringBuffer.append(tempLine);
                }
                JSONObject jsonObject = JSONObject.parseObject(jsonStringBuffer.toString());


                String loginparam="";
                String password="";
                String code="";

                if (jsonObject.get(LOGIN_PARAM)!=null&&jsonObject.get(PASSWORD_PARAM)!=null) {
                    loginparam = jsonObject.get(LOGIN_PARAM).toString();
                    password = jsonObject.get(PASSWORD_PARAM).toString();
                }
                else{
                    code = jsonObject.get(CODE_PARAM).toString();
                }
                if (loginparam == null) {
                    loginparam = "";
                }
                if (password == null) {
                    password = "";
                }
                if (code == null) {
                    code = "";
                }
                this.setLoginparam(loginparam);
                this.setPassword(password);
                this.setCode(code);

            } else {
                logger.info("Only POST method can be support REST!");

            }

        } catch (Exception e) {
            logger.info("RequestBean param error!");
        }

    }

    public String getLoginparam() {
        return loginparam;
    }

    public void setLoginparam(String loginparam) {
        this.loginparam = loginparam;
    }
}
