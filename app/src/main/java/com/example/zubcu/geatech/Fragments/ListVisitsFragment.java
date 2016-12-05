package com.example.zubcu.geatech.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zubcu.geatech.Activities.MainActivity;
import com.example.zubcu.geatech.Adapters.MyListVisitsAdapter;
import com.example.zubcu.geatech.Interfaces.Communicator;
import com.example.zubcu.geatech.Models.VisitItem;
import com.example.zubcu.geatech.R;
import com.example.zubcu.geatech.Managers.GeneralInfoReceiver;
import com.example.zubcu.geatech.Utils.SwipeDetector;

public class ListVisitsFragment extends ListFragment
{
    GeneralInfoReceiver generalInfoReceiver;
    private Communicator mCommunicator;
    SwipeDetector swipeDetector;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        generalInfoReceiver = GeneralInfoReceiver.getInstance();

        MyListVisitsAdapter myListAdapter =
                new MyListVisitsAdapter(getActivity(), R.layout.list_visits_fragment_row, MainActivity.visitItems);
        setListAdapter(myListAdapter);

        Context context = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mCommunicator = (Communicator)getActivity();
        swipeDetector = new SwipeDetector();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView =  inflater.inflate(R.layout.list_visits_fragment, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        final ListView lv = getListView();
        final SwipeDetector swipeDetector = new SwipeDetector();
        lv.setOnTouchListener(swipeDetector);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (swipeDetector.swipeDetected())
                {
                    if(swipeDetector.getAction() == SwipeDetector.Action.LR)
                    {
                        mCommunicator.OnListItemSwiped(position, generalInfoReceiver.getListVisitsArrayList().get(position).getVisitDay() != 0);
                    }
                } else
                {
                    mCommunicator.OnListItemSelected(position, generalInfoReceiver.getListVisitsArrayList().get(position).getVisitDay() != 0);
                }
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id)
            {
                if (swipeDetector.swipeDetected())
                {
                    // do the onSwipe action
                } else
                {
                    if(generalInfoReceiver.getListVisitsArrayList().get(position).getVisitDay() != 0)
                    {
                        mCommunicator.OnListItemSelected(position, false);
                    }
                }

                return false;
            }
        });
    }
}

/*        Toast.makeText(getActivity(),
                getListView().getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();*//*
    }*/

/*            int optionId = randomInteger!=0 ? R.layout.list_visits_cell_datetime_set : R.layout.list_visits_cell_datetime_set;
            View C = inflater.inflate(optionId, parent, false);
            int index = parent.indexOfChild(C);
            parent.addView(C, index);*/