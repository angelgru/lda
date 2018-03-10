package com.angel.lda;

/**
 * Created by Angel on 3/9/2018.
 */

import com.angel.lda.model.Hospital;
import com.angel.lda.repository.tdb.DatasetProvider;
import com.angel.lda.repository.tdb.HospitalTdbRepository;
import org.apache.commons.io.IOUtils;
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
import java.nio.charset.Charset;
import java.time.ZonedDateTime;

/**
 * @author Riste Stojanov
 */
@SuppressWarnings("Duplicates")
@RunWith(SpringRunner.class)
@ActiveProfiles("tdb")
@SpringBootTest
public class HospitalTest {

    @Autowired
    DatasetProvider datasetProvider;

    @Autowired
    HospitalTdbRepository hospitalTdbRepository;

    @Before
    public void initDataset() {
        Dataset guardedDataset = datasetProvider.guardedDataset();
        guardedDataset.begin(ReadWrite.WRITE);
        guardedDataset.asDatasetGraph().clear();
        DatasetGraph dsg = parseDataGraph(guardedDataset.asDatasetGraph(), this.getClass().getResourceAsStream("/observations.nq"), RDFLanguages.NQUADS);
        guardedDataset.commit();
    }

    @org.junit.Test
    public void test() throws IOException {
        String data = IOUtils.toString(this.getClass().getResourceAsStream("/in/template-new-hospital.trig"), Charset.defaultCharset());

        System.out.println("=============== START =========================");
        long total = 0;
//        for (int i = 0; i < 5; i++) {
//            total = createHospitalWithoutAuthentication(data);
//            System.out.println("create\t" + i + "\t" + (total / STEP));
//        }

//        List<Hospital> hospitals = hospitalTdbRepository.findAll();
//        System.out.println("Hospital TDB size : " + hospitals.size());
//        for(Hospital hospital: hospitals) {
//            System.out.println(hospital.getName() + " " + hospital.getNetworkAddress());
//        }

    }


    public long createHospitalWithoutAuthentication(String data) {
        long start = System.nanoTime();
        ZonedDateTime base = ZonedDateTime.now().minusDays(20);
        Dataset guardedDataset = getDataset();
        guardedDataset.begin(ReadWrite.WRITE);
        DatasetGraph dsg = guardedDataset.asDatasetGraph();
        String hospitalString = makeHospitalString(data, 1, "Hospital Saint P.", "127.0.0.0/64", 0);
        DatasetGraph datasetGraph = parseDataGraph(hospitalString, RDFLanguages.TRIG);
        datasetGraph.begin(ReadWrite.READ);
        datasetGraph.find().forEachRemaining(q -> dsg.add(q));
        datasetGraph.commit();
        datasetGraph.end();
        guardedDataset.commit();
        guardedDataset.end();
        return System.nanoTime() - start;
    }

    private String makeHospitalString(String data, int id, String name, String networkAddress, int location) {
        return String.format(data, id, name, networkAddress, location);
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
