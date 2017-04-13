package ru.alexangan.developer.geatech.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;

/**
 * Created by user on 11/10/2016*/

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

/*        TextView tvTechName = (TextView) rootView.findViewById(R.id.tvTechName);
        tvTechName.setText(selectedTech.getFullNameTehnic());

        Button btnNotifTimeNotSetVisits = (Button) rootView.findViewById(R.id.btnNotifTimeNotSetVisits);
        btnNotifTimeNotSetVisits.setOnClickListener(this);

        Button btnNotifUrgentReports = (Button) rootView.findViewById(R.id.btnNotifUrgentReports);
        btnNotifUrgentReports.setOnClickListener(this);

        Button btnAppSettings = (Button) rootView.findViewById(R.id.btnAppSettings);
        btnAppSettings.setOnClickListener(this);*/

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
        mCommunicator.onNotificationReportReturned(view);

/*        if(view.getId() == R.id.btnNotifTimeNotSetVisits)
        {
            mCommunicator.onNotificationReportReturned(view);
        }

        if(view.getId() == R.id.btnNotifUrgentReports)
        {
            mCommunicator.onNotificationReportReturned(view);
        }

        if(view.getId() == R.id.btnAppSettings)
        {
            mCommunicator.onNotificationReportReturned(view);
        }*/
    }
}
