package com.easylinker.proxy.server.app.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easylinker.proxy.server.app.dao.DailyLogRepository;
import com.easylinker.proxy.server.app.model.daily.DailyLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 日志service
 */
@Service("DailyLogService")
public class DailyLogService {
    @Autowired
    DailyLogRepository dailyLogRepository;

    public void save(DailyLog dailyLog) {
        dailyLogRepository.save(dailyLog);
    }

    /**
     * 分页查询用户日志
     * @param pageable
     * @return
     */
    public JSONObject getAllDailyLogByPage(Pageable pageable){
        Page<DailyLog> dataPage = dailyLogRepository.findAll(pageable);
        JSONArray pageArray = new JSONArray();
        for (DailyLog dailyLog : dataPage.getContent()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", dailyLog.getDate().toString());
            jsonObject.put("event", dailyLog.getEvent());
            jsonObject.put("operate", dailyLog.getOperate());
            jsonObject.put("who", dailyLog.getWho());

            pageArray.add(jsonObject);
        }

        JSONObject pageJson = new JSONObject();
        pageJson.put("page", dataPage.getNumber());
        pageJson.put("total", dataPage.getTotalPages());
        pageJson.put("size", dataPage.getSize());
        pageJson.put("isLast", dataPage.isLast());
        pageJson.put("totalPages", dataPage.getTotalPages());
        pageJson.put("isFirst", dataPage.isFirst());
        pageJson.put("totalElements", dataPage.getTotalElements());
        pageJson.put("data", pageArray);
        return pageJson;
    }




    /**
     * 根据who 查找log
     */

//    public JSONArray getDailyLogsByWho(Long whosId) {
//        Page<DailyLog> page = dailyLogRepository.findAllByWhosId(whosId);
//
//
//    }
}
