package com.angel.lda.service;

import com.angel.lda.model.Hospital;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */
public interface HospitalService {
    public List<Hospital> getAllHospitals() throws MalformedURLException;

    public Hospital findHospitalById(int hospitalId) throws IOException;
}
