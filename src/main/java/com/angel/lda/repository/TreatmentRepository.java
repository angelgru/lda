package com.angel.lda.repository;

import com.angel.lda.model.Treatment;
import com.angel.lda.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Angel on 1/13/2018.
 */

@Repository
public interface TreatmentRepository{
    
    @Query("select t from Treatment as t where t.hasDoctor is null")
    List<Treatment> getAllNonTakenTreatments() throws IllegalAccessException, InstantiationException, InvocationTargetException, ParseException, IOException;

    @Query("select t from Treatment as t where t.hasDoctor = :doctor and t.diagnosis is null ")
    List<Treatment> getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor(@Param("doctor")User user) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException;

    @Query("select t from Treatment as t where t.diagnosis is not null and t.hasDoctor = :doctor")
    List<Treatment> getCompletedTreatmentsAcceptedByCurrentlyLoggedInDoctor(@Param("doctor") User user) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException;

    @Query("select t from Treatment as t where t.hasDoctor = :doctor and t.id =:id")
    Treatment getTreatmentById(@Param("doctor") User user, @Param("id") int id) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException;

    Treatment findOne(int id) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException;

    Treatment save(Treatment newTreatment);
}
