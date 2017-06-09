package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.alexangan.developer.geatech.Adapters.CustomSpinnerAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.GlobalConstants;
import ru.alexangan.developer.geatech.Models.SpinnerItemData;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.LIST_VISITS_MODE_ALL;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;

/**
 * Created by user on 11/10/2016
 */

public class NotificationBarFragment extends Fragment implements View.OnClickListener
{
    private Communicator mCommunicator;
    private Activity activity;
    AlertDialog alert;
    Spinner spVisitsFilter;
    private TextView tvWindowTitle;
    private ImageView ivLogoSmall;
    private ImageView ivVisitsListsFilter;
    int spinnerCurItem;


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mCommunicator = (Communicator) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.notification_bar, container, false);

        String[] listItemsArray = {"Tutti i sopralluoghi", "I miei sopralluoghi", "Sopralluoghi da fissare"};
        Integer[] icons = new Integer[]{R.drawable.transparent21px, R.drawable.transparent21px, R.drawable.transparent21px};

        tvWindowTitle = (TextView) rootView.findViewById(R.id.tvWindowTitle);
        ivLogoSmall = (ImageView) rootView.findViewById(R.id.ivLogoSmall);
        ivVisitsListsFilter = (ImageView) rootView.findViewById(R.id.ivVisitsListsFilter);

        spVisitsFilter = (Spinner) rootView.findViewById(R.id.spVisitsFilter);

        ArrayList<SpinnerItemData> list = new ArrayList<>();

        for (int i = 0; i < listItemsArray.length; i++)
        {
            list.add(new SpinnerItemData(listItemsArray[i], icons[i]));
        }
        SpinnerAdapter adapter =
                new CustomSpinnerAdapter(activity, R.layout.alert_visits_filter_item_custom, R.id.tvVisitsFilterDialogItem, list);
        spVisitsFilter.setAdapter(adapter);

        //CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(activity, R.layout.alert_visits_filter_item_custom, map);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        spVisitsFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                if (spinnerCurItem == position)
                {
                    return;
                }
                mSettings.edit().putInt("listVisitsFilterMode", position).apply();
                //GlobalConstants.listVisitsFilterMode = position;
                mCommunicator.onNotificationReportReturned(position);

                spinnerCurItem = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
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

        activity = getActivity();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onClick(View view)
    {
/*        if(view.getId() == R.id.btnVisitsListFilter)
        {
            showVisitsFilterDialog();
        }*/

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

/*    private void showVisitsFilterDialog()
    {
        String[] listItemsArray = {"Tutti i sopralluoghi", "I miei sopralluoghi", "Sopralluoghi da fissare"};
        Integer[] icons = new Integer[]{R.drawable.three_balls, R.drawable.two_balls, R.drawable.yellow_ball};

        //ContextThemeWrapper themedContext = new ContextThemeWrapper
        // (this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.alert_dialog_custom, null);

        ListView listView = (ListView) layout.findViewById(R.id.alertList);
        //ArrayAdapter<String> listAdapter = new ArrayAdapter<>(activity, R.layout.alert_visits_filter_item_custom, R.id.tvVisitsFilterDialogItem, listItemsArray);
        //VisitsListFilterAdapter visitsListFilterAdapter = new VisitsListFilterAdapter(activity, alertDialogResources);
        ListAdapter adapter = new ArrayAdapterWithIcons(getActivity(), listItemsArray, icons);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int which, long id)
            {

                mCommunicator.onNotificationReportReturned(which);
            }
        });

        builder.setView(layout);
        alert = builder.create();
        alert.show();
    }*/

    public void setView(int tvWindowTitleString, int ivLogoSmallVisibility, int ivVisitsListsFilterVisibility)
    {
        tvWindowTitle.setText(tvWindowTitleString);
        ivLogoSmall.setVisibility(ivLogoSmallVisibility);
        ivVisitsListsFilter.setVisibility(ivVisitsListsFilterVisibility);
    }
}
