package com.angel.lda.repository.tdb;

import com.angel.lda.model.Location;
import com.angel.lda.utils.StatementToObjectUtil;
import com.angel.lda.utils.StatementsFromTdb;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by Angel on 2/21/2018.
 */

@Repository
public class LocationTdbRepository {

    private final DatasetProvider datasetProvider;

    @Autowired
    public LocationTdbRepository(DatasetProvider datasetProvider) {
        this.datasetProvider = datasetProvider;
    }

    public Location getLocation(String id) {

        Dataset dataset = datasetProvider.guardedDataset();
        Map<String, String> mapping=new HashMap<>();

        String model = "location";
        String URI = "http://lda.finki.ukim.mk/tdb#";
        String locationId = URI + "id";
        String locationLatitude = URI + "latitude";
        String locationLongitude = URI + "longitude";

        mapping.put(locationId, "setId");
        mapping.put(locationLatitude, "setLat");
        mapping.put(locationLongitude, "setLongitude");

//        mapping ги содржи setter-ите / методите за поставување на вредност за објектот
//        statements листата се состои од сите вратени резултати од тдб базата во вид на subject predicate object

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, URI + id, null, null);


        Collection<Location> locations = StatementToObjectUtil.parseList(statements, Location.class, mapping);
        System.out.println("Statements number " + locations.size());

        Iterator<Location> iterator = locations.iterator();

        if(!iterator.hasNext())
            return null;
        return iterator.next();
    }

    public void createLocation(){
        Dataset dataset = datasetProvider.guardedDataset();

        String URI = "http://lda.finki.ukim.mk/tdb#";

        String locationModel = "location";

        String locationId = URI + "id";
        String locationLatitude = URI + "latitude";
        String locationLongitude = URI + "longitude";

        StatementsFromTdb.addStatement(dataset, locationModel, URI + "1", locationId, "1");
        StatementsFromTdb.addStatement(dataset, locationModel, URI + "1", locationLatitude, "55.48");
        StatementsFromTdb.addStatement(dataset, locationModel, URI + "1", locationLongitude, "127");
    }
}
