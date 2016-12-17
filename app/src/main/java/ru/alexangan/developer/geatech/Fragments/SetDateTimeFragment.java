package ru.alexangan.developer.geatech.Fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.exceptions.RealmMigrationNeededException;
import ru.alexangan.developer.geatech.Adapters.SetVisitDateTimeListAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.SubproductItem;
import ru.alexangan.developer.geatech.Models.VisitStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Activities.LoginActivity.realm;
import static ru.alexangan.developer.geatech.Network.RESTdataReceiver.visitItems;

public class SetDateTimeFragment extends Fragment implements View.OnClickListener
{
    final Calendar calendarNow;
    Calendar calendar;
    long elapsedDays;
    String strDateTime;

    private TextView mDateSetTextView, mTimeSetTextView, mSetDateButton, mAnnullaSetDateTimeButton, mSetDateTimeSubmitButton,
            btnApriMappa, btnChiama;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int selectedIndex;
    private String mParam2;
    View rootView;

    private OnFragmentInteractionListener mListener;
    private Communicator mCommunicator;


    public SetDateTimeFragment()
    {
        calendarNow = Calendar.getInstance();
        calendar = Calendar.getInstance();
    }

    // TODO: Rename and change types and number of parameters
    public static SetDateTimeFragment newInstance(String param1, String param2)
    {
        SetDateTimeFragment fragment = new SetDateTimeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCommunicator = (Communicator)getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            }
            else
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
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.set_date_time_fragment, container, false);

        mSetDateButton = (TextView) rootView.findViewById(R.id.btnSetDate);
        mAnnullaSetDateTimeButton = (TextView) rootView.findViewById(R.id.btnAnnullaSetDateTime);
        mSetDateTimeSubmitButton = (TextView) rootView.findViewById(R.id.btnSetDateTimeSubmit);
        btnApriMappa = (TextView) rootView.findViewById(R.id.btnApriMappa);
        btnChiama = (TextView) rootView.findViewById(R.id.btnChiama);

        mSetDateButton.setOnClickListener(this);
        mAnnullaSetDateTimeButton.setOnClickListener(this);
        mSetDateTimeSubmitButton.setOnClickListener(this);
        btnApriMappa.setOnClickListener(this);
        btnChiama.setOnClickListener(this);

        VisitItem visitItem = visitItems.get(selectedIndex);
        ClientData clientData = visitItem.getClientData();
        ProductData productData = visitItem.getProductData();
        VisitStates visitStates = visitItem.getVisitStates();
        List<SubproductItem> list = productData.getSubItem();


            SetVisitDateTimeListAdapter adapter = new SetVisitDateTimeListAdapter(getActivity(),  list);

        ListView listView =(ListView)rootView.findViewById(R.id.list);

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

        String visitDateTime = visitStates.getDataOraSopralluogo();
        if(visitDateTime == null)
        {
            visitDateTime = visitStates.getDataSollecitoAppuntamento();
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

        try
        {
            calendar.setTime(sdf.parse(visitDateTime));
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        mYear = calendarNow.get(Calendar.YEAR);
        mMonth = calendarNow.get(Calendar.MONTH) + 1;
        mDay = calendarNow.get(Calendar.DAY_OF_MONTH);
        mHour = calendarNow.get(Calendar.HOUR_OF_DAY);
        mMinute = calendarNow.get(Calendar.MINUTE);

/*        mYear = visitsList.get(selectedIndex).getVisitYear();
        mMonth =  visitsList.get(selectedIndex).getVisitMonth();
        mDay =  visitsList.get(selectedIndex).getVisitDay();
        mHour =  visitsList.get(selectedIndex).getVisitHour();
        mMinute =  visitsList.get(selectedIndex).getVisitMinute();*/

/*        else
        {
            mYear = calendarNow.get(Calendar.YEAR);
            mMonth = calendarNow.get(Calendar.MONTH) + 1;
            mDay = calendarNow.get(Calendar.DAY_OF_MONTH);
            mHour = calendarNow.get(Calendar.HOUR_OF_DAY);
            mMinute = calendarNow.get(Calendar.MINUTE);
        }*/

        updateDisplay();





        // Initialize Realm
        //Realm.init(getActivity());

// Get a Realm instance for this thread
        //Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        //final VisitItem realmVisitItem = realm.createObject(VisitItem.class);

        int visitItemsSize = visitItems.size();
        VisitItem realmVisitItem = realm.where(VisitItem.class).equalTo("id", 5).findFirst();
        Boolean isReportsent = realmVisitItem.getReportStates().isReportSent();
        int notesCount = realm.where(VisitItem.class).findAll().size();
        //final VisitItem realmVisitItem = realm.copyToRealmOrUpdate(visitItem);

        notesCount = realm.where(ReportStates.class).findAll().size();

        ReportStates reportStates = realmVisitItem.getReportStates();
        String status = reportStates.getGeneralInfoCompletionStateString().Value();
        realmVisitItem.getReportStates().setReportSent(true);
        realm.commitTransaction();





        return  rootView;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void onClick(View v)
    {
        if (v.getId() == R.id.btnApriMappa)
        {
            double latitude = visitItems.get(selectedIndex).getClientData().getCoordNord();
            double longitude = visitItems.get(selectedIndex).getClientData().getCoordEst();

            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", (float)latitude, (float)longitude, "Where the party is at");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException ex)
            {
                try
                {
                    Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(unrestrictedIntent);
                }
                catch(ActivityNotFoundException innerEx)
                {
                    Toast.makeText(getActivity(), "Please install a maps application", Toast.LENGTH_LONG).show();
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

        if(v.getId() == R.id.btnSetDate)
        {
            TimePickerDialog dialogTimePicker = new TimePickerDialog(getActivity(), timePickerListener,
                    mHour, mMinute, DateFormat.is24HourFormat(getActivity()));
            //dialog.getTimePicker().setMaxDate(new Date().getTime());
            dialogTimePicker.show();

            DatePickerDialog dialogDatePicker = new DatePickerDialog(getActivity(), datePickerListener,
                    mYear, mMonth - 1, mDay);
            //dialog.getDatePicker().setMaxDate(new Date().getTime());
            dialogDatePicker.show();
        }

        if(v.getId() == R.id.btnAnnullaSetDateTime)
        {
            //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            mCommunicator.onDateTimeSetReturned(false);
        }

        if(v.getId() == R.id.btnSetDateTimeSubmit)
        {
/*            visitsList.get(selectedIndex).setVisitYear(mYear);

            Pair date = new Pair(mDay, mMonth );
            visitsList.get(selectedIndex).setVisitDate(date);

            Pair time = new Pair(mHour, mMinute);
            visitsList.get(selectedIndex).setVisitTime(time);*/


            VisitItem visitItem = visitItems.get(selectedIndex);
            VisitStates visitStates = visitItem.getVisitStates();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            strDateTime = sdf.format(calendar.getTime());
            visitStates.setDataOraSopralluogo(strDateTime);

            //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            mCommunicator.onDateTimeSetReturned(true);
        }
    }

    public void updateDisplay()
    {
/*        mDateSetTextView.setText(new StringBuilder().append(mDay).append(".")
                .append(mMonth).append(".").append(mYear));*/

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM", Locale.ITALIAN);
        String shortDateStr = sdfDate.format(calendar.getTime());
        mDateSetTextView.setText(shortDateStr);
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.ITALIAN);
        String shortTimeStr = sdfTime.format(calendar.getTime());
        mTimeSetTextView.setText(shortTimeStr);

/*        String minuteStr = Integer.toString(mMinute);
        if (minuteStr.length() == 1)
        {
            minuteStr = "0" + minuteStr;
        }

        mTimeSetTextView.setText(new StringBuilder().append(mHour).append(":")
                .append(minuteStr));
    }*/
    }

}
