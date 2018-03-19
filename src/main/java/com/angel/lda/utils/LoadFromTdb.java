package com.angel.lda.utils;

import org.apache.jena.query.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LoadFromTdb {

    public static <T> List<T> makeResultFromQuery(QueryExecution queryExecution, Function<Map<String, String>, T> mapToResult) {

        List<Map<String, String>> solutions = new ArrayList<>();

        ResultSet res = queryExecution.execSelect();

        res.forEachRemaining(qs -> {
            Map<String, String> map = new HashMap<>();
            qs.varNames().forEachRemaining(vn -> map.put(vn, qs.get(vn).toString()));
            solutions.add(map);
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
}
