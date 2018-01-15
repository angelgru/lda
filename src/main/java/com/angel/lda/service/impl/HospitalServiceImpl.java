package com.angel.lda.service.impl;

import com.angel.lda.model.Hospital;
import com.angel.lda.helperModel.General;
import com.angel.lda.helperModel.Solution;
import com.angel.lda.repository.HospitalRepository;
import com.angel.lda.service.HospitalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
    public List<Hospital> getAllHospitals() throws MalformedURLException {
        return hospitalRepository.findAll();
    }

    @Override
    public Hospital findHospitalById(int hospitalId) throws IOException {
        return hospitalRepository.findOne(hospitalId);
    }
}
