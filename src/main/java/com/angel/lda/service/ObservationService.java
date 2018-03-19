package com.angel.lda.service;

import com.angel.lda.model.Observation;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Angel on 1/1/2018.
 */

public interface ObservationService {

    Observation createObservation(Observation observation, int sensorId) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException;

    Observation findOne(int observationId) throws InvocationTargetException, IOException, InstantiationException, IllegalAccessException;
}
