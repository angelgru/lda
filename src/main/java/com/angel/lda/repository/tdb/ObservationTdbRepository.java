package com.angel.lda.repository.tdb;

import com.angel.lda.model.Observation;
import com.angel.lda.repository.ObservationRepository;
import org.apache.jena.query.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
}
