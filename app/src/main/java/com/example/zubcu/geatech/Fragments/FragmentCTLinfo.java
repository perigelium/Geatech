package com.example.zubcu.geatech.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zubcu.geatech.Interfaces.LocationDataEventListener;
import com.example.zubcu.geatech.Models.ClientData;
import com.example.zubcu.geatech.Models.DateTimeSetListCellModel;
import com.example.zubcu.geatech.Models.ProductData;
import com.example.zubcu.geatech.Models.VisitData;
import com.example.zubcu.geatech.Models.VisitItem;
import com.example.zubcu.geatech.Network.LocationReceiver;
import com.example.zubcu.geatech.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.zubcu.geatech.Network.RESTdataReceiver.visitItems;
import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class FragmentCTLinfo extends Fragment implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, LocationDataEventListener, Callback
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button mSetCurrentCoordsButton;
    private OnFragmentInteractionListener mListener;
    GoogleApiClient mGoogleApiClient = null;
    EditText etCoordNord, etCoordEst, etAltitude;
    Location mLastLocation;
    LocationRequest locationRequest;
    Call callDownloadURL;
    double altitude;
    private int selectedIndex;

    public FragmentCTLinfo() {}

    // TODO: Rename and change types and number of parameters
    public static FragmentCTLinfo newInstance(String param1, String param2) {
        FragmentCTLinfo fragment = new FragmentCTLinfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart()
    {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint)
    {
        if(mGoogleApiClient == null)
        {
            return;
        }

        if (getActivity().
                checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && getActivity().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if (getArguments() != null)
        {
            selectedIndex = getArguments().getInt("selectedIndex");
        }

        locationRequest = new LocationRequest()
                .setInterval(30000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            // Create an instance of GoogleAPIClient.
            if (mGoogleApiClient == null)
            {
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ctl_info_fragment, container, false);

        mSetCurrentCoordsButton = (Button) rootView.findViewById(R.id.btnSetCurrentCoords);
        etCoordNord = (EditText)rootView.findViewById(R.id.etCoordNord);
        etCoordEst = (EditText)rootView.findViewById(R.id.etCoordEst);
        etAltitude = (EditText)rootView.findViewById(R.id.etAltitude);

        mSetCurrentCoordsButton.setOnClickListener(this);

        VisitItem visitItem = visitItems.get(selectedIndex);
        ClientData clientData = visitItem.getClientData();
        ProductData productData = visitItem.getProductData();
        VisitData visitData = visitItem.getVisitData();

        TextView clientNameTextView = (TextView) rootView.findViewById(R.id.tvClientName);
        clientNameTextView.setText(clientData.getName());

        TextView clientPhoneTextView = (TextView) rootView.findViewById(R.id.tvClientPhone);
        clientPhoneTextView.setText(clientData.getMobile());

        TextView clientAddressTextView = (TextView) rootView.findViewById(R.id.tvClientAddress);
        clientAddressTextView.setText(clientData.getAddress());

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btnSetCurrentCoords)
        {
            if (getActivity().checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || getActivity().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                //LocationReceiver locationReceiver = new LocationReceiver(this, getActivity());

                //locationReceiver.getCurrentLocation();

                mLastLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);

                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);

                if (mLastLocation != null)
                {
                    etCoordNord.setText(String.valueOf(mLastLocation.getLatitude()), TextView.BufferType.EDITABLE);
                    etCoordEst.setText(String.valueOf(mLastLocation.getLongitude()), TextView.BufferType.EDITABLE);

                    if(mLastLocation.hasAltitude())
                    {
                        etAltitude.setText(String.valueOf(mLastLocation.getAltitude()), TextView.BufferType.EDITABLE);
                    }
                    else
                    if(isNetworkAvailable())
                    {
                        String elevationUrl = "http://maps.googleapis.com/maps/api/elevation/"
                                + "xml?locations=" + String.valueOf(mLastLocation.getLatitude())
                                + "," + String.valueOf(mLastLocation.getLongitude())
                                + "&sensor=true";

                        downloadURL(elevationUrl);
                    }
                }
            }
        }
    }

    @Override
    public void onLocationDataReceiveCompleted()
    {
        //Location location = locationReceiver.getLastLocation();

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                etAltitude.setText(String.valueOf(altitude), TextView.BufferType.EDITABLE);
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                etAltitude.setText(String.valueOf((int)altitude), TextView.BufferType.EDITABLE);
            }
        });

/*        if (callback != null)
        {
            callback.onLocationDataReceiveCompleted();
        }*/
    }
}
