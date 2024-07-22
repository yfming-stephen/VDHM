package com.easylinker.proxy.server.app.dao;

import com.easylinker.proxy.server.app.model.drive.DrivingHealth;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by ruilin on 2019/1/15.
 */
public interface DrivingHealthRepository  extends MongoRepository<DrivingHealth, Long> {
    List<DrivingHealth> findAllByHash(String hash);
    DrivingHealth findTopByHash(String hash);
}
