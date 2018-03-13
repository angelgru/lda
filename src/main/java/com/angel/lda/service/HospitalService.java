package com.angel.lda.service;

import com.angel.lda.model.Hospital;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */
public interface HospitalService {
    public List<Hospital> getAllHospitals() throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException;

    public Hospital findHospitalById(int hospitalId) throws IllegalAccessException, InvocationTargetException, InstantiationException;
}
