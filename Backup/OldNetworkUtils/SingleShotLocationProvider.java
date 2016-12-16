package com.example.zubcu.geatech.Network;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by user on 11/17/2016.
 */
public class SingleShotLocationProvider {

    public static interface LocationCallback {
        public void onNewLocationAvailable(GPSCoordinates location);
    }

    // calls back to calling thread, note this is for low grain: if you want higher precision, swap the
    // contents of the else and if. Also be sure to check gps permission/settings are allowed.
    // call usually takes <10ms
    public static void requestSingleUpdate(final Context context, final LocationCallback callback)
    {
        PackageManager pm = context.getPackageManager();
        if(!pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS))
        {
            return;
        }

        String permission_ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
        int ACCESS_FINE_LOCATION_perm_result = context.checkCallingOrSelfPermission(permission_ACCESS_FINE_LOCATION);
        String permission_ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
        int ACCESS_COARSE_LOCATION_perm_result = context.checkCallingOrSelfPermission(permission_ACCESS_COARSE_LOCATION);
        if (ACCESS_FINE_LOCATION_perm_result != PackageManager.PERMISSION_GRANTED || ACCESS_COARSE_LOCATION_perm_result != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isNetworkEnabled)
        {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            //criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setAltitudeRequired(true);
            criteria.setBearingRequired(false);
            criteria.setSpeedRequired(false);
            criteria.setCostAllowed(false);
            locationManager.requestSingleUpdate(criteria, new LocationListener()
            {
                @Override
                public void onLocationChanged(Location location)
                {
                    GPSCoordinates gpsCoordinates = new GPSCoordinates(location.getLatitude(), location.getLongitude());

                    callback.onNewLocationAvailable(gpsCoordinates);
                }

                @Override public void onStatusChanged(String provider, int status, Bundle extras) { }
                @Override public void onProviderEnabled(String provider) { }
                @Override public void onProviderDisabled(String provider) { }
            }, null);
        } else
        {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (isGPSEnabled)
            {
                Criteria criteria = new Criteria();
                //criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                //criteria.setPowerRequirement(Criteria.POWER_LOW);
                criteria.setAltitudeRequired(true);
                criteria.setBearingRequired(false);
                criteria.setSpeedRequired(false);
                criteria.setCostAllowed(true);

                locationManager.requestSingleUpdate(criteria, new LocationListener()
                {
                    @Override
                    public void onLocationChanged(Location location)
                    {
                        GPSCoordinates gpsCoordinates = new GPSCoordinates(location.getLatitude(), location.getLongitude());

                        callback.onNewLocationAvailable(gpsCoordinates);
                    }

                    @Override public void onStatusChanged(String provider, int status, Bundle extras) { }
                    @Override public void onProviderEnabled(String provider) { }
                    @Override public void onProviderDisabled(String provider) { }
                }, null);
            }
        }
    }

    // consider returning Location instead of this dummy wrapper class
    public static class GPSCoordinates {
        public float longitude = -1;
        public float latitude = -1;

        public GPSCoordinates(float theLongitude, float theLatitude) {
            longitude = theLongitude;
            latitude = theLatitude;
        }

        public GPSCoordinates(double theLongitude, double theLatitude) {
            longitude = (float) theLongitude;
            latitude = (float) theLatitude;
        }
    }
}
