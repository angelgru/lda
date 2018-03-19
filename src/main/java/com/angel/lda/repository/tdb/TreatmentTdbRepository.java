package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Treatment;
import com.angel.lda.model.User;
import com.angel.lda.repository.TreatmentRepository;
import com.angel.lda.service.UserService;
import com.angel.lda.utils.LoadFromTdb;
import com.angel.lda.utils.StatementsFromTdb;
import org.apache.commons.io.IOUtils;
import org.apache.jena.query.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Angel on 2/8/2018.
 */
@SuppressWarnings("Duplicates")
@Repository
@Profile("tdb")
public class TreatmentTdbRepository implements TreatmentRepository {

    private final DatasetProvider datasetProvider;
    private final UserTdbRepository userTdbRepository;
    private UserService userService;
    private SensorTdbRepository sensorTdbRepository;

    @Autowired
    public TreatmentTdbRepository(DatasetProvider datasetProvider, UserTdbRepository userTdbRepository, UserService userService, SensorTdbRepository sensorTdbRepository) {
        this.datasetProvider = datasetProvider;
        this.userTdbRepository = userTdbRepository;
        this.userService = userService;
        this.sensorTdbRepository = sensorTdbRepository;
    }

    @Override
    public List<Treatment> getAllNonTakenTreatments() throws IOException {
        Dataset dataset = datasetProvider.guardedDataset();

        String query = IOUtils.toString(this.getClass().getResourceAsStream("/queries/all_non_taken_treatments.rq"), Charset.defaultCharset());
        List<Treatment> treatments = LoadFromTdb.execQuery(dataset, query, this::prepareTreatment);

        if(treatments.isEmpty())
            throw new ResourceNotFound("There are no unclaimed treatments!");
        return treatments;
    }

    @Override
    public List<Treatment> getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor(User user) throws IOException {
        Dataset dataset = datasetProvider.guardedDataset();

        String data = IOUtils.toString(this.getClass().getResourceAsStream("/queries/all_treatments_accepted_by_doctor_without_diagnosis.rq"), Charset.defaultCharset());
        String query = getTreatmentQuery(data, user.getEmail());
        List<Treatment> treatments = LoadFromTdb.execQuery(dataset, query, this::prepareTreatment);

        if(treatments.isEmpty())
            throw new ResourceNotFound("There are no accepted treatments without diagnosis for this doctor!");
        return treatments;
    }

    @Override
    public List<Treatment> getCompletedTreatmentsAcceptedByCurrentlyLoggedInDoctor(User user) throws IOException {
        Dataset dataset = datasetProvider.guardedDataset();

        String data = IOUtils.toString(this.getClass().getResourceAsStream("/queries/all_treatments_accepted_by_doctor_with_diagnosis.rq"), Charset.defaultCharset());
        String query = getTreatmentQuery(data, user.getEmail());
        List<Treatment> treatments = LoadFromTdb.execQuery(dataset, query, this::prepareTreatment);

        if(treatments.isEmpty())
            throw new ResourceNotFound("There are no accepted treatments with diagnosis for this doctor!");
        return treatments;
    }

    @Override
    public Treatment getTreatmentById(User user, int id) throws IOException {
        Dataset dataset = datasetProvider.guardedDataset();

        String data = IOUtils.toString(this.getClass().getResourceAsStream("/queries/one_treatment.rq"), Charset.defaultCharset());
        String query = getTreatmentQuery(data, id, user.getEmail());
        List<Treatment> treatments = LoadFromTdb.execQuery(dataset, query, this::prepareTreatment);
        if(treatments.isEmpty())
            throw new ResourceNotFound("Treatment doesn't exist in our system");
        return treatments.get(0);
    }

    @Override
    public Treatment findOne(int id) throws IOException {
        return null;
    }

    private String getTreatmentQuery(String data, int treatmentId, String doctor) {
        return String.format(data, treatmentId, doctor, treatmentId, treatmentId, doctor);
    }

    private String getTreatmentQuery(String data, String doctor) {
        return String.format(data, doctor);
    }

    private Treatment prepareTreatment(Map<String, String> m) {
        Treatment treatment = new Treatment();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(m.get("from")));
        treatment.setFrom(calendar.getTime());
        treatment.setPatientRequest(m.get("patient_request"));

        User user = new User();
        user.setEmail(m.get("email"));
        user.setName(m.get("name"));
        if(m.get("phone") != null) {
            user.setPhoneNumber(m.get("phone"));
        }

        if(m.get("emergency_phone") != null) {
            user.setEmergencyPhone(m.get("emergency_phone"));
        }

        if(m.get("to") != null) {
            calendar.setTimeInMillis(Long.valueOf(m.get("to")));
            treatment.setTo(calendar.getTime());
        }

        if(m.get("diagnosis") != null) {
            treatment.setDiagnosis(m.get("diagnosis"));
        }

        if(m.get("doctorEmail") != null) {
            User doctor = new User();
            doctor.setEmail(m.get("doctorEmail"));
            treatment.setHasDoctor(doctor);
        }

        treatment.setForPatient(user);

        return treatment;
    }

    @Override
    public Treatment save(Treatment newTreatment) {

        Dataset dataset = datasetProvider.guardedDataset();

        String model = "http://example.com/treatment";
        String subject;
        if(newTreatment.getId() < 0 ) {
            Random random = new Random();
            subject = "http://example.com/treatment" + random.nextInt(10000) + 100;
        } else{
            subject = "http://example.com/treatment" + newTreatment.getId();
        }
        String from = "http://sm.example.com#from";
        String to = "http://sm.example.com#to";
        String patientRequest = "http://sm.example.com#patient_request";
        String diagnosis = "http://sm.example.com#diagnosis";
        String forPatient = "http://sm.example.com#for_patient";
        String hasDoctor = "http://sm.example.com#has_doctor";

        if(newTreatment.getDiagnosis() != null){
            StatementsFromTdb.addStatement(dataset, model, subject, diagnosis, newTreatment.getDiagnosis());
            StatementsFromTdb.addStatement(dataset, model, subject, to, String.valueOf(newTreatment.getTo().getTime()));
        } else if(newTreatment.getHasDoctor() != null) {
            StatementsFromTdb.addStatement(dataset, model, subject, hasDoctor, "http://example.com/" + newTreatment.getHasDoctor().getEmail());
        } else {
            StatementsFromTdb.addStatement(dataset, model, subject, from, String.valueOf(newTreatment.getFrom().getTime()));
            StatementsFromTdb.addStatement(dataset, model, subject, patientRequest, newTreatment.getPatientRequest());
            StatementsFromTdb.addStatement(dataset, model, subject, forPatient, "http://example.com/" + newTreatment.getForPatient().getEmail());
        }

        return newTreatment;
    }
}
