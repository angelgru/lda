package com.angel.lda.repository;

import com.angel.lda.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Angel on 1/13/2018.
 */

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Integer>{
}
