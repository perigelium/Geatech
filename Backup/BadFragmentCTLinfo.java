package com.example.zubcu.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zubcu.geatech.Interfaces.LocationDataEventListener;
import com.example.zubcu.geatech.Models.ClientData;
import com.example.zubcu.geatech.Models.DateTimeSetListCellModel;
import com.example.zubcu.geatech.Models.VisitData;
import com.example.zubcu.geatech.Models.VisitItem;
import com.example.zubcu.geatech.Network.GPSTracker;
import com.example.zubcu.geatech.Network.LocationReceiver;
import com.example.zubcu.geatech.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;

import static com.example.zubcu.geatech.Network.RESTdataReceiver.visitItems;


public class FragmentCTLinfo extends Fragment implements View.OnClickListener, LocationDataEventListener
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
    private int selectedIndex;

    GoogleApiClient mGoogleApiClient = null;
    EditText etCoordNord, etCoordEst, etAltitude;
    Location mLastLocation;
    LocationRequest locationRequest;

    public FragmentCTLinfo() {
        // Required empty public constructor
    }

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

        if (getArguments() != null)
        {
            selectedIndex = getArguments().getInt("selectedIndex");
        }

/*        locationRequest = new LocationRequest()
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
            }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ctl_info_fragment, container, false);

        ArrayList<DateTimeSetListCellModel> list = new ArrayList<>();
        VisitItem visitItem = visitItems.get(selectedIndex);
        ClientData clientData = visitItem.getClientData();
        VisitData visitData = visitItem.getVisitData();

        TextView clientNameTextView = (TextView) rootView.findViewById(R.id.tvClientName);
        clientNameTextView.setText(clientData.getName());

        TextView clientPhoneTextView = (TextView) rootView.findViewById(R.id.tvClientPhone);
        clientPhoneTextView.setText(clientData.getMobile());

        TextView clientAddressTextView = (TextView) rootView.findViewById(R.id.tvClientAddress);
        clientAddressTextView.setText(clientData.getAddress());


        //TextView technicianNameTextView = (TextView) rootView.findViewById(R.id.etTechnicianName);
        //technicianNameTextView.setText(visitsList.get(selectedIndex).getTechnicianName());

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
           // if (getActivity().checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || getActivity().checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                GPSTracker tracker = new GPSTracker(getActivity());

                if (!tracker.canGetLocation())
                {
                    tracker.showSettingsAlert();
                    return;
                } else {
                    mLastLocation = tracker.getLocation();
                }

                if (mLastLocation != null)
                {
                    etCoordNord.setText(String.valueOf(mLastLocation.getLatitude()), TextView.BufferType.EDITABLE);
                    etCoordEst.setText(String.valueOf(mLastLocation.getLongitude()), TextView.BufferType.EDITABLE);

                    if(mLastLocation.hasAltitude())
                    {
                        etAltitude.setText(String.valueOf(mLastLocation.getAltitude()), TextView.BufferType.EDITABLE);
                    }
                }
            else
                {
                    LocationReceiver locationReceiver = new LocationReceiver(this, getActivity());

                    if(locationReceiver.isNetworkAvailable())
                    {
                        locationReceiver.getCurrentLocation();
                    }
                }
        }
    }

    @Override
    public void onLocationDataReceiveCompleted()
    {
        if (mLastLocation != null)
        {
            etCoordNord.setText(String.valueOf(mLastLocation.getLatitude()), TextView.BufferType.EDITABLE);
            etCoordEst.setText(String.valueOf(mLastLocation.getLongitude()), TextView.BufferType.EDITABLE);

            if(mLastLocation.hasAltitude())
            {
                etAltitude.setText(String.valueOf(mLastLocation.getAltitude()), TextView.BufferType.EDITABLE);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
