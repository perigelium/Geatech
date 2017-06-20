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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.MyListVisitsAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.SwipeDetector;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class FragListVisitsToday extends ListFragment
{
    private Communicator mCommunicator;
    SwipeDetector swipeDetector;
    boolean ownVisitsOnly;
    ArrayList<VisitItem> visitItemsFiltered;
    List<ReportItem> reportItems;
    MyListVisitsAdapter myListAdapter;
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

        ownVisitsOnly = mSettings.getBoolean("ownVisitsOnly", false);

        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.list_visits_today, container, false);

        tvListVisitsTodayDate = (TextView) rootView.findViewById(R.id.tvListVisitsTodayDate);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        visitItemsFiltered = new ArrayList<>();
        realm.beginTransaction();
        RealmResults <ReportItem> rr_reportItems = realm.where(ReportItem.class).equalTo("company_id", company_id)
                .equalTo("tech_id", selectedTech.getId())
                .findAll();
        realm.commitTransaction();

        reportItems = new ArrayList<>();
        reportItems.addAll(rr_reportItems);

        TreeMap<Long, VisitItem> unsortedVisits = new TreeMap<>();

        Calendar calendarTodayLastMin = Calendar.getInstance(Locale.ITALY);
        String strMonth = calendarTodayLastMin.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ITALY);
        String dateString = " " + calendarTodayLastMin.get(Calendar.DAY_OF_MONTH) + " " + strMonth;

        tvListVisitsTodayDate.setText(dateString);

        calendarTodayLastMin.set(Calendar.HOUR_OF_DAY, 23);
        calendarTodayLastMin.set(Calendar.MINUTE, 59);
        calendarTodayLastMin.set(Calendar.SECOND, 59);

        Calendar calendarTodayFirstMin = Calendar.getInstance(Locale.ITALY);

        calendarTodayFirstMin.set(Calendar.HOUR_OF_DAY, 0);
        calendarTodayFirstMin.set(Calendar.MINUTE, 0);
        calendarTodayFirstMin.set(Calendar.SECOND, 0);

        long lastMilliSecondsOfToday = calendarTodayLastMin.getTimeInMillis();
        long firstMilliSecondsOfToday = calendarTodayFirstMin.getTimeInMillis();

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

            if (!ownVisitsOnly || (ownVisitsOnly && ownVisit))
            {
                try
                {
                    Date date = sdf.parse(data_ora_sopralluogo);
                    long time = date.getTime();

                    while (unsortedVisits.get(time) != null) // item with the same time already exists
                    {
                        time++;
                    }

                    if (time >= firstMilliSecondsOfToday && time <= lastMilliSecondsOfToday)
                    {
                        unsortedVisits.put(time, visitItem);
                    }

                } catch (ParseException e)
                {
                    e.printStackTrace();
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

        myListAdapter = new MyListVisitsAdapter(getActivity(), R.layout.list_visits_fragment_row, visitItemsFiltered, reportItems);
        setListAdapter(myListAdapter);

        lv = getListView();

        lv.setOnTouchListener(swipeDetector);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //int idSopralluogo = visitItemsFiltered.get(position).getGeaSopralluogo().getId_sopralluogo();

                int idVisit = visitItemsFiltered.get(position).getId();
/*                int id_tecnico = visitItemsFiltered.get(position).getGeaSopralluogo().getId_tecnico();
                int id_rapporto_sopralluogo = visitItemsFiltered.get(position).getGeaRapporto().getId_rapporto_sopralluogo();

                realm.beginTransaction();
                ReportItem reportItem = realm.where(ReportItem.class)
                        .equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                        .equalTo("id_sopralluogo", idSopralluogo).equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findFirst();
                realm.commitTransaction();

                boolean ownVisit = selectedTech.getId() == id_tecnico;*/

                mCommunicator.OnVisitListItemSelected(idVisit);

/*                    if (swipeDetector.swipeDetected())
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
                    }*/

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