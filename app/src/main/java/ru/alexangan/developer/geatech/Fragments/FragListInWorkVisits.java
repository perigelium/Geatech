package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import io.realm.Realm;
import ru.alexangan.developer.geatech.Adapters.InWorkListVisitsAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.SwipeDetector;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class FragListInWorkVisits extends ListFragment
{
    private Communicator mCommunicator;
    SwipeDetector swipeDetector;
    boolean timeNotSetItemsOnly;
    ArrayList<VisitItem> visitItemsFiltered;
    ListView lv;
    Activity activity;
    

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mCommunicator = (Communicator) getActivity();
        swipeDetector = new SwipeDetector();

        timeNotSetItemsOnly = false;

        if (getArguments() != null)
        {
            timeNotSetItemsOnly = getArguments().getBoolean("ownVisitsOnly", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.list_visits_no_title, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        visitItemsFiltered = new ArrayList<>();

        TreeMap<Long, VisitItem> unsortedVisits = new TreeMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

        for (int i = 0; i < visitItems.size(); i++)
        {
            VisitItem visitItem = visitItems.get(i);
            GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
            int id_sopralluogo = geaSopralluogo.getId_sopralluogo();

            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            ReportItem reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                    .equalTo("id_sopralluogo", id_sopralluogo).findFirst();
            realm.commitTransaction();
            realm.close();

            if (reportItem != null)
            {
                String data_ora_sopralluogo = reportItem.getGeaSopralluogo().getData_ora_sopralluogo();

                int generalInfoCompletionState = reportItem.getReportStates().getGeneralInfoCompletionState();
                int reportCompletionState = reportItem.getReportStates().getReportCompletionState();
                int photosAddedNumber = reportItem.getReportStates().getPhotosAddedNumber();

                boolean reportStartedNotCompleted =
                        (reportCompletionState > ReportStates.REPORT_NON_INITIATED || photosAddedNumber != 0)
                                &&
                                (! (generalInfoCompletionState == ReportStates.GENERAL_INFO_DATETIME_AND_COORDS_SET
                                        && reportCompletionState == ReportStates.REPORT_COMPLETED
                                        && photosAddedNumber >= ReportStates.PHOTOS_MIN_ADDED));

                if (data_ora_sopralluogo != null)
                {
                    try
                    {
                        Date date = sdf.parse(data_ora_sopralluogo);
                        long time = date.getTime();

                        while (unsortedVisits.get(time) != null) // item with the same time already exists
                        {
                            time++;
                        }

                        if (reportStartedNotCompleted)
                        {
                            unsortedVisits.put(time, visitItem);
                        }

                    } catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        for (Map.Entry entry : unsortedVisits.entrySet())
        {
            VisitItem visitItem = (VisitItem) entry.getValue();
            {
                visitItemsFiltered.add(visitItem);
            }
        }

        InWorkListVisitsAdapter inWorkListVisitsAdapter = new InWorkListVisitsAdapter(getActivity(), R.layout.in_work_list_visits_row, visitItemsFiltered);
        setListAdapter(inWorkListVisitsAdapter);

        lv = getListView();

        lv.setOnTouchListener(swipeDetector);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                int idVisit = visitItemsFiltered.get(position).getId();
                mCommunicator.OnVisitListItemSelected(idVisit);
            }
        });
    }
}
