package com.angel.lda.repository.tdb;

import com.angel.lda.exceptions.ResourceNotFound;
import com.angel.lda.model.Hospital;
import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.repository.SensorSyncApplicationRepository;
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
@SuppressWarnings("Duplicates")
@Repository
public class SensorSyncApplicationTdbRepository implements SensorSyncApplicationRepository{

    private final DatasetProvider datasetProvider;
    private final HospitalTdbRepository hospitalTdbRepository;

    @Autowired
    public SensorSyncApplicationTdbRepository(DatasetProvider datasetProvider, HospitalTdbRepository hospitalTdbRepository) {
        this.datasetProvider = datasetProvider;
        this.hospitalTdbRepository = hospitalTdbRepository;
    }

    @Override
    public SensorSyncApplication findOne(int idS) {

        createSensorSyncApplication(null);

        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";
        String model = "sensorSyncApplication";
        String id = URI + "id";
        String nameOfApplication = URI + "nameOfApplication";
        String tdbProvidedByHospital = URI + "tdbProvidedByHospital";

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, URI + String.valueOf(idS), null, null);

        Map<String, String> mapping = new HashMap<>();
        mapping.put(id, "setId");
        mapping.put(nameOfApplication, "setNameOfApplication");
        mapping.put(tdbProvidedByHospital, "setTdbProvidedByHospital");


        Collection<SensorSyncApplication> sensorSyncApplications = StatementToObjectUtil.parseList(statements, SensorSyncApplication.class, mapping);
        Iterator<SensorSyncApplication> iterator = sensorSyncApplications.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("No applications were found !");

        List<SensorSyncApplication> sensorSyncApplicationsList = new ArrayList<>();
        SensorSyncApplication sensorSyncApplication = null;

        while(iterator.hasNext()){
            sensorSyncApplication = iterator.next();
            Hospital tmpHospital = hospitalTdbRepository.findOne(Integer.valueOf(sensorSyncApplication.getTdbProvidedByHospital()));
            if(tmpHospital != null)
                sensorSyncApplication.setProvidedByHospital(tmpHospital);
            sensorSyncApplicationsList.add(sensorSyncApplication);
        }

        return sensorSyncApplicationsList.get(0);
    }

    @Override
    public List<SensorSyncApplication> findAll() {

        createSensorSyncApplication(null);

        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";
        String model = "sensorSyncApplication";
        String id = URI + "id";
        String nameOfApplication = URI + "nameOfApplication";
        String tdbProvidedByHospital = URI + "tdbProvidedByHospital";

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, null, null, null);

        Map<String, String> mapping = new HashMap<>();
        mapping.put(id, "setId");
        mapping.put(nameOfApplication, "setNameOfApplication");
        mapping.put(tdbProvidedByHospital, "setTdbProvidedByHospital");


        Collection<SensorSyncApplication> sensorSyncApplications = StatementToObjectUtil.parseList(statements, SensorSyncApplication.class, mapping);
        Iterator<SensorSyncApplication> iterator = sensorSyncApplications.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("No applications were found !");

        List<SensorSyncApplication> sensorSyncApplicationsList = new ArrayList<>();
        SensorSyncApplication sensorSyncApplication = null;

        while(iterator.hasNext()){
            sensorSyncApplication = iterator.next();
            Hospital tmpHospital = hospitalTdbRepository.findOne(Integer.valueOf(sensorSyncApplication.getTdbProvidedByHospital()));
            if(tmpHospital != null)
                sensorSyncApplication.setProvidedByHospital(tmpHospital);
            sensorSyncApplicationsList.add(sensorSyncApplication);
        }

        return sensorSyncApplicationsList;
    }

    private SensorSyncApplication createSensorSyncApplication(SensorSyncApplication sensorSyncApplication) {
        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";
        String model = "sensorSyncApplication";
        String id = URI + "id";
        String nameOfApplication = URI + "nameOfApplication";
        String tdbProvidedByHospital = URI + "tdbProvidedByHospital";

        StatementsFromTdb.addStatement(dataset, model, URI + "1", id, "1");
        StatementsFromTdb.addStatement(dataset, model, URI + "1", nameOfApplication, "Hospital Sync App");
        StatementsFromTdb.addStatement(dataset, model, URI + "1", tdbProvidedByHospital, "1");

        return sensorSyncApplication;
    }
}
