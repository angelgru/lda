package com.angel.lda.repository;

import com.angel.lda.model.Hospital;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Angel on 1/13/2018.
 */

@Repository
public interface HospitalRepository {

  List<Hospital> findAll();

  Hospital findOne(int hospitalId);

}
