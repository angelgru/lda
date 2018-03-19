package com.angel.lda.repository;

import com.angel.lda.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Angel on 1/13/2018.
 */

@Repository
public interface SensorRepository{
    Sensor save(Sensor sensor) throws IOException;

    Sensor findOne(int sensorId) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException;
}
