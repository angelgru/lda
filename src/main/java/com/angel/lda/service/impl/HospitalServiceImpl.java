package com.angel.lda.service.impl;

import com.angel.lda.model.Hospital;
import com.angel.lda.repository.HospitalRepository;
import com.angel.lda.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */

@Service
public class HospitalServiceImpl implements HospitalService{

    private HospitalRepository hospitalRepository;

    @Autowired
    public HospitalServiceImpl(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public List<Hospital> getAllHospitals() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return hospitalRepository.findAll();
    }

    @Override
    public Hospital findHospitalById(int hospitalId) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return hospitalRepository.findOne(hospitalId);
    }
}
