package com.driven.quakes;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
public class QuakeDetailActivity extends AppCompatActivity {
    public static final String EXTRA_QUAKE_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quake_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        int quakeID = (Integer)getIntent().getExtras().get(EXTRA_QUAKE_ID);
        ArrayList<Quake> quakeArrayList = getIntent().getExtras().getParcelable("quake");
        Quake quake = quakeArrayList.get(quakeID);
        TextView magnitudeText = (TextView) findViewById(R.id.quake_magnitude);
        TextView dateText = (TextView) findViewById(R.id.quake_date);
        TextView locationText = (TextView) findViewById(R.id.quake_location);
        TextView latsText = (TextView) findViewById(R.id.quake_lats);
        TextView depthText = (TextView) findViewById(R.id.quake_depth);
        magnitudeText.setText(String.valueOf(quake.getMagnitude()));
        String pattern = "EEE, d MMM yyyy";
        String datee = new SimpleDateFormat(pattern).format(quake.getDate());
        dateText.setText(datee);
        String lat = quake.getLat();
        String lon  = quake.getLon();
        locationText.setText("Lat: " + lat + "," + " Long: " + lon );
        latsText.setText(quake.getMagnitude() + " R");
        depthText.setText("Depth " + quake.getDepth() + "Km");
    }



}
