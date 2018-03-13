package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Sensor;
import com.angel.lda.model.Treatment;
import com.angel.lda.model.User;
import com.angel.lda.repository.TreatmentRepository;
import com.angel.lda.service.UserService;
import com.angel.lda.utils.StatementToObjectUtil;
import com.angel.lda.utils.StatementsFromTdb;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;

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
    public List<Treatment> getAllNonTakenTreatments() throws IllegalAccessException, InstantiationException, InvocationTargetException, ParseException {

        Dataset dataset = datasetProvider.guardedDataset();

        String model = "http://example.com/treatment";
        String from = "http://sm.example.com#from";
        String to = "http://sm.example.com#to";
        String patientRequest = "http://sm.example.com#patient_request";
        String diagnosis = "http://sm.example.com#diagnosis";
        String forPatient = "http://sm.example.com#for_patient";
        String hasDoctor = "http://sm.example.com#has_doctor";

        Map<String, String> mapping=new HashMap<>();

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, null, null, null);

        mapping.put(from, "setFrom");
        mapping.put(to, "setTo");
        mapping.put(patientRequest, "setPatientRequest");
        mapping.put(diagnosis, "setDiagnosis");
        mapping.put(forPatient, "setTdbForPatientId");
        mapping.put(hasDoctor, "setTdbHasDoctor");
        Collection<Treatment> treatments = StatementToObjectUtil.parseList(statements, Treatment.class, mapping);
        Iterator<Treatment> iterator = treatments.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("There aren't any treatments.");

        List<Treatment> treatmentsList = new ArrayList<>();
        Treatment treatment;

        while(iterator.hasNext()) {
            treatment = iterator.next();

            String patientEmail = null;
            if(treatment.getTdbForPatientId() != null)
                patientEmail = treatment.getTdbForPatientId().substring(19, treatment.getTdbForPatientId().length());

            User patient  = null;
            if(patientEmail != null) {
                patient = userTdbRepository.findByEmail(patientEmail);
            }
            if(patient != null) {
                treatment.setForPatient(patient);
                Set<Sensor> sensors = sensorTdbRepository.getSensorsForUser(patient);
                patient.setOwnsSensors(sensors);
            }

            if(treatment.getTdbHasDoctor() == null)
                treatmentsList.add(treatment);
        }

        return treatmentsList;
    }

    @Override
    public List<Treatment> getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor(User user) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Dataset dataset = datasetProvider.guardedDataset();

        String model = "http://example.com/treatment";
        String from = "http://sm.example.com#from";
        String to = "http://sm.example.com#to";
        String patientRequest = "http://sm.example.com#patient_request";
        String diagnosis = "http://sm.example.com#diagnosis";
        String forPatient = "http://sm.example.com#for_patient";
        String hasDoctor = "http://sm.example.com#has_doctor";

        Map<String, String> mapping=new HashMap<>();

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, null, null, null);

        mapping.put(from, "setFrom");
        mapping.put(to, "setTo");
        mapping.put(patientRequest, "setPatientRequest");
        mapping.put(diagnosis, "setDiagnosis");
        mapping.put(forPatient, "setTdbForPatientId");
        mapping.put(hasDoctor, "setTdbHasDoctor");
        Collection<Treatment> treatments = StatementToObjectUtil.parseList(statements, Treatment.class, mapping);
        Iterator<Treatment> iterator = treatments.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("There aren't any treatments.");

        List<Treatment> treatmentsList = new ArrayList<>();
        Treatment treatment;

        while(iterator.hasNext()) {
            treatment = iterator.next();

            String patientEmail = null;
            if(treatment.getTdbForPatientId() != null)
                patientEmail = treatment.getTdbForPatientId().substring(19, treatment.getTdbForPatientId().length());

            User patient = null;
            if(patientEmail != null)
                patient = userTdbRepository.findByEmail(patientEmail);

            if(patient != null) {
                treatment.setForPatient(patient);
                Set<Sensor> sensors = sensorTdbRepository.getSensorsForUser(patient);
                patient.setOwnsSensors(sensors);
            }

            String treatmentDoctor = null;
            String loggedInUser = "http://example.com/" + user.getEmail();
            if(treatment.getTdbHasDoctor() != null){
                treatmentDoctor = treatment.getTdbHasDoctor();
            }

            if(loggedInUser.equals(treatmentDoctor)) {
                treatmentsList.add(treatment);
            }
        }

        return treatmentsList;
    }

    @Override
    public List<Treatment> getCompletedTreatmentsAcceptedByCurrentlyLoggedInDoctor(User user) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Dataset dataset = datasetProvider.guardedDataset();

        String model = "http://example.com/treatment";
        String from = "http://sm.example.com#from";
        String to = "http://sm.example.com#to";
        String patientRequest = "http://sm.example.com#patient_request";
        String diagnosis = "http://sm.example.com#diagnosis";
        String forPatient = "http://sm.example.com#for_patient";
        String hasDoctor = "http://sm.example.com#has_doctor";

        Map<String, String> mapping=new HashMap<>();

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, null, null, null);

        mapping.put(from, "setFrom");
        mapping.put(to, "setTo");
        mapping.put(patientRequest, "setPatientRequest");
        mapping.put(diagnosis, "setDiagnosis");
        mapping.put(forPatient, "setTdbForPatientId");
        mapping.put(hasDoctor, "setTdbHasDoctor");
        Collection<Treatment> treatments = StatementToObjectUtil.parseList(statements, Treatment.class, mapping);
        Iterator<Treatment> iterator = treatments.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("There aren't any treatments.");

        List<Treatment> treatmentsList = new ArrayList<>();
        Treatment treatment;

        while(iterator.hasNext()) {
            treatment = iterator.next();


            String patientEmail = treatment.getTdbForPatientId().substring(19, treatment.getTdbForPatientId().length());

            User patient = userTdbRepository.findByEmail(patientEmail);
            if(patient != null) {
                treatment.setForPatient(patient);
                Set<Sensor> sensors = sensorTdbRepository.getSensorsForUser(patient);
                patient.setOwnsSensors(sensors);
            }

            String treatmentDoctor = null;
            String loggedInUser = "http://example.com/" + user.getEmail();
            if(treatment.getTdbHasDoctor() != null){
                treatmentDoctor = treatment.getTdbHasDoctor().trim();
            }

            if(loggedInUser.equals(treatmentDoctor) && treatment.getDiagnosis() != null) {
                treatmentsList.add(treatment);
            }
        }

        return treatmentsList;
    }

    @Override
    public Treatment getTreatmentById(User user, int id) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Dataset dataset = datasetProvider.guardedDataset();

        String model = "http://example.com/treatment";
        String subject = "http://example.com/treatment" + id;
        String from = "http://sm.example.com#from";
        String to = "http://sm.example.com#to";
        String patientRequest = "http://sm.example.com#patient_request";
        String diagnosis = "http://sm.example.com#diagnosis";
        String forPatient = "http://sm.example.com#for_patient";
        String hasDoctor = "http://sm.example.com#has_doctor";

        Map<String, String> mapping=new HashMap<>();

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, subject, null, null);

        mapping.put(from, "setTdbFrom");
        mapping.put(to, "setTdbTo");
        mapping.put(patientRequest, "setPatientRequest");
        mapping.put(diagnosis, "setDiagnosis");
        mapping.put(forPatient, "setTdbForPatientId");
        mapping.put(hasDoctor, "setTdbHasDoctor");
        Collection<Treatment> treatments = StatementToObjectUtil.parseList(statements, Treatment.class, mapping);
        Iterator<Treatment> iterator = treatments.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("There aren't any treatments.");

        Treatment treatment = null;

        while(iterator.hasNext()) {
            treatment = iterator.next();
            String patientEmail = treatment.getTdbForPatientId().substring(19, treatment.getTdbForPatientId().length());

            User patient = userTdbRepository.findByEmail(patientEmail);
            if(patient != null) {
                treatment.setForPatient(patient);
                Set<Sensor> sensors = sensorTdbRepository.getSensorsForUser(patient);
                patient.setOwnsSensors(sensors);
            }

            if(treatment.getTdbHasDoctor() != null && treatment.getTdbHasDoctor().equals("http://example.com/" + user.getEmail())) {
                treatment.setHasDoctor(user);
            } else{
                treatment = null;
            }


        }
        if(treatment == null)
            throw new ResourceNotFound("Non existing treatment!");
        return treatment;
    }

    @Override
    public Treatment findOne(int id) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Dataset dataset = datasetProvider.guardedDataset();

        String model = "http://example.com/treatment";
        String subject = "http://example.com/treatment" + id;
        String from = "http://sm.example.com#from";
        String to = "http://sm.example.com#to";
        String patientRequest = "http://sm.example.com#patient_request";
        String diagnosis = "http://sm.example.com#diagnosis";
        String forPatient = "http://sm.example.com#for_patient";
        String hasDoctor = "http://sm.example.com#has_doctor";

        Map<String, String> mapping=new HashMap<>();

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, subject, null, null);
        System.out.println("STATEMENTS NUMBER " + statements.size());

        mapping.put(from, "setTdbFrom");
        mapping.put(to, "setTdbTo");
        mapping.put(patientRequest, "setPatientRequest");
        mapping.put(diagnosis, "setDiagnosis");
        mapping.put(forPatient, "setTdbForPatientId");
        mapping.put(hasDoctor, "setTdbHasDoctor");
        Collection<Treatment> treatments = StatementToObjectUtil.parseList(statements, Treatment.class, mapping);
        Iterator<Treatment> iterator = treatments.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("There aren't any treatments.");

        Treatment treatment = null;

        while(iterator.hasNext()) {
            treatment = iterator.next();
            String patientEmail = treatment.getTdbForPatientId().substring(19, treatment.getTdbForPatientId().length());

            User patient = userService.getUserByEmail(patientEmail);
            if(patient != null) {
                treatment.setForPatient(patient);
                Set<Sensor> sensors = sensorTdbRepository.getSensorsForUser(patient);
                patient.setOwnsSensors(sensors);
            }

            String doctorEmail;
            doctorEmail = treatment.getTdbHasDoctor().substring(19, treatment.getTdbHasDoctor().length());

            User doctor = null;
            if(doctorEmail != null) {
                doctor = userTdbRepository.findByEmail(doctorEmail);
            }
            if(doctor != null)
                treatment.setHasDoctor(doctor);
        }
        if(treatment == null)
            throw new ResourceNotFound("Non existing treatment!");
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
            System.out.println("DOCTOR EMAIL: " + newTreatment.getHasDoctor().getEmail());
            StatementsFromTdb.addStatement(dataset, model, subject, hasDoctor, "http://example.com/" + newTreatment.getHasDoctor().getEmail());
        } else {
            StatementsFromTdb.addStatement(dataset, model, subject, from, String.valueOf(newTreatment.getFrom().getTime()));
            StatementsFromTdb.addStatement(dataset, model, subject, patientRequest, newTreatment.getPatientRequest());
            StatementsFromTdb.addStatement(dataset, model, subject, forPatient, "http://example.com/" + newTreatment.getForPatient().getEmail());
        }

        return newTreatment;
    }

    @Override
    public void delete(int id) {

    }
}
