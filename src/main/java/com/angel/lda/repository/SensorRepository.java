package com.angel.lda.repository;

import com.angel.lda.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Angel on 1/13/2018.
 */

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer>{
}
