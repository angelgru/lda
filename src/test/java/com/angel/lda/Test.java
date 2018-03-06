package com.angel.lda;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.system.ErrorHandlerFactory;
import org.apache.jena.sparql.core.DatasetGraph;
import org.junit.runner.RunWith;
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
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class Test {

  static int ITERATIONS = 100;
  static int STEP = 2500;
  static int WARM = 10;
  static int EVAL = 20;

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

  public void test() throws IOException {
    String bobIntentString = IOUtils.toString(this.getClass().getResourceAsStream("/intents/bob.jsonld"), Charset.defaultCharset());
    String johnIntentString = IOUtils.toString(this.getClass().getResourceAsStream("/intents/john.jsonld"), Charset.defaultCharset());

    Model bobIntent = extractIntent(new ImmutablePair<>(bobIntentString, Lang.JSONLD));
    Model johnIntent = extractIntent(new ImmutablePair<>(johnIntentString, Lang.JSONLD));

    String data = IOUtils.toString(this.getClass().getResourceAsStream("/in/template-new-observation.trig"), Charset.defaultCharset());
    String query = IOUtils.toString(this.getClass().getResourceAsStream("/queries/observations.rq"), Charset.defaultCharset());
    String johnsQuery = IOUtils.toString(this.getClass().getResourceAsStream("/queries/johns-observations.rq"), Charset.defaultCharset());

    ZonedDateTime base = ZonedDateTime.now().minusDays(20);
    long timestamp = base.toEpochSecond() * 1000;
    System.out.println("=============== START =========================");
    long total = 0;
    for (int i = 0; i < ITERATIONS; i++) {
      total = createObservationsWithoutAuthentication(data, STEP, i);

      // zavisnost na performansi od kolicina na podatoci
      // zavisnost na performansi od kolicina na dozvoleni (vrateni) podatoci
      // zavisnost na performansi od broj na polisi

      // Vo kolku fajlovi imam konfigurirano security uslovi
      // Kolku unit/integration testovi ke mi trebaat za da proveram tocnost na security

      System.out.println("create\t" + i + "\t" + (total / STEP));
    }
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

  public long createObservationsWithoutAuthentication(String data, int count, int iteration) {
    long start = System.nanoTime();
    ZonedDateTime base = ZonedDateTime.now().minusDays(20);
    Dataset guardedDataset = getDataset();
    guardedDataset.begin(ReadWrite.WRITE);
    DatasetGraph dsg = guardedDataset.asDatasetGraph();
    for (int i = 0; i < count; i++) {
      String observationString = makeObservationString(data, base.toEpochSecond() * 1000, iteration * count, i);

      DatasetGraph datasetGraph = parseDataGraph(
        observationString,
        RDFLanguages.TRIG);
      datasetGraph.begin(ReadWrite.READ);
      datasetGraph.find().forEachRemaining(q -> dsg.add(q));
      datasetGraph.commit();
      datasetGraph.end();
    }
    guardedDataset.commit();
    guardedDataset.end();
    return System.nanoTime() - start;
  }

  private String makeObservationString(String data, long timestamp, int base, int sensor) {
    return String.format(data, base + sensor, sensor, timestamp, 66);
  }

  public Dataset getDataset() {
    // TODO: return the dataset
    return null;
  }
}
