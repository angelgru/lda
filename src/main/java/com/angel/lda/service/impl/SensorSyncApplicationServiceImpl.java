package com.angel.lda.service.impl;

import com.angel.lda.accesscontrolmethods.AccessControl;
import com.angel.lda.exceptions.ResourceNotAllowed;
import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.model.User;
import com.angel.lda.repository.SensorSyncApplicationRepository;
import com.angel.lda.service.SensorSyncApplicationService;
import com.angel.lda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */

@Service
public class SensorSyncApplicationServiceImpl implements SensorSyncApplicationService {

    private SensorSyncApplicationRepository sensorSyncApplicationRepository;
    private UserService userService;
    private AccessControl accessControl;
    private AuthenticationService authenticationService;

    @Autowired
    public SensorSyncApplicationServiceImpl(@Qualifier("sensorSyncApplicationTdbRepository") SensorSyncApplicationRepository sensorSyncApplicationRepository, UserService userService, AccessControl accessControl, AuthenticationService authenticationService) {
        this.sensorSyncApplicationRepository = sensorSyncApplicationRepository;
        this.userService = userService;
        this.accessControl = accessControl;
        this.authenticationService = authenticationService;
    }

    @Override
    public List<SensorSyncApplication> getAllSensorSyncApplications() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        User currentlyLoggedinUser = authenticationService.getAuthenticatedUser();

        if(accessControl.canAccessSensorSyncApplication(currentlyLoggedinUser)){
            List<SensorSyncApplication> sensorSyncApplications = sensorSyncApplicationRepository.findAll();
            return accessControl.TS1(sensorSyncApplications, currentlyLoggedinUser.worksAtHospital.getId());
        }

        throw new ResourceNotAllowed("You are not allowed access to the Sensor Sync Application");
    }

    @Override
    public SensorSyncApplication getSensorSyncApplication(int id) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        User currentlyLoggedinUser = authenticationService.getAuthenticatedUser();
        if(accessControl.canAccessSensorSyncApplication(currentlyLoggedinUser)) {
            return sensorSyncApplicationRepository.findOne(id);
        }

        throw new ResourceNotAllowed("You are not allowed access to the Sensor Sync Application");
    }
}
