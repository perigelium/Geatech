package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import ru.alexangan.developer.geatech.Utils.ViewUtils;

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
    TextView tvListVisitsTodayDate;
    private Realm realm;

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
        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.list_visits_no_title, container, false);

        //tvListVisitsTodayDate = (TextView) rootView.findViewById(R.id.tvListVisitsTodayDate);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        visitItemsFiltered = new ArrayList<>();

        TreeMap<Long, VisitItem> unsortedVisits = new TreeMap<>();
        long n = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);
        Calendar calendarNow = Calendar.getInstance(Locale.ITALY);
        String strMonth = calendarNow.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ITALY);
        //String dateString = " " + calendarNow.get(Calendar.DAY_OF_MONTH) + " " + strMonth;

        //tvListVisitsTodayDate.setText(dateString);

        calendarNow.set(Calendar.HOUR_OF_DAY, 23);
        calendarNow.set(Calendar.MINUTE, 59);
        calendarNow.set(Calendar.SECOND, 59);
        long lastMilliSecondsOfToday = calendarNow.getTimeInMillis();

        //for (VisitItem visitItem : visitItems)
        for (int i = 0; i < visitItems.size(); i++)
        {
            //String data_ora_sopralluogo = visitItem.getGeaSopralluogo().getData_ora_sopralluogo();
            VisitItem visitItem = visitItems.get(i);
            GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
            int id_sopralluogo = geaSopralluogo.getId_sopralluogo();

            realm.beginTransaction();
            ReportItem reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                    .equalTo("id_sopralluogo", id_sopralluogo).findFirst();
            realm.commitTransaction();

            boolean reportStartedNotCompleted = false;

            if (reportItem != null)
            {
                String data_ora_sopralluogo = reportItem.getGeaSopralluogo().getData_ora_sopralluogo();

                int generalInfoCompletionState = reportItem.getReportStates().getGeneralInfoCompletionState();
                int reportCompletionState = reportItem.getReportStates().getReportCompletionState();
                int photosAddedNumber = reportItem.getReportStates().getPhotosAddedNumber();

                reportStartedNotCompleted = generalInfoCompletionState == ReportStates.GENERAL_INFO_DATETIME_AND_COORDS_SET
                        && ((reportCompletionState >= ReportStates.REPORT_INITIATED && reportCompletionState < ReportStates.REPORT_COMPLETED
                        || (photosAddedNumber > 0 && photosAddedNumber < ReportStates.PHOTOS_MIN_ADDED)));

                if (data_ora_sopralluogo != null)
                {
                    try
                    {
                        Date date = sdf.parse(data_ora_sopralluogo);
                        long time = date.getTime();
                        //Log.d("DEBUG", String.valueOf(time));

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
/*                    while(unsortedVisits.get(n) != null)
                    {
                        n++;
                    }
                    unsortedVisits.put(n++, visitItem);*/
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

        InWorkListVisitsAdapter inWorkListVisitsAdapter = new InWorkListVisitsAdapter(getActivity(), R.layout.in_work_list_visits_fragment_row, visitItemsFiltered);
        setListAdapter(inWorkListVisitsAdapter);

        lv = getListView();

        ////ViewUtils.setListViewHeightBasedOnChildren(lv);

        lv.setOnTouchListener(swipeDetector);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int idSopralluogo = visitItemsFiltered.get(position).getGeaSopralluogo().getId_sopralluogo();
                int id_rapporto_sopralluogo = visitItemsFiltered.get(position).getGeaRapporto().getId_rapporto_sopralluogo();

                int idVisit = visitItemsFiltered.get(position).getId();
                int id_tecnico = visitItemsFiltered.get(position).getGeaSopralluogo().getId_tecnico();

                realm.beginTransaction();
                ReportItem reportItem = realm.where(ReportItem.class)
                        .equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                        .equalTo("id_sopralluogo", idSopralluogo)
                        .equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findFirst();
                realm.commitTransaction();

                boolean ownVisit = selectedTech.getId() == id_tecnico;
                boolean freeVisit = (id_tecnico == 0);

                if (ownVisit || freeVisit) //
                {
                    if (swipeDetector.swipeDetected())
                    {
                        if (swipeDetector.getAction() == SwipeDetector.Action.LR)
                        {
                            mCommunicator.OnVisitListItemSwiped(idVisit, ownVisit && reportItem != null);
                        } else if (swipeDetector.getAction() == SwipeDetector.Action.RL)
                        {
                            mCommunicator.OnVisitListItemSwiped(idVisit, false);
                        }
                    } else
                    {
                        mCommunicator.OnVisitListItemSelected(idVisit, ownVisit && reportItem != null);
                    }
                }
            }
        });
    }

/*    private void showToastMessage(final String msg)
    {
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }
        });
    }*/
}

/*            int optionId = randomInteger!=0 ? R.layout.list_visits_cell_datetime_set : R.layout.list_visits_cell_datetime_set;
            View C = inflater.inflate(optionId, parent, false);
            int index = parent.indexOfChild(C);
            parent.addView(C, index);*/