package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Observation;
import com.angel.lda.model.Sensor;
import com.angel.lda.repository.ObservationRepository;
import com.angel.lda.utils.LoadFromTdb;
import com.angel.lda.utils.StatementsFromTdb;
import org.apache.commons.io.IOUtils;
import org.apache.jena.query.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Angel on 2/8/2018.
 */
@Repository
@Profile("tdb")
public class ObservationTdbRepository implements ObservationRepository {

    private final DatasetProvider datasetProvider;

    @Autowired
    public ObservationTdbRepository(DatasetProvider datasetProvider) {
        this.datasetProvider = datasetProvider;
    }

    @Override
    public Observation findOne(int observationId) throws IOException {

        Dataset dataset = datasetProvider.guardedDataset();

        String stringQuery = IOUtils.toString(this.getClass().getResourceAsStream("/queries/one_observation.rq"), Charset.defaultCharset());
        String query = makeObservationString(stringQuery, observationId);
        List<Observation> observations = LoadFromTdb.execQuery(dataset, query, this::prepareObservation);
        if(observations.isEmpty())
            throw new ResourceNotFound("The observation with the given id doesn't exist in the system");
        return observations.get(0);
    }

    public List<Observation> getAllObservationsFromSensor(int id) throws IOException {
        Dataset dataset = datasetProvider.guardedDataset();

        String stringQuery = IOUtils.toString(this.getClass().getResourceAsStream("/queries/all_observations.rq"), Charset.defaultCharset());
        String query = makeAllObservationsString(stringQuery, id);
        List<Observation> observations = LoadFromTdb.execQuery(dataset, query, this::prepareObservation);
        if(observations.isEmpty())
            return null;
        return observations;
    }

    private Observation prepareObservation(Map<String, String> m) {
        Observation observation = new Observation();
        observation.setVal(m.get("val"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(m.get("time")));
        observation.setTime(calendar.getTime());

        Sensor sensor = new Sensor();
        if(m.get("regular_from") != null)
            sensor.setRegularFrom(Integer.valueOf(m.get("regular_from")));
        if(m.get("regular_to") != null)
            sensor.setRegularTo(Integer.valueOf(m.get("regular_to")));
        sensor.setType(m.get("stype"));
        sensor.setUnit(m.get("unit"));

        observation.setSensor(sensor);

        return observation;
    }

    private String makeObservationString(String data, int id) {
        return String.format(data, id);
    }

    private String makeAllObservationsString(String data, int id) {
        return String.format(data, id, id);
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
        String type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

        String sensorValue = "http://example.com/sensor" + observation.getSensor().getId();

        if(observation.getVal() != null) {
            StatementsFromTdb.addStatement(dataset, model, subject, type, "http://sm.example.com#Observation");
            StatementsFromTdb.addStatement(dataset, model, subject, val, String.valueOf(observation.getVal()));
            Calendar calendar = Calendar.getInstance();
            StatementsFromTdb.addStatement(dataset, model, subject, time, String.valueOf(calendar.getTime().getTime()));
            StatementsFromTdb.addStatement(dataset, model, subject, sensor, sensorValue);
        }
        return observation;
    }
}
