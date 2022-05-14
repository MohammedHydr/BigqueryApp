package com.example.bigqueryapplication.BigQueryRead;
import com.google.cloud.bigquery.*;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import java.io.IOException;

import com.example.bigqueryapplication.LocationAnalysis.Location;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:63342", maxAge = 3600)
@RestController
public class ReadQuery{

    final String PROJECT_ID = "cognativex-intern";
    final String DATASET_NAME = "analysis";
    final String TABLE_NAME = "location";


    @RequestMapping("/data/view")
    public ArrayList<Location> readQuery(){
//        File credentialsPath = new File("cognativex-intern-e30d271d7fe8.json");
//
//        GoogleCredentials credentials;
//        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
//            credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
//        }



        // Instantiate a client.
        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
//                BigQueryOptions.newBuilder()
//                       // .setCredentials(credentials)
//                        .build()
//                        .getd();

        Dataset dataset = bigquery.getDataset(DATASET_NAME);

        TableId tableId = TableId.of(PROJECT_ID, DATASET_NAME, TABLE_NAME);

        Table table = bigquery.getTable(tableId);
        QueryJobConfiguration conf = QueryJobConfiguration.newBuilder(
                " SELECT * FROM " + DATASET_NAME + "." + TABLE_NAME + "  ").build();

        try{
            Iterable<FieldValueList> rows = dataset.getBigQuery().query(conf).iterateAll();
            ArrayList<Location> locations = new ArrayList<>();
            for(FieldValueList singleRow : rows){

                Location locationData = new Location();
                locationData.setCountryName(singleRow.get("country").getStringValue());
                locationData.setCount(singleRow.get("count").getNumericValue().intValue());

                locations.add(locationData);
            }
            ;
            return locations;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            return null;
    }
}
