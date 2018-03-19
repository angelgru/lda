package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Observation;
import com.angel.lda.model.Sensor;
import com.angel.lda.model.User;
import com.angel.lda.repository.SensorRepository;
import com.angel.lda.utils.LoadFromTdb;
import com.angel.lda.utils.StatementsFromTdb;
import org.apache.commons.io.IOUtils;
import org.apache.jena.query.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Angel on 2/8/2018.
 */
@Repository
@Profile("tdb")
public class SensorTdbRepository implements SensorRepository {

    private final DatasetProvider datasetProvider;
    private final ObservationTdbRepository observationTdbRepository;

    @Autowired
    public SensorTdbRepository(DatasetProvider datasetProvider, ObservationTdbRepository observationTdbRepository) {
        this.datasetProvider = datasetProvider;
        this.observationTdbRepository = observationTdbRepository;
    }

    @Override
    public Sensor findOne(int sensorId) throws IOException {
        Dataset dataset = datasetProvider.guardedDataset();
        String data = IOUtils.toString(this.getClass().getResourceAsStream("/queries/one_sensor.rq"), Charset.defaultCharset());
        String query = makeSensorStringTwoValues(data, sensorId);


        List<Sensor> sensors = LoadFromTdb.execQuery(dataset, query, this::prepareSensor);
        if(sensors.isEmpty())
            throw new ResourceNotFound("There is no sensor with the given id");

        List<Observation> observations = observationTdbRepository.getAllObservationsFromSensor(sensorId);
        if(observations != null)
            sensors.get(0).setUsedInObservations(new HashSet<>(observations));

        return sensors.get(0);
    }

    public Set<Sensor> getSensorsForUser(User user) throws IOException {
        Dataset dataset = datasetProvider.guardedDataset();
        String data = IOUtils.toString(this.getClass().getResourceAsStream("/queries/sensors_for_users.rq"), Charset.defaultCharset());
        String query = makeSensorString(data, user.getEmail());

        List<Sensor> sensors = LoadFromTdb.execQuery(dataset, query, this::prepareSensor);
        if(sensors.isEmpty())
            throw new ResourceNotFound("There are no sensors for the given user");

        for (Sensor sensor: sensors) {
            List<Observation> observations = observationTdbRepository.getAllObservationsFromSensor(sensor.getId());
            if(observations != null)
                sensor.setUsedInObservations(new HashSet<>(observations));
        }

        return new HashSet<>(sensors);
    }

    private <E>String makeSensorString(String data, E replace) {
        return String.format(data, replace);
    }

    private <E>String makeSensorStringTwoValues(String data, E replace) {
        return String.format(data, replace, replace);
    }

    private Sensor prepareSensor(Map<String, String> m) {
        Sensor sensor = new Sensor();
        if(m.get("regular_from") != null)
            sensor.setRegularFrom(Integer.valueOf(m.get("regular_from")));
        if(m.get("regular_to") != null)
            sensor.setRegularTo(Integer.valueOf(m.get("regular_to")));
        sensor.setType(m.get("stype"));
        sensor.setUnit(m.get("unit"));

        return sensor;
    }

    @Override
    public Sensor save(Sensor sensor) {

        Dataset dataset = datasetProvider.guardedDataset();
        String model = "http://example.com/sensor";
        Random random = new Random();
        int generatedSensorId = random.nextInt(100000) + 100;

        String subject = "http://example.com/sensor" + generatedSensorId;
        String type = "http://sm.example.com#stype";
        String unit = "http://sm.example.com#unit";
        String regularFrom = "http://sm.example.com#regular_from";
        String regularTo = "http://sm.example.com#regular_to";
        String owner = "http://sm.example.com#owner";
        String id = "http://sm.example.com#id";

        if(sensor.getType() != null)
            StatementsFromTdb.addStatement(dataset, model, subject, type, sensor.getType());
        if(sensor.getUnit() != null)
            StatementsFromTdb.addStatement(dataset, model, subject, unit, sensor.getUnit());
        if(sensor.getRegularFrom() > 0)
            StatementsFromTdb.addStatement(dataset, model, subject, regularFrom, String.valueOf(sensor.getRegularFrom()));
        if(sensor.getRegularTo() > 0)
            StatementsFromTdb.addStatement(dataset, model, subject, regularTo, String.valueOf(sensor.getRegularTo()));
        StatementsFromTdb.addStatement(dataset, model, subject, owner, "http://example.com/" + sensor.getOwner().getEmail());
        StatementsFromTdb.addStatement(dataset, model, subject, id, String.valueOf(generatedSensorId));

        return sensor;
    }
}
