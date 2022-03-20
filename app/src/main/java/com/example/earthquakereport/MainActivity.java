package com.example.earthquakereport;
// EarthquakeActivity
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //calling query util to parse json
        ArrayList<EarthQuake> earthquakes = QueryUtils.extractEarthquakes();

        //referring to the list view
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        //Creating an object of QuakeReportAdapter
        QuakeReportAdapter reportAdapter = new QuakeReportAdapter(this, earthquakes);



        //Connecting adapter with ListView
        earthquakeListView.setAdapter(reportAdapter);

    }
}