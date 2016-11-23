package com.example.zubcu.geatech.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zubcu.geatech.Adapters.InWorkListVisitsAdapter;
import com.example.zubcu.geatech.Adapters.MyListVisitsAdapter;
import com.example.zubcu.geatech.Adapters.NotSentListVisitsAdapter;
import com.example.zubcu.geatech.Models.VisitsListRowModel;
import com.example.zubcu.geatech.R;

import java.util.ArrayList;

public class NotSentListVisitsFragment extends ListFragment
{
    //OnItemSelectedListener listener;

    final String[] clientNames = new String[]{"Рыжик", "Барсик", "Мурзик",
            "Мурка", "Васька", "Томасина", "Пушок", "Дымка",
            "Кузя", "Китти", "Масяня", "Симба"};

    final String[] serviceNames = new String[]{"Termodinamico", "Fotovoltaico", "Service_4",
            "Service_5", "Service_6", "Service_7", "Service_8", "Service_9", "Service_10",
            "Service_11", "Service_12", "Service_13"};

    final String[] clientAddresses = new String[]{"Indirizzo_1", "Indirizzo_2", "Indirizzo_3",
            "Indirizzo_4", "Indirizzo_5", "Indirizzo_6", "Indirizzo_7", "Indirizzo_8", "Indirizzo_9",
            "Indirizzo_10", "Indirizzo_11", "Indirizzo_12"};


    private static final ArrayList<Pair<String, String>> visitsDate;
    static
    {
        visitsDate = new ArrayList<Pair<String, String>>();
        visitsDate.add(Pair.create( "1", "jan"));
        visitsDate.add(Pair.create("2", "feb"));
        visitsDate.add(Pair.create("3", "mar"));
        visitsDate.add(Pair.create("", ""));
        visitsDate.add(Pair.create("5", "mag"));
        visitsDate.add(Pair.create("6", "gun"));
        visitsDate.add(Pair.create("7", "lug"));
        visitsDate.add(Pair.create("8", "ago"));
        visitsDate.add(Pair.create("", ""));
        visitsDate.add(Pair.create("10", "oto"));
        visitsDate.add(Pair.create("11", "nov"));
        visitsDate.add(Pair.create("12", "dic"));
    }

    ArrayList<VisitsListRowModel> listVisitsArrayList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Context context = getActivity();
        listVisitsArrayList = new ArrayList<>();

        for (int i = 0; i < clientNames.length; i++)
        {
            listVisitsArrayList.add(
                    new VisitsListRowModel(clientNames[i], serviceNames[i], clientAddresses[i], visitsDate.get(i)));
        }

        NotSentListVisitsAdapter myListAdapter =
                new NotSentListVisitsAdapter(getActivity(), R.layout.not_sent_list_visits_fragment_row, listVisitsArrayList);
        setListAdapter(myListAdapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

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