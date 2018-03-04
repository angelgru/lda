package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Sensor;
import com.angel.lda.repository.SensorRepository;
import com.angel.lda.utils.StatementToObjectUtil;
import com.angel.lda.utils.StatementsFromTdb;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by Angel on 2/8/2018.
 */
@Repository
public class SensorTdbRepository implements SensorRepository {

    private final DatasetProvider datasetProvider;

    @Autowired
    public SensorTdbRepository(DatasetProvider datasetProvider) {
        this.datasetProvider = datasetProvider;
    }

    @Override
    public Sensor save(Sensor sensor) {
        return createSensor(sensor);
    }

    @Override
    public Sensor findOne(int sensorId) {
        createSensor(null);

        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";
        String model = "sensor";
        String id = URI + "id";
        String type = URI + "type";
        String unit = URI + "unit";
        String regularFrom = URI + "regularFrom";
        String regularTo = URI + "regularTo";
        String owner = URI + "owner";

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, URI + String.valueOf(sensorId), null, null);

        Map<String, String> mapping=new HashMap<>();
        mapping.put(id, "setId");
        mapping.put(type, "setType");
        mapping.put(unit, "setUnit");
        mapping.put(regularFrom, "setRegularFrom");
        mapping.put(regularTo, "setRegularTo");
        mapping.put(owner, "setTdbUserId");

        Collection<Sensor> sensors = StatementToObjectUtil.parseList(statements, Sensor.class, mapping);
        Iterator<Sensor> iterator = sensors.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("Sensor not found !");

        List<Sensor> sensorsList = new ArrayList<>();
        Sensor sensor = null;
        while(iterator.hasNext()){
            sensor = iterator.next();
            sensorsList.add(sensor);
        }

        return sensorsList.get(0);
    }

    @Override
    public void delete(Sensor sensorToBeDeleted) {

    }

    private Sensor createSensor(Sensor sensor) {
        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String model = "sensor";
        String id = URI + "id";
        String type = URI + "type";
        String unit = URI + "unit";
        String regularFrom = URI + "regularFrom";
        String regularTo = URI + "regularTo";
        String owner = URI + "owner";

        if(sensor == null){
            StatementsFromTdb.addStatement(dataset, model, URI + "1", id, "1");
            StatementsFromTdb.addStatement(dataset, model, URI + "1", type, "pulse");
            StatementsFromTdb.addStatement(dataset, model, URI + "1", unit, "bpm");
            StatementsFromTdb.addStatement(dataset, model, URI + "1", regularFrom, "50");
            StatementsFromTdb.addStatement(dataset, model, URI + "1", regularTo, "90");
        } else{
            StatementsFromTdb.addStatement(dataset, model, URI + "2", id, "2");
            StatementsFromTdb.addStatement(dataset, model, URI + "2", type, sensor.getType());
            StatementsFromTdb.addStatement(dataset, model, URI + "2", unit, sensor.getUnit());
            StatementsFromTdb.addStatement(dataset, model, URI + "2", regularFrom, String.valueOf(sensor.getRegularFrom()));
            StatementsFromTdb.addStatement(dataset, model, URI + "2", regularTo, String.valueOf(sensor.getRegularTo()));
            StatementsFromTdb.addStatement(dataset, model, URI + "2", owner, String.valueOf(sensor.getOwner().getId()));
        }

        return sensor;
    }
}
