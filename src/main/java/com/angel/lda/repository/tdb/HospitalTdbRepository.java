package com.angel.lda.repository.tdb;

import com.angel.lda.model.Hospital;
import com.angel.lda.model.Location;
import com.angel.lda.repository.HospitalRepository;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel on 1/13/2018.
 */

@SuppressWarnings("Duplicates")
@Repository
public class HospitalTdbRepository implements HospitalRepository {

    private final DatasetProvider datasetProvider;

    @Autowired
    public HospitalTdbRepository(DatasetProvider datasetProvider) {
        this.datasetProvider = datasetProvider;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public List<Hospital> findAll() {

        createHospital();

        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String locationModel = "location";

        String locationId = URI + "id";
        String locationLatitude = URI + "latitude";
        String locationLongitude = URI + "longitude";

        String model = "hospital";
        String id = URI + "id";
        String name = URI + "name";
        String networkAddress = URI + "networkAddress";
        String location = URI + "location";

        List<Statement> statements = getStatements(dataset, model, null, null, null);

        List<Hospital> hospitals = new ArrayList<>();

        Hospital hospital = new Hospital();
        for(Statement statement: statements) {
            if(statement.getPredicate().toString().equals(id)){
                hospital = new Hospital();
                hospital.setId(Integer.valueOf(statement.getObject().toString()));
                System.out.println(statement.getPredicate().toString());
            } else if(statement.getPredicate().toString().equals(name)){
                hospital.setName(statement.getObject().toString());
                System.out.println(statement.getPredicate().toString());
            } else if(statement.getPredicate().toString().equals(networkAddress)) {
                hospital.setNetworkAddress(statement.getObject().toString());
                hospital.setLocation(getLocation("1", dataset));
                hospitals.add(hospital);
                System.out.println(statement.getPredicate().toString());
            }
        }
        return hospitals;
    }

    @Override
    public Hospital findOne(int hospitalId) {
        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String locationModel = "location";

        String locationId = URI + "id";
        String locationLatitude = URI + "latitude";
        String locationLongitude = URI + "longitude";

        String model = "hospital";
        String id = URI + "id";
        String name = URI + "name";
        String networkAddress = URI + "networkAddress";
        String location = URI + "location";

        List<Statement> statements = getStatements(dataset, model, URI + String.valueOf(hospitalId), null, null);

        Hospital hospital = new Hospital();
        String locId = null;
        for(Statement statement: statements) {
            if(statement.getPredicate().toString().equals(id)){
                hospital.setId(Integer.valueOf(statement.getObject().toString()));
            } else if(statement.getPredicate().toString().equals(name)){
                hospital.setName(statement.getObject().toString());
            } else if(statement.getPredicate().toString().equals(networkAddress)) {
                hospital.setNetworkAddress(statement.getObject().toString());
            } else if(statement.getPredicate().toString().equals(location)) {
                locId = statement.getObject().toString();
            }
        }

        hospital.setLocation(getLocation(locId, dataset));

        return hospital;
    }

    public void createLocation(){
        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String locationModel = "location";

        String locationId = URI + "id";
        String locationLatitude = URI + "latitude";
        String locationLongitude = URI + "longitude";

        addStatement(dataset, locationModel, URI + "1", locationId, "1");
        addStatement(dataset, locationModel, URI + "1", locationLatitude, "55.48");
        addStatement(dataset, locationModel, URI + "1", locationLongitude, "127");
    }

    public void createHospital() {
        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String model = "hospital";
        String id = URI + "id";
        String name = URI + "name";
        String networkAddress = URI + "networkAddress";
        String location = URI + "location";

        addStatement(dataset, model, URI + "2", id, "2");
        addStatement(dataset, model, URI + "2", name, "ex:hospital");
        addStatement(dataset, model, URI + "2", networkAddress, "127.0.0.1");
        addStatement(dataset, model, URI + "2", location, "1");
    }

    private Location getLocation(String id, Dataset dataset){

        String model = "location";
        String URI = "http://lda.finki.ukim.mk/tdb#";
        String locationId = URI + "id";
        String locationLatitude = URI + "latitude";
        String locationLongitude = URI + "longitude";

        List<Statement> statements = getStatements(dataset, model, URI + id, null, null);

        Location locationInstance = new Location();

        for(Statement statement: statements) {
            if(statement.getPredicate().toString().equals(locationId)){
                locationInstance.setId(Integer.valueOf(statement.getObject().toString()));
            } else if(statement.getPredicate().toString().equals(locationLatitude)){
                locationInstance.setLat(Double.valueOf(statement.getObject().toString()));
            } else if(statement.getPredicate().toString().equals(locationLongitude)) {
                locationInstance.setLongitude(Double.valueOf(statement.getObject().toString()));
            }
        }

        return locationInstance;
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
