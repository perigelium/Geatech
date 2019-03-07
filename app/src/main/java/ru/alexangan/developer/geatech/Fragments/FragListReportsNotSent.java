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
import ru.alexangan.developer.geatech.Adapters.ListVisitsAdapter;
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

public class FragListReportsNotSent extends ListFragment
{
    private Communicator mCommunicator;
    SwipeDetector swipeDetector;
    boolean timeNotSetItemsOnly;
    ArrayList<VisitItem> visitItemsFilteredNotSent;
    ListVisitsAdapter myListAdapterNotSent;
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
        View rootView = inflater.inflate(R.layout.list_reports_not_sent, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        visitItemsFilteredNotSent = new ArrayList<>();

        TreeMap<Long, VisitItem> unsortedVisitsNotSent = new TreeMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

        for (VisitItem visitItem : visitItems)
        {
            GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
            int idSopralluogo = geaSopralluogo.getId_sopralluogo();
            String data_ora_sopralluogo = geaSopralluogo.getData_ora_sopralluogo();

            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            ReportItem reportItem = realm.where(ReportItem.class)
                    .equalTo("company_id", company_id)
                    .equalTo("tech_id", selectedTech.getId())
                    .equalTo("id_sopralluogo", idSopralluogo).findFirst();
            realm.commitTransaction();

            boolean reportCompleteNotSent;

            if (reportItem != null)
            {
                int generalInfoCompletionState = reportItem.getReportStates().getGeneralInfoCompletionState();
                int reportCompletionState = reportItem.getReportStates().getReportCompletionState();
                int photosAddedNumber = reportItem.getReportStates().getPhotosAddedNumber();

                reportCompleteNotSent = generalInfoCompletionState == ReportStates.GENERAL_INFO_DATETIME_AND_COORDS_SET
                        && reportCompletionState == ReportStates.REPORT_COMPLETED
                        && photosAddedNumber >= ReportStates.PHOTOS_MIN_ADDED
                        && reportItem.getGea_rapporto_sopralluogo().getData_ora_invio_rapporto() == null;

                if (reportCompleteNotSent)
                {
                    try
                    {
                        Date date = sdf.parse(data_ora_sopralluogo);
                        long time = date.getTime();

                        while (unsortedVisitsNotSent.get(time) != null) // item with the same time already exists
                        {
                            time++;
                        }

                        unsortedVisitsNotSent.put(time, visitItem);

                    } catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            realm.close();
        }

        for (Map.Entry entry : unsortedVisitsNotSent.entrySet())
        {
            VisitItem visitItem = (VisitItem) entry.getValue();
            {
                visitItemsFilteredNotSent.add(visitItem);
            }
        }

        myListAdapterNotSent = new ListVisitsAdapter(getActivity(), R.layout.list_visits_fragment_row, visitItemsFilteredNotSent, null);
        setListAdapter(myListAdapterNotSent);

        lv = getListView();

        lv.setOnTouchListener(swipeDetector);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int idVisit = visitItemsFilteredNotSent.get(position).getId();

                mCommunicator.OnNotSentListItemSelected(idVisit);
            }
        });
    }
}

