package com.angel.lda.service.impl;

import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.repository.SensorSyncApplicationRepository;
import com.angel.lda.service.SensorSyncApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    public List<SensorSyncApplication> getAllSensorSyncApplications() throws IOException {
        return sensorSyncApplicationRepository.findAll();
    }

    @Override
    public SensorSyncApplication getSensorSyncApplication(int id) throws IOException {
        return sensorSyncApplicationRepository.findOne(id);
    }
}
