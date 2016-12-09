package com.example.zubcu.geatech.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.zubcu.geatech.Interfaces.Communicator;
import com.example.zubcu.geatech.R;

/**
 * Created by user on 11/10/2016.
 */

public class NotificationBarFragment extends Fragment implements View.OnClickListener
{
    private Communicator mCommunicator;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mCommunicator = (Communicator)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.notification_bar, container, false);

        Button btnNotifTimeNotSetVisits = (Button) rootView.findViewById(R.id.btnNotifTimeNotSetVisits);
        btnNotifTimeNotSetVisits.setOnClickListener(this);

        Button btnNotifUrgentReports = (Button) rootView.findViewById(R.id.btnNotifUrgentReports);
        btnNotifUrgentReports.setOnClickListener(this);

        Button btnAppSettings = (Button) rootView.findViewById(R.id.btnAppSettings);
        btnAppSettings.setOnClickListener(this);

        return rootView;
    }

    @Override
    public View getView()
    {
        return super.getView();
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.btnNotifTimeNotSetVisits)
        {
            Toast.makeText(getActivity(),"btnNotifTimeNotSetVisits clicked", Toast.LENGTH_LONG).show();
            mCommunicator.onNotificationReportReturned(view);
        }

        if(view.getId() == R.id.btnNotifUrgentReports)
        {
            Toast.makeText(getActivity(),"btnNotifUrgentReports clicked", Toast.LENGTH_LONG).show();
            mCommunicator.onNotificationReportReturned(view);
        }

        if(view.getId() == R.id.btnAppSettings)
        {
            Toast.makeText(getActivity(),"btnAppSettings clicked", Toast.LENGTH_LONG).show();
            mCommunicator.onNotificationReportReturned(view);
        }
    }
}
