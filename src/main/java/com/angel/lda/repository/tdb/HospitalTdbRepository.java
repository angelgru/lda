package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Hospital;
import com.angel.lda.model.Location;
import com.angel.lda.repository.HospitalRepository;
import com.angel.lda.utils.StatementToObjectUtil;
import com.angel.lda.utils.StatementsFromTdb;
import org.apache.commons.io.IOUtils;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Angel on 1/13/2018.
 */
@Repository
@Profile("tdb")
public class HospitalTdbRepository implements HospitalRepository {

    private final DatasetProvider datasetProvider;
    private final LocationTdbRepository locationTdbRepository;

    @Autowired
    public HospitalTdbRepository(DatasetProvider datasetProvider, LocationTdbRepository locationTdbRepository) {
        this.datasetProvider = datasetProvider;
        this.locationTdbRepository = locationTdbRepository;
    }

    public static <T> List<T> makeResultFromQuery(QueryExecution queryExecution, Function<Map<String, String>, T> mapToResult) {

        List<Map<String, String>> solutions = new ArrayList<>();

        ResultSet res = queryExecution.execSelect();
        res.forEachRemaining(qs -> {
            Map<String, String> map = new HashMap<>();
            solutions.add(map);
            qs.varNames().forEachRemaining(vn -> {map.put(vn, qs.get(vn).toString()); System.out.println(qs.get(vn).toString());});
        });

        return solutions.stream().map(mapToResult).collect(Collectors.toList());
    }

    public static <T> List<T> execQuery(Dataset dataset, String query, Function<Map<String, String>, T> mapToResult) {
        List<T> result;
        Query selectQuery = QueryFactory.create(query);
        QueryExecution exec = QueryExecutionFactory.create(selectQuery, dataset);
        dataset.begin(ReadWrite.READ);
        result = makeResultFromQuery(exec, mapToResult);

        dataset.commit();
        return result;
    }


    @Override
    public List<Hospital> findAll() throws IOException {
        Dataset dataset = datasetProvider.guardedDataset();

        String model = "http://example.com/hospital";
        String name = "http://sm.example.com#name";
        String networkAddress = "http://sm.example.com#network_address";
        String location = "http://sm.example.com#location";

        Map<String, String> mapping=new HashMap<>();

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, null, null, null);

        mapping.put(name, "setName");
        mapping.put(networkAddress, "setNetworkAddress");
        mapping.put(location, "setTdbLocationId");

        Collection<Hospital> hospitals = StatementToObjectUtil.parseList(statements, Hospital.class, mapping);
        Iterator<Hospital> iterator = hospitals.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("There aren't any hospitals in the system.");

        List<Hospital> hospitalList = new ArrayList<>();
        Hospital hospital;

        while (iterator.hasNext()){
            hospital = iterator.next();
            int locationId = Integer.parseInt(String.valueOf(hospital.getTdbLocationId().charAt(hospital.getTdbLocationId().length()-1)));
            Location tmpLocation = locationTdbRepository.getLocation(locationId);
            if(tmpLocation != null)
                hospital.setLocation(tmpLocation);
            hospitalList.add(hospital);
        }

        return hospitalList;
    }

    @Override
    public Hospital findOne(int hospitalId) {

        Dataset dataset = datasetProvider.guardedDataset();

        String subject = "http://example.com/hospital" + hospitalId;
        String model = "http://example.com/hospital";
        String name = "http://sm.example.com#name";
        String networkAddress = "http://sm.example.com#network_address";
        String location = "http://sm.example.com#location";

        Map<String, String> mapping=new HashMap<>();

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, subject, null, null);

        mapping.put(name, "setName");
        mapping.put(networkAddress, "setNetworkAddress");
        mapping.put(location, "setTdbLocationId");

        Collection<Hospital> hospitals = StatementToObjectUtil.parseList(statements, Hospital.class, mapping);
        Iterator<Hospital> iterator = hospitals.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("There aren't any hospitals with the given id !!!");
        Hospital hospital = iterator.next();

        int locationId = Integer.parseInt(String.valueOf(hospital.getTdbLocationId().charAt(hospital.getTdbLocationId().length()-1)));
        Location tmpLocation = locationTdbRepository.getLocation(locationId);

        if(tmpLocation != null) {
            hospital.setLocation(tmpLocation);
        }

        return hospital;
    }

//    TO DO: Delete at the end of the project, will not need it anymore
//    TO DO: Give proper parameters instead of literals
//    private void createHospital(int i) {
//        Dataset dataset = datasetProvider.guardedDataset();
//
//        String model = "http://example.com/hospital";
//        String subject = "http://example.com/hospital" + i;
//        String name = "http://sm.example.com#name";
//        String networkAddress = "http://sm.example.com#network_address";
//        String location = "http://sm.example.com#location";
//
//        StatementsFromTdb.addStatement(dataset, model, subject, name, "ex:hospital");
//        StatementsFromTdb.addStatement(dataset, model, subject, networkAddress, "127.0.0.1");
//        StatementsFromTdb.addStatement(dataset, model, subject, location, "http://example.com/location1");
//    }
}
