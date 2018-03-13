package com.angel.lda.service;

import com.angel.lda.model.Sensor;

import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */
public interface SensorService {

    public Sensor createSensor(Sensor sensor, String email);

    public Sensor updateSensor(Sensor sensor, int sensorId);

    public void deleteSensor(int sensorId);
}
