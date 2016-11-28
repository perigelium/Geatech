package com.example.zubcu.geatech.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zubcu.geatech.Adapters.MyListVisitsAdapter;
import com.example.zubcu.geatech.R;
import com.example.zubcu.geatech.Services.GeneralInfoReceiver;

import java.util.ArrayList;

public class FragmentListVisits extends ListFragment
{
    OnItemSelectedListener listener;
    GeneralInfoReceiver generalInfoReceiver;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        generalInfoReceiver = GeneralInfoReceiver.getInstance();

        MyListVisitsAdapter myListAdapter =
                new MyListVisitsAdapter(getActivity(), R.layout.list_visits_fragment_row, generalInfoReceiver.getListVisitsArrayList());
        setListAdapter(myListAdapter);

        Context context = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        listener = (OnItemSelectedListener) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.list_visits_fragment, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        listener.OnListItemSelected(position, !generalInfoReceiver.getListVisitsArrayList().get(position).getVisitDay().isEmpty());

/*        Toast.makeText(getActivity(),
                getListView().getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();*/
    }

    public interface OnItemSelectedListener
    {
        void OnListItemSelected(int itemIndex, Boolean dateTimeHasSet);
    }
}

/*            int optionId = randomInteger!=0 ? R.layout.list_visits_cell_datetime_set : R.layout.list_visits_cell_datetime_set;
            View C = inflater.inflate(optionId, parent, false);
            int index = parent.indexOfChild(C);
            parent.addView(C, index);*/