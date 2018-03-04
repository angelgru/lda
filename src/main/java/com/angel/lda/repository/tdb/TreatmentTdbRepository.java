package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Treatment;
import com.angel.lda.model.User;
import com.angel.lda.repository.TreatmentRepository;
import com.angel.lda.utils.StatementToObjectUtil;
import com.angel.lda.utils.StatementsFromTdb;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Angel on 2/8/2018.
 */
@SuppressWarnings("Duplicates")
@Repository
public class TreatmentTdbRepository implements TreatmentRepository {

    private final DatasetProvider datasetProvider;
    private final UserTdbRepository userTdbRepository;

    @Autowired
    public TreatmentTdbRepository(DatasetProvider datasetProvider, UserTdbRepository userTdbRepository) {
        this.datasetProvider = datasetProvider;
        this.userTdbRepository = userTdbRepository;
    }

    @Override
    public List<Treatment> getAllNonTakenTreatments() throws IllegalAccessException, InstantiationException, InvocationTargetException, ParseException {

        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String model = "treatment";
        String id = URI + "id";
        String from = URI + "from";
        String to = URI + "to";
        String patientRequest = URI + "patientRequest";
        String diagnosis = URI + "diagnosis";
        String forPatient = URI + "forPatient";
        String hasDoctor = URI + "hasDoctor";

        Map<String, String> mapping=new HashMap<>();

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, null, null, null);

        mapping.put(id, "setId");
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

        List<Treatment> treatmentsList = new ArrayList<>();
        Treatment treatment = null;

        while(iterator.hasNext()) {
            treatment = iterator.next();
//            DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
//            Date fromDate = dateFormat.parse(treatment.getTdbFrom());
//            Date toDate = dateFormat.parse(treatment.getTdbTo());
//            treatment.setFrom(fromDate);
//            treatment.setTo(toDate);
            User user = userTdbRepository.findByEmail(treatment.getTdbForPatientId());
            if(user != null)
                treatment.setForPatient(user);

            User doctor = userTdbRepository.findByEmail(treatment.getTdbHasDoctor());
            if(doctor != null)
                treatment.setHasDoctor(doctor);

            if(treatment.getHasDoctor() == null)
                treatmentsList.add(treatment);
        }

        return treatmentsList;
    }

    @Override
    public List<Treatment> getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor(User users) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String model = "treatment";
        String id = URI + "id";
        String from = URI + "from";
        String to = URI + "to";
        String patientRequest = URI + "patientRequest";
        String diagnosis = URI + "diagnosis";
        String forPatient = URI + "forPatient";

        Map<String, String> mapping=new HashMap<>();

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, null, null, null);

        mapping.put(id, "setId");
        mapping.put(from, "setTdbFrom");
        mapping.put(to, "setTdbTo");
        mapping.put(patientRequest, "setPatientRequest");
        mapping.put(diagnosis, "setDiagnosis");
        mapping.put(forPatient, "setTdbForPatientId");

        Collection<Treatment> treatments = StatementToObjectUtil.parseList(statements, Treatment.class, mapping);
        Iterator<Treatment> iterator = treatments.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("There aren't any treatments.");

        List<Treatment> treatmentsList = new ArrayList<>();
        Treatment treatment = null;

        while(iterator.hasNext()) {
            treatment = iterator.next();
//            DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
//            Date fromDate = dateFormat.parse(treatment.getTdbFrom());
//            Date toDate = dateFormat.parse(treatment.getTdbTo());
//            treatment.setFrom(fromDate);
//            treatment.setTo(toDate);
            User user = userTdbRepository.findByEmail(treatment.getTdbForPatientId());
            if(user != null)
                treatment.setForPatient(user);

            User doctor = userTdbRepository.findByEmail(treatment.getTdbHasDoctor());
            if(doctor != null)
                treatment.setHasDoctor(doctor);

            assert doctor != null;
            if(treatment.getHasDoctor() != null && treatment.getHasDoctor().getEmail().equals(doctor.getEmail()) && treatment.getDiagnosis() == null)
                treatmentsList.add(treatment);
        }

        return treatmentsList;
    }

    @Override
    public List<Treatment> getCompletedTreatmentsAcceptedByCurrentlyLoggedInDoctor(User users) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String model = "treatment";
        String id = URI + "id";
        String from = URI + "from";
        String to = URI + "to";
        String patientRequest = URI + "patientRequest";
        String diagnosis = URI + "diagnosis";
        String forPatient = URI + "forPatient";

        Map<String, String> mapping=new HashMap<>();

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, null, null, null);

        mapping.put(id, "setId");
        mapping.put(from, "setTdbFrom");
        mapping.put(to, "setTdbTo");
        mapping.put(patientRequest, "setPatientRequest");
        mapping.put(diagnosis, "setDiagnosis");
        mapping.put(forPatient, "setTdbForPatientId");

        Collection<Treatment> treatments = StatementToObjectUtil.parseList(statements, Treatment.class, mapping);
        Iterator<Treatment> iterator = treatments.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("There aren't any treatments.");

        List<Treatment> treatmentsList = new ArrayList<>();
        Treatment treatment = null;

        while(iterator.hasNext()) {
            treatment = iterator.next();
//            DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
//            Date fromDate = dateFormat.parse(treatment.getTdbFrom());
//            Date toDate = dateFormat.parse(treatment.getTdbTo());
//            treatment.setFrom(fromDate);
//            treatment.setTo(toDate);
            User user = userTdbRepository.findByEmail(treatment.getTdbForPatientId());
            if(user != null)
                treatment.setForPatient(user);

            User doctor = userTdbRepository.findByEmail(treatment.getTdbHasDoctor());
            if(doctor != null)
                treatment.setHasDoctor(doctor);

            if(treatment.getHasDoctor() != null && treatment.getDiagnosis() != null)
                treatmentsList.add(treatment);
        }

        return treatmentsList;
    }

    @Override
    public Treatment getTreatmentById(User user, int id) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return findOne(id);
    }

    @Override
    public Treatment findOne(int givenId) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String model = "treatment";
        String id = URI + "id";
        String from = URI + "from";
        String to = URI + "to";
        String patientRequest = URI + "patientRequest";
        String diagnosis = URI + "diagnosis";
        String forPatient = URI + "forPatient";
        String hasDoctor = URI + "hasDoctor";

        Map<String, String> mapping=new HashMap<>();

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, URI + givenId, null, null);

        mapping.put(id, "setId");
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

        List<Treatment> treatmentsList = new ArrayList<>();
        Treatment treatment = null;

        while(iterator.hasNext()) {

            treatment = iterator.next();
            System.out.println("TREATMENT ID " + treatment.getId());
//            DateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
//            Date fromDate = dateFormat.parse(treatment.getTdbFrom());
//            Date toDate = dateFormat.parse(treatment.getTdbTo());
//            treatment.setFrom(fromDate);
//            treatment.setTo(toDate);
            User user = userTdbRepository.findByEmail(treatment.getTdbForPatientId());
            if(user != null)
                treatment.setForPatient(user);
            User doctor = userTdbRepository.findByEmail(treatment.getTdbHasDoctor());
            if(doctor != null)
                treatment.setHasDoctor(doctor);

            treatmentsList.add(treatment);
        }

        return treatmentsList.get(0);
    }

    @Override
    public Treatment save(Treatment newTreatment) {

        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String model = "treatment";
        String id = URI + "id";
        String from = URI + "from";
        String to = URI + "to";
        String patientRequest = URI + "patientRequest";
        String diagnosis = URI + "diagnosis";
        String forPatient = URI + "forPatient";
        String hasDoctor = URI + "hasDoctor";

        if(newTreatment.getDiagnosis() != null){
            StatementsFromTdb.addStatement(dataset, model, URI + newTreatment.getId(), diagnosis, newTreatment.getDiagnosis());
            StatementsFromTdb.addStatement(dataset, model, URI + newTreatment.getId(), to, String.valueOf(newTreatment.getTo()));
        } else if(newTreatment.getHasDoctor() != null) {
            StatementsFromTdb.addStatement(dataset, model, URI + newTreatment.getId(), hasDoctor, newTreatment.getHasDoctor().getEmail());
        } else {
            StatementsFromTdb.addStatement(dataset, model, URI+"5", id, "5");
            StatementsFromTdb.addStatement(dataset, model, URI+"5", from, newTreatment.getFrom().toString());
            StatementsFromTdb.addStatement(dataset, model, URI+"5", patientRequest, newTreatment.getPatientRequest());
            StatementsFromTdb.addStatement(dataset, model, URI+"5", forPatient, newTreatment.getForPatient().getEmail());
        }

        return newTreatment;
    }

    @Override
    public void delete(int id) {

    }
}
