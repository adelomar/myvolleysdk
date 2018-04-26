package com.example.sachin.mysdkapp.map;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class BackgroundService extends Service {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private ArrayList<LatLng> latLngs;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("---------serviceonStartCommand");

        savedatainbg();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFusedLocationProviderClient  = LocationServices.getFusedLocationProviderClient(this);;
        latLngs = new ArrayList<>();
        System.out.println("---------serviceonCreate");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent("abcd");
        intent.putParcelableArrayListExtra("key", latLngs);
        sendBroadcast(intent);
        System.out.println("---------serviceonDestroy");
    }

    public void savedatainbg(){
        try {


                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                mLocationRequest.setInterval(2000);
                mLocationRequest.setFastestInterval(2000);

                // Create LocationSettingsRequest object using location request
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
                builder.addLocationRequest(mLocationRequest);
                LocationSettingsRequest locationSettingsRequest = builder.build();

                // Check whether location settings are satisfied
                // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
                SettingsClient settingsClient = LocationServices.getSettingsClient(this);
                settingsClient.checkLocationSettings(locationSettingsRequest);


                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                // do work here
                                System.out.println("-----Locat---- "+locationResult.getLastLocation());
                                Location location = locationResult.getLastLocation();

                                latLngs.add(new LatLng(location.getLatitude(),location.getLongitude()));
                            }
                        },
                        Looper.myLooper());

        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


}
