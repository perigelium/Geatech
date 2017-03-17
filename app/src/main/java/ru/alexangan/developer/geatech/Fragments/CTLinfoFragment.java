package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;

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
    Button btnGetCurrentCoords, btnSaveCoords;
    TextView tvCoordNord, tvCoordEst, tvAltitude;
    Call callDownloadURL;
    double latitude, longitude;
    int altitude;
    private int selectedIndex;
    LocationRetriever locationRetriever;
    Activity activity;
    Location mLastLocation;
    ReportStates reportStates;
    Communicator communicator;
    private ProgressDialog requestServerDialog;
    ClientData clientData;

    public CTLinfoFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        if (getArguments() != null)
        {
            selectedIndex = getArguments().getInt("selectedIndex");
        }

        altitude = -999;
        longitude = 0;
        latitude = 0;

        communicator = (Communicator) getActivity();

        requestServerDialog = new ProgressDialog(getActivity());
        requestServerDialog.setTitle("");
        requestServerDialog.setMessage("Download dei dati, si prega di attendere un po'...");
        requestServerDialog.setIndeterminate(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ctl_info_fragment, container, false);

        btnGetCurrentCoords = (Button) rootView.findViewById(R.id.btnGetCurrentCoords);
        btnSaveCoords = (Button) rootView.findViewById(R.id.btnSaveCoords);

        btnGetCurrentCoords.setOnClickListener(this);
        btnSaveCoords.setOnClickListener(this);

        tvCoordNord = (TextView) rootView.findViewById(R.id.tvCoordNord);
        tvCoordEst = (TextView) rootView.findViewById(R.id.tvCoordEst);
        tvAltitude = (TextView) rootView.findViewById(R.id.tvAltitude);


        VisitItem visitItem = visitItems.get(selectedIndex);
        clientData = visitItem.getClientData();
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

        if (reportStates != null)
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

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btnGetCurrentCoords)
        {
/*            if (!NetworkUtils.isNetworkAvailable(activity))
            {
                Toast.makeText(activity, "Controlla la connessione a Internet", Toast.LENGTH_LONG).show();
                return;
            }*/

            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            {
                activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                return;
            }

            disableInputAndShowProgressDialog();

            locationRetriever = new LocationRetriever(activity, this);
        }

        if (view.getId() == R.id.btnSaveCoords)
        {
            if (reportStates != null)
            {
                btnSaveCoords.setEnabled(false);
                btnSaveCoords.setAlpha(.4f);

                realm.beginTransaction();

                if (latitude != 0)
                {
                    reportStates.setLatitudine(latitude);
                }
                if (longitude != 0)
                {
                    reportStates.setLongitudine(longitude);
                }

                if (altitude != -999)
                {
                    reportStates.setAltitudine(altitude);
                }

                if (reportStates != null && reportStates.getLatitudine() != 0 && reportStates.getLongitudine() != 0) // && altitude != -999
                {
                    reportStates.setGeneralInfoCompletionState(2);
                }

                realm.commitTransaction();

                showToastMessage("Salvato");
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
                if(NetworkUtils.isNetworkAvailable(activity))
                {

                String elevationUrl = "http://maps.googleapis.com/maps/api/elevation/"
                        + "xml?locations=" + String.valueOf(latitude)
                        + "," + String.valueOf(longitude)
                        + "&sensor=true";

                    NetworkUtils networkUtils = new NetworkUtils();

                    callDownloadURL = networkUtils.downloadURL(this, elevationUrl);
                }
                else
                {
                    showToastMessage("Non riuscito a ricevere altitudine, controlla connesione ad Internet");
                }
            }
        } else
        {
            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", (float) latitude, (float) longitude, "Where the party is at");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

            try
            {
                startActivity(intent);
            } catch (ActivityNotFoundException ex)
            {
                try
                {
                    Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(unrestrictedIntent);
                } catch (ActivityNotFoundException innerEx)
                {
                    Toast.makeText(getActivity(), "installa un'applicazione mappe", Toast.LENGTH_LONG).show();
                }
            }

            showToastMessage("Non riuscito a ricevere coordinati");
        }

        ////Log.d("new", "coords= " + String.valueOf(latitude) + " " + String.valueOf(longitude));

        enableInput();
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
        getActivity().runOnUiThread(new Runnable()
        {
            public void run()
            {
                enableInput();
            }
        });

        showToastMessage("Non riuscito a ricevere altitudine");
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
                if (reportStates != null)
                {
                    realm.beginTransaction();
                    reportStates.setAltitudine(altitude);
                    realm.commitTransaction();
                }

                tvAltitude.setText(String.valueOf(altitude), TextView.BufferType.EDITABLE);

                enableInput();
            }
        });
    }

    private void disableInputAndShowProgressDialog()
    {
        btnGetCurrentCoords.setEnabled(false);
        btnGetCurrentCoords.setAlpha(.4f);
        btnSaveCoords.setEnabled(false);
        btnSaveCoords.setAlpha(.4f);

        requestServerDialog.show();
    }

    private void enableInput()
    {
        btnGetCurrentCoords.setEnabled(true);
        btnGetCurrentCoords.setAlpha(1.0f);
        btnSaveCoords.setEnabled(true);
        btnSaveCoords.setAlpha(1.0f);

        requestServerDialog.dismiss();
    }

    private void showToastMessage(final String msg)
    {
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
