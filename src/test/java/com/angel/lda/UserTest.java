package com.angel.lda;

/**
 * Created by Angel on 3/9/2018.
 */

import com.angel.lda.model.User;
import com.angel.lda.repository.tdb.DatasetProvider;
import com.angel.lda.repository.tdb.UserTdbRepository;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.system.ErrorHandlerFactory;
import org.apache.jena.sparql.core.DatasetGraph;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Riste Stojanov
 */
@SuppressWarnings("Duplicates")
@RunWith(SpringRunner.class)
@ActiveProfiles("tdb")
@SpringBootTest
public class UserTest {

    @Autowired
    DatasetProvider datasetProvider;

    @Autowired
    UserTdbRepository userTdbRepository;

    @Before
    public void initDataset() {
        Dataset guardedDataset = datasetProvider.guardedDataset();
        guardedDataset.begin(ReadWrite.WRITE);
        guardedDataset.asDatasetGraph().clear();
        DatasetGraph dsg = parseDataGraph(guardedDataset.asDatasetGraph(), this.getClass().getResourceAsStream("/observations.nq"), RDFLanguages.NQUADS);
        guardedDataset.commit();
    }

    @org.junit.Test
    public void test() throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException {
        User user = userTdbRepository.findByEmail("john@mail.com");
    }

    public static DatasetGraph parseDataGraph(String data, Lang lang) {
        DatasetGraph datasetGraph = DatasetFactory.create().asDatasetGraph();
        parseDataGraph(datasetGraph, data, lang);
        return datasetGraph;
    }

    public static void parseDataGraph(DatasetGraph datasetGraph, String data, Lang lang) {
        try (InputStream in = new ByteArrayInputStream(data.getBytes())) {
            parseDataGraph(datasetGraph, in, lang);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DatasetGraph parseDataGraph(DatasetGraph datasetGraph, InputStream in, Lang lang) {
        RDFParser.create()
                .source(in)
                .lang(lang)
                .errorHandler(ErrorHandlerFactory.errorHandlerStrict)
                .parse(datasetGraph);
        return datasetGraph;
    }

    public Dataset getDataset() {
        return datasetProvider.guardedDataset();
    }
}
