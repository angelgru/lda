package com.angel.lda.service.impl;

import com.angel.lda.model.Observation;
import com.angel.lda.model.Sensor;
import com.angel.lda.repository.ObservationRepository;
import com.angel.lda.repository.SensorRepository;
import com.angel.lda.service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * Created by Angel on 1/1/2018.
 */

@Service
public class ObservationServiceImpl implements ObservationService{

    private ObservationRepository observationRepository;
    private SensorRepository sensorRepository;

    @Autowired
    public ObservationServiceImpl(ObservationRepository observationRepository, SensorRepository sensorRepository) {
        this.observationRepository = observationRepository;
        this.sensorRepository = sensorRepository;
    }

    @Override
    public Observation createObservation(Observation observation, int sensorId) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        Sensor sensor = sensorRepository.findOne(sensorId);
        observation.setTime(new Date());
        observation.setSensor(sensor);
        observationRepository.save(observation);
        return observation;
    }

    @Override
    public Observation findOne(int observationId) throws InvocationTargetException, IOException, InstantiationException, IllegalAccessException {
        return observationRepository.findOne(observationId);
    }
}
