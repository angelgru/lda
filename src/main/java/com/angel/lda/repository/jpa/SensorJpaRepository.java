package com.angel.lda.repository.jpa;

import com.angel.lda.model.Sensor;
import com.angel.lda.repository.SensorRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Angel on 2/8/2018.
 */
@Repository
@Profile("jpa")
public interface SensorJpaRepository extends JpaRepository<Sensor, Integer>, SensorRepository {
}
