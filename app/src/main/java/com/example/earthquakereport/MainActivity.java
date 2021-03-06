package com.example.earthquakereport;
// EarthquakeActivity

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {

    /**
     * Text view that is displayed when the list is empty
     * */
    private TextView mEmptyStateTextView;
    private ProgressBar mLoadingBar;

    /**
     * Log TAG
     */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

//    /** URL for earthquake data from the USGS dataset (Large)*/
//    private static final String USGS_REQUEST_URL =
//            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02";
    /**
     * URL for earthquake data from the USGS dataset (BASE URL)
     */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";

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

        //making reference to the Empty state text view
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        //making reference to the progress bar view
        mLoadingBar = (ProgressBar) findViewById(R.id.loading_bar);

        /**
         * Checking whether internet connection available or not, calling isNetworkAvailable() method,
         * it return true, if internet available
         */
        if (isNetworkAvailable()){
            Log.v(LOG_TAG,"Internet connection available");
        }
        else {
            Log.e(LOG_TAG,"No internet connection");
            //if internet is not conneted, then change the Empty view text view to say "internet is not connected"
            mEmptyStateTextView.setText(R.string.no_internet);
            //also hide the loading indicator
            mLoadingBar.setVisibility(View.GONE);
        }
        //calling loader even if internet available or not
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

        //specifying the empty view
        earthquakeListView.setEmptyView(mEmptyStateTextView);

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

        //Read user's latest preference
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        //buildine new url using the user preference
        Uri baseUrl = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUrl.buildUpon();

        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);


        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<EarthQuake>> loader, List<EarthQuake> earthQuakes) {
        Log.v(LOG_TAG,"on onLoadFinished method");
        // Set empty state text to display "No earthquakes found."

        //here even if internet is gone in the middle of using the app, the app shows no earthquakes found, to fix that
        if (isNetworkAvailable()){
            mEmptyStateTextView.setText(R.string.no_earthquakes);
        }
        else{
            Log.e(LOG_TAG,"No internet connection");
            mEmptyStateTextView.setText(R.string.no_internet);
        }

        //hiding the visibility of the progress bar
        mLoadingBar.setVisibility(View.GONE);

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

    /**
     * Method to check availability of internet connection
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    /**
     * overriding method to add a setting icon in the top bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}