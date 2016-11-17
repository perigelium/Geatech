package com.example.zubcu.geatech.Fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zubcu.geatech.Interfaces.SingleShotLocationProvider;
import com.example.zubcu.geatech.R;
import com.example.zubcu.geatech.Services.LocationGetter;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCTLinfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCTLinfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCTLinfo extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button mSetCurrentCoordsButton;
    private OnFragmentInteractionListener mListener;

    LocationManager mLocationManager;


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View rootView =  inflater.inflate(R.layout.fragment_ctl_info, container, false);

        mSetCurrentCoordsButton = (Button) rootView.findViewById(R.id.btnSetCurrentCoords);

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
    public void onClick(View view) {
        if (view.getId() == R.id.btnSetCurrentCoords) {
            //getActivity().getFragmentManager().beginTransaction().remove(this).commit();

/*            SingleShotLocationProvider.requestSingleUpdate(getActivity(), new SingleShotLocationProvider.LocationCallback()
            {
                        @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location)
                        {
                            Log.d("CurrentLocation", "my location is " + location.toString());
                        }
                    });*/

/*            final int MAX_WAITING_TIME = 10000; // в мс
            final int MINIMUM_TIME_BETWEEN_UPDATES = 10; // в мс
            LocationGetter locationGetter = null;
            try {
                locationGetter = new LocationGetter(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            locationGetter.getLocation(MAX_WAITING_TIME, MINIMUM_TIME_BETWEEN_UPDATES);*/


            // Get LocationManager object
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            // Create a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Get the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Get Current Location

            Location myLocation = getLastKnownLocation();

            //latitude of location
            double myLatitude = myLocation.getLatitude();

            //longitude og location
            double myLongitude = myLocation.getLongitude();

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

        }
    }
            private Location getLastKnownLocation()
            {
            mLocationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
            List<String> providers = mLocationManager.getProviders(true);
            Location bestLocation = null;

            for (String provider : providers)
            {
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
            return bestLocation;
        }




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
