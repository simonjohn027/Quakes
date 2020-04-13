package com.driven.quakes;

/**
 *
 * Author Simon John Sanga
 * S1803451
 */


import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class QuakeListActivity extends AppCompatActivity {

    private PositionService position;
    private boolean bound = false;
    public String locationName = "";
    ArrayList<Quake>arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quake_list);
        if(savedInstanceState == null){
            Intent data = getIntent();
            arrayList = data.getParcelableArrayListExtra("quakesbundle");
        }
        else{

            arrayList = savedInstanceState.getParcelableArrayList("savedData");
            locationName = savedInstanceState.getString("locationName");

        }
        if(arrayList.size() != 0) {

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("quakes",arrayList);
            QuakeListFragment listFragment = new QuakeListFragment();
            listFragment.setArguments(bundle);

        }
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);


    }

    public  ArrayList<Quake> arrayList(){
        Intent data = getIntent();
        return data.getParcelableArrayListExtra("quakesbundle");
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new QuakeListFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getText(R.string.home_tab);
                case 1:
                    return getResources().getText(R.string.quakes_tab);


            }
            return null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("savedData",arrayList);
        savedInstanceState.putString("locationName",locationName);
    }



    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, PositionService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            //Code that runs when the service is connected
            PositionService.PositionBinder positionBinder = (PositionService.PositionBinder) binder;
            position = positionBinder.getPosition();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
        }
    };

    private void getLocation() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                double distance = 0.0;
                if (bound && position != null) {
                    try {
                        locationName = position.getLocationName();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                handler.postDelayed(this, 10000);
            }
        });
    }



    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(connection);
            bound = false;
        }
    }



}
