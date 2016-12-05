package com.example.zubcu.geatech.Fragments;

import android.Manifest;
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

import com.example.zubcu.geatech.Models.GeneralInfoModel;
import com.example.zubcu.geatech.R;
import com.example.zubcu.geatech.Network.DownloadAsync;
import com.example.zubcu.geatech.Managers.GeneralInfoReceiver;
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

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCTLinfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCTLinfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCTLinfo extends Fragment implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button mSetCurrentCoordsButton;
    private OnFragmentInteractionListener mListener;
    private int selectedIndex;

    GeneralInfoReceiver generalInfoReceiver;
    ArrayList<GeneralInfoModel> visitsList;

    GoogleApiClient mGoogleApiClient = null;
    EditText etCoordNord, etCoordEst, etAltitude;
    Location mLastLocation;
    LocationRequest locationRequest;

    public FragmentCTLinfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCTLinfo.
     */
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
    public void onStart() {
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

        if (getActivity().checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            selectedIndex = getArguments().getInt("selectedIndex");
        }

        generalInfoReceiver = GeneralInfoReceiver.getInstance();
        visitsList = generalInfoReceiver.getListVisitsArrayList();

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


        TextView clientNameTextView = (TextView) rootView.findViewById(R.id.tvClientName);
        clientNameTextView.setText(visitsList.get(selectedIndex).getClientName());

        TextView clientPhoneTextView = (TextView) rootView.findViewById(R.id.tvClientPhone);
        clientPhoneTextView.setText(visitsList.get(selectedIndex).getClientPhone());

        TextView clientAddressTextView = (TextView) rootView.findViewById(R.id.tvClientAddress);
        clientAddressTextView.setText(visitsList.get(selectedIndex).getClientAddress());

        TextView technicianNameTextView = (TextView) rootView.findViewById(R.id.etTechnicianName);
        technicianNameTextView.setText(visitsList.get(selectedIndex).getTechnicianName());

        mSetCurrentCoordsButton = (Button) rootView.findViewById(R.id.btnSetCurrentCoords);
        etCoordNord = (EditText)rootView.findViewById(R.id.etCoordNord);
        etCoordEst = (EditText)rootView.findViewById(R.id.etCoordEst);
        etAltitude = (EditText)rootView.findViewById(R.id.etAltitude);

        mSetCurrentCoordsButton.setOnClickListener(this);

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
            if (getActivity().checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || getActivity().checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mLastLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);

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
                        String url = "http://maps.googleapis.com/maps/api/elevation/"
                                + "xml?locations=" + String.valueOf(mLastLocation.getLatitude())
                                + "," + String.valueOf(mLastLocation.getLongitude())
                                + "&sensor=true";

                        new DownloadAsync().execute(url); //needs to be replaced with async code
                    }
                }
            }
        }
    }

    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mInfoTextView.setText("Загружаем...");
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadOneUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            int mAltitude = getElevationFromGoogleMaps(result);
            etAltitude.setText(String.valueOf(mAltitude), TextView.BufferType.EDITABLE);
        }
    }

    private String downloadOneUrl(String address) throws IOException
    {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                int read = 0;
                while ((read = inputStream.read()) != -1) {
                    bos.write(read);
                }
                byte[] result = bos.toByteArray();
                bos.close();

                data = new String(result);

            } else {
                data = connection.getResponseMessage() + " . Error Code : " + responseCode;
            }
            connection.disconnect();
            //return data;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }  finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data;
    }

    private int getElevationFromGoogleMaps(String downloadedPage)
    {
        int result = 0;

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
                result = (Integer.parseInt(value)); // convert from meters to feet value*3.2808399
            }
        }
        return result;
    }

    private boolean isNetworkAvailable()
    {
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
}
