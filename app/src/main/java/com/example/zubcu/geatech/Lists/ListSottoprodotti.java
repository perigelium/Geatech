package com.example.zubcu.geatech.Lists;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.zubcu.geatech.Adapters.SetVisitDateTimeListAdapter;
import com.example.zubcu.geatech.Models.DateTimeSetListCellModel;
import com.example.zubcu.geatech.R;

import java.util.ArrayList;

/**
 * Created by user on 11/15/2016.
 */

public class ListSottoprodotti extends ListView
{
    ArrayList<DateTimeSetListCellModel> list;

    public ListSottoprodotti(Context context)
    {
        super(context);

        list = new ArrayList<>();
        // Construct the data source

        //ArrayList<DateTimeSetListCellModel> arrayOfUsers = new ArrayList<>();

// Create the adapter to convert the array to views

        SetVisitDateTimeListAdapter adapter = new SetVisitDateTimeListAdapter(context, list );

// Attach the adapter to a ListView

        ListView listView = (ListView) findViewById(R.id.listSottprodottiSDT);

        listView.setAdapter(adapter);

        //ArrayAdapter<String> itemsAdapter =

                //new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, testItems );

 /*       test = new ArrayAdapter<String>(getContext(), R.layout.visits_day_time_list_cell, DateTimeSetListCellMap.PRODOTTO_NOME , testItems);
        setAdapter(test);*/

 /*       ListAdapter adapter = new SimpleAdapter(context, list, R.layout.visits_day_time_list_cell,
                new String[] { DateTimeSetListCellMap.PRODOTTO_NOME, DateTimeSetListCellMap.PRODOTTO_TIPO }, new int[] {
                R.id.tvProdottoDataSDT, R.id.tvTipoDataSDT });*/

        //setAdapter(itemsAdapter);

    }

    @Override
    public void setAdapter(ListAdapter adapter)
    {
        super.setAdapter(adapter);

    }


}
