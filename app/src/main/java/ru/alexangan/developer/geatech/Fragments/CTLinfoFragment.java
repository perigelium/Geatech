package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Interfaces.LocationRetrievedEvents;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Network.LocationRetriever;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class CTLinfoFragment extends Fragment implements View.OnClickListener, LocationRetrievedEvents, Callback
{
    Button btnSetCurrentCoords, btnSaveCurrentCoords;
    TextView tvCoordNord, tvCoordEst, tvAltitude;
    Call callDownloadURL;
    double latitude, longitude;
    int altitude;
    private int selectedIndex;
    LocationRetriever locationRetriever;
    Context context;
    Location mLastLocation;
    ReportStates reportStates;
    Communicator communicator;

    public CTLinfoFragment()
    {
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
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

        altitude = -999;
        longitude = 0;
        latitude = 0;

        communicator = (Communicator)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.ctl_info_fragment, container, false);

        btnSetCurrentCoords = (Button) rootView.findViewById(R.id.btnSetCurrentCoords);
        btnSaveCurrentCoords = (Button) rootView.findViewById(R.id.btnSaveCurrentCoords);

        btnSetCurrentCoords.setOnClickListener(this);
        btnSaveCurrentCoords.setOnClickListener(this);

        tvCoordNord = (TextView) rootView.findViewById(R.id.tvCoordNord);
        tvCoordEst = (TextView) rootView.findViewById(R.id.tvCoordEst);
        tvAltitude = (TextView) rootView.findViewById(R.id.tvAltitude);


        VisitItem visitItem = visitItems.get(selectedIndex);
        ClientData clientData = visitItem.getClientData();
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        int idSopralluogo = geaSopralluogo.getId_sopralluogo();

        realm.beginTransaction();
        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

        TextView clientNameTextView = (TextView) rootView.findViewById(R.id.tvClientName);
        clientNameTextView.setText(clientData.getName());

        TextView clientPhoneTextView = (TextView) rootView.findViewById(R.id.tvClientPhone);
        clientPhoneTextView.setText(clientData.getMobile());

        TextView clientAddressTextView = (TextView) rootView.findViewById(R.id.tvClientAddress);
        clientAddressTextView.setText(clientData.getAddress());

        TextView tvTechnicianName = (TextView) rootView.findViewById(R.id.tvTechnicianName);
        tvTechnicianName.setText(selectedTech.getFullNameTehnic());

        if(reportStates!=null)
        {
            if (reportStates.getLatitudine() == 0)
            {
                latitude = clientData.getCoordNord();
            } else
            {
                latitude = reportStates.getLatitudine();
            }

            if (reportStates.getLongitudine() == 0)
            {
                longitude = clientData.getCoordEst();
            } else
            {
                longitude = reportStates.getLongitudine();
            }

            if (reportStates.getAltitudine() != 0)
            {
                altitude = reportStates.getAltitudine();
            }

            tvCoordNord.setText(String.valueOf(latitude));
            tvCoordEst.setText(String.valueOf(longitude));

            altitude = reportStates.getAltitudine();

            if (altitude != -999)
            {
                tvAltitude.setText(String.valueOf(altitude));
            }
        }

/*        if(reportStates.getData_ora_presa_appuntamento() == null && geaSopralluogo.getInizializzazione())
        {
            btnSetCurrentCoords.setEnabled(false);
            btnSetCurrentCoords.setAlpha(.4f);
            btnSaveCurrentCoords.setEnabled(false);
            btnSaveCurrentCoords.setAlpha(.4f);

            //communicator.disableCtrlButtons2(false);
        }*/
/*        else
        {
            communicator.disableCtrlButtons2(true);
        }*/

        return rootView;
    }

    @Override
    public void onPause()
    {
        super.onPause();


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

            btnSetCurrentCoords.setEnabled(false);
            btnSetCurrentCoords.setAlpha(.4f);
            btnSaveCurrentCoords.setEnabled(false);
            btnSaveCurrentCoords.setAlpha(.4f);

                locationRetriever = new LocationRetriever(context, this);
        }

        if(view.getId() == R.id.btnSaveCurrentCoords)
        {
            btnSaveCurrentCoords.setEnabled(false);
            btnSaveCurrentCoords.setAlpha(0.4f);

            if(reportStates!=null)
            {
                realm.beginTransaction();

                if(latitude != 0)
                {
                    reportStates.setLatitudine(latitude);
                }
                if(longitude != 0)
                {
                    reportStates.setLongitudine(longitude);
                }

                if(altitude!= -999)
                {
                    reportStates.setAltitudine(altitude);
                }

                if(reportStates!=null && reportStates.getLatitudine() != 0 && reportStates.getLongitudine() != 0) // && altitude != -999
                {
                    reportStates.setGeneralInfoCompletionState(2);
                }

                realm.commitTransaction();
            }
        }
    }

    @Override
    public void onLocationReceived()
    {
        mLastLocation = locationRetriever.getLastLocation();

        if (mLastLocation != null)
        {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

            tvCoordNord.setText(String.valueOf(latitude));
            tvCoordEst.setText(String.valueOf(longitude));

            if (mLastLocation.hasAltitude())
            {
                altitude = (int) mLastLocation.getAltitude();
                tvAltitude.setText(String.valueOf(altitude), TextView.BufferType.EDITABLE);
            } else
            {
                String elevationUrl = "http://maps.googleapis.com/maps/api/elevation/"
                        + "xml?locations=" + String.valueOf(latitude)
                        + "," + String.valueOf(longitude)
                        + "&sensor=true";

                NetworkUtils networkUtils = new NetworkUtils();
                callDownloadURL = networkUtils.downloadURL(this, elevationUrl);
            }
        }

        //Log.d("new", "coords= " + String.valueOf(latitude) + " " + String.valueOf(longitude));
    }

    private double parseElevationFromGoogleMaps(String downloadedPage)
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

        altitude = (int) parseElevationFromGoogleMaps(result);

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

                tvAltitude.setText(String.valueOf(altitude), TextView.BufferType.EDITABLE);

                btnSetCurrentCoords.setEnabled(true);
                btnSetCurrentCoords.setAlpha(1.0f);
                btnSaveCurrentCoords.setEnabled(true);
                btnSaveCurrentCoords.setAlpha(1.0f);
            }
        });
    }
}
