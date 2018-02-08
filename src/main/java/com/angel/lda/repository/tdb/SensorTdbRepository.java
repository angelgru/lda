package com.angel.lda.repository.tdb;

import com.angel.lda.model.Sensor;
import com.angel.lda.repository.SensorRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Angel on 2/8/2018.
 */
@Repository
public class SensorTdbRepository implements SensorRepository {
    @Override
    public Sensor save(Sensor sensor) {
        return null;
    }

    @Override
    public Sensor findOne(int sensorId) {
        return null;
    }

    @Override
    public void delete(Sensor sensorToBeDeleted) {

    }
}
