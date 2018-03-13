package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Sensor;
import com.angel.lda.model.User;
import com.angel.lda.repository.SensorRepository;
import com.angel.lda.utils.StatementToObjectUtil;
import com.angel.lda.utils.StatementsFromTdb;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by Angel on 2/8/2018.
 */
@SuppressWarnings("Duplicates")
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
    public Sensor save(Sensor sensor) {
//        TO DO: Create new way for adding sensors as for all the other entities
        if(sensor.getId() >= 0){
            Dataset dataset = datasetProvider.guardedDataset();
            String model = "http://example.com/sensor";
            String subject = "http://example.com/sensor" + sensor.getId();
            String type = "http://sm.example.com#stype";
            String unit = "http://sm.example.com#unit";
            String regularFrom = "http://sm.example.com#regular_from";
            String regularTo = "http://sm.example.com#regular_to";

            if(sensor.getType() != null)
                StatementsFromTdb.addStatement(dataset, model, subject, type, sensor.getType());
            if(sensor.getUnit() != null)
                StatementsFromTdb.addStatement(dataset, model, subject, unit, sensor.getUnit());
            if(sensor.getRegularFrom() > 0)
                StatementsFromTdb.addStatement(dataset, model, subject, regularFrom, String.valueOf(sensor.getRegularFrom()));
            if(sensor.getRegularTo() > 0)
                StatementsFromTdb.addStatement(dataset, model, subject, regularTo, String.valueOf(sensor.getRegularTo()));
        } else{
            Dataset dataset = datasetProvider.guardedDataset();
            String model = "http://example.com/sensor";
            Random random = new Random();
            int idValue = random.nextInt(100000) + 100;
            String subject = "http://example.com/sensor" + idValue;
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
            StatementsFromTdb.addStatement(dataset, model, subject, id, String.valueOf(idValue));
        }

        return sensor;
    }

    @Override
    public Sensor findOne(int sensorId) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        Dataset dataset = datasetProvider.guardedDataset();

        String model = "http://example.com/sensor";
        String subject = "http://example.com/sensor" + sensorId;
        String type = "http://sm.example.com#stype";
        String unit = "http://sm.example.com#unit";
        String regularFrom = "http://sm.example.com#regular_from";
        String regularTo = "http://sm.example.com#regular_to";
        String owner = "http://sm.example.com#owner";
        String id = "http://sm.example.com#id";

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, subject, null, null);

        Map<String, String> mapping=new HashMap<>();
        mapping.put(type, "setType");
        mapping.put(unit, "setUnit");
        mapping.put(regularFrom, "setRegularFrom");
        mapping.put(regularTo, "setRegularTo");
        mapping.put(owner, "setTdbUserId");
        mapping.put(id, "setId");

        Collection<Sensor> sensors = StatementToObjectUtil.parseList(statements, Sensor.class, mapping);
        Iterator<Sensor> iterator = sensors.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("Sensor not found !");

        List<Sensor> sensorsList = new ArrayList<>();
        Sensor sensor = null;
        while(iterator.hasNext()){
            sensor = iterator.next();
            sensor.setId(sensorId);

            sensorsList.add(sensor);
        }

        return sensorsList.get(0);
    }

    @Override
    public void delete(Sensor sensorToBeDeleted) {

    }

    public Set<Sensor> getSensorsForUser(User user) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Dataset dataset = datasetProvider.guardedDataset();

        String model = "http://example.com/sensor";
        String type = "http://sm.example.com#stype";
        String unit = "http://sm.example.com#unit";
        String regularFrom = "http://sm.example.com#regular_from";
        String regularTo = "http://sm.example.com#regular_to";
        String owner = "http://sm.example.com#owner";
        String id = "http://sm.example.com#id";

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, null, null, null);

        Map<String, String> mapping=new HashMap<>();
        mapping.put(type, "setType");
        mapping.put(unit, "setUnit");
        mapping.put(regularFrom, "setRegularFrom");
        mapping.put(regularTo, "setRegularTo");
        mapping.put(owner, "setTdbUserId");
        mapping.put(id, "setId");

        Collection<Sensor> sensors = StatementToObjectUtil.parseList(statements, Sensor.class, mapping);
        Iterator<Sensor> iterator = sensors.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("Sensor not found !");

        List<Sensor> sensorsList = new ArrayList<>();
        Sensor sensor = null;
        while(iterator.hasNext()){
            sensor = iterator.next();

            if(sensor.getOwner() != null && sensor.getOwner().getEmail().equals(user.getEmail())) {
                sensor.setUsedInObservations(observationTdbRepository.getAllObservationsFromSensor(sensor.getId()));
                sensorsList.add(sensor);
            }
        }

        Set<Sensor> returnSensors = new HashSet<>();

        returnSensors.addAll(sensorsList);

        return returnSensors;
    }
}
