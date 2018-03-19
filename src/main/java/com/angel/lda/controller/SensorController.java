package com.angel.lda.controller;

import com.angel.lda.model.Sensor;
import com.angel.lda.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Angel on 1/1/2018.
 */

@RequestMapping(value = "/api/sensor")
@RestController
public class SensorController {

    private SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Sensor createSensor(@RequestBody Sensor sensor) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        return sensorService.createSensor(sensor);
    }
}
