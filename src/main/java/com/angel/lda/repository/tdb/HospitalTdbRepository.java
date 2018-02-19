package com.angel.lda.repository.tdb;

import com.angel.lda.model.Hospital;
import com.angel.lda.repository.HospitalRepository;
import org.apache.jena.query.*;
import org.apache.jena.update.UpdateAction;
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

  private static <T> List<T> makeResultFromQuery(QueryExecution queryExecution, Function<Map<String, String>, T> mapToResult) {
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
  private static <T> List<T> execQuery(Dataset dataset, String query, Function<Map<String, String>, T> mapToResult) {
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
    dataset.begin(ReadWrite.WRITE);
    UpdateAction.parseExecute("INSERT DATA { <http://lda.finki.ukim.mk/tdb/Hospital1> <http://lda.finki.ukim.mk/tdb/name> 'St.Joseph'. }", dataset);
    UpdateAction.parseExecute("INSERT DATA { <http://lda.finki.ukim.mk/tdb/Hospital1> <http://lda.finki.ukim.mk/tdb/id> '1'. }", dataset);
    UpdateAction.parseExecute("INSERT DATA { <http://lda.finki.ukim.mk/tdb/Hospital1> <http://lda.finki.ukim.mk/tdb/networkAddress> '127.0.0.1'. }", dataset);
//    Како да ја поставам вредноста за location бидејќи е објект
    dataset.commit();

//    dataset = datasetProvider.guardedDataset();
//    dataset.begin(ReadWrite.READ);
//    Model model = dataset.getDefaultModel();
//    QueryExecution queryExecution = QueryExecutionFactory.create("SELECT * WHERE {}", model);
//    ResultSet resultSet = queryExecution.execSelect();
//    while(resultSet.hasNext()){
//      QuerySolution solution = resultSet.nextSolution();
//      while(solution.varNames().hasNext()){
//        System.out.println(solution.varNames().next());
//      }
//    }
//

//    String queryString = "SELECT * {} ";
//    dataset.begin(ReadWrite.READ);
//    try (QueryExecution queryExecution = QueryExecutionFactory.create(queryString, dataset)) {
//      ResultSet resultSet = queryExecution.execSelect();
//      ResultSetFormatter.out(resultSet);
//    } finally {
//      dataset.close();
//    }

    List<Hospital> result = execQuery(dataset, "SELECT * WHERE {}", m -> {
      Hospital h = new Hospital();
      h.setName(m.get("http://lda.finki.ukim.mk/tdb/name"));
      return h;
    });

    return result;
  }

  @Override
  public Hospital findOne(int hospitalId) {
    Dataset dataset = datasetProvider.guardedDataset();

    String queryText = "SELECT * WHERE {?subject <http://lda.finki.ukim.mk/tdb/id> " + String.valueOf(hospitalId) + "}";

    List<Hospital> result = execQuery(dataset, "SELECT * WHERE {}", m -> {
      Hospital h = new Hospital();
      h.setName(m.get("http://lda.finki.ukim.mk/tdb/name"));
//      h.setId(Integer.valueOf(m.get("http://lda.finki.ukim.mk/tdb/id")));
      h.setNetworkAddress(m.get("http://lda.finki.ukim.mk/tdb/networkAddress"));
      return h;
    });

    return result.get(0);
  }
}
