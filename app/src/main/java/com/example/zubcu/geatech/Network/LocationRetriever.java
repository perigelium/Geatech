package com.example.zubcu.geatech.Network;

import com.example.zubcu.geatech.Interfaces.LocationRetrievedEvents;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class LocationRetriever implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener
{
    // LogCat tag
    //private static final String TAG = LocationRetriever.class.getSimpleName();

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = true;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 1000; // 10 sec
    private static int FATEST_INTERVAL = 500; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    Context context;

    private LocationRetrievedEvents callback;

    public LocationRetriever(Context context, LocationRetrievedEvents callback)
    {
        this.context = context;
        this.callback = callback;

        createLocationRequest();

        if (checkPlayServices())
        {
            buildGoogleApiClient();
        }

        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.connect();
        }
    }

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private boolean checkPlayServices()
    {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);
        if (result != ConnectionResult.SUCCESS)
        {
            if (googleAPI.isUserResolvableError(result))
            {
                //Toast.makeText(context, "context device is not supported.", Toast.LENGTH_LONG).show();
                return false;
            }

            return false;
        }

        return true;
    }

    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    protected void startLocationUpdates()
    {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        Log.i("DEBUG", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0)
    {
        if (mRequestingLocationUpdates)
        {
            startLocationUpdates();
        }

        if (callback != null)
        {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null)
            {
                callback.onLocationReceived();
                stopLocationUpdates();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int arg0)
    {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
    }

    public Location getLastLocation()
    {
        return mLastLocation;
    }
}
