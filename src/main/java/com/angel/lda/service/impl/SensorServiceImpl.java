package com.angel.lda.service.impl;

import com.angel.lda.exceptions.ResourceNotAllowed;
import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Sensor;
import com.angel.lda.model.User;
import com.angel.lda.repository.SensorRepository;
import com.angel.lda.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    public SensorServiceImpl(SensorRepository sensorRepository, AuthenticationService authenticationService, UserRepository userRepository) {
        this.sensorRepository = sensorRepository;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
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

    @Override
    public Sensor findSensor(int id) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Sensor sensor = sensorRepository.findOne(id);
        String tdbUserId = sensor.getTdbUserId();

        if(tdbUserId != null)
            tdbUserId = sensor.getTdbUserId().substring(19);

        User sensorOwner;
        sensorOwner = userRepository.findByEmail(tdbUserId);
        if(sensorOwner != null)
            sensor.setOwner(sensorOwner);
        if(sensor == null)
            throw new ResourceNotFound("Sensor doesn't exist");
        return sensor;
    }
}
