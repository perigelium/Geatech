package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
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
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.TecnicianNameId;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Network.LocationRetriever;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class CTLinfoFragment extends Fragment implements View.OnClickListener, LocationRetrievedEvents, Callback
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button mSetCurrentCoordsButton;
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
            selectedIndex = getArguments().getInt("selectedIndex");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.ctl_info_fragment, container, false);

        mSetCurrentCoordsButton = (Button) rootView.findViewById(R.id.btnSetCurrentCoords);
        mSetCurrentCoordsButton.setOnClickListener(this);

        VisitItem visitItem = visitItems.get(selectedIndex);
        ClientData clientData = visitItem.getClientData();
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        int idSopralluogo = geaSopralluogo.getId_sopralluogo();

        realm.beginTransaction();
        reportStates = realm.where(ReportStates.class).equalTo("id_sopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

        etCoordNord = (TextView) rootView.findViewById(R.id.etCoordNord);
        etCoordNord.setText(String.valueOf(clientData.coordNord));
        
        etCoordEst = (TextView) rootView.findViewById(R.id.etCoordEst);
        etCoordEst.setText(String.valueOf(clientData.coordEst));

        etAltitude = (TextView) rootView.findViewById(R.id.etAltitude);

        TextView clientNameTextView = (TextView) rootView.findViewById(R.id.tvClientName);
        clientNameTextView.setText(clientData.getName());

        TextView clientPhoneTextView = (TextView) rootView.findViewById(R.id.tvClientPhone);
        clientPhoneTextView.setText(clientData.getMobile());

        TextView clientAddressTextView = (TextView) rootView.findViewById(R.id.tvClientAddress);
        clientAddressTextView.setText(clientData.getAddress());

        TextView tvTechnicianName = (TextView) rootView.findViewById(R.id.tvTechnicianName);
        tvTechnicianName.setText(selectedTech.getFullNameTehnic(), TextView.BufferType.EDITABLE);

        if(reportStates != null && reportStates.getLatitudine()!=0.0
                && reportStates.getLongitudine()!=0.0 && reportStates.getAltitudine()!=0.0)
        {
            etCoordNord.setText(String.valueOf(reportStates.getLatitudine()), TextView.BufferType.EDITABLE);
            etCoordEst.setText(String.valueOf(reportStates.getLongitudine()), TextView.BufferType.EDITABLE);
            etAltitude.setText(String.valueOf(reportStates.getAltitudine()), TextView.BufferType.EDITABLE);
        }

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btnSetCurrentCoords)
        {
            if (!NetworkUtils.isNetworkAvailable(context))
            {
                Toast.makeText(context, "Controlla la connessione a Internet", Toast.LENGTH_LONG).show();
                return;
            }

                locationRetriever = new LocationRetriever(context, this);
        }
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
                reportStates.setLatitudine(mLastLocation.getLatitude());
                reportStates.setLongitudine(mLastLocation.getLongitude());
                reportStates.setGeneralInfoCompletionState(2);
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
                    reportStates.setAltitudine(altitude);
                    realm.commitTransaction();
                }

                etAltitude.setText(String.valueOf(altitude), TextView.BufferType.EDITABLE);
            }
        });
    }
}
