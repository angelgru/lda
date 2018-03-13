package com.angel.lda.service.impl;

import com.angel.lda.AccessControlMethods.AccessControl;
import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.model.User;
import com.angel.lda.repository.SensorSyncApplicationRepository;
import com.angel.lda.service.SensorSyncApplicationService;
import com.angel.lda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */

@Service
public class SensorSyncApplicationServiceImpl implements SensorSyncApplicationService {

    private SensorSyncApplicationRepository sensorSyncApplicationRepository;
    private UserService userService;
    private AccessControl accessControl;

    @Autowired
    public SensorSyncApplicationServiceImpl(SensorSyncApplicationRepository sensorSyncApplicationRepository, UserService userService, AccessControl accessControl) {
        this.sensorSyncApplicationRepository = sensorSyncApplicationRepository;
        this.userService = userService;
        this.accessControl = accessControl;
    }

    @Override
    public List<SensorSyncApplication> getAllSensorSyncApplications(String email) {
        User tmpUser = userService.findByEmail(email);
        List<SensorSyncApplication> sensorSyncApplications = sensorSyncApplicationRepository.findAll();
        return accessControl.TS1(sensorSyncApplications, tmpUser.worksAtHospital.getId());
    }

    @Override
    public SensorSyncApplication getSensorSyncApplication(int id, String email) {
        return sensorSyncApplicationRepository.findOne(id);
    }
}
