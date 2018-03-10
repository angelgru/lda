package com.angel.lda.repository.jpa;

import com.angel.lda.model.Hospital;
import com.angel.lda.repository.HospitalRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Angel on 1/13/2018.
 */

@Repository
@Profile("jpa")
public interface HospitalJpaRepository extends JpaRepository<Hospital, Integer>, HospitalRepository {
}
