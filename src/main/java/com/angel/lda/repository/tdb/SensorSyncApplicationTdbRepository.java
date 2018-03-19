package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Hospital;
import com.angel.lda.model.Location;
import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.repository.SensorSyncApplicationRepository;
import com.angel.lda.utils.LoadFromTdb;
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
public class SensorSyncApplicationTdbRepository implements SensorSyncApplicationRepository{

    private final DatasetProvider datasetProvider;

    @Autowired
    public SensorSyncApplicationTdbRepository(DatasetProvider datasetProvider) {
        this.datasetProvider = datasetProvider;
    }

    @Override
    public SensorSyncApplication findOne(int sensorId) throws IOException {

        Dataset dataset = datasetProvider.guardedDataset();
        String data = IOUtils.toString(this.getClass().getResourceAsStream("/queries/one_sensor_sync_application.rq"), Charset.defaultCharset());
        String query = makeSensorSyncApplicationString(data, sensorId);

        List<SensorSyncApplication> sensorSyncApplications = LoadFromTdb.execQuery(dataset, query, this::prepareApplication);
        if(sensorSyncApplications.isEmpty())
            throw new ResourceNotFound("There are no sensor sync applications registered in the system!");

        return sensorSyncApplications.get(0);
    }

    @Override
    public List<SensorSyncApplication> findAll() throws IOException {
        Dataset dataset = datasetProvider.guardedDataset();
        String query = IOUtils.toString(this.getClass().getResourceAsStream("/queries/all_sensor_sync_applications.rq"), Charset.defaultCharset());

        List<SensorSyncApplication> sensorSyncApplications = LoadFromTdb.execQuery(dataset, query, this::prepareApplication);
        if(sensorSyncApplications.isEmpty())
            throw new ResourceNotFound("There are no sensor sync applications registered in the system!");

        return sensorSyncApplications;
    }

    private String makeSensorSyncApplicationString(String data, int id) {
        return String.format(data, id);
    }

    private SensorSyncApplication prepareApplication(Map<String, String> m) {
        SensorSyncApplication sensorSyncApplication = new SensorSyncApplication();
        sensorSyncApplication.setNameOfApplication(m.get("name"));

        Hospital hospital = new Hospital();
        hospital.setName(m.get("hospital_name"));
        hospital.setNetworkAddress(m.get("network_address"));

        Location location = new Location();
        location.setLongitude(Double.valueOf(m.get("longitude")));
        location.setLat(Double.valueOf(m.get("latitude")));

        hospital.setLocation(location);
        sensorSyncApplication.setProvidedByHospital(hospital);

        return sensorSyncApplication;
    }
}
