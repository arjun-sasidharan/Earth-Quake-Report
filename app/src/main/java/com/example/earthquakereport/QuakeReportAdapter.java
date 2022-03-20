package com.example.earthquakereport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        //formatting the decimal magnitude
        String formattedMagnitude = formatMagnitude(currentReport.getMagnitudeInDecimal());
        magnitudeView.setText(formattedMagnitude);

        // fetching the bg element of the text view which is gradient drawable
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        //get the appropriate bg color with the help of a helper method
        int magnitudeColor = getMagnitudeColor(currentReport.getMagnitudeInDecimal());
        magnitudeCircle.setColor(magnitudeColor);
        // get the full location in a string variable
        String fullLocation = currentReport.getLocation();

        // find the textview which have id offset location
        TextView offsetLocationView = (TextView) listItemView.findViewById(R.id.offset_location);
        offsetLocationView.setText(getOffsetLocation(fullLocation));

        // find the textview which have id primary_location
        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.primary_location);
        primaryLocationView.setText(getPrimaryLocation(fullLocation));

        //create new date object from the time in millisecond
        Date dateObject = new Date(currentReport.getTimeInMilliseconds());

        // find the textview with id date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        // calling the date format method
        String formattedDate = formatDate(dateObject);
        dateView.setText(formattedDate);

        // find the textview with id time
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        // calling the time format method
        String formattedTime = formatTime(dateObject);
        timeView.setText(formattedTime);

        return listItemView;
    }


    //helper method: to find the color of each magnitude range
    private int getMagnitudeColor(double magnitudeInDecimal) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitudeInDecimal);
        switch (magnitudeFloor){
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        //magnitudeColorResourceId only contain the refernce to the color resource, not the actual color
        // inorder to get the color, use ContextCompact. getColor method
        return ContextCompat.getColor(getContext(),magnitudeColorResourceId);
    }

    //helper method :  to format the magnitude
    private String formatMagnitude(double magnitude) {
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(magnitude);
    }


    //helper method : extract offset location from full location
    private String getOffsetLocation(String fullLocation) {
        //initially search if the full location contain location offset like "74km NW of", if not append near the
        int offsetStart = fullLocation.indexOf(" of ");
        if (offsetStart!= -1) {
            // it contain offset, so split the full location to extract offset
            return fullLocation.substring(0, offsetStart + 3); //adding 3 to include the "of", indexOf return the first index
        }
        else
            return getContext().getString(R.string.near_the);
    }
    //helper method : extract primary location from full location
    private String getPrimaryLocation(String fullLocation) {
        //finding where is the offset ends
        int offsetStart = fullLocation.indexOf(" of ");
        if (offsetStart!= -1) {
            // it contain offset, so split the full location to extract primary location
            return fullLocation.substring(offsetStart + 4);
        }
        else
            return fullLocation;
    }


    //helper methods : format date
    private String formatDate(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
    //helper methods : format time
    private String formatTime(Date dateObject){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }



}
