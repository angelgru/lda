package com.angel.lda.repository.jpa;

import com.angel.lda.model.Hospital;
import com.angel.lda.repository.HospitalRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Angel on 1/13/2018.
 */

@Repository
public interface HospitalJpaRepository extends JpaRepository<Hospital, Integer>, HospitalRepository {
}
