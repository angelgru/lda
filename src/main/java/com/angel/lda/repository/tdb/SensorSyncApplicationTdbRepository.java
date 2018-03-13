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
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by Angel on 2/8/2018.
 */
@SuppressWarnings("Duplicates")
@Repository
@Profile("tdb")
public class SensorSyncApplicationTdbRepository implements SensorSyncApplicationRepository{

    private final DatasetProvider datasetProvider;
    private final HospitalTdbRepository hospitalTdbRepository;

    @Autowired
    public SensorSyncApplicationTdbRepository(DatasetProvider datasetProvider, HospitalTdbRepository hospitalTdbRepository) {
        this.datasetProvider = datasetProvider;
        this.hospitalTdbRepository = hospitalTdbRepository;
    }

    @Override
    public SensorSyncApplication findOne(int sensorId) {

        Dataset dataset = datasetProvider.guardedDataset();

        String model = "http://example.com/ssa";
        String subject = "http://example.com/ssa" + sensorId;
        String nameOfApplication = "http://sm.example.com#name";
        String tdbProvidedByHospital = "http://sm.example.com#provided_by";

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, subject, null, null);

        Map<String, String> mapping = new HashMap<>();
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
            int hospitalId = Integer.parseInt(String.valueOf(sensorSyncApplication.getTdbProvidedByHospital().charAt(sensorSyncApplication.getTdbProvidedByHospital().length()-1)));
            Hospital tmpHospital = hospitalTdbRepository.findOne(hospitalId);
            if(tmpHospital != null)
                sensorSyncApplication.setProvidedByHospital(tmpHospital);
            sensorSyncApplicationsList.add(sensorSyncApplication);
        }

        return sensorSyncApplicationsList.get(0);
    }

    @Override
    public List<SensorSyncApplication> findAll() {

        Dataset dataset = datasetProvider.guardedDataset();

        String model = "http://example.com/ssa";
        String nameOfApplication = "http://sm.example.com#name";
        String tdbProvidedByHospital = "http://sm.example.com#provided_by";

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, null, null, null);

        Map<String, String> mapping = new HashMap<>();
        mapping.put(nameOfApplication, "setNameOfApplication");
        mapping.put(tdbProvidedByHospital, "setTdbProvidedByHospital");

        Collection<SensorSyncApplication> sensorSyncApplications = StatementToObjectUtil.parseList(statements, SensorSyncApplication.class, mapping);
        Iterator<SensorSyncApplication> iterator = sensorSyncApplications.iterator();

        if(!iterator.hasNext())
            throw new ResourceNotFound("No applications were found !");

        List<SensorSyncApplication> sensorSyncApplicationsList = new ArrayList<>();
        SensorSyncApplication sensorSyncApplication;

        while(iterator.hasNext()){
            sensorSyncApplication = iterator.next();
            int hospitalId = Integer.parseInt(String.valueOf(sensorSyncApplication.getTdbProvidedByHospital().charAt(sensorSyncApplication.getTdbProvidedByHospital().length()-1)));
            Hospital tmpHospital = hospitalTdbRepository.findOne(hospitalId);
            if(tmpHospital != null)
                sensorSyncApplication.setProvidedByHospital(tmpHospital);
            sensorSyncApplicationsList.add(sensorSyncApplication);
        }

        return sensorSyncApplicationsList;
    }

//    TO DO : Delete after completing project
//
//    private SensorSyncApplication createSensorSyncApplication(SensorSyncApplication sensorSyncApplication) {
//        Dataset dataset = datasetProvider.guardedDataset();
//
//        String model = "http://example.com/ssa";
//        String subject = "http://example.com/ssa" + sensorSyncApplication.getId();
//        String nameOfApplication = "http://sm.example.com#name";
//        String tdbProvidedByHospital = "http://sm.example.com#provided_by";
//
//        StatementsFromTdb.addStatement(dataset, model, subject, nameOfApplication, sensorSyncApplication.getNameOfApplication());
//        StatementsFromTdb.addStatement(dataset, model, subject, tdbProvidedByHospital, sensorSyncApplication.getTdbProvidedByHospital());
//
//        return sensorSyncApplication;
//    }
}
