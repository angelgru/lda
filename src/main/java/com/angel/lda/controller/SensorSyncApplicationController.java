package com.angel.lda.controller;

import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.service.SensorSyncApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by Angel on 1/1/2018.
 */

@RequestMapping("/rest/sensorsyncapplication")
@RestController
public class SensorSyncApplicationController {

    private SensorSyncApplicationService sensorSyncApplicationService;

    @Autowired
    public SensorSyncApplicationController(SensorSyncApplicationService sensorSyncApplicationService) {
        this.sensorSyncApplicationService = sensorSyncApplicationService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SensorSyncApplication> getAllSensorSyncApplications() {
        return sensorSyncApplicationService.getAllSensorSyncApplications();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SensorSyncApplication getSensorSyncApplication(@PathVariable("id") int id) {
        return sensorSyncApplicationService.getSensorSyncApplication(id);
    }
}
