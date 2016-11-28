package com.example.zubcu.geatech.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zubcu.geatech.Models.GeneralInfoModel;
import com.example.zubcu.geatech.Models.ItalianMonths;
import com.example.zubcu.geatech.R;

import java.util.ArrayList;

/**
 * Created by user on 11/21/2016.
 */

public class InWorkListVisitsAdapter extends BaseAdapter
{
    private Context mContext;
    ArrayList<GeneralInfoModel> visitsList;
    int layout_id;

    public InWorkListVisitsAdapter(Context context, int layout_id, ArrayList<GeneralInfoModel> objects)
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

        TextView tvVisitDay = (TextView)row.findViewById(R.id.tvVisitDay);
        TextView tvVisitMonth = (TextView)row.findViewById(R.id.tvVisitMonth);

        TextView clientNameTextView = (TextView) row.findViewById(R.id.tvClientName);
        clientNameTextView.setText(visitsList.get(position).getClientName());

        TextView serviceTypeTextView = (TextView) row.findViewById(R.id.tvTypeOfService);
        serviceTypeTextView.setText(visitsList.get(position).getServiceName());

        TextView clientAddressTextView = (TextView) row.findViewById(R.id.tvClientAddress);
        clientAddressTextView.setText(visitsList.get(position).getClientAddress());

        if(visitsList.get(position).getVisitDay() != 0)
        {
            tvVisitDay.setText(Integer.toString(visitsList.get(position).getVisitDay()));
            tvVisitMonth.setText(ItalianMonths.numToString(visitsList.get(position).getVisitMonth()));
        }

        return row;
    }
}
