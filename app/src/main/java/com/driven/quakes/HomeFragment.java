package com.driven.quakes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    ArrayList<Quake> quakeArrayList = new ArrayList<Quake>();
    Quake largestQuake = new Quake();
    Quake smallestQuake = new Quake();


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        QuakeListActivity activity = (QuakeListActivity) getActivity();
        quakeArrayList = activity.arrayList();
        String location = activity.locationName;
        int size = quakeArrayList.size();
        smallestQuake.setMagnitude(10);
        if (size != 0) {
            for (Quake quake : quakeArrayList) {
                float mag = quake.getMagnitude();
                if (largestQuake.getMagnitude() < mag) {
                    largestQuake = quake;
                }
                if (smallestQuake.getMagnitude() > mag) {
                    smallestQuake = quake;
                }
            }
        }
        TextView locationText = (TextView)view.findViewById(R.id.title_location);
        locationText.setText(location);

        TextView summaryText = (TextView)view.findViewById(R.id.info_sum);
        summaryText.setText(String.valueOf(size));

        TextView infoLargest = (TextView)view.findViewById(R.id.info_largest);
        TextView locationLargest = (TextView)view.findViewById(R.id.location_largest);
        TextView dateLargest = (TextView)view.findViewById(R.id.date_largest);
        infoLargest.setText(String.valueOf(largestQuake.getMagnitude()));
        String pattern = "EEE, d MMM yyyy";
        String datee = new SimpleDateFormat(pattern).format(largestQuake.getDate());
        dateLargest.setText(datee);
        locationLargest.setText(largestQuake.getLocation());


        TextView infoSmallest = (TextView)view.findViewById(R.id.info_smallest);
        TextView locationSmallest = (TextView)view.findViewById(R.id.location_smallest);
        TextView dateSmallest = (TextView)view.findViewById(R.id.date_smallest);
        infoSmallest.setText(String.valueOf(smallestQuake.getMagnitude()));
        String dates = new SimpleDateFormat(pattern).format(smallestQuake.getDate());
        dateSmallest.setText(dates);
        locationSmallest.setText(smallestQuake.getLocation());




    }

}
