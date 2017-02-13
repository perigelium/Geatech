package ru.alexangan.developer.geatech.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.ComingListVisitsAdapter;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class ComingListVisitsFragment extends ListFragment
{
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Context context = getActivity();

        ArrayList<VisitItem> visitItemsDateTimeSet = new ArrayList<>();

        realm.beginTransaction();
        RealmResults <ReportStates> reportStatesList = realm.where(ReportStates.class).findAll();
        realm.commitTransaction();


        for (VisitItem visitItem : visitItems)
        {
            for(ReportStates reportStates : reportStatesList)
            {
                if (visitItem.getVisitStates().getIdSopralluogo() == reportStates.getIdSopralluogo()
                        && reportStates.getDataOraSopralluogo()!=null)
                {
                    visitItemsDateTimeSet.add(visitItem);
                    break;
                }
            }
        }

        ComingListVisitsAdapter myListAdapter =
                new ComingListVisitsAdapter(getActivity(), R.layout.coming_list_visits_fragment_row, visitItemsDateTimeSet);

        setListAdapter(myListAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.list_visits_fragment, container, false);
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