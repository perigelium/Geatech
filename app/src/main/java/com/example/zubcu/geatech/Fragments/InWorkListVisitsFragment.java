package com.example.zubcu.geatech.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zubcu.geatech.Adapters.InWorkListVisitsAdapter;
import com.example.zubcu.geatech.Interfaces.Communicator;
import com.example.zubcu.geatech.Models.GeneralInfoModel;
import com.example.zubcu.geatech.R;
import com.example.zubcu.geatech.Managers.GeneralInfoReceiver;

import java.util.ArrayList;

public class InWorkListVisitsFragment extends ListFragment
{
    GeneralInfoReceiver generalInfoReceiver;
    //private Communicator mCommunicator;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        Context context = getActivity();

        generalInfoReceiver = GeneralInfoReceiver.getInstance();
        ArrayList<GeneralInfoModel> visitsSetArrayList = new ArrayList<>();

        for (GeneralInfoModel item : generalInfoReceiver.listVisitsArrayList)
        {
            if(item.getVisitDay() != 0)
            {
                visitsSetArrayList.add(item);
            }
        }

        InWorkListVisitsAdapter myListAdapter =
                new InWorkListVisitsAdapter(getActivity(), R.layout.in_work_list_visits_fragment_row, visitsSetArrayList);
        setListAdapter(myListAdapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //mCommunicator = (Communicator)getActivity();

        //listener = (OnItemSelectedListener) getActivity();

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

/*    public interface OnItemSelectedListener
    {
        void OnListItemSelected(int itemIndex, Boolean dateTimeHasSet);
    }*/
}

/*            int optionId = randomInteger!=0 ? R.layout.list_visits_cell_datetime_set : R.layout.list_visits_cell_datetime_set;
            View C = inflater.inflate(optionId, parent, false);
            int index = parent.indexOfChild(C);
            parent.addView(C, index);*/