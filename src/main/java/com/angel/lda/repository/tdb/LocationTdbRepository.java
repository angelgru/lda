package com.angel.lda.repository.tdb;

import com.angel.lda.model.Location;
import com.angel.lda.utils.StatementToObjectUtil;
import com.angel.lda.utils.StatementsFromTdb;
import org.apache.jena.base.Sys;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by Angel on 2/21/2018.
 */

@Repository
@Profile("tdb")
public class LocationTdbRepository {

    private final DatasetProvider datasetProvider;

    @Autowired
    public LocationTdbRepository(DatasetProvider datasetProvider) {
        this.datasetProvider = datasetProvider;
    }

    public Location getLocation(int id) {

        String subject = "http://example.com/location" + id;
        String model = "http://example.com/location";
        String locationLatitude = "http://sm.example.com#latitude";
        String locationLongitude = "http://sm.example.com#longitude";

        Dataset dataset = datasetProvider.guardedDataset();
        Map<String, String> mapping=new HashMap<>();

        mapping.put(locationLatitude, "setLat");
        mapping.put(locationLongitude, "setLongitude");

//        mapping ги содржи setter-ите / методите за поставување на вредност за објектот
//        statements листата се состои од сите вратени резултати од тдб базата во вид на subject predicate object

        List<Statement> statements = StatementsFromTdb.getStatements(dataset, model, subject, null, null);

        Collection<Location> locations = StatementToObjectUtil.parseList(statements, Location.class, mapping);

        Iterator<Location> iterator = locations.iterator();
        if(!iterator.hasNext())
            return null;
        Location location = iterator.next();
        System.out.println("LOCATION:" + location.getLat());
        return location;
    }

    //    TO DO: Give proper parameters instead of literals
    public void createLocation(int locationId){
        Dataset dataset = datasetProvider.guardedDataset();

        String subject = "http://example.com/location" + locationId;
        String model = "http://example.com/location";
        String locationLatitude = "http://sm.example.com#latitude";
        String locationLongitude = "http://sm.example.com#longitude";
        String type = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

        StatementsFromTdb.addStatement(dataset, model, subject, type, "http://sm.example.com#Location");
        StatementsFromTdb.addStatement(dataset, model, subject, locationLatitude, "55.48");
        StatementsFromTdb.addStatement(dataset, model, subject, locationLongitude, "127");
    }
}
