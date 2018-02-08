package com.angel.lda.repository.tdb;

import com.angel.lda.model.Treatment;
import com.angel.lda.model.User;
import com.angel.lda.repository.TreatmentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Angel on 2/8/2018.
 */
@Repository
public class TreatmentTdbRepository implements TreatmentRepository {
    @Override
    public List<Treatment> getAllNonTakenTreatments() {
        return null;
    }

    @Override
    public List<Treatment> getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor(User user) {
        return null;
    }

    @Override
    public List<Treatment> getCompletedTreatmentsAcceptedByCurrentlyLoggedInDoctor(User user) {
        return null;
    }

    @Override
    public Treatment getTreatmentById(User user, int id) {
        return null;
    }

    @Override
    public Treatment findOne(int id) {
        return null;
    }

    @Override
    public Treatment save(Treatment newTreatment) {
        return null;
    }

    @Override
    public void delete(int id) {

    }
}
