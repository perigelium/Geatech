package ru.alexangan.developer.geatech.Fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.ReportsListAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.SwipeDetector;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;

public class ReportsListFragment extends ListFragment
{
    private Communicator mCommunicator;
    ArrayList<Integer> reportStatesItemsPositions;
    SwipeDetector swipeDetector;
    ListView lv;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        lv = getListView();
        lv.setOnTouchListener(swipeDetector);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mCommunicator = (Communicator) getActivity();
        swipeDetector = new SwipeDetector();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.list_visits_fragment, container, false);

        ArrayList<ReportStates> reportStatesItems = new ArrayList<>();
        reportStatesItemsPositions = new ArrayList<>();

        realm.beginTransaction();

        RealmResults<ReportStates> reportStatesList = realm.where(ReportStates.class).equalTo("company_id", company_id)
                .equalTo("tech_id", selectedTech.getId()).findAll();

        realm.commitTransaction();


        TreeMap<Long, ReportStates> unsortedReports = new TreeMap<>();


        for (ReportStates reportStates : reportStatesList)
        {
            //int photoAddedStatus = reportStates.getPhotoAddedNumber();
            //int generalInfoCompletionState = reportStates.getGeneralInfoCompletionState();
            //int reportCompletionState = reportStates.getReportCompletionState();
            String data_ora_sopralluogo = reportStates.getData_ora_sopralluogo();

            if (data_ora_sopralluogo == null)
            {
                continue;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

            if (reportStates.getData_ora_invio_rapporto() != null)
            {
                try
                {
                    Date date = sdf.parse(reportStates.getData_ora_invio_rapporto());
                    long time = date.getTime();
                    ////Log.d("DEBUG", String.valueOf(time));
                    unsortedReports.put(time, reportStates);
                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
        }

        for (Map.Entry entry : unsortedReports.entrySet())
        {
            ReportStates reportStates = (ReportStates) entry.getValue();
            reportStatesItems.add(reportStates);

            reportStatesItemsPositions.add(reportStates.getId_rapporto_sopralluogo());
        }

        ReportsListAdapter myListAdapter =
                new ReportsListAdapter(getActivity(), R.layout.reports_list_fragment_row, reportStatesItems);
        setListAdapter(myListAdapter);

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        if (swipeDetector.swipeDetected())
        {
            if (swipeDetector.getAction() == SwipeDetector.Action.LR)
            {
                mCommunicator.OnReportListItemSelected(reportStatesItemsPositions.get(position));
            }
        } else
        {
            mCommunicator.OnReportListItemSelected(reportStatesItemsPositions.get(position));
        }
    }
}

/*            int optionId = randomInteger!=0 ? R.layout.list_visits_cell_datetime_set : R.layout.list_visits_cell_datetime_set;
            View C = inflater.inflate(optionId, parent, false);
            int index = parent.indexOfChild(C);
            parent.addView(C, index);*/