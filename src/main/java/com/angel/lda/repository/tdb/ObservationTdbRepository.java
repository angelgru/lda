package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Observation;
import com.angel.lda.model.Sensor;
import com.angel.lda.repository.ObservationRepository;
import com.angel.lda.utils.StatementToObjectUtil;
import com.angel.lda.utils.StatementsFromTdb;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Angel on 2/8/2018.
 */
@Repository
@Profile("tdb")
public class ObservationTdbRepository implements ObservationRepository {

    private final DatasetProvider datasetProvider;
    private final SensorTdbRepository sensorTdbRepository;

    @Autowired
    public ObservationTdbRepository(DatasetProvider datasetProvider, SensorTdbRepository sensorTdbRepository) {
        this.datasetProvider = datasetProvider;
        this.sensorTdbRepository = sensorTdbRepository;
    }

    @Override
    public Observation save(Observation observation) {
        Dataset dataset = datasetProvider.guardedDataset();

        String subject;
        Random random = new Random();
        subject = "http://example.com/observation" + random.nextInt(100000) + 100;
        String model = "http://example.com/observation";
        String val = "http://sm.example.com#val";
        String time = "http://sm.example.com#time";
        String sensor = "http://sm.example.com#sensor";

        String sensorValue = "http://example.com/sensor" + observation.getSensor().getId();

        StatementsFromTdb.addStatement(dataset, model, subject, val, String.valueOf(observation.getVal()));
        StatementsFromTdb.addStatement(dataset, model, subject, time, String.valueOf(observation.getTime().getTime()));
        StatementsFromTdb.addStatement(dataset, model, subject, sensor, sensorValue);
        return observation;
    }

    @Override
    public Observation findOne(int observationId) throws IllegalAccessException, InvocationTargetException, InstantiationException {

        Dataset dataset = datasetProvider.guardedDataset();

        String subject = "http://example.com/observation" + observationId;
        String model = "http://example.com/observation";
        String val = "http://sm.example.com#val";
        String time = "http://sm.example.com#time";
        String sensor = "http://sm.example.com#sensor";

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, subject, null, null);

        Map<String, String> mapping = new HashMap<>();
        mapping.put(val, "setVal");
        mapping.put(time, "setTime");
        mapping.put(sensor, "setTdbSensorId");

        Collection<Observation> observations = StatementToObjectUtil.parseList(statements, Observation.class, mapping);
        Iterator<Observation> iterator = observations.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("No observations were found!");

        List<Observation> observationsList = new ArrayList<>();
        Observation observation = null;

        while (iterator.hasNext()){
            observation = iterator.next();
            observationsList.add(observation);
        }

        return observationsList.get(0);
    }
}
