package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.GlobalConstants;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Services.NotificationEventReceiver;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

/**
 * Created by Alex Angan on 11/10/2016.*/

public class SettingsFragment extends Fragment implements View.OnClickListener
{

    FrameLayout flTermsOfUse, flLogout;
    private Communicator mCommunicator;
    private TextView tvTechnicianName;
    private LinearLayout llTermsOfUse, llSettings;
    private Button btnTermsOfUseOk;
    Spinner spVisitRemindTime;
    Activity activity;
    Calendar calendarNow;
    private int curReminderPos;
    long milliSecondsForNow;
    String [] strRemindTimeDelays = {"No", "1 ora", "2 ore", "3 ore"};
    long [] remindTimeDelays = {-1, 3600000, 2*3600000, 3*3600000};

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mCommunicator = (Communicator)getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        calendarNow = Calendar.getInstance(Locale.ITALY);
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
        tvTechnicianName.setText(selectedTech.getFullNameTehnic());

        flTermsOfUse = (FrameLayout) rootView.findViewById(R.id.flTermsOfUse);
        flLogout = (FrameLayout) rootView.findViewById(R.id.flLogout);

        flTermsOfUse.setOnClickListener(this);
        flLogout.setOnClickListener(this);

        spVisitRemindTime = (Spinner) rootView.findViewById(R.id.spVisitRemindTime);
        FrameLayout flVisitRemindTime = (FrameLayout) rootView.findViewById(R.id.flVisitRemindTime);

        flVisitRemindTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                spVisitRemindTime.performClick();
            }
        });

        spVisitRemindTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
            {
                if (curReminderPos == position)
                {
                    return;
                } else
                {
                    curReminderPos = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        List<String> saRemindTimePoints = Arrays.asList(strRemindTimeDelays);

        ArrayAdapter<String> visitRemindTimeListAdapter =
                new ArrayAdapter<>(activity, R.layout.spinner_visit_reminder_time_row, R.id.tvSpinnerVisitItem, saRemindTimePoints);
        spVisitRemindTime.setAdapter(visitRemindTimeListAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View getView()
    {
        return super.getView();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        curReminderPos = mSettings.getInt("curReminderPos", 0);
        spVisitRemindTime.setSelection(curReminderPos);
    }

    @Override
    public void onPause()
    {
        super.onPause();

        mSettings.edit().putInt("curReminderPos", curReminderPos).apply();

        if(curReminderPos == 0)
        {
            return;
        }

        mSettings.edit().putString("reminderDelayHours", strRemindTimeDelays[curReminderPos]).apply();

        milliSecondsForNow = calendarNow.getTimeInMillis();

        for (VisitItem visitItem : visitItems)
        //for (int i = 0; i < visitItems.size(); i++)
        {
            GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
            String data_ora_sopralluogo = geaSopralluogo.getData_ora_sopralluogo();

            if (data_ora_sopralluogo == null)
            {
                continue;
            }

            int id_tecnico = visitItem.getGeaSopralluogo().getId_tecnico();
            boolean ownVisit = selectedTech.getId() == id_tecnico;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

            if (ownVisit)
            {
                try
                {
                    Date date = sdf.parse(data_ora_sopralluogo);
                    long time = date.getTime();
                    long timeToShowNotification = time + remindTimeDelays[curReminderPos];

                    if (timeToShowNotification > milliSecondsForNow)
                    {
                        NotificationEventReceiver.setupAlarm(activity.getApplicationContext(), timeToShowNotification);
                    }

                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
        }
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

            mSettings.edit().putBoolean("geaModelsIsObsolete", true).apply();
        }

        if(view.getId() == R.id.flLogout)
        {
            mCommunicator.onLogoutCommand();
        }

        if(view.getId() == R.id.btnTermsOfUseOk)
        {
            llSettings.setVisibility(View.VISIBLE);
            llTermsOfUse.setVisibility(View.GONE);
        }
    }
}
