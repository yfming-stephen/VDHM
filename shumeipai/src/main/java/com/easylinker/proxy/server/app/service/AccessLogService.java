package com.easylinker.proxy.server.app.service;

import com.easylinker.proxy.server.app.dao.AccessLogRepository;
import com.easylinker.proxy.server.app.model.daily.AccessLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("AccessLogService")
public class AccessLogService {
    @Autowired
    AccessLogRepository accessLogRepository;


    public void save(AccessLog accessLog) {
        accessLogRepository.save(accessLog);
    }
}
