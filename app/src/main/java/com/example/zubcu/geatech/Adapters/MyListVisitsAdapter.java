package com.example.zubcu.geatech.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zubcu.geatech.Models.VisitsListRowModel;
import com.example.zubcu.geatech.R;

import java.util.ArrayList;

/**
 * Created by user on 11/21/2016.
 */

public class MyListVisitsAdapter extends BaseAdapter
{
    private Context mContext;
    ArrayList<VisitsListRowModel> visitsList;
    int layout_id;

    public MyListVisitsAdapter(Context context, int layout_id, ArrayList<VisitsListRowModel> objects)
    {
        //super(context, textViewResourceId, objects);
        mContext = context;
        visitsList = objects;
        this.layout_id = layout_id;
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
        View row = inflater.inflate(layout_id, parent, false);

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
