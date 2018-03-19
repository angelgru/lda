package com.angel.lda.service.impl;

import com.angel.lda.accesscontrolmethods.AccessControl;
import com.angel.lda.exceptions.ResourceNotAllowed;
import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Treatment;
import com.angel.lda.model.User;
import com.angel.lda.repository.TreatmentRepository;
import com.angel.lda.service.TreatmentService;
import com.angel.lda.service.UserService;
import org.apache.jena.ext.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Angel on 1/1/2018.
 */

@Service
public class TreatmentServiceImpl implements TreatmentService{

    private TreatmentRepository treatmentRepository;
    private AccessControl accessControl;
    private AuthenticationService authenticationService;
    private UserService userService;

    Logger logger = Logger.getLogger("LOG");

    @Autowired
    public TreatmentServiceImpl(TreatmentRepository treatmentRepository, AccessControl accessControl, AuthenticationService authenticationService, UserService userService) {
        this.treatmentRepository = treatmentRepository;
        this.accessControl = accessControl;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @Override
    public List<Treatment> getAllNonTakenTreatments() throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException, ParseException {
        User currentlyLoggedInUser = authenticationService.getAuthenticatedUser();
        if(accessControl.canTakeTreatments(currentlyLoggedInUser)) {
            List<Treatment> treatments = treatmentRepository.getAllNonTakenTreatments();

            return treatments;
        }
        throw new ResourceNotAllowed("Only doctors can access the treatments!");
    }

    @Override
    public List<Treatment> getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor() throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        List<Treatment> treatments = treatmentRepository.getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor(authenticationService.getAuthenticatedUser());
        return treatments;
    }

    @Override
    public List<Treatment> getCompletedTreatmentsAcceptedByCurrentlyLoggedInDoctor() throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        List<Treatment> treatments = treatmentRepository.getCompletedTreatmentsAcceptedByCurrentlyLoggedInDoctor(authenticationService.getAuthenticatedUser());
        return treatments;
    }

    @Override
    public Treatment getTreatment(int id) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        Treatment treatment = treatmentRepository.getTreatmentById(authenticationService.getAuthenticatedUser(), id);
        if(treatment == null){
            throw new ResourceNotFound("Treatment doesn't exist");
        }
        return treatment;
    }

    @Override
    public List<Treatment> createTreatment(Treatment treatment) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        Treatment newTreatment = new Treatment();
        newTreatment.setPatientRequest(treatment.getPatientRequest());
        newTreatment.setFrom(new Date());
        newTreatment.setId(-1);
        User patient = authenticationService.getAuthenticatedUser();
        newTreatment.setForPatient(patient);

        return Lists.newArrayList(treatmentRepository.save(newTreatment));
    }

    @Override
    public List<Treatment> updateTreatment(Treatment treatment, int treatmentId, String doctorIPAddress) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException {

//      Проверува дали моментално логираниот корисник кој ја повикува оваа функција има улога доктор
//      Ако не е доктор се фрла exception
        if(!accessControl.canTakeTreatments(authenticationService.getAuthenticatedUser())){
            throw new ResourceNotAllowed("Only doctors can update treatments!!!");
        }

        Treatment updateTreatment = treatmentRepository.getTreatmentById(authenticationService.getAuthenticatedUser(), treatmentId);
        updateTreatment.setId(treatmentId);

//        Ако испратениот treatment има поставено дијагноза, направи update, инаку додели го тој treatment на логираниот доктор
        if(treatment.getDiagnosis() != null){
            if(accessControl.isTreatmentTakenByDoctor(updateTreatment, authenticationService.getAuthenticatedUser())) {
                if(accessControl.D1(doctorIPAddress, authenticationService.getAuthenticatedUser())){
                    updateTreatment.setTo(new Date());
                    updateTreatment.setDiagnosis(treatment.getDiagnosis());
                    treatmentRepository.save(updateTreatment);
                    return Lists.newArrayList(updateTreatment);
                }else{
                    throw new ResourceNotAllowed("You can modify this treatment only from the hospital network during office hours!");
                }
            } else{
                throw new ResourceNotAllowed("This treatment was claimed by another doctor and you are not allowed to set diagnosis!");
            }
        }else if(updateTreatment.getHasDoctor() == null) {
            User doctor = authenticationService.getAuthenticatedUser();
            updateTreatment.setHasDoctor(doctor);
            treatmentRepository.save(updateTreatment);
            return Lists.newArrayList(updateTreatment);
        } else
            throw new ResourceNotAllowed("This treatment was claimed by another doctor!");
    }
}
