package com.angel.lda.controller;

import com.angel.lda.model.Observation;
import com.angel.lda.model.Sensor;
import com.angel.lda.service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */

@RequestMapping("/api/observation")
@RestController
public class ObservationController {

    private ObservationService observationService;

    @Autowired
    public ObservationController(ObservationService observationService) {
        this.observationService = observationService;
    }

    @RequestMapping(value = "/{sensorId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Observation createObservation(@RequestBody Observation observation, @PathVariable("sensorId") int sensorId) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return observationService.createObservation(observation, sensorId);
    }
}
