package com.angel.lda.service;

import com.angel.lda.model.Sensor;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */
public interface SensorService {

    public Sensor createSensor(Sensor sensor) throws IllegalAccessException, InvocationTargetException, InstantiationException;

    public Sensor updateSensor(Sensor sensor, int sensorId) throws IllegalAccessException, InvocationTargetException, InstantiationException;

    public Sensor findSensor(int id) throws IllegalAccessException, InvocationTargetException, InstantiationException;

}
