package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Hospital;
import com.angel.lda.model.Location;
import com.angel.lda.repository.HospitalRepository;
import com.angel.lda.utils.StatementToObjectUtil;
import com.angel.lda.utils.StatementsFromTdb;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by Angel on 1/13/2018.
 */
@Repository
public class HospitalTdbRepository implements HospitalRepository {

    private final DatasetProvider datasetProvider;
    private final LocationTdbRepository locationTdbRepository;

    @Autowired
    public HospitalTdbRepository(DatasetProvider datasetProvider, LocationTdbRepository locationTdbRepository) {
        this.datasetProvider = datasetProvider;
        this.locationTdbRepository = locationTdbRepository;
    }

    @Override
    public List<Hospital> findAll() {

        locationTdbRepository.createLocation();
        createHospital();

        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String model = "hospital";
        String id = URI + "id";
        String name = URI + "name";
        String networkAddress = URI + "networkAddress";
        String location = URI + "location";

        Map<String, String> mapping=new HashMap<>();

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, null, null, null);

        mapping.put(id, "setId");
        mapping.put(name, "setName");
        mapping.put(networkAddress, "setNetworkAddress");
        mapping.put(location, "setTdbLocationId");

        Collection<Hospital> hospitals = StatementToObjectUtil.parseList(statements, Hospital.class, mapping);
        Iterator<Hospital> iterator = hospitals.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("There aren't any hospitals with the given id !!!");

        List<Hospital> hospitalList = new ArrayList<>();
        Hospital hospital = null;

        while (iterator.hasNext()){
            hospital = iterator.next();
            Location tmpLocation = locationTdbRepository.getLocation(hospital.getTdbLocationId());
            if(tmpLocation != null)
                hospital.setLocation(tmpLocation);
            hospitalList.add(hospital);
        }

        return hospitalList;
    }

    @Override
    public Hospital findOne(int hospitalId) {

        locationTdbRepository.createLocation();
        createHospital();

        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String model = "hospital";
        String id = URI + "id";
        String name = URI + "name";
        String networkAddress = URI + "networkAddress";
        String location = URI + "location";

        Map<String, String> mapping=new HashMap<>();

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, URI + String.valueOf(hospitalId), null, null);

        mapping.put(id, "setId");
        mapping.put(name, "setName");
        mapping.put(networkAddress, "setNetworkAddress");
        mapping.put(location, "setTdbLocationId");

        Collection<Hospital> hospitals = StatementToObjectUtil.parseList(statements, Hospital.class, mapping);
        Iterator<Hospital> iterator = hospitals.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("There aren't any hospitals with the given id !!!");
        Hospital hospital = iterator.next();

        Location tmpLocation = locationTdbRepository.getLocation(hospital.getTdbLocationId());

        if(tmpLocation != null)
            hospital.setLocation(tmpLocation);

        return hospital;
    }

    private void createHospital() {
        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String model = "hospital";
        String id = URI + "id";
        String name = URI + "name";
        String networkAddress = URI + "networkAddress";
        String location = URI + "location";

        StatementsFromTdb.addStatement(dataset, model, URI + "1", id, "1");
        StatementsFromTdb.addStatement(dataset, model, URI + "1", name, "ex:hospital");
        StatementsFromTdb.addStatement(dataset, model, URI + "1", networkAddress, "127.0.0.1");
        StatementsFromTdb.addStatement(dataset, model, URI + "1", location, "1");
    }
}
