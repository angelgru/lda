package com.angel.lda.repository;

import com.angel.lda.model.Treatment;
import com.angel.lda.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Angel on 1/13/2018.
 */

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Integer>{

    @Query("select t from Treatment as t where t.hasDoctor is null")
    List<Treatment> getAllNonTakenTreatments();

    @Query("select t from Treatment as t where t.hasDoctor = :doctor and t.diagnosis is null ")
    List<Treatment> getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor(@Param("doctor")User user);

    @Query("select t from Treatment as t where t.diagnosis is not null and t.hasDoctor = :doctor")
    List<Treatment> getLockedTreatmentsAcceptedByCurrentlyLoggedInDoctor(@Param("doctor") User user);
}
