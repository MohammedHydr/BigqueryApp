package com.example.bigqueryapplication;

import com.google.cloud.bigquery.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

public class TestBQ {
    public static void main(String[] args) throws IOException, InterruptedException {
        final String PROJECT_ID = "cognativex-intern";
        final String DATASET_NAME = "analysis";
        final String TABLE_NAME = "location";
        File credentialsPath = new File("cognativex-intern-e30d271d7fe8.json");

        GoogleCredentials credentials;
        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
            credentials = ServiceAccountCredentials.fromStream(serviceAccountStream);
        }

// Instantiate a client.
        BigQuery bigquery =
                BigQueryOptions.newBuilder()
                        .setCredentials(credentials)
                        .build()
                        .getService();

        TableId tableId = TableId.of(PROJECT_ID, DATASET_NAME, TABLE_NAME);

        Table table = bigquery.getTable(tableId);
        QueryJobConfiguration conf = QueryJobConfiguration.newBuilder(
                " SELECT * FROM cognativex-intern.analysis.location").setUseLegacySql(false).build();
        JobId jobId = JobId.of(UUID.randomUUID().toString());
        Job queryJob = bigquery.create(JobInfo.newBuilder(conf).setJobId(jobId).build());
        if (queryJob == null) {
            throw new RuntimeException("Job no longer exists");
        } else if (queryJob.getStatus().getError() != null) {
            // You can also look at queryJob.getStatus().getExecutionErrors() for all
            // errors, not just the latest one.
            throw new RuntimeException(queryJob.getStatus().getError().toString());
        }
        TableResult result = queryJob.getQueryResults();
        for (FieldValueList row : result.iterateAll()) {
            // String type
            String country = row.get("country").getStringValue();
            System.out.println(country);
        }
    }
}
