package com.angel.lda.repository.tdb;

import org.apache.jena.query.Dataset;

/**
 * @author Riste Stojanov
 */
public interface DatasetProvider {

  Dataset create();

  Dataset guardedDataset();

  Dataset guardedDataset(String graph);
}
