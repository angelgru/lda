package com.angel.lda.service;

import com.angel.lda.model.Observation;

import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */

public interface ObservationService {

    public Observation createObservation(Observation observation, int sensorId) ;

    public void deleteObservation(int observationId);
}
