package com.angel.lda.controller;

import com.angel.lda.accesscontrolmethods.AccessControl;
import com.angel.lda.exceptions.ResourceNotAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Angel on 1/29/2018.
 */

@RestController
@RequestMapping("/api/general")
public class GeneralController {

    @Autowired
    private AccessControl accessControl;

    @RequestMapping(method = RequestMethod.GET, value = "/generateReport", produces = MediaType.APPLICATION_JSON_VALUE)
    public String generateReport(){
        if(accessControl.SU1()) {
            return "Dummy report generated from user ben";
        }

        throw  new ResourceNotAllowed("You are not permitted to generate reports");
    }
}
