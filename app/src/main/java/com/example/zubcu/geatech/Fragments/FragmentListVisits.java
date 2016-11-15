package com.example.zubcu.geatech.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zubcu.geatech.R;

import java.util.Random;

import static android.R.attr.onClick;

public class FragmentListVisits extends ListFragment
{
    OnItemSelectedListener listener;

    final String[] catNames = new String[]{"Рыжик", "Барсик", "Мурзик",
            "Мурка", "Васька", "Томасина", "Кристина", "Пушок", "Дымка",
            "Кузя", "Китти", "Масяня", "Симба"};

    final String[] serviceName = new String[]{"Termodinamico", "Fotovoltaico", "Service_4",
            "Service_5", "Service_6", "Service_7", "Service_8", "Service_9", "Service_10",
            "Service_11", "Service_12", "Service_13", "Service_14"};

    final String[] clientAddress = new String[]{"Indirizzo_1", "Indirizzo_2", "Indirizzo_3",
            "Indirizzo_4", "Indirizzo_5", "Indirizzo_6", "Indirizzo_7", "Indirizzo_8", "Indirizzo_9",
            "Indirizzo_10", "Indirizzo_11", "Indirizzo_12", "Indirizzo_13"};

    Random rnd;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MyListAdapter myListAdapter = new MyListAdapter(getActivity(),
                R.layout.list_visits_fragment_row, catNames);
        setListAdapter(myListAdapter);
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

    public class MyListAdapter extends ArrayAdapter<String>
    {
        private Context mContext;

        public MyListAdapter(Context context, int textViewResourceId, String[] objects)
        {
            super(context, textViewResourceId, objects);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // return super.getView(position, convertView, parent);

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.list_visits_fragment_row, parent, false);

            TextView clientNameTextView = (TextView) row.findViewById(R.id.tvVisitsListName);
            clientNameTextView.setText(catNames[position]);

            int randomInteger = rnd.nextInt(2);

            if(randomInteger!=0)
            {
                TextView serviceTypeTextView = (TextView) row.findViewById(R.id.tvVisitsListTOS);
                serviceTypeTextView.setText(serviceName[position]);

                TextView clientAddressTextView = (TextView) row.findViewById(R.id.tvVisitsListAddress);
                clientAddressTextView.setText(clientAddress[position]);

                ImageView clientIcon = (ImageView) row.findViewById(R.id.imageViewPerson);

                clientIcon.setImageResource(R.drawable.person_green);


                ImageView vizitDateIcon = (ImageView) row.findViewById(R.id.ivVizitDate);

                vizitDateIcon.setImageResource(R.drawable.transparent24px);
            }

            return row;
        }
    }
}
