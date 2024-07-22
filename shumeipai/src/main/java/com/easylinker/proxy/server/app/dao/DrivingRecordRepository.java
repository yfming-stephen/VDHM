package com.easylinker.proxy.server.app.dao;

import com.easylinker.proxy.server.app.model.drive.DrivingRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by ruilin on 2019/1/12.
 */
public interface DrivingRecordRepository extends MongoRepository<DrivingRecord, Long> {
    List<DrivingRecord> findAllByHash(String hash);
    DrivingRecord findTopByHash(String hash);
}
