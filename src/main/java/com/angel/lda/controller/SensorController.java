package com.angel.lda.controller;

import com.angel.lda.model.Sensor;
import com.angel.lda.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.List;

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
    public Sensor createSensor(@RequestBody Sensor sensor) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return sensorService.createSensor(sensor);
    }

    @RequestMapping(value = "/{sensorId}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public Sensor updateSensor(@RequestBody Sensor sensor, @PathVariable("sensorId") int sensorId) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return sensorService.updateSensor(sensor, sensorId);
    }
}
