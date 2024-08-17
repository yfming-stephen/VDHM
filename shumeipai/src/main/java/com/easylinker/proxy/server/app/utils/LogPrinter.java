package com.easylinker.proxy.server.app.utils;

import com.easylinker.proxy.server.app.model.daily.DailyLog;
import com.easylinker.proxy.server.app.service.DailyLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class LogPrinter {
    private static final Logger logger = LoggerFactory.getLogger(LogPrinter.class);
    @Autowired
    DailyLogService dailyLogService;


//    SimpleDateFormat bjSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     // 北京

    public void log(String event, String operate, String who) {
//        bjSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));  // 设置北京时区
        Date date = new Date();  // 对应的北京时间是2017-08-24 11:17:10
//        System.out.println(date.toString());
        DailyLog dailyLog = new DailyLog();
        dailyLog.setDate(date);
        dailyLog.setEvent(event);
        dailyLog.setOperate(operate);
        dailyLog.setWho(who);
        dailyLogService.save(dailyLog);
        logger.info("在时间[" + dailyLog.getDate() + "][" + dailyLog.getWho() + "]在事件[" + dailyLog.getEvent() + "]执行了[" + dailyLog.getOperate() + "]操作.");

    }
}
