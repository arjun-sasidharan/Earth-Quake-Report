package com.example.earthquakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<EarthQuake>> {
    public static final String LOG_TAG = EarthquakeLoader.class.getSimpleName();

    private String mUrl;
    /**
     * @param context
     * @deprecated
     */
    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;

    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG,"on onStartLoading method");
        forceLoad();
    }

    @Nullable
    @Override
    public List<EarthQuake> loadInBackground() {
        Log.v(LOG_TAG,"on loadInBackground method");
// dont perform http request if the url is empty
        if (mUrl.length() == 0 || mUrl== null) {
            return null;
        }
        List<EarthQuake> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }

}
