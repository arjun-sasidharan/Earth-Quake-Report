package com.example.earthquakereport;
// EarthquakeActivity
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
        final QuakeReportAdapter reportAdapter = new QuakeReportAdapter(this, earthquakes);

        //Connecting adapter with ListView
        earthquakeListView.setAdapter(reportAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                EarthQuake currentEarthquake = reportAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });

    }
}