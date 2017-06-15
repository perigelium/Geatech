package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.GlobalConstants;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Services.NotificationEventReceiver;

/**
 * Created by Alex Angan on 11/10/2016.*/

public class SettingsFragment extends Fragment implements View.OnClickListener
{

    FrameLayout flTermsOfUse, flLogout;
    private Communicator mCommunicator;
    private TextView tvTechnicianName;
    private LinearLayout llTermsOfUse, llSettings;
    private Button btnTermsOfUseOk;
    Activity activity;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mCommunicator = (Communicator)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);

        llSettings = (LinearLayout) rootView.findViewById(R.id.llSettings);

        llTermsOfUse = (LinearLayout) rootView.findViewById(R.id.llTermsOfUse);
        llTermsOfUse.setVisibility(View.GONE);
        btnTermsOfUseOk = (Button) rootView.findViewById(R.id.btnTermsOfUseOk);
        btnTermsOfUseOk.setOnClickListener(this);


        tvTechnicianName = (TextView) rootView.findViewById(R.id.tvTechnicianName);
        tvTechnicianName.setText(GlobalConstants.selectedTech.getFullNameTehnic());

        flTermsOfUse = (FrameLayout) rootView.findViewById(R.id.flTermsOfUse);
        flLogout = (FrameLayout) rootView.findViewById(R.id.flLogout);

        flTermsOfUse.setOnClickListener(this);
        flLogout.setOnClickListener(this);

        final Spinner spVisitRemindTime = (Spinner) rootView.findViewById(R.id.spVisitRemindTime);
        FrameLayout flVisitRemindTime = (FrameLayout) rootView.findViewById(R.id.flVisitRemindTime);

        flVisitRemindTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                spVisitRemindTime.performClick();
            }
        });

        String [] remindTimePoints = {"1 ora", "2 ore", "4 ore", "1 giorno", "2 giorni", "una settimana"};
        List<String> saRemindTimePoints = Arrays.asList(remindTimePoints);

        ArrayAdapter<String> visitRemindTimeListAdapter =
                new ArrayAdapter<>(activity, R.layout.spinner_visit_reminder_time_row, R.id.tvSpinnerVisitItem, saRemindTimePoints);
        spVisitRemindTime.setAdapter(visitRemindTimeListAdapter);

        spVisitRemindTime.setSelection(0);

        return rootView;
    }

    @Override
    public View getView()
    {
        return super.getView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        Calendar calendarNow = Calendar.getInstance(Locale.ITALY);
        long milliSecondsForNow = calendarNow.getTimeInMillis();
        long timeToShowNotification = milliSecondsForNow + 1000*10;
        NotificationEventReceiver.setupAlarm(activity.getApplicationContext(), timeToShowNotification);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.flTermsOfUse)
        {
            //getActivity().getFragmentManager().beginTransaction().remove(this).commit();

            //show Terms Of Use
            llSettings.setVisibility(View.GONE);
            llTermsOfUse.setVisibility(View.VISIBLE);
        }

        if(view.getId() == R.id.flLogout)
        {
            //mCommunicator.onLogoutCommand();
            mCommunicator.refreshGeaModels();
        }

        if(view.getId() == R.id.btnTermsOfUseOk)
        {
            llSettings.setVisibility(View.VISIBLE);
            llTermsOfUse.setVisibility(View.GONE);
        }
    }
}
