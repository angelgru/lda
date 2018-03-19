package com.angel.lda.controller;

import com.angel.lda.model.Hospital;
import com.angel.lda.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */

@RequestMapping("/api/hospital")
@RestController
public class HospitalController {

    private HospitalService hospitalService;

    @Autowired
    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Hospital> getAllHospitals() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        return hospitalService.getAllHospitals();
    }

    @RequestMapping(value = "/{hospitalId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Hospital findHospitalById(@PathVariable("hospitalId") int hospitalId) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        return hospitalService.findHospitalById(hospitalId);
    }
}
