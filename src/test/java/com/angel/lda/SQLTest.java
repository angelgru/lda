package com.angel.lda;

import com.angel.lda.model.Observation;
import com.angel.lda.model.Sensor;
import com.angel.lda.model.Treatment;
import com.angel.lda.model.User;
import com.angel.lda.repository.ObservationRepository;
import com.angel.lda.repository.tdb.DatasetProvider;
import com.angel.lda.service.HospitalService;
import com.angel.lda.service.SensorService;
import com.angel.lda.service.TreatmentService;
import com.angel.lda.service.UserService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.system.ErrorHandlerFactory;
import org.apache.jena.sparql.core.DatasetGraph;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

@SuppressWarnings("Duplicates")
@RunWith(SpringRunner.class)
@ActiveProfiles("jpa")
@SpringBootTest
public class SQLTest {

    @Autowired
    DatasetProvider datasetProvider;

    @Autowired
    HospitalService hospitalService;

    @Autowired
    TreatmentService treatmentService;

    @Autowired
    SensorService sensorService;

    @Autowired
    ObservationRepository observationRepository;

    @Autowired
    UserService userService;

    static int ITERATIONS = 100;
    static int STEP = 2500;
    static int WARM = 10;
    static int EVAL = 20;

    @org.junit.Test
    public void test() throws Exception {
        String bobIntentString = IOUtils.toString(this.getClass().getResourceAsStream("/intents/bob.jsonld"), Charset.defaultCharset());
        String johnIntentString = IOUtils.toString(this.getClass().getResourceAsStream("/intents/john.jsonld"), Charset.defaultCharset());

        Model bobIntent = extractIntent(new ImmutablePair<>(bobIntentString, Lang.JSONLD));
        Model johnIntent = extractIntent(new ImmutablePair<>(johnIntentString, Lang.JSONLD));

        StmtIterator iterator = bobIntent.listStatements();

        String ipAddress = null;
        String username = null;

        while(iterator.hasNext()) {
            Statement s = iterator.nextStatement();
            Property property = s.getPredicate();
            if(property.toString().equals("http://int.example.com#ip_value")) {
                ipAddress = s.getObject().toString();
            } else if(property.toString().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#id")){
                username = s.getObject().toString();
            }
        }

        System.out.println("=============== START =========================");
        long total = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            total = createObservationsWithoutAuthentication(STEP, i);
            System.out.println("create\t" + i + "\t" + (total / STEP));

            Authentication authentication = new UsernamePasswordAuthenticationToken("bob", "bob");
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            eval("getAllHospitals", i, WARM/2, EVAL/2, j -> hospitalService.getAllHospitals());
            eval("getAllUnclaimedTreatments", i, WARM/2, EVAL/2, j -> treatmentService.getAllNonTakenTreatments());
            eval("getCompleted", i, WARM/2, EVAL/2, j -> treatmentService.getCompletedTreatmentsAcceptedByCurrentlyLoggedInDoctor());
            eval("getAccepted", i, WARM/2, EVAL/2, j -> treatmentService.getAllTreatmentsAcceptedByCurrentlyLoggedInDoctor());

            Treatment newTreatment = new Treatment();
            newTreatment.setDiagnosis("test diagnosis");
            String ip = ipAddress;
            eval("updateTreatment", i, WARM/2, EVAL/2, j -> treatmentService.updateTreatment(newTreatment, 1, ip));

            Treatment treatment = new Treatment();
            treatment.setFrom(new Date());
            treatment.setPatientRequest("Test treatment");
            User user = userService.getUserByEmail("bob@mail.com");
            treatment.setForPatient(user);
            eval("createTreatment", i,WARM/2, EVAL/2, j -> treatmentService.createTreatment(treatment));

        }
    }

    public <T>void eval(String label, int iteration, int warmCycles, int evalCycles, Evaluation<T> consumer) throws Exception {

        long start, size = 0;
        for (int i = 0; i < warmCycles; i++) {
            List result = consumer.evaluate(i);
            size = result.size();
        }

        start = System.nanoTime();
        for (int i = 0; i < evalCycles; i++) {
            consumer.evaluate(i);
        }
        long total = (System.nanoTime() - start);
        System.out.println(String.format("%s\t%d\t%d\t%d", label, iteration, size, (total / evalCycles)));
    }

    interface Evaluation<T> {
        List<T> evaluate(int i) throws Exception;
    }

    public Model extractIntent(Pair<String, Lang> environment) {
        Model model = ModelFactory.createDefaultModel();
        try (InputStream in = new ByteArrayInputStream(environment.getLeft().getBytes())) {
            RDFParser.create()
                    .source(in)
                    .lang(environment.getRight())
                    .errorHandler(ErrorHandlerFactory.errorHandlerStrict)
                    .parse(model.getGraph());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model;
    }

    public long createObservationsWithoutAuthentication(int count, int iteration) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        long start = System.nanoTime();

        for (int i = 0; i < count; i++) {
            Observation observation = new Observation();
            Sensor sensor = sensorService.findSensor(1);
            observation.setSensor(sensor);
            observation.setVal("66");
            observation.setTime(new Date());
            observationRepository.save(observation);
        }

        return System.nanoTime() - start;
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
