package com.angel.lda.controller;

import com.angel.lda.model.Treatment;
import com.angel.lda.model.User;
import com.angel.lda.service.TreatmentService;
import com.angel.lda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */

@RequestMapping("/api/treatment")
@RestController
public class TreatmentController {

    private TreatmentService treatmentService;
    private UserService userService;

    @Autowired
    public TreatmentController(TreatmentService treatmentService, UserService userService) {
        this.treatmentService = treatmentService;
        this.userService = userService;
    }

//    Ги зимаме сите treatments кои се слободни (ниеден доктор не ги прифатил)
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Treatment> getAllNonTakenTreatments() throws IOException {
        return treatmentService.getAllNonTakenTreatments();
    }

    // Враќаме treatment од кој ги прикажуваме деталите за истото кога некој доктор ќе го прифати treatment-от
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Treatment getTreatment(@PathVariable("id") int id) {
        return treatmentService.getTreatment(id);
    }

//    Враќаме treatment-и прифатени од доктор за кои сеуште нема поставено дијагноза
    @RequestMapping(value = "/accepted", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Treatment> getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return treatmentService.getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor(user);
    }

    @RequestMapping(value = "/accepted/locked", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Treatment> getLockedTreatmentsAcceptedByCurrentlyLoggedInDoctor(Principal principal){
        User user = userService.findByEmail(principal.getName());
        return treatmentService.getLockedTreatmentsAcceptedByCurrentlyLoggedInDoctor(user);
    }

//    Пациентот бара нов treatment
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Treatment createTreatment(@RequestBody Treatment treatment, Principal principal) {
        return treatmentService.createTreatment(treatment, principal.getName());
    }

//    Кога докторот ќе прифати treatment правиме апдејт на тој treatment во базата, или поставуваме дијагноза
//    ( сервисот прави проверка дали сме испратиле дијагноза во request body, ако сме испратиле ја запишува во база, или
//    се запишува кој доктор го земал тој treatment
    @RequestMapping(value = "/{treatmentId}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public Treatment updateTreatment(@RequestBody Treatment treatment, @PathVariable("treatmentId") int treatmentId, Principal principal, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        return treatmentService.updateTreatment(treatment, treatmentId, principal.getName(), ipAddress);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteTreatment(@PathVariable("id") int id) {
        treatmentService.deleteTreatment(id);
    }
}
