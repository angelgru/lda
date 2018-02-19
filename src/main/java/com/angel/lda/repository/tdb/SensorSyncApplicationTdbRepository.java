package com.angel.lda.repository.tdb;

import com.angel.lda.model.Hospital;
import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.repository.SensorSyncApplicationRepository;
import org.apache.jena.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Angel on 2/8/2018.
 */
@Repository
public class SensorSyncApplicationTdbRepository implements SensorSyncApplicationRepository{

    private final DatasetProvider datasetProvider;

    @Autowired
    public SensorSyncApplicationTdbRepository(DatasetProvider datasetProvider) {
        this.datasetProvider = datasetProvider;
    }

    public static <T> List<T> makeResultFromQuery(QueryExecution queryExecution, Function<Map<String, String>, T> mapToResult) {

        List<Map<String, String>> solutions = new ArrayList<>();

        ResultSet res = queryExecution.execSelect();
        res.forEachRemaining(qs -> {
            Map<String, String> map = new HashMap<>();
            solutions.add(map);
            qs.varNames().forEachRemaining(vn -> map.put(vn, qs.get(vn).toString()));
        });

        return solutions.stream().map(mapToResult).collect(Collectors.toList());
    }

    @SuppressWarnings("Duplicates")
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
    public SensorSyncApplication findOne(int id) {
        return null;
    }

    @Override
    public List<SensorSyncApplication> findAll() {
        Dataset dataset = datasetProvider.guardedDataset();
        String query = "SELECT DISTINCT * WHERE {}";
        List<SensorSyncApplication> result = execQuery(dataset, query, m -> {
            SensorSyncApplication app = new SensorSyncApplication();
            app.setNameOfApplication(m.get("http://lda.finki.ukim.mk/name"));
            return app;
        });
        return result;
    }
}
