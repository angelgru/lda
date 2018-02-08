package com.angel.lda.repository.jpa;

import com.angel.lda.model.Observation;
import com.angel.lda.repository.ObservationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Angel on 2/8/2018.
 */
@Repository
public interface ObservationJpaRepository extends JpaRepository<Observation, Integer>, ObservationRepository {
}
