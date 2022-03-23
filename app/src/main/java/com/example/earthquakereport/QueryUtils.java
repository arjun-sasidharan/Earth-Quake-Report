package com.example.earthquakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    //to get usgs dataset and return the earthquake events
    public static List<EarthQuake> fetchEarthquakeData(String requestUrl) {
        Log.v(LOG_TAG,"on fetchEarthquakeData method");

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<EarthQuake> earthquakes = extractEarthquakesFromJson(jsonResponse);

        // Return the {@link Event}
        return earthquakes;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    //helper method : to covert the string url to URL type
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    //helper method : to make http request to the usgs url, and read data using input stream
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    //helper method : to convert byte date in the input stream to string using buffer reader
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }




    /**
     * Return a list of {@link EarthQuake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<EarthQuake> extractEarthquakesFromJson(String earthquakeJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<EarthQuake> earthquakes = new ArrayList<>();

        try {

            //creating a json object to track the root object
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
            // finding the json array
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            //Loop through the array
            for (int i = 0; i < earthquakeArray.length(); i++) {
                //get the ith object in the array
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                // get the properties object
                JSONObject properties = currentEarthquake.getJSONObject("properties");
                //get magnitude value
                double magnitude = properties.getDouble("mag");
                //get place value
                String location = properties.getString("place");
                //get time value
                long time = properties.getLong("time");
                //get url value
                String url = properties.getString("url");

                //adding data into array list
                earthquakes.add(new EarthQuake(magnitude, location, time, url));
            }
            // Return the list of earthquakes
            return earthquakes;

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return null;
    }
}
