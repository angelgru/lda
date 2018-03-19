package com.angel.lda.controller;

import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.model.User;
import com.angel.lda.service.SensorSyncApplicationService;
import com.angel.lda.service.impl.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.List;


/**
 * Created by Angel on 1/1/2018.
 */

@RequestMapping("/api/sensorsyncapplication")
@RestController
public class SensorSyncApplicationController {

    private SensorSyncApplicationService sensorSyncApplicationService;

    @Autowired
    public SensorSyncApplicationController(SensorSyncApplicationService sensorSyncApplicationService) {
        this.sensorSyncApplicationService = sensorSyncApplicationService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SensorSyncApplication> getAllSensorSyncApplications() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        return sensorSyncApplicationService.getAllSensorSyncApplications();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SensorSyncApplication getSensorSyncApplication(@PathVariable("id") int id) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        return sensorSyncApplicationService.getSensorSyncApplication(id);
    }
}
