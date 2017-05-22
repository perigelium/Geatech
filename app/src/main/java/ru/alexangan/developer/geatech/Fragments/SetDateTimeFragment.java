package ru.alexangan.developer.geatech.Fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Adapters.SetVisitDateTimeListAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Interfaces.LocationRetrievedEvents;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.GeaImagineRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.SubproductItem;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Network.LocationRetriever;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.ViewUtils;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.SET_DATA_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.tokenStr;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class SetDateTimeFragment extends Fragment implements View.OnClickListener, Callback, LocationRetrievedEvents
{
    Calendar calendarNow;
    Calendar calendar;
    long elapsedDays;
    String strDateTimeSet, strDateTimeNow;
    Call callSetDateTime;
    int idSopralluogo, stakedOut;
    VisitItem visitItem;
    String product_type;
    EditText etCoordNord, etCoordEst, etAltitude;
    Call callGetPageWithAltitude;
    double latitude, longitude;
    int altitude;
    LocationRetriever locationRetriever;
    Activity activity;
    Location mLastLocation;
    ReportStates reportStates;
    Communicator communicator;
    private ProgressDialog requestServerDialog;
    ClientData clientData;
    private int PERMISSION_REQUEST_CODE = 11;
    private boolean coordsUnchanged;

    private TextView btnSetDateTimeSubmit;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private int selectedIndex;
    View rootView;
    NetworkUtils networkUtils;

    private Communicator mCommunicator;
    AlertDialog alert;
    boolean dateSet, timeSet;
    private TextView tvdataOraSopralluogo;
    private TextView tvTechnicianName;
    private Button btnOpenMap, btnOpenDialer;
    private Button btnGetCurrentCoords;
    private TextView tvSetDateTime;


    public SetDateTimeFragment()
    {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mCommunicator = (Communicator) getActivity();
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

        networkUtils = new NetworkUtils();

        dateSet = false;
        timeSet = false;

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

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener()
    {
        // when Dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay)
        {
            dateSet = true;

            mYear = selectedYear;
            calendar.set(Calendar.YEAR, mYear);
            mMonth = selectedMonth + 1;
            calendar.set(Calendar.MONTH, selectedMonth);
            mDay = selectedDay;
            calendar.set(Calendar.DAY_OF_MONTH, mDay);
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
        {
            if(dateSet)
            {
                enableInput();
            }

            mHour = selectedHour;
            calendar.set(Calendar.HOUR_OF_DAY, mHour);
            mMinute = selectedMinute;
            calendar.set(Calendar.MINUTE, mMinute);

            int millsDiff = calendar.compareTo(calendarNow);

            if (millsDiff <= 0)
            {
                elapsedDays = 0;
            } else
            {
                long milliSeconds = calendar.getTimeInMillis();
                long milliSecondsNow = calendarNow.getTimeInMillis();
                long periodMilliSeconds = (milliSeconds - milliSecondsNow);
                elapsedDays = periodMilliSeconds / 1000 / 60 / 60 / 24;
            }

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);
            String dateTimeStr = sdfDate.format(calendar.getTime());
            tvdataOraSopralluogo.setText(dateTimeStr);

            tvSetDateTime.setVisibility(View.GONE);
            tvTechnicianName.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.set_date_time_fragment, container, false);

        tvSetDateTime = (TextView) rootView.findViewById(R.id.btnSetDateTime);
        tvSetDateTime.setPaintFlags(tvSetDateTime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btnSetDateTimeSubmit = (TextView) rootView.findViewById(R.id.btnSetDateTimeSubmit);
        btnOpenMap = (Button) rootView.findViewById(R.id.btnOpenMap);
        btnOpenDialer = (Button) rootView.findViewById(R.id.btnOpenDialer);
        tvdataOraSopralluogo = (TextView) rootView.findViewById(R.id.tvdataOraSopralluogo);
        tvTechnicianName = (TextView) rootView.findViewById(R.id.tvTechnicianName);

        btnGetCurrentCoords = (Button) rootView.findViewById(R.id.btnGetCurrentCoords);

        btnGetCurrentCoords.setOnClickListener(this);

        etCoordNord = (EditText) rootView.findViewById(R.id.etCoordNord);
        //etCoordNord.setOnTouchListener(this);
        etCoordEst = (EditText) rootView.findViewById(R.id.etCoordEst);
        //etCoordEst.setOnTouchListener(this);
        etAltitude = (EditText) rootView.findViewById(R.id.etAltitude);
        //etAltitude.setOnTouchListener(this);

        TextView tvTechnicianName = (TextView) rootView.findViewById(R.id.tvTechnicianName);
        tvTechnicianName.setText(selectedTech.getFullNameTehnic());

        visitItem = visitItems.get(selectedIndex);
        ClientData clientData = visitItem.getClientData();
        ProductData productData = visitItem.getProductData();
        int id_product_type = productData.getIdProductType();

        realm.beginTransaction();
        GeaModelloRapporto geaModello = realm.where(GeaModelloRapporto.class).equalTo("id_product_type", id_product_type).findFirst();
        realm.commitTransaction();

        if(geaModello != null)
        {
            product_type = geaModello.getNome_modello();
        }

        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        idSopralluogo = geaSopralluogo.getId_sopralluogo();
        List<SubproductItem> listSubproducts = productData.getSubItem();

        realm.beginTransaction();
        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

        String dataOraSopralluogo = null;

        SetVisitDateTimeListAdapter adapter = new SetVisitDateTimeListAdapter(getActivity(), listSubproducts);

        ListView listView = (ListView) rootView.findViewById(R.id.listSubproducts);
        listView.setAdapter(adapter);

        ViewUtils.setListViewHeightBasedOnChildren(listView);

        TextView clientNameTextView = (TextView) rootView.findViewById(R.id.tvClientName);
        clientNameTextView.setText(clientData.getName());

        TextView clientPhoneTextView = (TextView) rootView.findViewById(R.id.tvClientPhone);
        clientPhoneTextView.setText(clientData.getMobile());

        TextView serviceTypeTextView = (TextView) rootView.findViewById(R.id.tvVisitTOS);
        serviceTypeTextView.setText(productData.getProductType());

        TextView tvProductModel = (TextView) rootView.findViewById(R.id.tvProductModel);
        tvProductModel.setText(productData.getProduct());

        TextView clientAddressTextView = (TextView) rootView.findViewById(R.id.tvClientAddress);
        clientAddressTextView.setText(clientData.getAddress());

        //String visitDateTime = reportStates!=null ? reportStates.getData_ora_sopralluogo() : " ";

        calendarNow = Calendar.getInstance();
        calendar = Calendar.getInstance();

        //strDateTimeNow = sdf.format(calendarNow.getTime());

        if(reportStates!=null)
        {
            dataOraSopralluogo = reportStates.getData_ora_sopralluogo();
        }

        if(dataOraSopralluogo!=null && dataOraSopralluogo.length() > 4)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

            try
            {
                calendar.setTime(sdf.parse(dataOraSopralluogo));

                tvdataOraSopralluogo.setText(dataOraSopralluogo);
                tvTechnicianName.setVisibility(View.VISIBLE);
                tvSetDateTime.setVisibility(View.GONE);
                btnSetDateTimeSubmit.setVisibility(View.GONE);

            } catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            tvTechnicianName.setVisibility(View.GONE);
            tvSetDateTime.setVisibility(View.VISIBLE);
        }

/*        if(dataOraSopralluogo.length() > 4)
        {
            try
            {
                calendar.setTime(sdf.parse(dataOraSopralluogo));
                int millsDiff = calendar.compareTo(calendarNow);

                if (millsDiff < 0)
                {
                    calendar = calendarNow;
                }

                SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM HH:mm", Locale.ITALIAN);
                String dateTimeStr = sdfDate.format(calendar.getTime());

                tvdataOraSopralluogo.setText(dateTimeStr);

            } catch (ParseException e)
            {
                e.printStackTrace();

                calendar = calendarNow;
            }
        }
        else
        {
            disableInput();
        }*/

/*        if(reportStates==null || visitDateTime == null)
        {
*//*            btnAnnullaSetDateTime.setEnabled(false);
            btnAnnullaSetDateTime.setAlpha(.4f);*//*
        }*/

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        tvSetDateTime.setOnClickListener(this);
        btnSetDateTimeSubmit.setOnClickListener(this);
        btnOpenMap.setOnClickListener(this);
        btnOpenDialer.setOnClickListener(this);

        return rootView;
    }

    public void onClick(View v)
    {
        if (v.getId() == R.id.btnGetCurrentCoords)
        {
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
                //disableInputAndShowProgressDialog();
                requestServerDialog.show();

                locationRetriever = new LocationRetriever(activity, this);
            }
        }

        if (v.getId() == R.id.btnOpenMap)
        {
            if(!NetworkUtils.isNetworkAvailable(activity))
            {
                showToastMessage(getString(R.string.CheckInternetConnection));
                return;
            }

            double latitude = visitItems.get(selectedIndex).getClientData().getCoordNord();
            double longitude = visitItems.get(selectedIndex).getClientData().getCoordEst();

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
        }

        if (v.getId() == R.id.btnOpenDialer)
        {
            String phoneNumber = "tel:" + visitItems.get(selectedIndex).getClientData().getMobile();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(phoneNumber));
            startActivity(intent);
        }

        if (v.getId() == R.id.btnSetDateTime)
        {
            TimePickerDialog DialogTimePicker = new TimePickerDialog(getActivity(), timePickerListener,
                    mHour, mMinute, DateFormat.is24HourFormat(getActivity()));
            DialogTimePicker.show();

            DatePickerDialog DialogDatePicker = new DatePickerDialog(getActivity(), datePickerListener,
                    mYear, mMonth - 1, mDay);
            DialogDatePicker.show();
        }

        if (v.getId() == R.id.btnSetDateTimeSubmit)
        {
            if(!NetworkUtils.isNetworkAvailable(activity))
            {
                showToastMessage(getString(R.string.CheckInternetConnection));
                return;
            }

            if(tokenStr == null)
            {
                //showToastMessage("Modalità offline, si prega di logout e login di nuovo");
                alertDialog("Info", getString(R.string.OfflineModeShowLoginScreenQuestion));
                return;
            }

            stakedOut = 1;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            strDateTimeSet = sdf.format(calendar.getTime());

            disableInput();

            notifyServerDataOraSopralluogo(idSopralluogo, stakedOut);
        }
    }

    private void notifyServerDataOraSopralluogo(int idSopralluogo, int stakedOut)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("token", tokenStr);
            jsonObject.put("id_sopralluogo", idSopralluogo);
            jsonObject.put("data_ora_sopralluogo", strDateTimeSet);
            jsonObject.put("data_ora_presa_appuntamento", strDateTimeNow);

            if (stakedOut == 1)
            {
                jsonObject.put("id_tecnico", selectedTech.getId());
            } else
            {
                jsonObject.put("id_tecnico", 0);
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        callSetDateTime = networkUtils.setData(this, SET_DATA_URL_SUFFIX, String.valueOf(jsonObject));
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        if(call == callGetPageWithAltitude)
        {
            showToastMessage(String.valueOf(R.string.GetAltitudeFailedCheckIfOnline));
        }

        if (call == callSetDateTime)
        {
            showToastMessage(getString(R.string.SetDateTimeFailed));
        }

        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                enableInput();
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        if(call == callGetPageWithAltitude)
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

        if (call == callSetDateTime)
        {
            String strVisitDateTimeResponse = response.body().string();

            final JSONObject jsonObject;

            try
            {
                jsonObject = new JSONObject(strVisitDateTimeResponse);
            } catch (JSONException e)
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        enableInput();
                    }
                });

                e.printStackTrace();
                showToastMessage(getString(R.string.ReceivedDataError));
                return;
            }

            if (jsonObject.has("error"))
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        enableInput();
                    }
                });

                alertDialog("Info", getString(R.string.OfflineModeShowLoginScreenQuestion));

                //final String errorStr;

/*                try
                {
                    errorStr = jsonObject.getString("error");

                    if (errorStr.length() != 0)
                    {
                        showToastMessage(errorStr);
                    }

                } catch (JSONException e)
                {
                    getActivity().runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            enableInput();
                        }
                    });
                    e.printStackTrace();
                    return;
                }*/
            } else
            {
                if (jsonObject.has("success"))
                {
                    try
                    {
                        final String strSuccess = jsonObject.getString("success");

                        if (strSuccess.equals("1"))
                        {
                            activity.runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    if (jsonObject.has("id_rapporto_sopralluogo"))
                                    {
                                        try
                                        {
                                            String str_id_rapporto_sopralluogo = jsonObject.getString("id_rapporto_sopralluogo");
                                            int id_rapporto_sopralluogo = Integer.valueOf(str_id_rapporto_sopralluogo);

                                            if (reportStates == null)
                                            {
                                                realm.beginTransaction();
                                                reportStates = new ReportStates(company_id, selectedTech.getId(), idSopralluogo, id_rapporto_sopralluogo);
                                                realm.copyToRealm(reportStates);
                                                realm.commitTransaction();
                                            }

                                            if (stakedOut == 1)
                                            {
                                                realm.beginTransaction();
                                                reportStates = realm.where(ReportStates.class)
                                                        .equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                                                        .equalTo("id_sopralluogo", idSopralluogo).findFirst();
                                                realm.commitTransaction();

                                                if (reportStates != null)
                                                {
                                                    realm.beginTransaction();
                                                    reportStates.setId_rapporto_sopralluogo(id_rapporto_sopralluogo);
                                                    reportStates.setData_ora_sopralluogo(strDateTimeSet);
                                                    reportStates.setData_ora_presa_appuntamento(strDateTimeNow);
                                                    reportStates.setNome_tecnico(selectedTech.getFullNameTehnic());

                                                    reportStates.setClientName(visitItem.getClientData().getName());
                                                    reportStates.setClientMobile(visitItem.getClientData().getMobile());
                                                    reportStates.setClientAddress(visitItem.getClientData().getAddress());
                                                    reportStates.setProductType(product_type);
                                                    realm.commitTransaction();
                                                }

                                                showToastMessage(getString(R.string.DateTimeSetSuccessfully));//, server ritorna: " + strVisitDateTimeResponse
                                            }
                                        } catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }

                                    if (stakedOut == 0)
                                    {
                                        if (reportStates != null)
                                        {
                                            cleanUpOldReportData();
                                        }

                                        showToastMessage(getString(R.string.VisitHasCancelled));
                                    }

                                    enableInput();

                                    mCommunicator.onDateTimeSetReturned(selectedIndex);
                                }
                            });
                        }

                    } catch (JSONException e)
                    {
                        getActivity().runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                enableInput();
                            }
                        });
                        e.printStackTrace();
                    }
                }
                else
                {
                    getActivity().runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            enableInput();
                        }
                    });
                }
            }
        }
    }

    private void cleanUpOldReportData()
    {
        if(reportStates != null)
        {
            realm.beginTransaction();

            RealmResults geaItemsRapporto = realm.where(GeaItemRapporto.class).equalTo("company_id", company_id)
                    .equalTo("tech_id", selectedTech.getId()).equalTo("id_rapporto_sopralluogo", reportStates.getId_rapporto_sopralluogo()).findAll();

            RealmResults<GeaImagineRapporto> listReportImages = realm.where(GeaImagineRapporto.class)
                    .equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                    .equalTo("id_rapporto_sopralluogo", reportStates.getId_rapporto_sopralluogo()).findAll();

            geaItemsRapporto.deleteAllFromRealm();
            listReportImages.deleteAllFromRealm();
            reportStates.deleteFromRealm();

            realm.commitTransaction();
        }
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

    private void disableInput()
    {
        btnSetDateTimeSubmit.setEnabled(false);
        btnSetDateTimeSubmit.setAlpha(.4f);
        btnGetCurrentCoords.setEnabled(false);
        btnGetCurrentCoords.setAlpha(.4f);

        requestServerDialog.show();
    }

    private void enableInput()
    {
        btnSetDateTimeSubmit.setEnabled(true);
        btnSetDateTimeSubmit.setAlpha(1.0f);
        btnGetCurrentCoords.setEnabled(true);
        btnGetCurrentCoords.setAlpha(1.0f);

        requestServerDialog.dismiss();
    }

    private void alertDialog(String title, String message)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Si",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                mCommunicator.onLogoutCommand();
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                alert.dismiss();
                            }
                        });

        alert = builder.create();

        alert.show();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (reportStates != null)
        {
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
                reportStates.setAltitudine(etAltitude.getText().toString());
            }

            if (reportStates != null && reportStates.getLatitudine() != 0 && reportStates.getLongitudine() != 0) // && altitude != -999
            {
                reportStates.setGeneralInfoCompletionState(ReportStates.COORDS_SET);
            }

            realm.commitTransaction();
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

                    callGetPageWithAltitude = networkUtils.downloadURL(this, elevationUrl);
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

                locationRetriever = new LocationRetriever(activity, this);
            }
        }
    }

    private void disableInputAndShowProgressDialog()
    {
        btnGetCurrentCoords.setEnabled(false);
        btnGetCurrentCoords.setAlpha(.4f);

        requestServerDialog.show();
    }
}
