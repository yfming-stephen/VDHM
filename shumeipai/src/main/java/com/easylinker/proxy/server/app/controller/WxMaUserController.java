package com.easylinker.proxy.server.app.controller;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.constants.result.ReturnResult;
import com.easylinker.proxy.server.app.model.user.AppUser;
import com.easylinker.proxy.server.app.model.user.UserRole;
import com.easylinker.proxy.server.app.service.AppUserService;
import com.easylinker.proxy.server.app.service.UserRoleService;
import com.easylinker.proxy.server.app.utils.MD5Generator;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wechat")
public class WxMaUserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    AppUserService appUserService;
    @Autowired
    private WxMaService wxService;
    @Autowired
    UserRoleService userRoleService;

    /**
     * 注册接口
     */
    @PostMapping("/register")
    public JSONObject register(@RequestBody JSONObject userBody) {
        String username = userBody.getString("username");
        String password = userBody.getString("password");
        String passwordRetry = userBody.getString("passwordRetry");

        String code = userBody.getString("code");
        String encryptedData = userBody.getString("encryptedData");
        String iv=userBody.getString("iv");
        if (username == null || password == null || passwordRetry == null) {
            return ReturnResult.returnTipMessage(0, "参数不全!");
        } else if (appUserService.getAAppUserWithParameter(username) != null) {
            return ReturnResult.returnTipMessage(0, "该用户名已经被注册!");

        } else if (password.matches("\"^[A-Za-z0-9]{6,12}+$")) {
            return ReturnResult.returnTipMessage(0, "密码必须为6-12为字母数字组合");
        } else if (!password.equals(passwordRetry)) {
            return ReturnResult.returnTipMessage(0, "两次输入密码不一样!");


        }
        if (StringUtils.isBlank(code)) {
            return ReturnResult.returnTipMessage(0, "empty jscode");
        }

        try {
            WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(code);
            this.logger.info(session.getSessionKey());
            this.logger.info(session.getOpenid());

            AppUser appUser = new AppUser();
            appUser.setUsername(username);
            appUser.setPassword(MD5Generator.EncodingMD5(password));
            appUser.setOpenId(session.getOpenid());

            WxMaPhoneNumberInfo phoneNoInfo = this.wxService.getUserService().getPhoneNoInfo(session.getSessionKey(), encryptedData, iv);
            appUser.setPhone(phoneNoInfo.getPurePhoneNumber());

            //添加权限
            appUserService.save(appUser);
            UserRole userRole = new UserRole();
            userRole.setAppUser(appUser);
            userRole.setRole("ROLE_USER");
            userRoleService.save(userRole);

            return ReturnResult.returnTipMessage(1, "注册成功!");
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return ReturnResult.returnTipMessage(0, "注册失败!");
        }
    }

    /**
     * 用户绑定接口
     */
    @PostMapping("/bind")
    public JSONObject bind(@RequestBody JSONObject userBody) {
        String username = userBody.getJSONObject("user").getString("loginParam");
        String password = userBody.getJSONObject("user").getString("password");
        String code = userBody.getString("code");
        if (username == null || password == null ) {
            return ReturnResult.returnTipMessage(0, "参数不全!");
        }
        AppUser appUser=appUserService.getAAppUserByUsername(username);
        if (appUser==null) {
            return ReturnResult.returnTipMessage(0, "用户不存在!");
        }
        else if (appUser.getPassword().equals(MD5Generator.EncodingMD5(password))){
            try {
                WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(code);
                appUser.setOpenId(session.getOpenid());
                appUserService.save(appUser);
                return ReturnResult.returnTipMessage(1, "绑定成功!");
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        }
        return ReturnResult.returnTipMessage(0, "绑定失败!");

    }




    @PostMapping("/whetherBind")
    public JSONObject whetherBind(@RequestBody JSONObject userBody) {
        String username = userBody.getJSONObject("user").getString("loginParam");
        String password = userBody.getJSONObject("user").getString("password");
        if (username == null || password == null ) {
            return ReturnResult.returnTipMessage(0, "参数不全!");
        }
        AppUser appUser=appUserService.getAAppUserByUsername(username);
        if (appUser==null) {
            return ReturnResult.returnTipMessage(0, "用户不存在!");
        }
        else if (appUser.getPassword().equals(MD5Generator.EncodingMD5(password))){
            try {
                if (appUserService.getAAppUserByUsername(username).getOpenId()==null|| "".equals(appUserService.getAAppUserByUsername(username).getOpenId())){
                    return ReturnResult.returnTipMessage(1, "未绑定!");
                }
                else {
                    return ReturnResult.returnTipMessage(0, "已经绑定!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ReturnResult.returnTipMessage(0, "绑定失败!");

    }


}
