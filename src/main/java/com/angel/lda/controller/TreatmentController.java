package com.angel.lda.controller;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Treatment;
import com.angel.lda.service.TreatmentService;
import com.angel.lda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
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
    public List<Treatment> getAllNonTakenTreatments() throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException, ParseException {
        return treatmentService.getAllNonTakenTreatments();
    }

    // Враќаме treatment за кој ги прикажуваме деталите за истиот кога некој доктор ќе го прифати treatment-от
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Treatment getTreatment(@PathVariable("id") int id) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        return treatmentService.getTreatment(id);
    }

//    Враќаме treatment-и прифатени од доктор за кои сеуште нема поставено дијагноза
    @RequestMapping(value = "/accepted", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Treatment> getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        List<Treatment> treatments = treatmentService.getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor();
        if(treatments.isEmpty()){
            throw new ResourceNotFound("There are no treatments accepted by this user!");
        }
        return treatments;
    }

//    Treatments за кој е поставена дијагноза, од базата ги враќа само оние кои припаѓаат на моментално логираниот корисник
    @RequestMapping(value = "/completed", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Treatment> getCompletedTreatmentsAcceptedByCurrentlyLoggedInDoctor() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        List<Treatment> treatments = treatmentService.getCompletedTreatmentsAcceptedByCurrentlyLoggedInDoctor();
        if(treatments.isEmpty()){
            throw new ResourceNotFound("There are no treatments completed by this doctor");
        }
        return treatments;
    }

//    Пациентот бара нов treatment
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void createTreatment(@RequestBody Treatment treatment) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        treatmentService.createTreatment(treatment);
    }

//    Кога докторот ќе прифати treatment, го доделувам treatment-от на доктор-от или поставуваме дијагноза
//    за treatment-от ако е проследена истата
    @RequestMapping(value = "/{treatmentId}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Treatment> updateTreatment(@RequestBody Treatment treatment, @PathVariable("treatmentId") int treatmentId, HttpServletRequest request) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        String ipAddress = request.getRemoteAddr();
        return treatmentService.updateTreatment(treatment, treatmentId, ipAddress);
    }
}
