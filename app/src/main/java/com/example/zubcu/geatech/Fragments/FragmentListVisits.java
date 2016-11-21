package com.example.zubcu.geatech.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zubcu.geatech.Models.VisitsListRowModel;
import com.example.zubcu.geatech.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FragmentListVisits extends ListFragment
{
    OnItemSelectedListener listener;

    final String[] catNames = new String[]{"Рыжик", "Барсик", "Мурзик",
            "Мурка", "Васька", "Томасина", "Пушок", "Дымка",
            "Кузя", "Китти", "Масяня", "Симба"};

    final String[] serviceName = new String[]{"Termodinamico", "Fotovoltaico", "Service_4",
            "Service_5", "Service_6", "Service_7", "Service_8", "Service_9", "Service_10",
            "Service_11", "Service_12", "Service_13"};

    final String[] clientAddress = new String[]{"Indirizzo_1", "Indirizzo_2", "Indirizzo_3",
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

    Random rnd;
    ArrayList<VisitsListRowModel> listVisitsArrayList;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listVisitsArrayList = new ArrayList<>();

        for (int i = 0; i < catNames.length; i++)
        {
            listVisitsArrayList.add(new VisitsListRowModel(catNames[i], serviceName[i], clientAddress[i], visitsDate.get(i)));
        }

        MyListAdapter myListAdapter =
                new MyListAdapter(getActivity(), R.layout.list_visits_fragment_row, listVisitsArrayList);
        setListAdapter(myListAdapter);

        Context context = getActivity();

/*        ArrayList<String> listVisitsArrayList = new ArrayList<>();
        Collections.addAll(listVisitsArrayList, catNames);*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        rnd = new Random();

        listener = (OnItemSelectedListener) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.list_visits_fragment, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        listener.OnListItemSelected(position);

/*        Toast.makeText(getActivity(),
                getListView().getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();*/
    }

    public interface OnItemSelectedListener
    {
        void OnListItemSelected(int itemIndex);
    }


    public class MyListAdapter extends BaseAdapter
    {
        private Context mContext;
        ArrayList<VisitsListRowModel> visitsList;

        public MyListAdapter(Context context, int list_visits_fragment_row, ArrayList<VisitsListRowModel> objects)
        {
            //super(context, textViewResourceId, objects);
            mContext = context;
            visitsList = objects;
        }

        @Override
        public int getCount()
        {
            return visitsList.size();
        }

        @Override
        public Object getItem(int i)
        {
            return i;
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // return super.getView(position, convertView, parent);

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.list_visits_fragment_row, parent, false);

            ImageView calendarioIcon = (ImageView) row.findViewById(R.id.calendario);
            View vListVisitsDateView = row.findViewById(R.id.vListVisitsDateCell);
            TextView tvListVisitsDay = (TextView)row.findViewById(R.id.tvListVisitsDay);
            TextView tvListVisitsMonth = (TextView)row.findViewById(R.id.tvListVisitsMonth);
            ImageView ivPersonTimeSet = (ImageView) row.findViewById(R.id.ivPersonTimeSet);
            ImageView ivPersonTimeUnset = (ImageView) row.findViewById(R.id.ivPersonTimeUnset);

            TextView clientNameTextView = (TextView) row.findViewById(R.id.tvVisitsListName);
            clientNameTextView.setText(visitsList.get(position).getCLIENT_NAME());

            TextView serviceTypeTextView = (TextView) row.findViewById(R.id.tvVisitsListTOS);
            serviceTypeTextView.setText(visitsList.get(position).getSERVICE_NAME());

            TextView clientAddressTextView = (TextView) row.findViewById(R.id.tvVisitsListAddress);
            clientAddressTextView.setText(visitsList.get(position).getCLIENT_ADDRESS());

            if(visitsList.get(position).getVISIT_DAY().length() != 0)
            {

                vListVisitsDateView.setBackgroundColor(Color.parseColor("#009922"));
                tvListVisitsDay.setVisibility(View.VISIBLE);
                tvListVisitsMonth.setVisibility(View.VISIBLE);
                ivPersonTimeSet.setVisibility(View.VISIBLE);

                tvListVisitsDay.setText(visitsList.get(position).getVISIT_DAY());
                tvListVisitsMonth.setText(visitsList.get(position).getVISIT_MONTH());
            }
            else
            {
                calendarioIcon.setVisibility(View.VISIBLE);
                ivPersonTimeUnset.setVisibility(View.VISIBLE);
            }

            return row;
        }
    }
}

/*            int optionId = randomInteger!=0 ? R.layout.list_visits_cell_datetime_set : R.layout.list_visits_cell_datetime_set;
            View C = inflater.inflate(optionId, parent, false);
            int index = parent.indexOfChild(C);
            parent.addView(C, index);*/