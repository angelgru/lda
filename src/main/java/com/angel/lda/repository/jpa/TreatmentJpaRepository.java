package com.angel.lda.repository.jpa;

import com.angel.lda.model.Treatment;
import com.angel.lda.model.User;
import com.angel.lda.repository.TreatmentRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Angel on 2/8/2018.
 */

@Repository
@Profile("jpa")
public interface TreatmentJpaRepository extends JpaRepository<Treatment, Integer>, TreatmentRepository {
}
