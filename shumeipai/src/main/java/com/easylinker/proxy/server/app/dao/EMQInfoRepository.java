package com.easylinker.proxy.server.app.dao;

import com.easylinker.proxy.server.app.model.EMQInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EMQInfoRepository extends JpaRepository<EMQInfo,Long> {
}
