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
import android.widget.FrameLayout;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Adapters.SetVisitDateTimeListAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Interfaces.LocationRetrievedEvents;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.GeaImmagineRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaRapporto;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.GlobalConstants;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.SubproductItem;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Network.LocationRetriever;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.Overrides.CustomTimePickerDialog;
import ru.alexangan.developer.geatech.R;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.SET_DATA_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.tokenStr;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class SetDateTimeFragment extends Fragment implements View.OnClickListener, Callback, LocationRetrievedEvents
{
    Realm realm;
    Calendar calendarNow;
    Calendar calendar;
    String strDateTimeSet, strDateTimeNow;
    Call callSetDateTime;
    int idSopralluogo;
    VisitItem visitItem;
    String product_type;
    EditText etCoordNord, etCoordEst, etAltitude;
    Call callGetPageWithAltitude;
    String latitude, longitude;
    String altitude;
    LocationRetriever locationRetriever;
    Activity activity;
    Location mLastLocation;
    ReportItem reportItem;
    Communicator communicator;
    private ProgressDialog requestServerDialog;
    ClientData clientData;
    private int PERMISSION_REQUEST_CODE = 11;
    String dataOraSopralluogo;
    GeaRapporto geaRapportoSopralluogo;
    String strNotKnown;

    private TextView btnSetDateTimeSubmit;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private int selectedVisitId;
    View rootView;
    NetworkUtils networkUtils;

    private Communicator mCommunicator;
    AlertDialog alert;
    boolean dateSet, timeSet;
    private TextView tvdataOraSopralluogo;
    private TextView tvTechnicianName;
    private Button btnOpenMap, btnOpenDialer;
    private Button btnGetCurrentCoords;
    private FrameLayout flSetDateTimeSubmit;
    private ProductData productData;
    private TextView tvListSottprodottiTitle;
    private TextView tvClientName, tvTypeOfService, tvProductModel, tvClientPhone, tvClientAddress;
    private int id_rapporto_sopralluogo;
    private FrameLayout flGetCurrentCoords;
    private String tech_name;
    private String formattedDate;
    private int tech_id;
    private boolean ownReportMode;

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
        realm = Realm.getDefaultInstance();

        if (getArguments() != null)
        {
            selectedVisitId = getArguments().getInt("selectedVisitId");
        }

        networkUtils = new NetworkUtils();

        dateSet = false;
        timeSet = false;

        communicator = (Communicator) getActivity();

        requestServerDialog = new ProgressDialog(getActivity());
        requestServerDialog.setTitle("");
        requestServerDialog.setMessage(getString(R.string.DownloadingDataPleaseWait));
        requestServerDialog.setIndeterminate(true);

        strNotKnown = getString(R.string.Unknown);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.set_date_time_fragment, container, false);

        flSetDateTimeSubmit = (FrameLayout) rootView.findViewById(R.id.flSetDateTimeSubmit);

        btnSetDateTimeSubmit = (TextView) rootView.findViewById(R.id.btnSetDateTimeSubmit);
        btnSetDateTimeSubmit.setOnClickListener(this);

        btnOpenMap = (Button) rootView.findViewById(R.id.btnOpenMap);
        btnOpenMap.setOnClickListener(this);

        btnOpenDialer = (Button) rootView.findViewById(R.id.btnOpenDialer);
        btnOpenDialer.setOnClickListener(this);

        tvdataOraSopralluogo = (TextView) rootView.findViewById(R.id.tvdataOraSopralluogo);
        tvListSottprodottiTitle = (TextView) rootView.findViewById(R.id.tvListSottprodottiTitle);
        //svVisitInfoScrollView = (ScrollView) rootView.findViewById(R.id.svVisitInfoScrollView);
/*        LinearLayout llSetDateTime = (LinearLayout) rootView.findViewById(R.id.llSetDateTime);
        //llSetDateTime.setOnClickListener(this);

        llSetDateTime.setOnTouchListener(new ActivitySwipeDetector(activity)
        {
            @Override
            public void onLeftToRightSwipe()
            {
                saveVariablesToTheDatabase();
                mCommunicator.onCompilationHorisontalSwipeReturned(R.id.btnSopralluogoInfo, false);
            }

            @Override
            public void onRightToLeftSwipe()
            {
                mCommunicator.onCompilationHorisontalSwipeReturned(R.id.btnSopralluogoInfo, true);
            }
        });*/

        tvTechnicianName = (TextView) rootView.findViewById(R.id.tvTechnicianName);

        flGetCurrentCoords = (FrameLayout) rootView.findViewById(R.id.flGetCurrentCoords);
        btnGetCurrentCoords = (Button) rootView.findViewById(R.id.btnGetCurrentCoords);
        btnGetCurrentCoords.setOnClickListener(this);

        etCoordNord = (EditText) rootView.findViewById(R.id.etCoordNord);
        //etCoordNord.setOnTouchListener(this);
        etCoordEst = (EditText) rootView.findViewById(R.id.etCoordEst);
        //etCoordEst.setOnTouchListener(this);
        etAltitude = (EditText) rootView.findViewById(R.id.etAltitude);
        //etAltitude.setOnTouchListener(this);

        tvClientName = (TextView) rootView.findViewById(R.id.tvClientName);

        tvClientPhone = (TextView) rootView.findViewById(R.id.tvClientPhone);

        tvTypeOfService = (TextView) rootView.findViewById(R.id.tvTypeOfService);

        tvProductModel = (TextView) rootView.findViewById(R.id.tvProductModel);

        tvClientAddress = (TextView) rootView.findViewById(R.id.tvClientAddress);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        visitItem = visitItems.get(selectedVisitId);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        clientData = visitItem.getClientData();
        productData = visitItem.getProductData();
        geaRapportoSopralluogo = visitItem.getGeaRapporto();

        tech_id = geaSopralluogo.getId_tecnico();
        tech_name = geaRapportoSopralluogo.getNome_tecnico();
        int id_product_type = productData.getIdProductType();
        idSopralluogo = geaSopralluogo.getId_sopralluogo();
        id_rapporto_sopralluogo = geaRapportoSopralluogo.getId_rapporto_sopralluogo();
        List<SubproductItem> listSubproducts = productData.getSubproductItems();
        dataOraSopralluogo = geaSopralluogo.getData_ora_sopralluogo();

        realm.beginTransaction();
        GeaModelloRapporto geaModello = realm.where(GeaModelloRapporto.class).equalTo("id_product_type", id_product_type).findFirst();
        realm.commitTransaction();

        if (geaModello != null)
        {
            product_type = geaModello.getNome_modello();
        }

        tvClientName.setText(clientData.getName());
        tvClientPhone.setText(clientData.getMobile());
        tvClientAddress.setText(clientData.getAddress());
        tvTypeOfService.setText(productData.getProductType());
        tvProductModel.setText(productData.getProduct());

        SetVisitDateTimeListAdapter adapter = new SetVisitDateTimeListAdapter(getActivity(), listSubproducts);

        ListView listView = (ListView) rootView.findViewById(R.id.listSubproducts);
        listView.setAdapter(adapter);

        if (adapter.getCount() == 0)
        {
            tvListSottprodottiTitle.setText("");
        }

        calendarNow = Calendar.getInstance();
        calendar = Calendar.getInstance();

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        //strDateTimeNow = sdf.format(calendarNow.getTime());

        ownReportMode = tech_id == GlobalConstants.selectedTech.getId();

        if (tech_id == 0)
        {
            tvTechnicianName.setText("");
            flSetDateTimeSubmit.setVisibility(View.VISIBLE);
            return;
        } else
        {
            tvTechnicianName.setText(tech_name);
        }

        if (tech_id == GlobalConstants.selectedTech.getId())
        {
            realm.beginTransaction();
            reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                    .equalTo("id_sopralluogo", idSopralluogo)
                    .equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo)
                    .findFirst();
            realm.commitTransaction();

            if (reportItem != null)
            {
                GeaSopralluogo gea_sopralluoghi = reportItem.getGeaSopralluogo();
                dataOraSopralluogo = gea_sopralluoghi.getData_ora_sopralluogo();
            } else
            {
                String strDateTimeVisitSet = geaSopralluogo.getData_ora_presa_appuntamento();
                GeaSopralluogo gea_sopralluoghi = new GeaSopralluogo(idSopralluogo, selectedTech.getId(),
                        strDateTimeVisitSet, dataOraSopralluogo);
                GeaRapporto gea_rapporto = new GeaRapporto(idSopralluogo, id_rapporto_sopralluogo);

                ClientData clientDataEx = realm.copyFromRealm(visitItem.getClientData());

                realm.beginTransaction();
                ReportItem reportItem = new ReportItem(company_id, selectedTech.getId(),
                        idSopralluogo, id_rapporto_sopralluogo,
                        new ReportStates(ReportStates.GENERAL_INFO_DATETIME_SET),
                        gea_sopralluoghi, clientDataEx, gea_rapporto,
                        new RealmList<GeaItemRapporto>(),
                        new RealmList<GeaImmagineRapporto>());
                realm.copyToRealm(reportItem);
                realm.commitTransaction();
            }
        } else
        {
            flGetCurrentCoords.setVisibility(View.GONE);
        }

        if (dataOraSopralluogo != null)
        {
            reformatDateTime();
        }
    }

    private void reformatDateTime()
    {
        SimpleDateFormat sdfSopralluogo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);
        Date dateSopralluogo;
        try
        {
            dateSopralluogo = sdfSopralluogo.parse(dataOraSopralluogo);
            sdfSopralluogo = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ITALIAN);
            formattedDate = sdfSopralluogo.format(dateSopralluogo);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (reportItem != null)
        {
            GeaRapporto geaRapporto = reportItem.getGea_rapporto_sopralluogo();

            latitude = geaRapporto.getLatitudine();
            longitude = geaRapporto.getLongitudine();
            altitude = geaRapporto.getAltitudine();

            //coordsUnchanged = latitude != 0 && longitude != 0;// && altitude != ReportStates.ALTITUDE_UNKNOWN;

            if (latitude == null)
            {
                latitude = String.valueOf(clientData.getCoordNord());
            }

            if (longitude == null)
            {
                longitude = String.valueOf(clientData.getCoordEst());
            }
        } else
        {
            latitude = String.valueOf(clientData.getCoordNord());
            longitude = String.valueOf(clientData.getCoordEst());
            altitude = geaRapportoSopralluogo.getAltitudine();
        }

        if (latitude.equals("0.0"))
        {
            latitude = strNotKnown;
        }

        if (longitude.equals("0.0"))
        {
            longitude = strNotKnown;
        }

        if (altitude == null || altitude.length() == 0)
        {
            altitude = strNotKnown;
        }

        etCoordNord.setText(latitude);
        etCoordEst.setText(longitude);
        etAltitude.setText(altitude);

        if (tech_id != 0)
        {
            tvdataOraSopralluogo.setText(formattedDate);
            tvTechnicianName.setText(tech_name);
            flSetDateTimeSubmit.setVisibility(View.GONE);
        } else
        {
            tvTechnicianName.setText("");
            flSetDateTimeSubmit.setVisibility(View.VISIBLE);
        }
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
            if (dateSet)
            {
                enableInput();
            }

            mHour = selectedHour;
            calendar.set(Calendar.HOUR_OF_DAY, mHour);
            mMinute = selectedMinute;
            calendar.set(Calendar.MINUTE, mMinute);
            calendar.set(Calendar.SECOND, 0);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            strDateTimeSet = sdf.format(calendar.getTime());

            Calendar calendarNow = Calendar.getInstance();
            strDateTimeNow = sdf.format(calendarNow.getTime());

/*            int millsDiff = calendar.compareTo(calendarNow);

            if (millsDiff <= 0)
            {
                elapsedDays = 0;
            } else
            {
                long milliSeconds = calendar.getTimeInMillis();
                long milliSecondsNow = calendarNow.getTimeInMillis();
                long periodMilliSeconds = (milliSeconds - milliSecondsNow);
                elapsedDays = periodMilliSeconds / 1000 / 60 / 60 / 24;
            }*/

            //tvdataOraSopralluogo.setText(strDateTimeSet);
            //tvTechnicianName.setVisibility(View.VISIBLE);

            if (!NetworkUtils.isNetworkAvailable(activity))
            {
                showToastMessage(getString(R.string.CheckInternetConnection));
                return;
            }

            if (tokenStr == null)
            {
                //showToastMessage("Modalità offline, si prega di logout e login di nuovo");
                alertDialog("Info", getString(R.string.OfflineModeShowLoginScreenQuestion));
                return;
            }

            if (strDateTimeSet != null && strDateTimeNow != null)
            {
                disableInput();
                notifyServerDataOraSopralluogo(idSopralluogo);
            }
        }
    };

    public void onClick(View v)
    {
        if (v.getId() == R.id.btnGetCurrentCoords)
        {
            if (!ownReportMode)
            {
                showToastMessage(getString(R.string.SetDateAndTimeFirst));
                return;
            }

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
            if (!NetworkUtils.isNetworkAvailable(activity))
            {
                showToastMessage(getString(R.string.CheckInternetConnection));
                return;
            }

            double latitude = visitItems.get(selectedVisitId).getClientData().getCoordNord();
            double longitude = visitItems.get(selectedVisitId).getClientData().getCoordEst();

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
            String phoneNumber = "tel:" + visitItems.get(selectedVisitId).getClientData().getMobile();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(phoneNumber));
            startActivity(intent);
        }

        if (v.getId() == R.id.btnSetDateTimeSubmit)
        {
            openSetDateTimeDialog();
        }

/*        if(v.getId() == R.id.llSetDateTime)
        {
            if (swipeDetector.swipeDetected())
            {
                if (swipeDetector.getAction() == SwipeDetector.Action.LR)
                {
showToastMessage("swipe detected");
                }

                if (swipeDetector.getAction() == SwipeDetector.Action.RL)
                {
                    showToastMessage("swipe detected");
                }
            }
        }*/
    }

    private void openSetDateTimeDialog()
    {
        TimePickerDialog DialogTimePicker = new CustomTimePickerDialog(getActivity(), timePickerListener,
                mHour, mMinute, DateFormat.is24HourFormat(getActivity()));
        DialogTimePicker.show();

        DatePickerDialog DialogDatePicker = new DatePickerDialog(getActivity(), datePickerListener,
                mYear, mMonth - 1, mDay);
        DialogDatePicker.show();
    }

    private void notifyServerDataOraSopralluogo(int idSopralluogo)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("token", tokenStr);
            jsonObject.put("id_sopralluogo", idSopralluogo);
            jsonObject.put("data_ora_sopralluogo", strDateTimeSet);
            jsonObject.put("data_ora_presa_appuntamento", strDateTimeNow);
            jsonObject.put("id_tecnico", selectedTech.getId());

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        callSetDateTime = networkUtils.setData(this, SET_DATA_URL_SUFFIX, String.valueOf(jsonObject));
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        if (call == callGetPageWithAltitude)
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
        if (call == callGetPageWithAltitude)
        {
            String result = response.body().string();

            int intAltitude = (int) parseElevationFromGoogleMaps(result);
            altitude = String.valueOf(intAltitude);

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
                                            realm.beginTransaction();
                                            reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                                                    .equalTo("id_sopralluogo", idSopralluogo)
                                                    .equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo)
                                                    .findFirst();
                                            realm.commitTransaction();

                                            if (reportItem == null)
                                            {
                                                String str_id_rapporto_sopralluogo = jsonObject.getString("id_rapporto_sopralluogo");
                                                int id_rapporto_sopralluogo = Integer.valueOf(str_id_rapporto_sopralluogo);
                                                tech_id = GlobalConstants.selectedTech.getId();
                                                tech_name = GlobalConstants.selectedTech.getFullNameTehnic();

                                                GeaSopralluogo gea_sopralluoghi = new GeaSopralluogo(idSopralluogo, selectedTech.getId(),
                                                        strDateTimeNow, strDateTimeSet);

                                                ClientData clientDataEx = realm.copyFromRealm(visitItem.getClientData());

                                                GeaRapporto gea_rapporto = new GeaRapporto(idSopralluogo, id_rapporto_sopralluogo);

                                                realm.beginTransaction();
                                                ReportItem reportItem = new ReportItem(company_id, tech_id,
                                                        idSopralluogo, id_rapporto_sopralluogo,
                                                        new ReportStates(ReportStates.GENERAL_INFO_DATETIME_SET),
                                                        gea_sopralluoghi, clientDataEx, gea_rapporto,
                                                        new RealmList<GeaItemRapporto>(),
                                                        new RealmList<GeaImmagineRapporto>());
                                                realm.copyToRealm(reportItem);
                                                realm.commitTransaction();
                                            } else
                                            {
                                                realm.beginTransaction();
                                                reportItem.getGeaSopralluogo().setData_ora_presa_appuntamento(strDateTimeNow);
                                                reportItem.getGeaSopralluogo().setData_ora_sopralluogo(strDateTimeSet);
                                                realm.commitTransaction();
                                            }
                                            tvTechnicianName.setText(tech_name);

                                            reformatDateTime();
                                            if (formattedDate != null)
                                            {
                                                tvdataOraSopralluogo.setText(formattedDate);
                                            }
                                            flSetDateTimeSubmit.setVisibility(View.GONE);
                                            btnGetCurrentCoords.setVisibility(View.VISIBLE);
                                            btnGetCurrentCoords.getParent().requestChildFocus(btnGetCurrentCoords, btnGetCurrentCoords);

                                            showToastMessage(getString(R.string.DateTimeSetSuccessfully));//, server ritorna: " + strVisitDateTimeResponse
                                        } catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }

/*                                    if (stakedOut == 0)
                                    {
                                        if (reportItem != null)
                                        {
                                            cleanUpOldReportData();
                                        }

                                        showToastMessage(getString(R.string.VisitHasCancelled));
                                    }*/

                                    enableInput();

                                    mCommunicator.onDateTimeSetReturned(selectedVisitId);
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
                } else
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

    private void showToastMessage(final String msg)
    {
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
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

        saveVariablesToTheDatabase();
    }

    private void saveVariablesToTheDatabase()
    {
        if (reportItem != null)
        {
            GeaRapporto geaRapporto = reportItem.getGea_rapporto_sopralluogo();
            realm.beginTransaction();

            if (etCoordNord.getText().toString().length() != 0)
            {
                geaRapporto.setLatitudine(etCoordNord.getText().toString());
            }
            if (etCoordEst.getText().toString().length() != 0)
            {
                geaRapporto.setLongitudine(etCoordEst.getText().toString());
            }

            if (etAltitude.getText().toString().length() != 0)
            {
                geaRapporto.setAltitudine(etAltitude.getText().toString());
            }

            if (geaRapporto.getLatitudine() != null && !geaRapporto.getLatitudine().equals(strNotKnown)
                    && geaRapporto.getLongitudine() != null && !geaRapporto.getLongitudine().equals(strNotKnown))
            {
                reportItem.getReportStates().setGeneral_info_coords_set(ReportStates.GENERAL_INFO_COORDS_SET);
            } else
            {
                reportItem.getReportStates().setGeneral_info_coords_set(ReportStates.GENERAL_INFO_COORDS_NOT_SET);
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
            latitude = String.valueOf(mLastLocation.getLatitude());
            longitude = String.valueOf(mLastLocation.getLongitude());

            etCoordNord.setText(latitude);
            etCoordEst.setText(longitude);

            if (mLastLocation.hasAltitude())
            {
                int intAltitude = (int) mLastLocation.getAltitude();
                altitude = String.valueOf(intAltitude);
                etAltitude.setText(altitude, TextView.BufferType.EDITABLE);
            } else
            {
                if (NetworkUtils.isNetworkAvailable(activity))// && NetworkUtils.isOnline())
                {
                    String elevationUrl = "http://maps.googleapis.com/maps/api/elevation/"
                            + "xml?locations=" + latitude
                            + "," + longitude
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
            if(!latitude.equals("Sconosciuto") && !longitude.equals("Sconosciuto"))
            {
                Float fLatitude = Float.valueOf(latitude);
                Float fLongitude = Float.valueOf(longitude);
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", fLatitude, fLongitude, "Where the party is at");
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
