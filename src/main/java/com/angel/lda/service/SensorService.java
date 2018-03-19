package com.angel.lda.service;

import com.angel.lda.model.Sensor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */
public interface SensorService {

    Sensor createSensor(Sensor sensor) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException;

    Sensor findSensor(int id) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException;

}
