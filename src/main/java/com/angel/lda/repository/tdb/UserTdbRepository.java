package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Hospital;
import com.angel.lda.model.Sensor;
import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.model.User;
import com.angel.lda.repository.UserRepository;
import com.angel.lda.utils.StatementToObjectUtil;
import com.angel.lda.utils.StatementsFromTdb;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by Angel on 2/8/2018.
 */
@SuppressWarnings("Duplicates")
@Repository
@Profile("tdb")
public class UserTdbRepository implements UserRepository {

    private final DatasetProvider datasetProvider;
    private HospitalTdbRepository hospitalTdbRepository;
    private SensorSyncApplicationTdbRepository sensorSyncApplicationTdbRepository;
    private final SensorTdbRepository sensorTdbRepository;

    @Autowired
    public UserTdbRepository(DatasetProvider datasetProvider, HospitalTdbRepository hospitalTdbRepository, SensorSyncApplicationTdbRepository sensorSyncApplicationTdbRepository, SensorTdbRepository sensorTdbRepository) {
        this.datasetProvider = datasetProvider;
        this.hospitalTdbRepository = hospitalTdbRepository;
        this.sensorSyncApplicationTdbRepository = sensorSyncApplicationTdbRepository;
        this.sensorTdbRepository = sensorTdbRepository;
    }

    @Override
    public User findByEmail(String emailT) throws IllegalAccessException, InvocationTargetException, InstantiationException {

        Dataset dataset = datasetProvider.guardedDataset();

        String subject = "http://example.com/" + emailT;
        String model = "http://example.com/user";
        String email = "http://sm.example.com#email";
        String password = "http://sm.example.com#password";
        String name = "http://sm.example.com#name";
        String phoneNumber = "http://sm.example.com#phone";
        String emergencyPhone = "http://sm.example.com#emergency_phone";
        String doctor = "http://sm.example.com#doctor";
        String tdbWorksAtHospitalId = "http://sm.example.com#works_at";
        String tdbUsesSensorSyncApplicationId = "http://sm.example.com#uses";

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, subject, null, null);

        Map<String, String> mapping=new HashMap<>();
        mapping.put(email, "setEmail");
        mapping.put(password, "setPassword");
        mapping.put(name, "setName");
        mapping.put(phoneNumber, "setPhoneNumber");
        mapping.put(emergencyPhone, "setEmergencyPhone");
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

            int hospitalId = -1;
            if(user.getTdbWorksAtHospitalId() != null)
                hospitalId = Integer.parseInt(String.valueOf(user.getTdbWorksAtHospitalId().charAt(user.getTdbWorksAtHospitalId().length()-1)));

            Hospital hospital = null;
            if(hospitalId != -1)
                hospital = hospitalTdbRepository.findOne(hospitalId);
            if(hospital != null)
                user.setWorksAtHospital(hospital);

            int ssaId = -1;
            if(user.getTdbUsesSensorSyncApplicationId() != null)
                ssaId = Integer.parseInt(String.valueOf(user.getTdbUsesSensorSyncApplicationId().charAt(user.getTdbUsesSensorSyncApplicationId().length()-1)));

            SensorSyncApplication ssa = null;
            if(ssaId != -1)
               ssa  = sensorSyncApplicationTdbRepository.findOne(ssaId);

            if(ssa != null)
                user.setUsesSensorSyncApplication(ssa);

            Set<Sensor> sensors = sensorTdbRepository.getSensorsForUser(user);
            user.setOwnsSensors(sensors);

            userList.add(user);
        }

        return userList.get(0);
    }

    @Override
    public List<User> getDoctors() throws IllegalAccessException, InvocationTargetException, InstantiationException {

        Dataset dataset = datasetProvider.guardedDataset();

        String model = "http://example.com/user";
        String email = "http://sm.example.com#email";
        String password = "http://sm.example.com#password";
        String name = "http://sm.example.com#name";
        String phoneNumber = "http://sm.example.com#phone";
        String emergencyPhone = "http://sm.example.com#emergency_phone";
        String active = "http://sm.example.com#active";
        String doctor = "http://sm.example.com#doctor";
        String tdbWorksAtHospitalId = "http://sm.example.com#works_at";
        String tdbUsesSensorSyncApplicationId = "http://sm.example.com#uses";

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, null, null, null);

        Map<String, String> mapping=new HashMap<>();
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
        User user;

        while (iterator.hasNext()){
            user = iterator.next();
            if(user.getDoctor() == 0)
                continue;

            int hospitalId = Integer.parseInt(String.valueOf(user.getTdbWorksAtHospitalId().charAt(user.getTdbWorksAtHospitalId().length()-1)));

            Hospital hospital = null;
            if(hospitalId > -1)
                hospital = hospitalTdbRepository.findOne(hospitalId);

            if(hospital != null)
                user.setWorksAtHospital(hospital);
            int ssaId = Integer.parseInt(String.valueOf(user.getTdbUsesSensorSyncApplicationId().charAt(user.getTdbUsesSensorSyncApplicationId().length()-1)));
            SensorSyncApplication ssa = null;
            if(ssaId > -1)
                ssa = sensorSyncApplicationTdbRepository.findOne(ssaId);
            if(ssa != null)
                user.setUsesSensorSyncApplication(ssa);

            Set<Sensor> sensors = sensorTdbRepository.getSensorsForUser(user);
            user.setOwnsSensors(sensors);

            userList.add(user);
        }

        return userList;
    }

    @Override
    public User save(User user) {
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
