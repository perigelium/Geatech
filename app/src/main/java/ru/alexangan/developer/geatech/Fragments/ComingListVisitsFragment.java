package ru.alexangan.developer.geatech.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.ComingListVisitsAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class ComingListVisitsFragment extends ListFragment
{
    private Communicator mCommunicator;
    ArrayList<Integer> visitItemsPositions;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Context context = getActivity();


/*        realm.beginTransaction();
        RealmResults <ReportStates> reportStatesList = realm.where(ReportStates.class).equalTo("company_id", company_id)
                .equalTo("tech_id", selectedTech.getId()).findAll();
        realm.commitTransaction();

        for (VisitItem visitItem : visitItems)
        {
            for(ReportStates reportStates : reportStatesList)
            {
                if (visitItem.getGeaSopralluogo().getId_sopralluogo() == reportStates.getId_sopralluogo()
                        && reportStates.getData_ora_sopralluogo()!=null)
                {
                    visitItemsDateTimeSet.add(visitItem);
                    break;
                }
            }
        }*/


    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mCommunicator = (Communicator)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView =  inflater.inflate(R.layout.list_visits_fragment, container, false);

        ArrayList<VisitItem> visitItemsDateTimeSet = new ArrayList<>();
        visitItemsPositions = new ArrayList<>();


        realm.beginTransaction();

        RealmResults <ReportStates> reportStatesResults = realm.where(ReportStates.class)
                .equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId()).findAll();

        realm.commitTransaction();

        TreeMap <Long, VisitItem> unsortedVisits = new TreeMap<>();

        for (VisitItem visitItem : visitItems)
        {

            for(ReportStates reportStates : reportStatesResults)
            {
                if (visitItem.getGeaSopralluogo().getId_sopralluogo() == reportStates.getId_sopralluogo())
                {
                    //visitItemsDateTimeSet.add(visitItem);

                    String data_ora_sopralluogo = visitItem.getGeaSopralluogo().getData_ora_sopralluogo();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

                    try
                    {
                        Date date = sdf.parse(data_ora_sopralluogo);
                        long time = date.getTime();
                        Log.d("DEBUG", String.valueOf(time));
                        unsortedVisits.put(time, visitItem);
                    } catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        for (Map.Entry entry : unsortedVisits.entrySet())
        {
            VisitItem visitItem = (VisitItem)entry.getValue();
            visitItemsDateTimeSet.add(visitItem);

            visitItemsPositions.add(visitItem.getId());
        }


        ComingListVisitsAdapter myListAdapter =
                new ComingListVisitsAdapter(getActivity(), R.layout.coming_list_visits_fragment_row, visitItemsDateTimeSet);

        setListAdapter(myListAdapter);


        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        mCommunicator.OnComingListItemSelected(visitItemsPositions.get(position));
    }

/*    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        //listener.OnListItemSelected(position, !listVisitsArrayList.get(position).getVISIT_DAY().isEmpty());

*//*        Toast.makeText(getActivity(),
                getListView().getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();*//*
    }*/
}

/*            int optionId = randomInteger!=0 ? R.layout.list_visits_cell_datetime_set : R.layout.list_visits_cell_datetime_set;
            View C = inflater.inflate(optionId, parent, false);
            int index = parent.indexOfChild(C);
            parent.addView(C, index);*/