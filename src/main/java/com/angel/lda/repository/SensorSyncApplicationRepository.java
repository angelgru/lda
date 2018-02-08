package com.angel.lda.repository;

import com.angel.lda.model.SensorSyncApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Angel on 1/13/2018.
 */

@Repository
public interface SensorSyncApplicationRepository{
    SensorSyncApplication findOne(int id);

    List<SensorSyncApplication> findAll();
}
