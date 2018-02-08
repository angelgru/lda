package com.angel.lda.repository.tdb;

import com.angel.lda.model.Observation;
import com.angel.lda.repository.ObservationRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Angel on 2/8/2018.
 */
@Repository
public class ObservationTdbRepository implements ObservationRepository {
    @Override
    public Observation save(Observation observation) {
        return null;
    }

    @Override
    public Observation findOne(int observationId) {
        return null;
    }

    @Override
    public void delete(Observation observation) {

    }
}
