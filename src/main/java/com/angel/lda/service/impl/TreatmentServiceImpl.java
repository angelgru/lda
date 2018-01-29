package com.angel.lda.service.impl;

import com.angel.lda.AccessControlMethods.AccessControl;
import com.angel.lda.model.Treatment;
import com.angel.lda.model.User;
import com.angel.lda.repository.TreatmentRepository;
import com.angel.lda.repository.UserRepository;
import com.angel.lda.service.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Autowired
    public TreatmentServiceImpl(TreatmentRepository treatmentRepository, UserRepository userRepository, AccessControl accessControl) {
        this.treatmentRepository = treatmentRepository;
        this.userRepository = userRepository;
        this.accessControl = accessControl;
    }

    @Override
    public List<Treatment> getAllNonTakenTreatments() throws IOException {
        List<Treatment> treatments = treatmentRepository.getAllNonTakenTreatments();
        for(Treatment treatment: treatments) {
            accessControl.A2(treatment.getForPatient());
        }

        return treatments;
    }

    @Override
    public List<Treatment> getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor(User user) {
        List<Treatment> treatments = treatmentRepository.getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor(user);
        for(Treatment treatment: treatments) {
            accessControl.A2(treatment.getForPatient());
        }
        return treatments;
    }

    @Override
    public List<Treatment> getLockedTreatmentsAcceptedByCurrentlyLoggedInDoctor(User user) {
        List<Treatment> treatments = treatmentRepository.getLockedTreatmentsAcceptedByCurrentlyLoggedInDoctor(user);
        for(Treatment treatment: treatments) {
            accessControl.A2(treatment.getForPatient());
        }

        return treatments;
    }

    @Override
    public Treatment getTreatment(int id) {
        Treatment treatment = treatmentRepository.findOne(id);
        treatment.setForPatient(accessControl.A2(treatment.getForPatient()));
        return treatment;
    }

    @Override
    public Treatment createTreatment(Treatment treatment, String email) {
        // Да креирам нова инстанца од Treatment во која ќе ги сместам from, patientRequest, forPatient при што from ќе го генерирам тука
        Treatment newTreatment = new Treatment();
        newTreatment.setPatientRequest(treatment.getPatientRequest());
        newTreatment.setFrom(new Date());
        User patient = userRepository.findByEmail(email);
        newTreatment.setForPatient(patient);

        newTreatment = treatmentRepository.save(newTreatment);
        newTreatment.setForPatient(accessControl.A2(treatment.getForPatient()));
        return newTreatment;
    }

    @Override
    public Treatment updateTreatment(Treatment treatment, int treatmentId, String email, String doctorIPAddress) {
        Treatment treatmentToBeUpdated = treatmentRepository.findOne(treatmentId);

//      Првиот услов е во request -от да имаме добиено параметер за дијагноза treatment.getDiagnose()
//      Вториот услов проверува дали treatment-от за кој сакаме да ставиме дијагноза има доктор, за да спречиме обид
//      пример преку Postman некој да проба да постави дијагноза за treatment каде немаме доктор
//      TODO: да проследам на овој метод и Principal за да проверам дека treatmentot за кој поставуваме дијагноза како доктор
//      го има барателот на акцијата

        if(accessControl.D2(treatment)){
            if(accessControl.checkConditionsForDiagnosis(treatmentToBeUpdated, email)){
                User doctor = userRepository.findByEmail(email);
                if(this.accessControl.D1(doctorIPAddress, doctor)){
                    treatmentToBeUpdated.setTo(new Date());
                    treatmentToBeUpdated.setDiagnosis(treatment.getDiagnosis());
                }else{
                    return null;
//                    Throw an error that the doctor must access the application from the hospital's network and during 8 am to 5 pm time frame
                }
            } else{
                return null;
//                Throw an error that the doctor doesn't have the permissions for setting a diagnosis for that particular treatment
            }
            treatmentToBeUpdated = treatmentRepository.save(treatmentToBeUpdated);
            treatmentToBeUpdated.setForPatient(accessControl.A2(treatmentToBeUpdated.getForPatient()));
            return treatmentToBeUpdated;
        } else if (treatment.getHasDoctor() == null) {
//            Да фрлам грешка за недозволен пристап доколку некој проба со директен пристап до промени доктор за
//            treatment кој веќе има доктор
            User doctor = userRepository.findByEmail(email);
            treatmentToBeUpdated.setHasDoctor(doctor);
            treatmentToBeUpdated = treatmentRepository.save(treatmentToBeUpdated);
            treatmentToBeUpdated.setForPatient(accessControl.A2(treatmentToBeUpdated.getForPatient()));
            return treatmentToBeUpdated;
        }
        return null;
    }

    @Override
    public void deleteTreatment(int id) {
        treatmentRepository.delete(id);
    }
}
