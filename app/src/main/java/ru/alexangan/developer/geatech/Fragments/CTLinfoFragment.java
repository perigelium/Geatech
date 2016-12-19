package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Interfaces.LocationRetrievedEvents;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Network.LocationRetriever;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Activities.MainActivity.realm;
import static ru.alexangan.developer.geatech.Network.RESTdataReceiver.visitItems;

public class CTLinfoFragment extends Fragment implements View.OnClickListener, LocationRetrievedEvents, Callback
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button mSetCurrentCoordsButton;
    private OnFragmentInteractionListener mListener;
    TextView etCoordNord, etCoordEst, etAltitude;
    Call callDownloadURL;
    int altitude;
    private int selectedIndex;
    LocationRetriever locationRetriever;
    Context context;
    Location mLastLocation;
    ReportStates reportStates;

    public CTLinfoFragment()
    {
    }

    // TODO: Rename and change types and number of parameters
    public static CTLinfoFragment newInstance(String param1, String param2)
    {
        CTLinfoFragment fragment = new CTLinfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = getActivity();

        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if (getArguments() != null)
        {
            selectedIndex = getArguments().getInt("selectedIndex");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.ctl_info_fragment, container, false);

        mSetCurrentCoordsButton = (Button) rootView.findViewById(R.id.btnSetCurrentCoords);
        etCoordNord = (TextView) rootView.findViewById(R.id.etCoordNord);
        etCoordEst = (TextView) rootView.findViewById(R.id.etCoordEst);
        etAltitude = (TextView) rootView.findViewById(R.id.etAltitude);

        mSetCurrentCoordsButton.setOnClickListener(this);

        VisitItem visitItem = visitItems.get(selectedIndex);
        ClientData clientData = visitItem.getClientData();
        VisitStates visitStates = visitItem.getVisitStates();
        int idSopralluogo = visitStates.getIdSopralluogo();

        TextView clientNameTextView = (TextView) rootView.findViewById(R.id.tvClientName);
        clientNameTextView.setText(clientData.getName());

        TextView clientPhoneTextView = (TextView) rootView.findViewById(R.id.tvClientPhone);
        clientPhoneTextView.setText(clientData.getMobile());

        TextView clientAddressTextView = (TextView) rootView.findViewById(R.id.tvClientAddress);
        clientAddressTextView.setText(clientData.getAddress());

        realm.beginTransaction();
        reportStates = realm.where(ReportStates.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

        if(reportStates != null && reportStates.getLatitude()!=0.0 && reportStates.getLongitude()!=0.0 && reportStates.getAltitude()!=0.0)
        {
            etCoordNord.setText(String.valueOf(reportStates.getLatitude()), TextView.BufferType.EDITABLE);
            etCoordEst.setText(String.valueOf(reportStates.getLongitude()), TextView.BufferType.EDITABLE);
            etAltitude.setText(String.valueOf(reportStates.getAltitude()), TextView.BufferType.EDITABLE);
        }

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btnSetCurrentCoords)
        {
            if (!isNetworkAvailable())
            {
                Toast.makeText(context, "Check internet connection.", Toast.LENGTH_LONG).show();
                return;
            }

                locationRetriever = new LocationRetriever(context, this);
        }
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onLocationReceived()
    {
        mLastLocation = locationRetriever.getLastLocation();

        if (mLastLocation != null)
        {
            etCoordNord.setText(String.valueOf(mLastLocation.getLatitude()), TextView.BufferType.EDITABLE);
            etCoordEst.setText(String.valueOf(mLastLocation.getLongitude()), TextView.BufferType.EDITABLE);

            if(reportStates!=null)
            {
                realm.beginTransaction();
                reportStates.setLatitude(mLastLocation.getLatitude());
                reportStates.setLongitude(mLastLocation.getLongitude());
                realm.commitTransaction();
            }

            if (mLastLocation.hasAltitude())
            {
                etAltitude.setText(String.valueOf((int) mLastLocation.getAltitude()), TextView.BufferType.EDITABLE);
            } else
            {
                String elevationUrl = "http://maps.googleapis.com/maps/api/elevation/"
                        + "xml?locations=" + String.valueOf(mLastLocation.getLatitude())
                        + "," + String.valueOf(mLastLocation.getLongitude())
                        + "&sensor=true";

                downloadURL(elevationUrl);
            }
        }

        //Log.d("new", "coords= " + String.valueOf(latitude) + " " + String.valueOf(longitude));
    }

    public interface OnFragmentInteractionListener
    {
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

        altitude = (int) getElevationFromGoogleMaps(result);

        getActivity().runOnUiThread(new Runnable()
        {
            public void run()
            {
                if(reportStates!=null)
                {
                    realm.beginTransaction();
                    reportStates.setAltitude(altitude);
                    realm.commitTransaction();
                }

                etAltitude.setText(String.valueOf(altitude), TextView.BufferType.EDITABLE);
            }
        });
    }
}
