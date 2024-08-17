package com.easylinker.proxy.server.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.constants.result.ReturnResult;
import com.easylinker.proxy.server.app.model.user.AppUser;
import com.easylinker.proxy.server.app.model.user.UserRole;
import com.easylinker.proxy.server.app.service.AppUserService;
import com.easylinker.proxy.server.app.service.UserRoleService;
import com.easylinker.proxy.server.app.utils.EmailSender;
import com.easylinker.proxy.server.app.utils.MD5Generator;
import com.sun.mail.smtp.SMTPAddressFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

/**
 * 用户基本操作
 * 注册 登陆 修改
 */
@RestController
@RequestMapping("/user")
public class UserRegisterEtcController {
    static final String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
    static final String RULE_PHONE = "(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{7,14}";
    static final String RULE_USERNAME = "^[0-9a-zA-Z\\u4e00-\\u9fa5_]{6,12}$";
    @Autowired
    AppUserService appUserService;

    @Autowired
    UserRoleService userRoleService;
    @Autowired
    EmailSender emailSender;


    /**
     * 注册用户
     *
     * @return
     */
    @RequestMapping("/register")
    @Transient
    public JSONObject register(@RequestBody JSONObject userBody) {

        String username = userBody.getString("username");
        String password = userBody.getString("password");
        String passwordRetry = userBody.getString("passwordRetry");
        String email = userBody.getString("email");
        String phone = userBody.getString("phone");
        String alternatePhone =userBody.getString("alternatePhone");

        if (username == null || password == null || email == null || phone == null || passwordRetry == null) {
            return ReturnResult.returnTipMessage(0, "参数不全!");
        } else if (appUserService.getAAppUserWithParameter(username) != null) {
            return ReturnResult.returnTipMessage(0, "该用户名已经被注册!");

        } else if (appUserService.getAAppUserWithParameter(phone) != null && appUserService.getAAppUserWithParameter(alternatePhone) != null) {
            return ReturnResult.returnTipMessage(0, "该电话号已经被注册!");

        } else if (appUserService.getAAppUserWithParameter(email) != null) {
            return ReturnResult.returnTipMessage(0, "该邮箱已经被注册!");

        } else if (password.matches("\"^[A-Za-z0-9]{6,12}+$")) {
            return ReturnResult.returnTipMessage(0, "密码必须为6-12为字母数字组合");
        } else if (!password.equals(passwordRetry)) {
            return ReturnResult.returnTipMessage(0, "两次输入密码不一样!");
        } else {//处理邮箱 电话号码格式
            if ((!email.matches(RULE_EMAIL))) {
                return ReturnResult.returnTipMessage(0, "邮箱格式错误!");

            } else if ((!phone.matches(RULE_PHONE))&&(!alternatePhone.matches(RULE_PHONE))) {
                return ReturnResult.returnTipMessage(0, "电话号码格式错误!");

            } else if ((!username.matches(RULE_USERNAME))) {
                return ReturnResult.returnTipMessage(0, "用户名必须6-12位!");

            } else {//所有的非法条件过滤以后，开始注册用户
                AppUser appUser = new AppUser();
                appUser.setUsername(username);
                appUser.setPassword(MD5Generator.EncodingMD5(password));
                appUser.setEmail(email);
                appUser.setEmail(alternatePhone);
                appUser.setPhone(phone);
                appUser.setEnabled(true);
                try {

                    //emailSender.sendHtmlMail(appUser);//发送激活邮件
                    //添加权限  默认新用户全部是 ROLE_USER 普通用户
                    appUserService.save(appUser);
                    UserRole userRole = new UserRole();
                    userRole.setAppUser(appUser);
                    userRole.setRole("ROLE_USER");
                    userRoleService.save(userRole);
                    return ReturnResult.returnTipMessage(1, "注册成功!");
                } catch (Exception e) {
                    if (e instanceof SMTPAddressFailedException) {
                        return ReturnResult.returnTipMessage(0, "邮箱无效！请使用正确的邮箱!");
                    } else {
                        e.printStackTrace();
                        return ReturnResult.returnTipMessage(0, "服务器邮件发送失败,请联系管理员！");
                    }


                }

            }
        }
    }

    /**
     * 更新用户
     *
     * @param userBody
     * @return
     */
    @Transient
    @RequestMapping(value = "/updateUser")
    public JSONObject updateUser(@RequestBody JSONObject userBody) {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userBody.getString("email");
        String phone = userBody.getString("phone");
        String params[] = new String[]{"password", "email", "phone"};
        for (String param : params) {
            if (userBody.containsKey(param)) {
                if ("username".equals(param)) {
                    if (appUserService.getAAppUserWithParameter(phone) != null) {
                        return ReturnResult.returnTipMessage(0, "该用户名已经被注册!");
                    }
                    appUser.setUsername(userBody.getString(param));

                }
                if ("email".equals(param)) {
                    if (appUserService.getAAppUserWithParameter(email) != null) {
                        return ReturnResult.returnTipMessage(0, "该邮箱已经被注册!");
                    }
                    appUser.setEmail(userBody.getString(param));

                }
                if ("phone".equals(param)) {
                    if (appUserService.getAAppUserWithParameter(email) != null) {
                        return ReturnResult.returnTipMessage(0, "该电话号码已经被注册!");
                    }
                    appUser.setPhone(userBody.getString(param));
                }

            }
        }

        appUser.setEmail(email);
        appUser.setPhone(phone);
        appUserService.save(appUser);
        return ReturnResult.returnTipMessage(1, "用户信息更新成功");

    }

    /**
     * 激活用户
     *
     * @param activeCode username 的Base64
     * @return
     */
    @RequestMapping("/activeUser/{activeCode}")
    public JSONObject activeUser(@PathVariable String activeCode) {
        String username = new String(Base64.getDecoder().decode(activeCode.getBytes()));
        AppUser appUser = appUserService.getAAppUserByUsername(username);
        if (appUser == null) {
            return ReturnResult.returnTipMessage(0, "用户不存在!");
        } else {
            appUser.setEnabled(true);
            appUserService.save(appUser);
            return ReturnResult.returnTipMessage(1, "激活成功!");
        }
    }

    /**
     * 发送激活邮件
     *
     * @param userId
     * @return
     */

    @RequestMapping("/sendActiveMailAgain/{userId}")
    public JSONObject sendActiveMailAgain(@PathVariable Long userId) {

        AppUser appUser = appUserService.findAAppUser(userId);
        if (appUser == null) {
            return ReturnResult.returnTipMessage(0, "用户不存在!");
        } else {
            try {
                emailSender.sendHtmlMail(appUser);
                return ReturnResult.returnTipMessage(1, "邮件发送成功!");
            } catch (Exception e) {
                return ReturnResult.returnTipMessage(0, "邮件发送失败!");
            }
        }
    }

    /**
     * 改密码
     *
     * @param body
     * @return
     */
    @RequestMapping(value = "/changePassword")
    public JSONObject changePassword(@RequestBody JSONObject body) {
        String newPassword = body.getString("newPassword");
        String newPasswordRetry = body.getString("newPasswordRetry");

        if (newPassword == null || newPasswordRetry == null) {
            return ReturnResult.returnTipMessage(0, "参数不全");
        } else if (!newPassword.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$")) {
            return ReturnResult.returnTipMessage(0, "密码必须是6-16位字母数字组合!");
        } else if (!newPassword.equals(newPasswordRetry)) {
            return ReturnResult.returnTipMessage(0, " 两次密码不一样!");
        } else {
            AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            appUser.setPassword(MD5Generator.EncodingMD5(newPassword));
            appUserService.save(appUser);
            return ReturnResult.returnTipMessage(0, " 密码重置成功!");

        }
    }


}
