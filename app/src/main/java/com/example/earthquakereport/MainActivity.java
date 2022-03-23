package com.example.earthquakereport;
// EarthquakeActivity
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    /** URL for earthquake data from the USGS dataset (Large)*/
//    private static final String USGS_REQUEST_URL =
//            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02";
    /**
     * URL for earthquake data from the USGS dataset
     */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    /**
     * Adapter for the list of earthquakes
     */
    private QuakeReportAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //calling the asyntask
        EarthquakeAsyncTask backgroundTask = new EarthquakeAsyncTask();
        backgroundTask.execute(USGS_REQUEST_URL);

        //referring to the list view
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        // Create a new adapter that takes an empty list of earthquakes as input
        /**
         * here empty list is first added to the adapter, so when user first open the app, the app will show empty list other than BLANK SCREEN,
         * while the app is fetching the data from the internet in the background.
         */
        mAdapter = new QuakeReportAdapter(this, new ArrayList<EarthQuake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                EarthQuake currentEarthquake = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }

    /**
     * old way, so commenting out
     */
//    private void updateUI(List<EarthQuake> earthquakesList){
//        //referring to the list view
//        ListView earthquakeListView = (ListView) findViewById(R.id.list);
//
//        //Creating an object of QuakeReportAdapter
//        final QuakeReportAdapter reportAdapter = new QuakeReportAdapter(this, earthquakesList);
//
//        //Connecting adapter with ListView
//        earthquakeListView.setAdapter(reportAdapter);
//
//        // on click : open the earth quake url in web browser
//        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                EarthQuake currentEarthquake = reportAdapter.getItem(position);
//                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
//
//                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
//                startActivity(websiteIntent);
//            }
//        });
//    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<EarthQuake>> {

        @Override
        protected List<EarthQuake> doInBackground(String... url) {
            // dont perform http request if the url is empty
            if (url.length == 0 || url[0] == null) {
                return null;
            }
            List<EarthQuake> earthquakes = QueryUtils.fetchEarthquakeData(url[0]);
            return earthquakes;
        }

        @Override
        protected void onPostExecute(List<EarthQuake> result) {
//            //if list is null, do not update ui
//            if (result == null){
//                return;
//            }
//            updateUI(result);
            /**
             * New way
             */
            //Clear the adapter of previous earthquake data
            mAdapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (result != null && !result.isEmpty()) {
                mAdapter.addAll(result);
            }
        }
    }
}