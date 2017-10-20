package com.rocket.sivico.Data;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.rocket.sivico.Interfaces.HandleNewLocation;

/**
 * Created by JuanCamilo on 18/10/2017.
 */

public class LocalizedActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = LocalizedActivity.class.getSimpleName();
    private GoogleApiClient mGoogleClient;
    public LatLng userPos;
    public boolean located;
    private LocationRequest mLocReq;
    public HandleNewLocation locationCallback;
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 102;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleClient);
        if (location != null) {
            handleNewLocation(location);

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient, mLocReq, new com.google.android.gms.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                handleNewLocation(location);
            }
        });
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "ConnectionSuspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    public void handleNewLocation(Location location) {
        Log.i(TAG, "New Location");
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        userPos = new LatLng(latitude, longitude);
        if (locationCallback != null) {
            locationCallback.handleNewLocation(location);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "ConnectionFailed!!");
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    public void initLocalization() {
        mGoogleClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        mLocReq = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);
        mGoogleClient.connect();
    }
}
