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

import ru.alexangan.developer.geatech.Adapters.MyListVisitsAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.SwipeDetector;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class ListVisitsFragmentOld extends ListFragment
{
    private Communicator mCommunicator;
    SwipeDetector swipeDetector;
    boolean timeNotSetItemsOnly;
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

        timeNotSetItemsOnly = false;

        if (getArguments() != null)
        {
            timeNotSetItemsOnly = getArguments().getBoolean("timeNotSetItemsOnly", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.list_visits_fragment, container, false);

        visitItemsFiltered = new ArrayList<>();

        TreeMap<Long, VisitItem> unsortedVisits = new TreeMap<>();
        long n = 0;

        for (VisitItem visitItem : visitItems)
        //for (int i = 0; i < visitItems.size(); i++)
        {
            String data_ora_sopralluogo = visitItem.getGeaSopralluogo().getData_ora_sopralluogo();
            int id_tecnico = visitItem.getGeaSopralluogo().getId_tecnico();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

            if ((!timeNotSetItemsOnly) || (timeNotSetItemsOnly && id_tecnico == 0))
            {
                try
                {
                    Date date = sdf.parse(data_ora_sopralluogo);
                    long time = date.getTime();
                    //Log.d("DEBUG", String.valueOf(time));

                    while(unsortedVisits.get(time) != null)
                    {
                        time++;
                    }
                    unsortedVisits.put(time, visitItem);

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
        }

        for (Map.Entry entry : unsortedVisits.entrySet()) // add own visits first
        {
                VisitItem visitItem = (VisitItem) entry.getValue();
                int id_tecnico = visitItem.getGeaSopralluogo().getId_tecnico();
                int id_sopralluogo = visitItem.getGeaSopralluogo().getId_sopralluogo();
                boolean ownReport = selectedTech.getId() == id_tecnico;

            if(ownReport)
            {
/*                realm.beginTransaction();
                ReportStates reportStates = realm.where(ReportStates.class)
                        .equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                        .equalTo("id_sopralluogo", id_sopralluogo).findFirst();
                realm.commitTransaction();*/

/*                if (reportStates != null)
                {
                    realm.beginTransaction();
                    if(reportStates.getId_rapporto_sopralluogo() != 0)
                    {*/
                        visitItemsFiltered.add(visitItem);
/*                    }
                    realm.commitTransaction();
                }*/
            }
        }

        for (Map.Entry entry : unsortedVisits.entrySet()) // add free visits
        {
            VisitItem visitItem = (VisitItem) entry.getValue();
            int id_tecnico = visitItem.getGeaSopralluogo().getId_tecnico();
            boolean ownReport = selectedTech.getId() == id_tecnico;

            if(!ownReport && id_tecnico == 0)
            {
                visitItemsFiltered.add(visitItem);
            }
        }

        for (Map.Entry entry : unsortedVisits.entrySet()) // add other visits
        {
            VisitItem visitItem = (VisitItem) entry.getValue();
            int id_tecnico = visitItem.getGeaSopralluogo().getId_tecnico();
            boolean ownReport = selectedTech.getId() == id_tecnico;

            if(!ownReport && id_tecnico != 0)
            {
                visitItemsFiltered.add(visitItem);
            }
        }

        myListAdapter = new MyListVisitsAdapter(getActivity(), R.layout.list_visits_fragment_row, visitItemsFiltered);
        setListAdapter(myListAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        lv = getListView();
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
                            mCommunicator.OnListItemSwiped(idVisit, ownVisit && reportStates!=null);
                        } else if (swipeDetector.getAction() == SwipeDetector.Action.RL)
                        {
                            mCommunicator.OnListItemSwiped(idVisit, false);
                        }
                    } else
                    {
                        mCommunicator.OnListItemSelected(idVisit, ownVisit && reportStates!=null);
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