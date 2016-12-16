package com.example.zubcu.geatech.Network;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.zubcu.geatech.Interfaces.LocationDataEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

/**
 * Created by user on 11/30/2016.
 */

public class NetworkUtils implements Callback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    Call callDownloadURL;
    LocationDataEventListener callback;
    Context context;
    GoogleApiClient mGoogleApiClient;

    public static Location mLastLocation;

    static double altitude;
    LocationRequest locationRequest;

    public NetworkUtils(LocationDataEventListener cb, Context context)
    {
        callback = cb;
        this.context = context;

        locationRequest = new LocationRequest()
                .setInterval(30000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this.context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

/*        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.connect();
        }*/
    }

    public void getCurrentLocation()
    {
        if (context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mLastLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        if(isNetworkAvailable() && mLastLocation != null)
        {
            String url = "http://maps.googleapis.com/maps/api/elevation/"
                    + "xml?locations=" + String.valueOf(mLastLocation.getLatitude())
                    + "," + String.valueOf(mLastLocation.getLongitude())
                    + "&sensor=true";

            downloadURL(url);
        }
    }

    private double getElevationFromGoogleMaps(String downloadedPage)
    {
        double result = 0;

        if (downloadedPage.length() > 0)
        {
            int r = -1;
            StringBuffer respStr = new StringBuffer(downloadedPage);
            respStr.append((char) r);
            String tagOpen = "<elevation>";
            String tagClose = "</elevation>";
            if (respStr.indexOf(tagOpen) != -1)
            {
                int start = respStr.indexOf(tagOpen) + tagOpen.length();
                int end = respStr.indexOf(tagClose);
                String value = respStr.substring(start, end);
                result = (Double.parseDouble(value)); // convert from meters to feet value*3.2808399
            }
        }
        return result;
    }

    @Override
    public void onFailure(Call call, IOException e)
    {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        String result = response.body().string();
        //mLastLocation.setAltitude(getElevationFromGoogleMaps(result));

        altitude = getElevationFromGoogleMaps(result);

        if (callback != null)
        {
            callback.onLocationDataReceiveCompleted();
        }
    }

    public void downloadURL(String url)
    {
        OkHttpClient.Builder defaultHttpClient = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = defaultHttpClient.build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        callDownloadURL = okHttpClient.newCall(request);
        callDownloadURL.enqueue(this);
    }

    @Override
    public void onConnected(Bundle connectionHint)
    {
        if (context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        if(mGoogleApiClient == null)
        {
            return;
        }

        FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Log.d("DEBUG", "connection failed");
    }

    @Override
    public void onLocationChanged(Location location)
    {

    }

    public static Location getLastLocation()
    {
        return mLastLocation;
    }

    public static double getAltitude()
    {
        return altitude;
    }
}

/*
if(location==null){
        new android.os.Handler().postDelayed(new Runnable() {
@Override
public void run() {
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        }, 2000);//your waiting time.. I prefer 2 secs
        }*/
