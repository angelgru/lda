package com.angel.lda.repository;

import com.angel.lda.model.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Angel on 1/13/2018.
 */

@Repository
public interface ObservationRepository {
    Observation save(Observation observation);

    Observation findOne(int observationId);

    void delete(Observation observation);
}
