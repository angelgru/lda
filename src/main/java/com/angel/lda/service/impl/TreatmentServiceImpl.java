package com.angel.lda.service.impl;

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

    @Autowired
    public TreatmentServiceImpl(TreatmentRepository treatmentRepository, UserRepository userRepository) {
        this.treatmentRepository = treatmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Treatment> getAllNonTakenTreatments() throws IOException {
        return treatmentRepository.getAllNonTakenTreatments();
    }

    @Override
    public List<Treatment> getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor(User user) {
        return treatmentRepository.getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor(user);
    }

    @Override
    public List<Treatment> getLockedTreatmentsAcceptedByCurrentlyLoggedInDoctor(User user) {
        return treatmentRepository.getLockedTreatmentsAcceptedByCurrentlyLoggedInDoctor(user);
    }

    @Override
    public Treatment getTreatment(int id) {
        return treatmentRepository.findOne(id);
    }

    @Override
    public Treatment createTreatment(Treatment treatment, String email) {
        // Да креирам нова инстанца од Treatment во која ќе ги сместам from, patientRequest, forPatient при што from ќе го генерирам тука
        treatment.setFrom(new Date());
        User patient = userRepository.findByEmail(email);
        treatment.setForPatient(patient);
        return treatmentRepository.save(treatment);
    }

    @Override
    public Treatment updateTreatment(Treatment treatment, int treatmentId, String email) {
        Treatment treatmentToBeUpdated = treatmentRepository.findOne(treatmentId);

//      Првиот услов е во request -от да имаме добиено параметер за дијагноза treatment.getDiagnose()
//      Вториот услов е проверувам дали treatment-от за кој сакаме да ставиме дијагноза има доктор, за да спречиме обид
//      пример преку Postman некој да проба да постави дијагноза за treatment каде немаме доктор
//      TODO: да проследам на овој метод и Principal за да проверам дека treatmentot за кој поставуваме дијагноза како доктор
//      го има барателот на акцијата
        if(treatment.getDiagnosis() != null && treatmentToBeUpdated.getHasDoctor() != null){
            treatmentToBeUpdated.setTo(new Date());
            treatmentToBeUpdated.setDiagnosis(treatment.getDiagnosis());
        } else if (treatment.getHasDoctor() == null) {
//            Да фрлам грешка за недозволен пристап доколку некој проба со директен пристап до промени доктор за
//            treatment кој веќе има доктор
            User doctor = userRepository.findByEmail(email);
            treatmentToBeUpdated.setHasDoctor(doctor);
        }
        treatmentRepository.save(treatmentToBeUpdated);
        return treatmentToBeUpdated;
    }

    @Override
    public void deleteTreatment(int id) {
        treatmentRepository.delete(id);
    }
}
