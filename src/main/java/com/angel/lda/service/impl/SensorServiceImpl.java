package com.angel.lda.service.impl;

import com.angel.lda.model.Sensor;
import com.angel.lda.model.User;
import com.angel.lda.repository.SensorRepository;
import com.angel.lda.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Angel on 1/1/2018.
 */

@Service
public class SensorServiceImpl implements SensorService {

    private SensorRepository sensorRepository;
    private AuthenticationService authenticationService;
    @Autowired
    public SensorServiceImpl(SensorRepository sensorRepository, AuthenticationService authenticationService) {
        this.sensorRepository = sensorRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public Sensor createSensor(Sensor sensor) throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException {
        sensorRepository.save(sensor);
        User owner = authenticationService.getAuthenticatedUser();
        sensor.setOwner(owner);
        return sensor;
    }

    @Override
    public Sensor findSensor(int id) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        return sensorRepository.findOne(id);
    }
}
