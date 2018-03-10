package com.angel.lda.repository.tdb;

import org.apache.jena.query.ARQ;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.tdb.TDB;
import org.apache.jena.tdb.TDBFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author Riste Stojanov
 */
@Service
public class DatasetProviderImpl implements DatasetProvider {

  @Autowired
  Environment environment;

  private Dataset guardedDataset;

  public Dataset create() {
    Dataset ds = DatasetFactory.create();
    ds.asDatasetGraph().getContext().set(TDB.symUnionDefaultGraph, true);
    return ds;
  }


  @Override
  public Dataset guardedDataset() {
    if (guardedDataset == null) {
      guardedDataset = TDBFactory.createDataset(environment.getProperty("dataset.location-path"));
      guardedDataset.asDatasetGraph().getContext().set(TDB.symUnionDefaultGraph, true);
      guardedDataset.asDatasetGraph().getContext().set(ARQ.optFilterImplicitJoin, false);
    }
    return guardedDataset;
  }

  @Override
  public Dataset guardedDataset(String graph) {
    if(guardedDataset == null) {
      guardedDataset();
    }
    Dataset dataset = (Dataset) guardedDataset().getNamedModel(graph);
    if(dataset == null)
      guardedDataset().addNamedModel(graph, guardedDataset.getDefaultModel());
    return (Dataset) guardedDataset().getNamedModel(graph);
  }
}
