package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
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
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.GeaImagineRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;
import ru.alexangan.developer.geatech.Models.GeaModelloRapporto;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.SubproductItem;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.ViewUtils;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.SET_DATA_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.tokenStr;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class SetDateTimeFragment extends Fragment implements View.OnClickListener, Callback
{
    Calendar calendarNow;
    Calendar calendar;
    long elapsedDays;
    String strDateTimeSet, strDateTimeNow;
    ReportStates reportStates;
    Call setDateTimeCall;
    String strVisitDateTimeResponse;
    Activity activity;
    int idSopralluogo, stakedOut;
    VisitItem visitItem;
    String product_type;

    private TextView btnSetDateTimeSubmit;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private int selectedIndex;
    View rootView;
    NetworkUtils networkUtils;

    private Communicator mCommunicator;
    AlertDialog alert;
    boolean dateSet, timeSet;


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

/*        ProgressDialog requestServerDialog = new ProgressDialog(getActivity());
        requestServerDialog.setTitle("");
        requestServerDialog.setMessage(getString(R.string.TransmittingDataPleaseWait));
        requestServerDialog.setIndeterminate(true);*/

        dateSet = false;
        timeSet = false;
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

            updateDisplay();
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

            updateDisplay();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.set_date_time_fragment, container, false);

        TextView btnSetDateTime = (TextView) rootView.findViewById(R.id.btnSetDateTime);
        //btnAnnullaSetDateTime = (TextView) rootView.findViewById(R.id.btnAnnullaSetDateTime);
        btnSetDateTimeSubmit = (TextView) rootView.findViewById(R.id.btnSetDateTimeSubmit);
        TextView btnOpenMap = (TextView) rootView.findViewById(R.id.btnOpenMap);
        TextView btnOpenDialer = (TextView) rootView.findViewById(R.id.btnOpenDialer);

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
        String dataOraSopralluogo = geaSopralluogo.getData_ora_sopralluogo();
        idSopralluogo = geaSopralluogo.getId_sopralluogo();
        List<SubproductItem> listSubproducts = productData.getSubItem();

        realm.beginTransaction();
        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

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

        //mDateSetTextView = (TextView) rootView.findViewById(R.id.tvDateSet);
        //mTimeSetTextView = (TextView) rootView.findViewById(R.id.tvTimeSet);

        //String visitDateTime = reportStates!=null ? reportStates.getData_ora_sopralluogo() : " ";

        calendarNow = Calendar.getInstance();
        calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

        strDateTimeNow = sdf.format(calendarNow.getTime());

        if(dataOraSopralluogo.length() > 4)
        {
            try
            {
                calendar.setTime(sdf.parse(dataOraSopralluogo));

                int millsDiff = calendar.compareTo(calendarNow);

                if (millsDiff < 0)
                {
                    calendar = calendarNow;
                }

                updateDisplay();

            } catch (ParseException e)
            {
                e.printStackTrace();

                calendar = calendarNow;
            }
        }
        else
        {
            disableInput();
        }

        if(reportStates==null || reportStates.getData_ora_sopralluogo() == null)
        {
/*            btnAnnullaSetDateTime.setEnabled(false);
            btnAnnullaSetDateTime.setAlpha(.4f);*/
        }

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        btnSetDateTime.setOnClickListener(this);

        //btnAnnullaSetDateTime.setOnClickListener(this);

        btnSetDateTimeSubmit.setOnClickListener(this);
        btnOpenMap.setOnClickListener(this);
        btnOpenDialer.setOnClickListener(this);

        return rootView;
    }

    public void onClick(View v)
    {
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

/*        if (v.getId() == R.id.btnAnnullaSetDateTime)
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

            stakedOut = 0;

            disableInput();

            notifyServerDataOraSopralluogo(idSopralluogo, stakedOut);
        }*/

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

        setDateTimeCall = networkUtils.setData(this, SET_DATA_URL_SUFFIX, String.valueOf(jsonObject));
    }

    public void updateDisplay()
    {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM", Locale.ITALIAN);
        String shortDateStr = sdfDate.format(calendar.getTime());
        //mDateSetTextView.setText(shortDateStr);
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.ITALIAN);
        String shortTimeStr = sdfTime.format(calendar.getTime());
        //mTimeSetTextView.setText(shortTimeStr);
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        showToastMessage(getString(R.string.SetDateTimeFailed));

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
        if (call == setDateTimeCall)
        {
            strVisitDateTimeResponse = response.body().string();

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
/*        btnAnnullaSetDateTime.setEnabled(false);
        btnAnnullaSetDateTime.setAlpha(.4f);*/

        //requestServerDialog.show();
    }

    private void enableInput()
    {
        btnSetDateTimeSubmit.setEnabled(true);
        btnSetDateTimeSubmit.setAlpha(1.0f);
/*        btnAnnullaSetDateTime.setEnabled(true);
        btnAnnullaSetDateTime.setAlpha(1.0f);*/

        //requestServerDialog.dismiss();
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
}
