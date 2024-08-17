package com.easylinker.proxy.server.app.service;

import com.easylinker.proxy.server.app.dao.EMQInfoRepository;
import com.easylinker.proxy.server.app.model.EMQInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EMQInfoService {
    @Autowired
    EMQInfoRepository emqInfoRepository;

    public void save(EMQInfo emqInfo) {
        emqInfoRepository.save(emqInfo);
    }

    public EMQInfo getEMQInfo() {
        return emqInfoRepository.getOne(1L);
    }
}
