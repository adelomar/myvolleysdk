package com.example.sachin.mysdkapp.map;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sachin.mysdkapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DrawLines extends AppCompatActivity implements OnMapReadyCallback {

    private Button button;
    private Context context;

    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 99;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private GoogleMap mMap;
    private Location mLastKnownLocation;
    private ArrayList<LatLng> latLngs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        context = this;

        IntentFilter intentFilter = new IntentFilter("abcd");
        registerReceiver(retrieveData,intentFilter);


        button = (Button) findViewById(R.id.button);
        if(checkifServiceIsRunning()){
            button.setText("stop".toUpperCase());
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (button.getText().toString().toLowerCase().equalsIgnoreCase("start")) {
                    button.setText("stop".toUpperCase());

                    Intent intent
                            = new Intent(DrawLines.this,BackgroundService.class);
                    startService(intent);

                } else {
                    button.setText("start".toUpperCase());

                    Intent intent
                            = new Intent(DrawLines.this,BackgroundService.class);
                    stopService(intent);


                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);


        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationPermission();


        mapFragment.getMapAsync(this);

    }

    private void stylePolyline(Polyline polyline) {
        String type = "";
        // Get the data object stored with the polyline.
        if (polyline.getTag() != null) {
            type = polyline.getTag().toString();
        }

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "A":
                // Use a custom bitmap as the cap at the start of the line.
//                polyline.setStartCap(
//                        new CustomCap(
//                             //   BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher), 10));
//
//                polyline.setEndCap(
//                        new CustomCap(
//                                BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher), 10));
                break;
            case "B":
                // Use a round cap at the start of the line.
                polyline.setStartCap(new RoundCap());
                break;
        }

        polyline.setEndCap(new RoundCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(COLOR_BLACK_ARGB);
        polyline.setJointType(JointType.ROUND);
    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

//        LatLng sydney = new LatLng(19.474284, 72.829854);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title("Marker in Virar"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));




        mMap = googleMap;

        // Do other setup activities here too, as described elsewhere in this tutorial.

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getDeviceLocation();
//            }
//        }, 10000);


        System.out.println("------mapinitialised------");
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), 4));


                            System.out.println("-----currentLocation----- " + mLastKnownLocation.getLatitude() + " , " + mLastKnownLocation.getLongitude());
                        } else {
                            Log.d("tag", "Current location is null. Using defaults.");
                            Log.e("tag", "Exception: %s", task.getException());
                            //   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 4));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });

                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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

                final Looper looper = null;
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                // do work here
                                System.out.println("-----Locat---- " + locationResult.getLastLocation());
//
                                Location location = locationResult.getLastLocation();

                                latLngs.add(new LatLng(location.getLatitude(),location.getLongitude()));
                            }
                        },
                        looper.myLooper());


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                                        .clickable(true)
                                        .addAll(latLngs));
                                // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.474284, 72.829854), 4));
                                polyline1.setTag("A");
                                stylePolyline(polyline1);
                        System.out.println("-----pol---- ");
                    }
                }, 11000);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private BroadcastReceiver retrieveData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null ) {
              latLngs =   intent.getParcelableArrayListExtra("key");
                Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .addAll(latLngs));
                // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.474284, 72.829854), 4));
                polyline1.setTag("A");
                stylePolyline(polyline1);
            }
        }
    };



    public boolean checkifServiceIsRunning(){
        boolean isServiceRunning=false;

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            System.out.println("------services----- "+runningServiceInfo.service.getClassName());
            if (runningServiceInfo.service.getClassName().equals(BackgroundService.class.getName())){
                isServiceRunning= true;
            }
        }

        return isServiceRunning;
    }
}

