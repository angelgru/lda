package com.angel.lda.utils;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel on 2/21/2018.
 */
@SuppressWarnings("Duplicates")
public class StatementsFromTdb {
    public static List<Statement> getStatements(Dataset ds, String modelName, String subject, String property, String object) {
        List<Statement> results = new ArrayList<Statement>();

        Model model = null;

        ds.begin(ReadWrite.READ);

        try{
            model = ds.getNamedModel(modelName);

            Selector selector = new SimpleSelector(
                    (subject != null) ? model.createResource(subject) : (Resource) null,
                    (property != null) ? model.createProperty(property) : (Property) null,
                    (object != null) ? model.createResource(object) : (RDFNode) null
            );

            StmtIterator it = model.listStatements(selector);

            while(it.hasNext()) {
                Statement statement = it.next();
                results.add(statement);
            }

            ds.commit();
        } finally {
            ds.end();
        }

        return results;
    }

    public static void addStatement(Dataset ds, String modelName, String subject, String property, String object){
        Model model = null;

        ds.begin(ReadWrite.WRITE);
        try{
            model = ds.getNamedModel(modelName);

            Statement statement = model.createStatement(
                    model.createResource(subject),
                    model.createProperty(property),
                    model.createResource(object)
            );
            model.add(statement);
            ds.commit();
        } finally {
            ds.end();
        }
    }
}
