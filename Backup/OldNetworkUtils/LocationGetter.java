package com.example.zubcu.geatech.Network;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import com.example.zubcu.geatech.Models.Coordinates;

/**
 * Created by user on 11/17/2016.
 */

public class LocationGetter
{
    Coordinates coordinates;
    private final Context context;
    private Location location = null;
    private final Object gotLocationLock = new Object();
    private final LocationResolver.LocationResult locationResult = new LocationResolver.LocationResult()
    {
        @Override
        public void gotLocation(Location location) {
            synchronized (gotLocationLock) {
                LocationGetter.this.location = location;
                gotLocationLock.notifyAll();
                Looper.myLooper().quit();
            }
        }
    };

    public LocationGetter(Context context) throws Exception
    {
        if (context == null)
            throw new IllegalArgumentException("context == null");

        String permission_ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
        int ACCESS_FINE_LOCATION_perm_result = context.checkCallingOrSelfPermission(permission_ACCESS_FINE_LOCATION);
        String permission_ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
        int ACCESS_COARSE_LOCATION_perm_result = context.checkCallingOrSelfPermission(permission_ACCESS_COARSE_LOCATION);
        if (ACCESS_FINE_LOCATION_perm_result != PackageManager.PERMISSION_GRANTED || ACCESS_COARSE_LOCATION_perm_result != PackageManager.PERMISSION_GRANTED)
        {

            throw new Exception("Location permissions not granted");
        }

        this.context = context;
    }

    public synchronized Coordinates getLocation(int maxWaitingTime, int updateTimeout)
    {
        try {
            final int updateTimeoutPar = updateTimeout;
            synchronized (gotLocationLock) {
                new Thread() {
                    public void run() {
                        Looper.prepare();
                        LocationResolver locationResolver = new LocationResolver();
                        locationResolver.prepare();
                        locationResolver.getLocation(context, locationResult, updateTimeoutPar);
                        Looper.loop();
                    }
                }.start();

                gotLocationLock.wait(maxWaitingTime);
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        if (location != null)
            coordinates = new Coordinates(location.getLatitude(), location.getLongitude());
        else
            coordinates = Coordinates.UNDEFINED;
        return coordinates;
    }
}
