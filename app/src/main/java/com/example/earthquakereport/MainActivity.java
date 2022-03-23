package com.example.earthquakereport;
// EarthquakeActivity

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {

    /**
     * Log TAG
     */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

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

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /**
     * Async task code, old way, not using anymore
        //calling the asyntask
        EarthquakeAsyncTask backgroundTask = new EarthquakeAsyncTask();
        backgroundTask.execute(USGS_REQUEST_URL);
    */
        //calling loader
        LoaderManager loaderManager = getLoaderManager();
        Log.v(LOG_TAG,"calling init loader");
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);


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

    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG,"on onCreateLoader method");
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<EarthQuake>> loader, List<EarthQuake> earthQuakes) {
        Log.v(LOG_TAG,"on onLoadFinished method");
       //clear the adapter of previous earthquake data
        mAdapter.clear();
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthQuakes != null && !earthQuakes.isEmpty()) {
            mAdapter.addAll(earthQuakes);
        }
    }
    @Override
    public void onLoaderReset(@NonNull Loader<List<EarthQuake>> loader) {
        Log.v(LOG_TAG,"on onLoaderReset method");
        //loader reset, so we can clear out our existing data
        mAdapter.clear();
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
/**
 * Async task code, old way, not using anymore
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

             //New way

            //Clear the adapter of previous earthquake data
            mAdapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (result != null && !result.isEmpty()) {
                mAdapter.addAll(result);
            }
        }
    }
 */

}