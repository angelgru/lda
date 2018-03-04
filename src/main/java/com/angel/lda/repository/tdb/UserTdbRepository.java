package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Hospital;
import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.model.User;
import com.angel.lda.repository.UserRepository;
import com.angel.lda.utils.StatementToObjectUtil;
import com.angel.lda.utils.StatementsFromTdb;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by Angel on 2/8/2018.
 */
@SuppressWarnings("Duplicates")
@Repository
public class UserTdbRepository implements UserRepository {

    private final DatasetProvider datasetProvider;
    private HospitalTdbRepository hospitalTdbRepository;
    private SensorSyncApplicationTdbRepository sensorSyncApplicationTdbRepository;

    @Autowired
    public UserTdbRepository(DatasetProvider datasetProvider, HospitalTdbRepository hospitalTdbRepository, SensorSyncApplicationTdbRepository sensorSyncApplicationTdbRepository) {
        this.datasetProvider = datasetProvider;
        this.hospitalTdbRepository = hospitalTdbRepository;
        this.sensorSyncApplicationTdbRepository = sensorSyncApplicationTdbRepository;
    }

    @Override
    public User findByEmail(String emailT) throws IllegalAccessException, InvocationTargetException, InstantiationException {

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
        String tdbWorksAtHospitalId = URI + "tdbWorksAtHospitalId";
        String tdbUsesSensorSyncApplicationId = URI + "tdbUsesSensorSyncApplicationId";

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, null, null, null);

        Map<String, String> mapping=new HashMap<>();
        mapping.put(id, "setId");
        mapping.put(email, "setEmail");
        mapping.put(password, "setPassword");
        mapping.put(name, "setName");
        mapping.put(phoneNumber, "setPhoneNumber");
        mapping.put(emergencyPhone, "setEmergencyPhone");
        mapping.put(active, "setActive");
        mapping.put(doctor, "setDoctor");
        mapping.put(tdbWorksAtHospitalId, "setTdbWorksAtHospitalId");
        mapping.put(tdbUsesSensorSyncApplicationId, "setTdbUsesSensorSyncApplicationId");


        Collection<User> users = StatementToObjectUtil.parseList(statements, User.class, mapping);
        Iterator<User> iterator = users.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("There aren't any users in the system !!!");

        List<User> userList = new ArrayList<>();
        User user = null;

        while (iterator.hasNext()){
            user = iterator.next();
            userList.add(user);
            System.out.println("USER" + user.getPassword());
        }

        return userList.get(0);
    }

    @Override
    public List<User> getDoctors() throws IllegalAccessException, InvocationTargetException, InstantiationException {

        createUser(null);

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
        String tdbWorksAtHospitalId = URI + "tdbWorksAtHospitalId";
        String tdbUsesSensorSyncApplicationId = URI + "tdbUsesSensorSyncApplicationId";

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, null, null, null);

        Map<String, String> mapping=new HashMap<>();
        mapping.put(id, "setId");
        mapping.put(email, "setEmail");
        mapping.put(password, "setPassword");
        mapping.put(name, "setName");
        mapping.put(phoneNumber, "setPhoneNumber");
        mapping.put(emergencyPhone, "setEmergencyPhone");
        mapping.put(active, "setActive");
        mapping.put(doctor, "setDoctor");
        mapping.put(tdbWorksAtHospitalId, "setTdbWorksAtHospitalId");
        mapping.put(tdbUsesSensorSyncApplicationId, "setTdbUsesSensorSyncApplicationId");


        Collection<User> users = StatementToObjectUtil.parseList(statements, User.class, mapping);
        Iterator<User> iterator = users.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("There aren't any users in the system !!!");

        List<User> userList = new ArrayList<>();
        User user = null;

        while (iterator.hasNext()){
            user = iterator.next();

            if(user.getTdbUsesSensorSyncApplicationId() != null) {
                SensorSyncApplication sensorSyncApplication = sensorSyncApplicationTdbRepository.findOne(Integer.valueOf(user.getTdbUsesSensorSyncApplicationId()));
                if(sensorSyncApplication != null) {
                    user.setUsesSensorSyncApplication(sensorSyncApplication);
                }
            }

            if(user.getTdbWorksAtHospitalId() != null && Integer.valueOf(user.getTdbWorksAtHospitalId()) >= 1){
                Hospital tmpHospital = hospitalTdbRepository.findOne(Integer.valueOf(user.getTdbWorksAtHospitalId()));
                if(tmpHospital != null) {
                    user.setWorksAtHospital(tmpHospital);
                }
                userList.add(user);
            }


        }

        return userList;
    }

    @Override
    public User save(User user) {
        return createUser(user);
    }

    @Override
    public void delete(User userToBeDeleted) {

    }

//    Во SQL базата за корисниците примарен клуч ми е id колоната кој се генерира од базата
    private User createUser(User user) {
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
        String tdbWorksAtHospitalId = URI + "tdbWorksAtHospitalId";
        String tdbUsesSensorSyncApplicationId = URI + "tdbUsesSensorSyncApplicationId";

//        Ако user==null го креирам како тест доктор, инаку креирај корисник со податоци од аргументот user
        if(user == null){
            StatementsFromTdb.addStatement(dataset, model, URI + "doctor1@test.com", id, "1");
            StatementsFromTdb.addStatement(dataset, model, URI + "doctor1@test.com", email, "doctor1@test.com");
            StatementsFromTdb.addStatement(dataset, model, URI + "doctor1@test.com", password, "qwerty");
            StatementsFromTdb.addStatement(dataset, model, URI + "doctor1@test.com", name, "Angel");
            StatementsFromTdb.addStatement(dataset, model, URI + "doctor1@test.com", phoneNumber, "0458747");
            StatementsFromTdb.addStatement(dataset, model, URI + "doctor1@test.com", emergencyPhone, "484884");
            StatementsFromTdb.addStatement(dataset, model, URI + "doctor1@test.com", active, "1");
            StatementsFromTdb.addStatement(dataset, model, URI + "doctor1@test.com", doctor, "1");
            StatementsFromTdb.addStatement(dataset, model, URI + "doctor1@test.com", tdbWorksAtHospitalId, "1");
            StatementsFromTdb.addStatement(dataset, model, URI + "doctor1@test.com", tdbUsesSensorSyncApplicationId, "1");
        } else if(user.getUsesSensorSyncApplication() != null){
            StatementsFromTdb.addStatement(dataset, model, URI + user.getEmail(), tdbUsesSensorSyncApplicationId, String.valueOf(user.getUsesSensorSyncApplication().getId()));
        } else{
            StatementsFromTdb.addStatement(dataset, model, URI + user.getEmail(), id, "2");
            if(user.getEmail() != null)
                StatementsFromTdb.addStatement(dataset, model, URI + user.getEmail(), email, user.getEmail());
            if(user.getPassword() != null)
                StatementsFromTdb.addStatement(dataset, model, URI + user.getEmail(), password, user.getPassword());
            if(user.getName() != null)
                StatementsFromTdb.addStatement(dataset, model, URI + user.getEmail(), name, user.getName());
            if(user.getPhoneNumber() != null)
                StatementsFromTdb.addStatement(dataset, model, URI + user.getEmail(), phoneNumber, user.getPhoneNumber());
            if(user.getEmergencyPhone() != null)
                StatementsFromTdb.addStatement(dataset, model, URI + user.getEmail(), emergencyPhone, user.getEmergencyPhone());
            if(user.getActive() > 1)
                StatementsFromTdb.addStatement(dataset, model, URI + user.getEmail(), active, "1");
            if(user.getDoctor() > -1)
                StatementsFromTdb.addStatement(dataset, model, URI + user.getEmail(), doctor, "1");
            if(user.getTdbWorksAtHospitalId() != null)
                StatementsFromTdb.addStatement(dataset, model, URI + user.getEmail(), tdbWorksAtHospitalId, "1");
        }

        return user;
    }

}
