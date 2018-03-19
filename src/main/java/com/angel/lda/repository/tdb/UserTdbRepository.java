package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.InvalidRequest;
import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Hospital;
import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.model.User;
import com.angel.lda.repository.UserRepository;
import com.angel.lda.utils.LoadFromTdb;
import com.angel.lda.utils.StatementsFromTdb;
import org.apache.commons.io.IOUtils;
import org.apache.jena.query.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by Angel on 2/8/2018.
 */
@Repository
@Profile("tdb")
public class UserTdbRepository implements UserRepository {

    private final DatasetProvider datasetProvider;

    @Autowired
    public UserTdbRepository(DatasetProvider datasetProvider) {
        this.datasetProvider = datasetProvider;
    }

    @Override
    public User findByEmail(String email) throws IOException {
        Dataset dataset = datasetProvider.guardedDataset();
        String data = IOUtils.toString(this.getClass().getResourceAsStream("/queries/one_user.rq"), Charset.defaultCharset());
        String query = getUserQuery(data, email);

        List<User> users = LoadFromTdb.execQuery(dataset, query, this::prepareUser);
        if(users.isEmpty())
            throw new ResourceNotFound("User not found!");
        return users.get(0);
    }

    @Override
    public List<User> getDoctors() throws IOException {
        Dataset dataset = datasetProvider.guardedDataset();
        String query = IOUtils.toString(this.getClass().getResourceAsStream("/queries/all_doctors.rq"), Charset.defaultCharset());

        List<User> users = LoadFromTdb.execQuery(dataset, query, this::prepareUser);
        if(users.isEmpty())
            throw new ResourceNotFound("Doctors not found!");
        return users;
    }

    private String getUserQuery(String data, String email) {
        return String.format(data, email ,email, email, email, email);
    }

    private User prepareUser(Map<String, String> m) {
        User user = new User();
        user.setEmail(m.get("email"));
        user.setPassword(m.get("password"));
        user.setName(m.get("user_name"));
        user.setDoctor(Integer.valueOf(m.get("doctor")));

        if(m.get("phone") != null)
            user.setPhoneNumber(m.get("phone"));
        if(m.get("emergency_phone") != null)
            user.setEmergencyPhone(m.get("emergency_phone"));
        if(m.get("hospital_name") != null){
            Hospital hospital = new Hospital();
            hospital.setName(m.get("hospital_name"));
            hospital.setNetworkAddress(m.get("network_address"));
            user.setWorksAtHospital(hospital);
        }

        if(m.get("sensor_sync_app_name") != null) {
            SensorSyncApplication sensorSyncApplication = new SensorSyncApplication();
            sensorSyncApplication.setNameOfApplication(m.get("sensor_sync_app_name"));
            user.setUsesSensorSyncApplication(sensorSyncApplication);
        }

        return user;
    }

    @Override
    public User save(User user) throws IOException {
        Dataset dataset = datasetProvider.guardedDataset();

        String model = "http://example.com/user";

        String type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
        String email = "http://sm.example.com#email";
        String password = "http://sm.example.com#password";
        String name = "http://sm.example.com#name";
        String phoneNumber = "http://sm.example.com#phone";
        String emergencyPhone = "http://sm.example.com#emergency_phone";
        String doctor = "http://sm.example.com#doctor";

        User dbUser = findByEmail(user.getEmail());

        if(dbUser == null) {
            if(user.getEmail() == null || user.getPassword() == null)
                throw new InvalidRequest("Unable to create user without email address or password");

            StatementsFromTdb.addStatement(dataset, model, "http://example.com/" + user.getEmail(), type, "http://sm.example.com#User");
            StatementsFromTdb.addStatement(dataset, model, "http://example.com/" + user.getEmail(), email, user.getEmail());
            StatementsFromTdb.addStatement(dataset, model, "http://example.com/" + user.getEmail(), password, user.getPassword());
            if(user.getName() != null)
                StatementsFromTdb.addStatement(dataset, model, "http://example.com/" + user.getEmail(), name, user.getName());
            StatementsFromTdb.addStatement(dataset, model, "http://example.com/" + user.getEmail(), doctor, "0");
            if(user.getPhoneNumber() != null)
                StatementsFromTdb.addStatement(dataset, model, "http://example.com/" + user.getEmail(), phoneNumber, user.getPhoneNumber());
            if(user.getEmergencyPhone() != null)
                StatementsFromTdb.addStatement(dataset, model, "http://example.com/" + user.getEmail(), emergencyPhone, user.getEmergencyPhone());

        } else{
            if(user.getName() != null)
                StatementsFromTdb.addStatement(dataset, model, "http://example.com/" + user.getEmail(), name, user.getName());
            if(user.getPhoneNumber() != null)
                StatementsFromTdb.addStatement(dataset, model, "http://example.com/" + user.getEmail(), phoneNumber, user.getPhoneNumber());
            if(user.getEmergencyPhone() != null)
                StatementsFromTdb.addStatement(dataset, model, "http://example.com/" + user.getEmail(), emergencyPhone, user.getEmergencyPhone());
        }
        return user;
    }
}