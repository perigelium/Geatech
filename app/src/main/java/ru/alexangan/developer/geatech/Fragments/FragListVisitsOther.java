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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import ru.alexangan.developer.geatech.Adapters.MyListVisitsAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.SwipeDetector;
import ru.alexangan.developer.geatech.Utils.ViewUtils;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class FragListVisitsOther extends ListFragment
{
    private Communicator mCommunicator;
    SwipeDetector swipeDetector;
    boolean ownVisitsOnly;
    ArrayList<VisitItem> visitItemsFiltered;
    MyListVisitsAdapter myListAdapter;
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

        ownVisitsOnly = mSettings.getBoolean("ownVisitsOnly", false);

/*        if (getArguments() != null)
        {
            ownVisitsOnly = getArguments().getBoolean("ownVisitsOnly", false);
        }*/


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.list_visits_other, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        visitItemsFiltered = new ArrayList<>();

        TreeMap<Long, VisitItem> unsortedVisits = new TreeMap<>();

        visitItemsFiltered = new ArrayList<>();
        Calendar calendarNow = Calendar.getInstance(Locale.ITALY);
        calendarNow.set(Calendar.HOUR, 23);
        calendarNow.set(Calendar.MINUTE, 59);
        calendarNow.set(Calendar.SECOND, 59);
        long lastMilliSecondsOfToday = calendarNow.getTimeInMillis();

        long n = 0;

        for (VisitItem visitItem : visitItems)
        //for (int i = 0; i < visitItems.size(); i++)
        {
            String data_ora_sopralluogo = visitItem.getGeaSopralluogo().getData_ora_sopralluogo();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

            try
            {
                Date date = sdf.parse(data_ora_sopralluogo);
                long time = date.getTime();
                //Log.d("DEBUG", String.valueOf(time));

                while(unsortedVisits.get(time) != null)
                {
                    time++;
                }

                if(time > lastMilliSecondsOfToday)
                {
                    unsortedVisits.put(time, visitItem);
                }

            } catch (ParseException e)
            {
                while(unsortedVisits.get(n) != null)
                {
                    n++;
                }
                unsortedVisits.put(n++, visitItem);
                e.printStackTrace();
            }
        }

        for (Map.Entry entry : unsortedVisits.entrySet()) // add other visits
        {
            VisitItem visitItem = (VisitItem) entry.getValue();
            int id_tecnico = visitItem.getGeaSopralluogo().getId_tecnico();
            boolean ownReport = selectedTech.getId() == id_tecnico;

            if(id_tecnico != 0 && (!ownVisitsOnly || ownReport))
            {
                visitItemsFiltered.add(visitItem);
            }
        }

        myListAdapter = new MyListVisitsAdapter(getActivity(), R.layout.list_visits_fragment_row, visitItemsFiltered);
        setListAdapter(myListAdapter);

        lv = getListView();

        ViewUtils.setListViewHeightBasedOnChildren(lv);

        lv.setOnTouchListener(swipeDetector);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int idSopralluogo = visitItemsFiltered.get(position).getGeaSopralluogo().getId_sopralluogo();

                int idVisit = visitItemsFiltered.get(position).getId();

                int id_tecnico = visitItemsFiltered.get(position).getGeaSopralluogo().getId_tecnico();

                realm.beginTransaction();
                ReportStates reportStates = realm.where(ReportStates.class)
                        .equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                        .equalTo("id_sopralluogo", idSopralluogo).findFirst();
                realm.commitTransaction();

                boolean ownVisit = selectedTech.getId() == id_tecnico;
                boolean freeVisit = id_tecnico == 0;

                if (ownVisit || freeVisit) //
                {
                    if (swipeDetector.swipeDetected())
                    {
                        if (swipeDetector.getAction() == SwipeDetector.Action.LR)
                        {
                            mCommunicator.OnVisitListItemSwiped(idVisit, ownVisit && reportStates!=null);
                        } else if (swipeDetector.getAction() == SwipeDetector.Action.RL)
                        {
                            mCommunicator.OnVisitListItemSwiped(idVisit, false);
                        }
                    } else
                    {
                        mCommunicator.OnVisitListItemSelected(idVisit, ownVisit && reportStates!=null);
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