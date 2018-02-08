package com.angel.lda.repository.tdb;

import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.repository.SensorSyncApplicationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Angel on 2/8/2018.
 */
@Repository
public class SensorSyncApplicationTdbRepository implements SensorSyncApplicationRepository{
    @Override
    public SensorSyncApplication findOne(int id) {
        return null;
    }

    @Override
    public List<SensorSyncApplication> findAll() {
        return null;
    }
}
