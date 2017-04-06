package ru.alexangan.developer.geatech.Fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class CTLinfoFragment extends Fragment implements View.OnClickListener, LocationRetrievedEvents, Callback, View.OnTouchListener
{
    Button btnGetCurrentCoords, btnSaveCoords;
    EditText etCoordNord, etCoordEst, etAltitude;
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
    private int PERMISSION_REQUEST_CODE = 11;
    private boolean coordsUnchanged;

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

        altitude = ReportStates.ALTITUDE_UNKNOWN;
        longitude = 0;
        latitude = 0;

        communicator = (Communicator) getActivity();

        requestServerDialog = new ProgressDialog(getActivity());
        requestServerDialog.setTitle("");
        requestServerDialog.setMessage(getString(R.string.DownloadingDataPleaseWait));
        requestServerDialog.setIndeterminate(true);

        coordsUnchanged = true;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (reportStates != null)
        {
            latitude = reportStates.getLatitudine();
            longitude = reportStates.getLongitudine();
            altitude = reportStates.getAltitude();

            coordsUnchanged = latitude != 0 && longitude != 0 && altitude != ReportStates.ALTITUDE_UNKNOWN;

            if(coordsUnchanged)
            {
                btnSaveCoords.setEnabled(false);
                btnSaveCoords.setAlpha(.4f);
            }

            if (latitude == 0)
            {
                latitude = clientData.getCoordNord();
            }

            if (longitude == 0)
            {
                longitude = clientData.getCoordEst();
            }

            etCoordNord.setText(String.valueOf(latitude));
            etCoordEst.setText(String.valueOf(longitude));

            if (altitude != ReportStates.ALTITUDE_UNKNOWN)
            {
                etAltitude.setText(String.valueOf(altitude));
            }
            else
            {
                etAltitude.setText(R.string.Unknown);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.ctl_info_fragment, container, false);

        VisitItem visitItem = visitItems.get(selectedIndex);
        clientData = visitItem.getClientData();
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        int idSopralluogo = geaSopralluogo.getId_sopralluogo();

        realm.beginTransaction();
        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

        btnGetCurrentCoords = (Button) rootView.findViewById(R.id.btnGetCurrentCoords);
        btnSaveCoords = (Button) rootView.findViewById(R.id.btnSaveCoords);

        btnGetCurrentCoords.setOnClickListener(this);
        btnSaveCoords.setOnClickListener(this);

        etCoordNord = (EditText) rootView.findViewById(R.id.etCoordNord);
        etCoordNord.setOnTouchListener(this);
        etCoordEst = (EditText) rootView.findViewById(R.id.etCoordEst);
        etCoordEst.setOnTouchListener(this);
        etAltitude = (EditText) rootView.findViewById(R.id.etAltitude);
        etAltitude.setOnTouchListener(this);

        TextView clientNameTextView = (TextView) rootView.findViewById(R.id.tvClientName);
        clientNameTextView.setText(clientData.getName());

        TextView clientPhoneTextView = (TextView) rootView.findViewById(R.id.tvClientPhone);
        clientPhoneTextView.setText(clientData.getMobile());

        TextView clientAddressTextView = (TextView) rootView.findViewById(R.id.tvClientAddress);
        clientAddressTextView.setText(clientData.getAddress());

        TextView tvTechnicianName = (TextView) rootView.findViewById(R.id.tvTechnicianName);
        tvTechnicianName.setText(selectedTech.getFullNameTehnic());

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

            if (checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {

                    String[] permissions = new String[]
                            {
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            };

                    requestMultiplePermissions(permissions);
                }
            } else
            {

                disableInputAndShowProgressDialog();

                locationRetriever = new LocationRetriever(activity, this);
            }
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
                    reportStates.setLatitudine(Double.valueOf(etCoordNord.getText().toString()));
                }
                if (longitude != 0)
                {
                    reportStates.setLongitudine(Double.valueOf(etCoordEst.getText().toString()));
                }

                if (altitude != ReportStates.ALTITUDE_UNKNOWN)
                {
                    reportStates.setAltitude(Integer.valueOf(etAltitude.getText().toString()));
                }

                if (reportStates != null && reportStates.getLatitudine() != 0 && reportStates.getLongitudine() != 0) // && altitude != -999
                {
                    reportStates.setGeneralInfoCompletionState(ReportStates.COORDS_SET);
                }

                realm.commitTransaction();

                showToastMessage(getString(R.string.Saved));

                communicator.onCoordsSetReturned(selectedIndex);
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

            etCoordNord.setText(String.valueOf(latitude));
            etCoordEst.setText(String.valueOf(longitude));

            if (mLastLocation.hasAltitude())
            {
                altitude = (int) mLastLocation.getAltitude();
                etAltitude.setText(String.valueOf(altitude), TextView.BufferType.EDITABLE);
            } else
            {
                if (NetworkUtils.isNetworkAvailable(activity))// && NetworkUtils.isOnline())
                {
                    String elevationUrl = "http://maps.googleapis.com/maps/api/elevation/"
                            + "xml?locations=" + String.valueOf(latitude)
                            + "," + String.valueOf(longitude)
                            + "&sensor=true";

                    NetworkUtils networkUtils = new NetworkUtils();

                    callDownloadURL = networkUtils.downloadURL(this, elevationUrl);
                } else
                {
                    showToastMessage(getString(R.string.GetAltitudeFailedCheckIfOnline));
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
                    Toast.makeText(getActivity(), R.string.InstallMapApp, Toast.LENGTH_LONG).show();
                }
            }

            showToastMessage(getString(R.string.FailedToGetCoords));
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
            StringBuilder respStr = new StringBuilder(downloadedPage);
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
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                enableInput();
            }
        });

        showToastMessage(String.valueOf(R.string.GetAltitudeFailedCheckIfOnline));
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        String result = response.body().string();

        altitude = (int) parseElevationFromGoogleMaps(result);

        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                etAltitude.setText(String.valueOf(altitude), TextView.BufferType.EDITABLE);

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
        if(this.isAdded())
        {
            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestMultiplePermissions(String[] permissions)
    {
        requestPermissions(permissions, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length >= 1)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                disableInputAndShowProgressDialog();

/*                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }*/
                locationRetriever = new LocationRetriever(activity, this);
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if (view.getId() == R.id.etCoordNord || view.getId() == R.id.etCoordEst || view.getId() == R.id.etAltitude)
        {
            btnSaveCoords.setEnabled(true);
            btnSaveCoords.setAlpha(1.0f);
        }

        return false;
    }
}
