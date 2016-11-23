package com.example.zubcu.geatech.Fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
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

import com.example.zubcu.geatech.Adapters.SetVisitDateTimeListAdapter;
import com.example.zubcu.geatech.Interfaces.Communicator;
import com.example.zubcu.geatech.Models.DateTimeSetListCellModel;
import com.example.zubcu.geatech.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DateTimeSetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DateTimeSetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DateTimeSetFragment extends Fragment implements View.OnClickListener
{
    private TextView mDateSetTextView, mTimeSetTextView, mSetDateButton, mAnnullaSetDateTimeButton, mSetDateTimeSubmitButton;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootView;

    private OnFragmentInteractionListener mListener;
    private Communicator mCommunicator;


    public DateTimeSetFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DateTimeSetFragment newInstance(String param1, String param2)
    {
        DateTimeSetFragment fragment = new DateTimeSetFragment();
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay)
        {
            mYear = selectedYear;
            mMonth = selectedMonth;
            mDay = selectedDay;

            updateDisplay();
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

            mHour = selectedHour;
            mMinute = selectedMinute;

            updateDisplay();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.date_time_set_fragment, container, false);

        ArrayList<DateTimeSetListCellModel> list = new ArrayList<>();

        // Construct the data source
        list.add(new DateTimeSetListCellModel("1","2","3"));
        list.add(new DateTimeSetListCellModel("4","5","6"));

// Create the adapter to convert the array to views

            SetVisitDateTimeListAdapter adapter = new SetVisitDateTimeListAdapter(getActivity(), list );

        ListView listView =(ListView)rootView.findViewById(R.id.list);

            listView.setAdapter(adapter);

        mDateSetTextView = (TextView) rootView.findViewById(R.id.tvDateSet);
        mTimeSetTextView = (TextView) rootView.findViewById(R.id.tvTimeSet);
        mSetDateButton = (TextView) rootView.findViewById(R.id.btnSetDate);
        mAnnullaSetDateTimeButton = (TextView) rootView.findViewById(R.id.btnAnnullaSetDateTime);
        mSetDateTimeSubmitButton = (TextView) rootView.findViewById(R.id.btnSetDateTimeSubmit);

        mSetDateButton.setOnClickListener(this);
        mAnnullaSetDateTimeButton.setOnClickListener(this);
        mSetDateTimeSubmitButton.setOnClickListener(this);

        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        updateDisplay();

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
        if(v.getId() == R.id.btnSetDate)
        {
            TimePickerDialog dialogTimePicker = new TimePickerDialog(getActivity(), timePickerListener,
                    mHour, mMinute, DateFormat.is24HourFormat(getActivity()));
            //dialog.getTimePicker().setMaxDate(new Date().getTime());
            dialogTimePicker.show();

            DatePickerDialog dialogDatePicker = new DatePickerDialog(getActivity(), datePickerListener,
                    mYear, mMonth, mDay);
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
            //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            mCommunicator.onDateTimeSetReturned(true);
        }
    }

    public void updateDisplay() {
        mDateSetTextView.setText(new StringBuilder().append(mDay).append(".")
                .append(mMonth).append(".").append(mYear));

        mTimeSetTextView.setText(new StringBuilder().append(mHour).append(".")
                .append(mMinute));
    }
}
