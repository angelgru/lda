package com.angel.lda.service.impl;

import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.repository.SensorSyncApplicationRepository;
import com.angel.lda.service.SensorSyncApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */

@Service
public class SensorSyncApplicationServiceImpl implements SensorSyncApplicationService {

    private SensorSyncApplicationRepository sensorSyncApplicationRepository;

    @Autowired
    public SensorSyncApplicationServiceImpl(SensorSyncApplicationRepository sensorSyncApplicationRepository) {
        this.sensorSyncApplicationRepository = sensorSyncApplicationRepository;
    }

    @Override
    public List<SensorSyncApplication> getAllSensorSyncApplications() {
        return sensorSyncApplicationRepository.findAll();
    }

    @Override
    public SensorSyncApplication getSensorSyncApplication(int id) {
        return sensorSyncApplicationRepository.findOne(id);
    }
}
