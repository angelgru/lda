package com.angel.lda.service.impl;

import com.angel.lda.model.Sensor;
import com.angel.lda.model.User;
import com.angel.lda.repository.SensorRepository;
import com.angel.lda.repository.UserRepository;
import com.angel.lda.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */

@Service
public class SensorServiceImpl implements SensorService {

    private SensorRepository sensorRepository;
    private UserRepository userRepository;

    @Autowired
    public SensorServiceImpl(SensorRepository sensorRepository, UserRepository userRepository) {
        this.sensorRepository = sensorRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Sensor createSensor(Sensor sensor, String email) {
        User patient = userRepository.findByEmail(email);
        sensor.setOwner(patient);
        sensorRepository.save(sensor);
        return sensor;
    }

    @Override
    public Sensor updateSensor(Sensor sensor, int sensorId) {
        Sensor sensorToBeUpdated = sensorRepository.findOne(sensorId);
        sensorToBeUpdated.setRegularFrom(sensor.getRegularFrom());
        sensorToBeUpdated.setRegularTo(sensor.getRegularTo());
        sensorToBeUpdated.setType(sensor.getType());
        sensorToBeUpdated.setUnit(sensor.getUnit());
        sensorRepository.save(sensorToBeUpdated);
        return sensorToBeUpdated;
    }

    @Override
    public void deleteSensor(int sensorId) {
        Sensor sensorToBeDeleted = sensorRepository.findOne(sensorId);
        sensorRepository.delete(sensorToBeDeleted);
    }
}
