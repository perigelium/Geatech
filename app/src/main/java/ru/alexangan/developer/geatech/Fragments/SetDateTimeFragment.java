package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Adapters.SetVisitDateTimeListAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.SubproductItem;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;

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
    String strDateTime;
    ReportStates reportStates;
    Call setDateTimeCall;
    String strVisitDateTimeResponse;
    Activity activity;
    int idSopralluogo, stakedOut;

    private TextView mDateSetTextView, mTimeSetTextView, mSetDateButton, mAnnullaSetDateTimeButton, mSetDateTimeSubmitButton,
            btnApriMappa, btnChiama;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private int selectedIndex;
    View rootView;

    private Communicator mCommunicator;


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
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener()
    {
        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay)
        {
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

        mSetDateButton = (TextView) rootView.findViewById(R.id.btnSetDate);
        mAnnullaSetDateTimeButton = (TextView) rootView.findViewById(R.id.btnAnnullaSetDateTime);
        mSetDateTimeSubmitButton = (TextView) rootView.findViewById(R.id.btnSetDateTimeSubmit);
        btnApriMappa = (TextView) rootView.findViewById(R.id.btnApriMappa);
        btnChiama = (TextView) rootView.findViewById(R.id.btnChiama);

        VisitItem visitItem = visitItems.get(selectedIndex);
        ClientData clientData = visitItem.getClientData();
        ProductData productData = visitItem.getProductData();
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        idSopralluogo = geaSopralluogo.getId_sopralluogo();
        List<SubproductItem> list = productData.getSubItem();

        realm.beginTransaction();
        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

        SetVisitDateTimeListAdapter adapter = new SetVisitDateTimeListAdapter(getActivity(), list);

        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

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

        mDateSetTextView = (TextView) rootView.findViewById(R.id.tvDateSet);
        mTimeSetTextView = (TextView) rootView.findViewById(R.id.tvTimeSet);

        String visitDateTime = reportStates!=null ? reportStates.getData_ora_sopralluogo() : " ";

        calendarNow = Calendar.getInstance();
        calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

        try
        {
            calendar.setTime(sdf.parse(visitDateTime));

            int millsDiff = calendar.compareTo(calendarNow);

            if (millsDiff < 0)
            {
                calendar = calendarNow;
            }

        } catch (ParseException e)
        {
            e.printStackTrace();

            calendar = calendarNow;
        }

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        updateDisplay();

        mSetDateButton.setOnClickListener(this);
        mAnnullaSetDateTimeButton.setOnClickListener(this);
        mSetDateTimeSubmitButton.setOnClickListener(this);
        btnApriMappa.setOnClickListener(this);
        btnChiama.setOnClickListener(this);

        return rootView;
    }

    public void onClick(View v)
    {
        if (v.getId() == R.id.btnApriMappa)
        {
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
                    Toast.makeText(getActivity(), "installare un'applicazione mappe", Toast.LENGTH_LONG).show();
                }
            }
        }

        if (v.getId() == R.id.btnChiama)
        {
            String phoneNumber = "tel:" + visitItems.get(selectedIndex).getClientData().getMobile();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(phoneNumber));
            startActivity(intent);
        }

        if (v.getId() == R.id.btnSetDate)
        {
            TimePickerDialog dialogTimePicker = new TimePickerDialog(getActivity(), timePickerListener,
                    mHour, mMinute, DateFormat.is24HourFormat(getActivity()));
            dialogTimePicker.show();

            DatePickerDialog dialogDatePicker = new DatePickerDialog(getActivity(), datePickerListener,
                    mYear, mMonth - 1, mDay);
            dialogDatePicker.show();
        }

        if (v.getId() == R.id.btnAnnullaSetDateTime)
        {
            stakedOut = 0;
            notifyServerDataOraSopralluogo(idSopralluogo, stakedOut);

            //mCommunicator.onDateTimeSetReturned(false);
        }

        if (v.getId() == R.id.btnSetDateTimeSubmit)
        {
            stakedOut = 1;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            strDateTime = sdf.format(calendar.getTime());

            notifyServerDataOraSopralluogo(idSopralluogo, stakedOut);

            // send date-time and id_sopralluogo
            //mCommunicator.onDateTimeSetReturned(true);
        }
    }

    private void notifyServerDataOraSopralluogo(int idSopralluogo, int stakedOut)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("token", tokenStr);
            jsonObject.put("id_sopralluogo", idSopralluogo);
            jsonObject.put("data_ora_presa_appuntamento", strDateTime);
            jsonObject.put("inizializzazione", stakedOut);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        NetworkUtils networkUtils = new NetworkUtils();
        setDateTimeCall = networkUtils.setData(this, SET_DATA_URL_SUFFIX, String.valueOf(jsonObject));
    }

    public void updateDisplay()
    {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM", Locale.ITALIAN);
        String shortDateStr = sdfDate.format(calendar.getTime());
        mDateSetTextView.setText(shortDateStr);
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.ITALIAN);
        String shortTimeStr = sdfTime.format(calendar.getTime());
        mTimeSetTextView.setText(shortTimeStr);
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        showToastMessage("Dato e ora inviato, risposta non ha ricevuto");
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        if (call == setDateTimeCall)
        {
            strVisitDateTimeResponse = response.body().string();

            if (strVisitDateTimeResponse == null)
            {
                showToastMessage("Dato e ora inviato, risposta non ha ricevuto");

                return;
            } else
            {
                final JSONObject jsonObject;

                try
                {
                    jsonObject = new JSONObject(strVisitDateTimeResponse);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                    return;
                }

                if (jsonObject.has("error"))
                {
                    final String errorStr;

                    try
                    {
                        errorStr = jsonObject.getString("error");
                        if (errorStr.length() != 0)
                        {
                            showToastMessage(errorStr);
                        }

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                        return;
                    }
                } else
                {
                    showToastMessage("Dato e ora inviato, server ritorna: " + strVisitDateTimeResponse);

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
                                                final String str_id_rapporto_sopralluogo = jsonObject.getString("id_rapporto_sopralluogo");
                                                int id_rapporto_sopralluogo = Integer.valueOf(str_id_rapporto_sopralluogo);


                                                if (reportStates == null)
                                                {
                                                    realm.beginTransaction();
                                                    ReportStates newReportStates = new ReportStates(company_id, selectedTech.getId(), idSopralluogo, id_rapporto_sopralluogo);
                                                    realm.copyToRealm(newReportStates);
                                                    realm.commitTransaction();
                                                }

                                                if (stakedOut == 1)
                                                {
                                                    realm.beginTransaction();
                                                    reportStates.setData_ora_sopralluogo(strDateTime);
                                                    reportStates.setId_rapporto_sopralluogo(id_rapporto_sopralluogo);

                                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);
                                                    String shortDateStr = sdf.format(calendarNow.getTime());
                                                    reportStates.setData_ora_presa_appuntamento(shortDateStr);

                                                    reportStates.setNome_tecnico(selectedTech.getFullNameTehnic());
                                                    realm.commitTransaction();
                                                }
                                            } catch (JSONException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }

                                        if (stakedOut == 0)
                                        {
                                            realm.beginTransaction();
                                            reportStates.setData_ora_sopralluogo(null);
                                            reportStates.setNome_tecnico(null);
                                            realm.commitTransaction();
                                        }

                                        mCommunicator.onDateTimeSetReturned(false);
                                    }
                                });
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
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
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
