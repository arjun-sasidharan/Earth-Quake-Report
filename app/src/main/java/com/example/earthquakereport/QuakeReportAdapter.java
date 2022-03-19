package com.example.earthquakereport;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QuakeReportAdapter extends ArrayAdapter<EarthQuake> {


    //constructor
    public QuakeReportAdapter(Context context, List<EarthQuake> earthquakes) {
        //here second argument which is used to specify the default layout like simple_list_item1, but here we are
        //using custom layout, so put 0 there
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //check if the existing view (convertView) is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.quake_report_list_item, parent, false);
        }
        //get the object located at the "position" in the list
        EarthQuake currentReport = getItem(position);

        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);
        magnitudeView.setText(currentReport.getMagnitude());

        TextView locationView = (TextView) listItemView.findViewById(R.id.location);
        locationView.setText(currentReport.getLocation());

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText(currentReport.getDate());

        return listItemView;
    }
}
