package com.angel.lda.service;

import com.angel.lda.model.SensorSyncApplication;

import java.io.IOException;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */
public interface SensorSyncApplicationService {

    List<SensorSyncApplication> getAllSensorSyncApplications() throws IOException;

    SensorSyncApplication getSensorSyncApplication(int id) throws IOException;
}
