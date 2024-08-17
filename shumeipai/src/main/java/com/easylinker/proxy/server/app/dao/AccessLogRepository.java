package com.easylinker.proxy.server.app.dao;

import com.easylinker.proxy.server.app.model.daily.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogRepository extends JpaRepository<AccessLog,Long> {
}
