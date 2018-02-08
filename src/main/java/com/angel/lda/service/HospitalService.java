package com.angel.lda.service;

import com.angel.lda.model.Hospital;

import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */
public interface HospitalService {
    public List<Hospital> getAllHospitals();

    public Hospital findHospitalById(int hospitalId);
}
