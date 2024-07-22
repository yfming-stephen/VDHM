package com.easylinker.proxy.server.app.config.securityconfig.filter;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.model.user.AppUser;
import com.easylinker.proxy.server.app.service.AppUserService;
import com.easylinker.proxy.server.app.utils.LogPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ruilin on 2018/9/14.
 */
public class OpenIdAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    LogPrinter logPrinter;
    private static final String DEFAULT_LOGIN_URL = "/wechatLogin";
    private static final String DEFAULT_LOGIN_METHOD = "POST";
    private Log logger = LogFactory.getLog(OpenIdAuthenticationFilter.class);
    LoginParameterCatcher loginParameterCatcher = null;

    @Autowired
    private WxMaService wxService;
    @Autowired
    private AppUserService appUserService;

    public OpenIdAuthenticationFilter() {
        setAuthenticationManager(super.getAuthenticationManager());

        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(DEFAULT_LOGIN_URL, DEFAULT_LOGIN_METHOD));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(DEFAULT_LOGIN_URL, DEFAULT_LOGIN_METHOD));
        loginParameterCatcher = new LoginParameterCatcher(request);

        String code = loginParameterCatcher.getCode();


        Authentication authentication = null;

        if (true && !"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Only support post!");
        } else {

            if (StringUtils.isBlank(code)) {
                JSONObject resultJson = new JSONObject();
                resultJson.put("state", 0);
                resultJson.put("message", "empty jscode");
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                try {
                    response.getWriter().write(resultJson.toJSONString());
                    response.getWriter().flush();
                } catch (IOException e1) {
                    logger.error("输出流写入JSON失败!");
                    logPrinter.log("向输出流写入数据的时候", "写入失败", "HttpResponse");
                }
            }

            try {
                WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(code);
                this.logger.info(session.getSessionKey());
                this.logger.info(session.getOpenid());
                AppUser appUser = appUserService.getAUserByOpenId(session.getOpenid());
                if (appUser != null) {

                    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword());

                    this.setDetails(request, authRequest);
                    authentication = this.getAuthenticationManager().authenticate(authRequest);
                    logPrinter.log("登陆", "登录成功", code);
                }else{


                    logPrinter.log("登陆", "登录失败", null);
                    JSONObject resultJson = new JSONObject();
                    resultJson.put("state", 0);
                    resultJson.put("message", "登录失败!用户不存在!请绑定用户!");
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");

                    try {
                        response.getWriter().write(resultJson.toJSONString());
                        response.getWriter().flush();
                    } catch (IOException e1) {
                        logger.error("输出流写入JSON失败!");
                        logPrinter.log("向输出流写入数据的时候", "写入失败", "HttpResponse");
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                logPrinter.log("登陆", "登录失败", null);
                JSONObject resultJson = new JSONObject();
                resultJson.put("state", 0);
                resultJson.put("message", "登录失败!用户不存在!请绑定用户!");
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                try {
                    response.getWriter().write(resultJson.toJSONString());
                    response.getWriter().flush();
                } catch (IOException e1) {
                    logger.error("输出流写入JSON失败!");
                    logPrinter.log("向输出流写入数据的时候", "写入失败", "HttpResponse");
                }

            }

        }
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        logPrinter.log("登录", "登陆成功!", ((AppUser) authResult.getPrincipal()).getUsername());

    }
}
