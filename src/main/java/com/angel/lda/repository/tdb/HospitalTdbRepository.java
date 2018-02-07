package com.angel.lda.repository.tdb;

import com.angel.lda.model.Hospital;
import com.angel.lda.repository.HospitalRepository;
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
 * Created by Angel on 1/13/2018.
 */

@Repository
public class HospitalTdbRepository implements HospitalRepository {

  private final DatasetProvider datasetProvider;

  @Autowired
  public HospitalTdbRepository(DatasetProvider datasetProvider) {
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
  public List<Hospital> findAll() {
    Dataset dataset = datasetProvider.guardedDataset();
    List<Hospital> result = execQuery(dataset, "todo: query", m -> {
      Hospital h = new Hospital();
      h.setName(m.get("http://lda.finki.ukim.mk/name"));
      return h;
    });
    return result;
  }

  @Override
  public Hospital findOne(int hospitalId) {
    return null;
  }
}
