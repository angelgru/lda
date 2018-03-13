package com.angel.lda.service;

import com.angel.lda.model.SensorSyncApplication;

import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */
public interface SensorSyncApplicationService {

    public List<SensorSyncApplication> getAllSensorSyncApplications(String email);

    public SensorSyncApplication getSensorSyncApplication(int id, String email);
}
