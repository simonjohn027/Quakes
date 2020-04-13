package com.driven.quakes;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.location.LocationListener;
import android.location.Location;
import android.support.v4.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PositionService extends Service {

    private LocationListener listener;
    private LocationManager locManager;
    private static Location location;
    private double latitude,longitude;
    public static final String PERMISSION_STRING = android.Manifest.permission.ACCESS_FINE_LOCATION;

    public PositionService() {
    }

    private final Binder binder = new PositionBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, PERMISSION_STRING) == PackageManager.PERMISSION_GRANTED) {
            String provider = locManager.getBestProvider(new Criteria(), true);
            if (provider != null) {
                locManager.requestLocationUpdates(provider, 1000, 1, listener);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locManager != null && listener != null) {
            if (ContextCompat.checkSelfPermission(this, PERMISSION_STRING) == PackageManager.PERMISSION_GRANTED) {
                locManager.removeUpdates(listener);
            }
            locManager = null;
            listener = null;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class PositionBinder extends Binder {

        PositionService getPosition() {
            return PositionService.this;
        }
    }

    public String getLocationName() throws IOException {
        Locale locale = new Locale("en");
        String city,country;
        //OR Locale.getDefault()
        try {
            Geocoder geocoder = new Geocoder(PositionService.this, locale);
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            city = addresses.get(0).getLocality();

            if (city == null) {
                city = addresses.get(0).getAdminArea();
            }

            country = addresses.get(0).getCountryName();
        }catch (IOException e){
            return "Cant Get Location";
        }
        String locationName = city + ", " + country;

        return locationName;
    }
}
