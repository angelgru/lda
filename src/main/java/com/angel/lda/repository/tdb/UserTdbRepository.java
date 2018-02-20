package com.angel.lda.repository.tdb;

import com.angel.lda.model.User;
import com.angel.lda.repository.UserRepository;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel on 2/8/2018.
 */
@SuppressWarnings("Duplicates")
@Repository
public class UserTdbRepository implements UserRepository {

    private final DatasetProvider datasetProvider;
    private HospitalTdbRepository hospitalTdbRepository;

    @Autowired
    public UserTdbRepository(DatasetProvider datasetProvider, HospitalTdbRepository hospitalTdbRepository) {
        this.datasetProvider = datasetProvider;
        this.hospitalTdbRepository = hospitalTdbRepository;
    }

    @Override
    public User findByEmail(String emailT) {

        createUser();

        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String model = "user";
        String id = URI + "id";
        String email = URI + "email";
        String password = URI + "password";
        String name = URI + "name";
        String phoneNumber = URI + "phoneNumber";
        String emergencyPhone = URI + "emergencyPhone";
        String active = URI + "active";
        String doctor = URI + "doctor";
        String worksAtHospital = URI + "worksAtHospital";
        String usesSensorSyncApplication = URI + "usesSensorSyncApplication";

        List<Statement> statements = getStatements(dataset, model, URI + "angel@yahoo.com", null, null);

        User user = new User();

        for(Statement statement: statements) {
            if(statement.getPredicate().toString().equals(id)){
                user.setId(Integer.valueOf(statement.getObject().toString()));
            } else if(statement.getPredicate().toString().equals(email)) {
                user.setEmail(statement.getObject().toString());
            } else if(statement.getPredicate().toString().equals(password)) {
                user.setPassword(statement.getObject().toString());
            } else if(statement.getPredicate().toString().equals(name)) {
                user.setName(statement.getObject().toString());
            } else if(statement.getPredicate().toString().equals(phoneNumber)) {
                user.setPhoneNumber(statement.getObject().toString());
            } else if(statement.getPredicate().toString().equals(emergencyPhone)) {
                user.setEmergencyPhone(statement.getObject().toString());
            } else if (statement.getPredicate().toString().equals(worksAtHospital)) {
                user.setWorksAtHospital(hospitalTdbRepository.findOne(Integer.valueOf(statement.getObject().toString())));
            }
        }

        return user;
    }

    @Override
    public List<User> getDoctors() {
        System.out.println("getDOctors()");
        createUser();

        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String model = "user";
        String id = URI + "id";
        String email = URI + "email";
        String password = URI + "password";
        String name = URI + "name";
        String phoneNumber = URI + "phoneNumber";
        String emergencyPhone = URI + "emergencyPhone";
        String active = URI + "active";
        String doctor = URI + "doctor";
        String worksAtHospital = URI + "worksAtHospital";
        String usesSensorSyncApplication = URI + "usesSensorSyncApplication";

        List<Statement> statements = getStatements(dataset, model, null, null, null);

        User user = new User();
        List<User> users = new ArrayList<>();

        for(Statement statement: statements) {
            if(statement.getPredicate().toString().equals(id)){
                user = new User();
                user.setId(Integer.valueOf(statement.getObject().toString()));
                System.out.println(statement.getPredicate().toString());
            } else if(statement.getPredicate().toString().equals(email)) {
                user.setEmail(statement.getObject().toString());
                System.out.println(statement.getPredicate().toString());
            } else if(statement.getPredicate().toString().equals(password)) {
                user.setPassword(statement.getObject().toString());
                System.out.println(statement.getPredicate().toString());
            } else if(statement.getPredicate().toString().equals(name)) {
                user.setName(statement.getObject().toString());
                System.out.println(statement.getPredicate().toString());
            } else if(statement.getPredicate().toString().equals(phoneNumber)) {
                user.setPhoneNumber(statement.getObject().toString());
                System.out.println(statement.getPredicate().toString());
            } else if(statement.getPredicate().toString().equals(emergencyPhone)) {
                user.setEmergencyPhone(statement.getObject().toString());
                System.out.println(statement.getPredicate().toString());
            } else if (statement.getPredicate().toString().equals(worksAtHospital)) {
                user.setWorksAtHospital(hospitalTdbRepository.findOne(Integer.valueOf(statement.getObject().toString())));
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void delete(User userToBeDeleted) {

    }

    public void createUser() {
        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String model = "user";
        String id = URI + "id";
        String email = URI + "email";
        String password = URI + "password";
        String name = URI + "name";
        String phoneNumber = URI + "phoneNumber";
        String emergencyPhone = URI + "emergencyPhone";
        String active = URI + "active";
        String doctor = URI + "doctor";
        String worksAtHospital = URI + "worksAtHospital";
        String usesSensorSyncApplication = URI + "usesSensorSyncApplication";

        addStatement(dataset, model, URI + "angel@yahoo.com", id, "1");
        addStatement(dataset, model, URI + "angel@yahoo.com", email, "angel@yahoo.com");
        addStatement(dataset, model, URI + "angel@yahoo.com", password, "qwerty");
        addStatement(dataset, model, URI + "angel@yahoo.com", name, "Angel");
        addStatement(dataset, model, URI + "angel@yahoo.com", phoneNumber, "0458747");
        addStatement(dataset, model, URI + "angel@yahoo.com", emergencyPhone, "484884");
        addStatement(dataset, model, URI + "angel@yahoo.com", active, "1");
        addStatement(dataset, model, URI + "angel@yahoo.com", doctor, "1");
        addStatement(dataset, model, URI + "angel@yahoo.com", worksAtHospital, "1");
    }

    private List<Statement> getStatements(Dataset ds, String modelName, String subject, String property, String object) {
        List<Statement> results = new ArrayList<Statement>();

        Model model = null;

        ds.begin(ReadWrite.READ);

        try{
            model = ds.getNamedModel(modelName);

            Selector selector = new SimpleSelector(
                    (subject != null) ? model.createResource(subject) : (Resource) null,
                    (property != null) ? model.createProperty(property) : (Property) null,
                    (object != null) ? model.createResource(object) : (RDFNode) null
            );

            StmtIterator it = model.listStatements(selector);

            while(it.hasNext()) {
                Statement statement = it.next();
                results.add(statement);
            }

            ds.commit();
        } finally {
            ds.end();
        }

        return results;
    }

    private void addStatement(Dataset ds, String modelName, String subject, String property, String object){
        Model model = null;

        ds.begin(ReadWrite.WRITE);
        try{
            model = ds.getNamedModel(modelName);

            Statement statement = model.createStatement(
                    model.createResource(subject),
                    model.createProperty(property),
                    model.createResource(object)
            );
            model.add(statement);
            ds.commit();
        } finally {
            ds.end();
        }
    }
}
