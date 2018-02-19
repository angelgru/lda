package com.angel.lda.tests;

import com.angel.lda.repository.tdb.DatasetProvider;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Angel on 2/18/2018.
 */
public class TdbHospitalTest {

    @Autowired
    private DatasetProvider datasetProvider;

    public TdbHospitalTest() {
    }

    @Test
    public void createHospital() {
        Dataset dataset = datasetProvider.guardedDataset();
        Model model = null;
        dataset.begin(ReadWrite.WRITE);

        String subject = "http://lda.finki.ukim.mk/tdb#Hospital1";
        String property = "http://lda.finki.ukim.mk/tdb#name";
        String object = "http://lda.finki.ukim.mk/tdb#HospitalJohn";

        try{
            model = dataset.getUnionModel();

            Statement statement = model.createStatement(
                    model.createResource(subject),
                    model.createProperty(property),
                    model.createResource(object));
            model.add(statement);
            dataset.commit();
        } catch (Exception e){e.getStackTrace();}
    }
}
