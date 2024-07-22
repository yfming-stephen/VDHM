package com.easylinker.proxy.server.app.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.dao.AppUserRepository;
import com.easylinker.proxy.server.app.dao.UserRoleRepository;
import com.easylinker.proxy.server.app.model.user.AppUser;
import com.easylinker.proxy.server.app.model.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "AppUserService")
public class AppUserService {
    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    UserRoleRepository userRoleRepository;
    /**
     * @param parameter 表示 UsernameOrEmailOrPhone，三种字段都可以登陆
     * @return 返回一个User Info
     */
    public AppUser getAAppUserWithParameter(String parameter) {
        return appUserRepository.findTopByUsernameOrEmailOrPhone(parameter, parameter, parameter);
    }

    public void save(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    public AppUser getAAppUserByUsername(String username) {
        return appUserRepository.findTopByUsername(username);
    }

    public AppUser findAAppUser(Long id) {
        return appUserRepository.findTopById(id);
    }

    /**
     * 获取所有的数据
     *
     * @return
     */
    public JSONArray getAllUsers() {
        JSONArray data = new JSONArray();
        List<AppUser> appUsers = appUserRepository.findAll();
        for (AppUser appUser : appUsers) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", appUser.getId());
            jsonObject.put("username", appUser.getUsername());
            jsonObject.put("email", appUser.getEmail());
            jsonObject.put("phone", appUser.getPhone());
            jsonObject.put("avatar", appUser.getAvatar());
            jsonObject.put("isEnable", appUser.isEnabled());
            data.add(jsonObject);
        }
        return data;
    }

    /**
     * 页码获取所有的数据
     *
     * @return
     */
    public JSONObject getAllUsersByPage(Pageable pageable) {
        JSONArray data = new JSONArray();
        Page<AppUser> dataPage = appUserRepository.findAll(pageable);
        JSONObject pageJson = new JSONObject();
        pageJson.put("page", dataPage.getNumber());
        pageJson.put("totalElements", dataPage.getTotalElements());
        pageJson.put("totalPages", dataPage.getTotalPages());
        pageJson.put("size", dataPage.getSize());
        pageJson.put("isLast", dataPage.isLast());
        pageJson.put("isFirst", dataPage.isFirst());
        for (AppUser appUser : dataPage.getContent()) {
            JSONObject jsonObject = new JSONObject();
            List<UserRole> list = userRoleRepository.findAllByAppUser(appUser);
            ArrayList arrayList = new ArrayList();
            for (UserRole userrole:list
                 ) {
                arrayList.add(userrole.getRole());
            }
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(arrayList);
            jsonObject.put("role",jsonArray);
            jsonObject.put("id", appUser.getId());
            jsonObject.put("avatar", appUser.getAvatar());
            jsonObject.put("username", appUser.getUsername());
            jsonObject.put("email", appUser.getEmail());
            jsonObject.put("phone", appUser.getPhone());
            jsonObject.put("isEnable", appUser.isEnabled());
            data.add(jsonObject);
        }
        pageJson.put("data", data);
        return pageJson;
    }


    /**
     * 通过ID查找用户
     */

    public AppUser getAUserById(Long id) {
        return appUserRepository.findTopById(id);
    }

    /**
     * 通过openId查找用户 wechat
     * @param openId
     * @return
     */
    public AppUser getAUserByOpenId(String openId){
        return appUserRepository.findTopByOpenId(openId);
    }

}
