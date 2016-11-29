package com.example.zubcu.geatech.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.zubcu.geatech.Adapters.ReportsListAdapter;
import com.example.zubcu.geatech.Interfaces.Communicator;
import com.example.zubcu.geatech.R;
import com.example.zubcu.geatech.Services.GeneralInfoReceiver;

public class ReportsListFragment extends ListFragment
{
    //OnItemSelectedListener listener;
    private Communicator mCommunicator;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Context context = getActivity();

        GeneralInfoReceiver generalInfoReceiver = GeneralInfoReceiver.getInstance();

        ReportsListAdapter myListAdapter =
                new ReportsListAdapter(getActivity(), R.layout.reports_list_fragment_row, generalInfoReceiver.getListVisitsArrayList());
        setListAdapter(myListAdapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //listener = (OnItemSelectedListener) getActivity();

        mCommunicator = (Communicator)getActivity();

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

        mCommunicator.OnListItemSelected(position);

/*       Toast.makeText(getActivity(),
                getListView().getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();*/
    }

/*    public interface OnItemSelectedListener
    {
        void OnListItemSelected(int itemIndex, boolean b);
    }*/
}

/*            int optionId = randomInteger!=0 ? R.layout.list_visits_cell_datetime_set : R.layout.list_visits_cell_datetime_set;
            View C = inflater.inflate(optionId, parent, false);
            int index = parent.indexOfChild(C);
            parent.addView(C, index);*/