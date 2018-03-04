package com.angel.lda.service.impl;

import com.angel.lda.accesscontrolmethods.AccessControl;
import com.angel.lda.exceptions.ResourceNotAllowed;
import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.exceptions.UserValuesExposed;
import com.angel.lda.model.Treatment;
import com.angel.lda.model.User;
import com.angel.lda.repository.TreatmentRepository;
import com.angel.lda.repository.UserRepository;
import com.angel.lda.service.TreatmentService;
import com.angel.lda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */

@Service
public class TreatmentServiceImpl implements TreatmentService{

    private TreatmentRepository treatmentRepository;
    private UserRepository userRepository;
    private AccessControl accessControl;
    private AuthenticationService authenticationService;
    private UserService userService;

    @Autowired
    public TreatmentServiceImpl(@Qualifier("treatmentTdbRepository") TreatmentRepository treatmentRepository, @Qualifier("userTdbRepository") UserRepository userRepository, AccessControl accessControl, AuthenticationService authenticationService, UserService userService) {
        this.treatmentRepository = treatmentRepository;
        this.userRepository = userRepository;
        this.accessControl = accessControl;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @Override
    public List<Treatment> getAllNonTakenTreatments() throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException, ParseException {
        User currentlyLoggedInUser = authenticationService.getAuthenticatedUser();
        if(accessControl.canTakeTreatments(currentlyLoggedInUser)) {
            List<Treatment> treatments = treatmentRepository.getAllNonTakenTreatments();
            treatments = getTreatmentsReady(treatments);
            return treatments;
        }
        throw new ResourceNotAllowed("Only doctors can access the treatments!");
    }

    @Override
    public List<Treatment> getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Treatment> treatments = treatmentRepository.getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor(authenticationService.getAuthenticatedUser());
        return getTreatmentsReady(treatments);
    }

    @Override
    public List<Treatment> getCompletedTreatmentsAcceptedByCurrentlyLoggedInDoctor() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Treatment> treatments = treatmentRepository.getCompletedTreatmentsAcceptedByCurrentlyLoggedInDoctor(authenticationService.getAuthenticatedUser());
        return getTreatmentsReady(treatments);
    }

    @Override
    public Treatment getTreatment(int id) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Treatment treatment = treatmentRepository.getTreatmentById(authenticationService.getAuthenticatedUser(), id);
        if(treatment == null){
            throw new ResourceNotFound("Treatment doesn't exist");
        }
        return treatment;
    }

    private List<Treatment> getTreatmentsReady(List<Treatment> tre) {

        Treatment copyTreatment;

        List<Treatment> treatments = new ArrayList<>();
        for(Treatment treatment: tre) {
            copyTreatment = Treatment.copy(treatment);
            copyTreatment.setForPatient(userService.cleanUser(copyTreatment.getForPatient()));

            if(!accessControl.A2(copyTreatment.getForPatient())){
                throw new UserValuesExposed("User sensitive values exposed");
            }
            treatments.add(copyTreatment);
        }


        return treatments;
    }

    @Override
    public Treatment createTreatment(Treatment treatment) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Treatment newTreatment = new Treatment();
        newTreatment.setPatientRequest(treatment.getPatientRequest());
        newTreatment.setFrom(new Date());
        User patient = authenticationService.getAuthenticatedUser();
        newTreatment.setForPatient(patient);

        newTreatment = treatmentRepository.save(newTreatment);
        newTreatment.setForPatient(userService.cleanUser(newTreatment.getForPatient()));
        if(!accessControl.A2(newTreatment.getForPatient()))
            throw new UserValuesExposed("User sensitive values exposed");
        return newTreatment;
    }

    @Override
    public Treatment updateTreatment(Treatment treatment, int treatmentId, String doctorIPAddress) throws IllegalAccessException, InvocationTargetException, InstantiationException {

//      Проверува дали моментално логираниот корисник кој ја повикува оваа функција има улога доктор
//      Ако не е доктор се фрла exception
        if(!accessControl.canTakeTreatments(authenticationService.getAuthenticatedUser())){
            throw new ResourceNotAllowed("Only doctors can update treatments!!!");
        }

        if(treatmentRepository.findOne(treatmentId) == null) {
            System.out.println("TREATMENT FOUND");
        }

        Treatment updateTreatment = Treatment.copy(treatmentRepository.findOne(treatmentId));

        if(updateTreatment.getHasDoctor() == null) {
            System.out.println("NULL");
        }

        if(updateTreatment == null) {
            throw new ResourceNotFound("Treatment not found!!!");
        }

//      Ако предадениот објект treatment нема поставено дијагноза, испратеното барање е за докторот да го земе тој treatment
        if(treatment.getDiagnosis() != null){
            if(accessControl.isTreatmentTakenByDoctor(updateTreatment, authenticationService.getAuthenticatedUser())) {
                if(accessControl.D1(doctorIPAddress, authenticationService.getAuthenticatedUser())){
                    updateTreatment.setTo(new Date());
                    updateTreatment.setDiagnosis(treatment.getDiagnosis());
                    updateTreatment = treatmentRepository.save(updateTreatment);
                    updateTreatment.setForPatient(userService.cleanUser(updateTreatment.getForPatient()));
                    return updateTreatment;
                }else{
                    throw new ResourceNotAllowed("You can modify this treatment only from the hospital network during office hours!");
                }
            } else{
                throw new ResourceNotAllowed("This treatment was claimed by another doctor and you are not allowed to set diagnosis!");
            }
        }else{
            if(updateTreatment.getHasDoctor() == null) {
                User doctor = authenticationService.getAuthenticatedUser();
                updateTreatment.setHasDoctor(doctor);
                updateTreatment = treatmentRepository.save(updateTreatment);
                updateTreatment.setForPatient(userService.cleanUser(updateTreatment.getForPatient()));
                return updateTreatment;
            } else {
                throw new ResourceNotAllowed("This treatment was claimed by another doctor!");
            }
        }
    }
}
