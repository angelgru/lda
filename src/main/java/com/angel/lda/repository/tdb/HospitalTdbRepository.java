package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Hospital;
import com.angel.lda.model.Location;
import com.angel.lda.repository.HospitalRepository;
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
 * Created by Angel on 1/13/2018.
 */
@Repository
@Profile("tdb")
public class HospitalTdbRepository implements HospitalRepository {

    private final DatasetProvider datasetProvider;

    @Autowired
    public HospitalTdbRepository(DatasetProvider datasetProvider) {
        this.datasetProvider = datasetProvider;
    }

    @Override
    public List<Hospital> findAll() throws IOException {
        Dataset dataset = datasetProvider.guardedDataset();
        String query = IOUtils.toString(this.getClass().getResourceAsStream("/queries/all_hospitals.rq"), Charset.defaultCharset());

        List<Hospital> hospitals = LoadFromTdb.execQuery(dataset, query, this::prepareHospital);
        if(hospitals.isEmpty())
            throw new ResourceNotFound("There are no hospitals in the system.");
        return hospitals;
    }

    @Override
    public Hospital findOne(int hospitalId) throws IOException {
        Dataset dataset = datasetProvider.guardedDataset();
        String stringQuery = IOUtils.toString(this.getClass().getResourceAsStream("/queries/one_hospital.rq"), Charset.defaultCharset());
        String query = makeHospitalString(stringQuery, hospitalId);
        List<Hospital> hospitals = LoadFromTdb.execQuery(dataset, query, this::prepareHospital);

        if(hospitals.isEmpty())
            throw new ResourceNotFound("The hospital with the given id doesn't exist.");
        return hospitals.get(0);
    }

    private String makeHospitalString(String data, int id) {
        return String.format(data, id);
    }

    private Hospital prepareHospital(Map<String, String> m) {
        Hospital hospital = new Hospital();
        hospital.setName(m.get("name"));
        hospital.setNetworkAddress(m.get("network_address"));

        Location location = new Location();
        location.setLat(Double.valueOf(m.get("latitude")));
        location.setLongitude(Double.valueOf(m.get("longitude")));

        hospital.setLocation(location);
        return hospital;
    }
}
