package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Observation;
import com.angel.lda.model.Sensor;
import com.angel.lda.repository.ObservationRepository;
import com.angel.lda.utils.StatementToObjectUtil;
import com.angel.lda.utils.StatementsFromTdb;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by Angel on 2/8/2018.
 */
@Repository
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
        return createObservation(observation);
    }

    @Override
    public Observation findOne(int observationId) {

        createObservation(null);
        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String model = "observation";

        String id = URI + "id";
        String val = URI + "val";
        String time = URI + "time";

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, URI + observationId, null, null);

        Map<String, String> mapping = new HashMap<>();
        mapping.put(id, "setId");
        mapping.put(val, "setVal");
        mapping.put(time, "setTime");

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

    private Observation createObservation(Observation observation) {
        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String model = "observation";

        String id = URI + "id";
        String val = URI + "val";
        String time = URI + "time";
        String sensor = URI + "sensor";

        if(observation == null){
            StatementsFromTdb.addStatement(dataset, model, URI + "1", id, "1");
            StatementsFromTdb.addStatement(dataset, model, URI + "1", val, "55");
            StatementsFromTdb.addStatement(dataset, model, URI + "1", time, "31122017");
        } else {
            StatementsFromTdb.addStatement(dataset, model, URI + "2", id, "2");
            StatementsFromTdb.addStatement(dataset, model, URI + "2", val, String.valueOf(observation.getVal()));
            StatementsFromTdb.addStatement(dataset, model, URI + "2", time, String.valueOf(observation.getTime()));
            StatementsFromTdb.addStatement(dataset, model, URI + "2", sensor, String.valueOf(observation.getTime()));
        }

        return observation;
    }
}
