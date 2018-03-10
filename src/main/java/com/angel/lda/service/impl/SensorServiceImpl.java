package com.angel.lda.service.impl;

import com.angel.lda.exceptions.ResourceNotAllowed;
import com.angel.lda.model.Sensor;
import com.angel.lda.model.User;
import com.angel.lda.repository.SensorRepository;
import com.angel.lda.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Sensor createSensor(Sensor sensor) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        User patient = authenticationService.getAuthenticatedUser();
        if(patient != null){
            sensor.setOwner(patient);
            sensor.setId(-1);
            sensorRepository.save(sensor);
        }
        return sensor;
    }

    @Override
    public Sensor updateSensor(Sensor sensor, int sensorId) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Sensor updateSensor = sensorRepository.findOne(sensorId);

        if(updateSensor.getOwner().getEmail().equals(authenticationService.getAuthenticatedUser().getEmail())){
            updateSensor.setId(sensorId);
            if(sensor.getRegularFrom() != 0)
                updateSensor.setRegularFrom(sensor.getRegularFrom());
            if(sensor.getRegularTo() != 0)
                updateSensor.setRegularTo(sensor.getRegularTo());
            if(sensor.getType() != null)
                updateSensor.setType(sensor.getType());
            if(sensor.getUnit() != null)
                updateSensor.setUnit(sensor.getUnit());
            sensorRepository.save(updateSensor);
            return updateSensor;
        }

        throw new ResourceNotAllowed("You are not allowed to modify this resource!");
    }
}
