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

        //creating a fake list of earthquake location
        ArrayList<EarthQuake> earthquakes = new ArrayList<>();
        earthquakes.add(new EarthQuake("5.5","San Francisco","15/02/2021"));
        earthquakes.add(new EarthQuake("5.5","London","15/02/2021"));
        earthquakes.add(new EarthQuake("5.5","Tokyo","15/02/2021"));
        earthquakes.add(new EarthQuake("5.5","Mexico City","15/02/2021"));
        earthquakes.add(new EarthQuake("5.5","Moscow","15/02/2021"));
        earthquakes.add(new EarthQuake("5.5","Rio de Janeiro","15/02/2021"));
        earthquakes.add(new EarthQuake("5.5","Paris","15/02/2021"));

        //referring to the list view
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        //Creating an object of QuakeReportAdapter
        QuakeReportAdapter reportAdapter = new QuakeReportAdapter(this, earthquakes);



        //Connecting adapter with ListView
        earthquakeListView.setAdapter(reportAdapter);

    }
}