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
import java.util.Calendar;

/**
 * Created by user on 11/21/2016.
 */

public class ComingListVisitsAdapter extends BaseAdapter
{
    private Context mContext;
    ArrayList<GeneralInfoModel> visitsList;
    int layout_id;
    final Calendar calendarNow;
    Calendar calendar;
    long elapsedDays;

    public ComingListVisitsAdapter(Context context, int layout_id, ArrayList<GeneralInfoModel> objects)
    {
        //super(context, textViewResourceId, objects);
        mContext = context;
        visitsList = objects;
        this.layout_id = layout_id;

        calendarNow = Calendar.getInstance();
        calendar = Calendar.getInstance();
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // return super.getView(position, convertView, parent);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(layout_id, parent, false);

        TextView tvVisitDay = (TextView)row.findViewById(R.id.tvVisitDay);
        //TextView tvVisitMonth = (TextView)row.findViewById(R.id.tvVisitMonth);

        TextView clientNameTextView = (TextView) row.findViewById(R.id.tvClientName);
        clientNameTextView.setText(visitsList.get(position).getClientName());

        TextView serviceTypeTextView = (TextView) row.findViewById(R.id.tvTypeOfService);
        serviceTypeTextView.setText(visitsList.get(position).getServiceName());

        TextView clientAddressTextView = (TextView) row.findViewById(R.id.tvClientAddress);
        clientAddressTextView.setText(visitsList.get(position).getClientAddress());

        if(visitsList.get(position).getVisitDay() != 0)
        {
            calendar.set(Calendar.YEAR, visitsList.get(position).getVisitYear());
            calendar.set(Calendar.MONTH, visitsList.get(position).getVisitMonth() - 1);
            calendar.set(Calendar.DAY_OF_MONTH, visitsList.get(position).getVisitDay());
            calendar.set(Calendar.HOUR_OF_DAY, visitsList.get(position).getVisitHour());
            calendar.set(Calendar.MINUTE, visitsList.get(position).getVisitMinute());

            int millsDiff = calendar.compareTo(calendarNow);

            if (millsDiff <= 0)
            {
                elapsedDays = 0;
            }
            else
            {
                long milliSeconds = calendar.getTimeInMillis();
                long milliSecondsNow = calendarNow.getTimeInMillis();
                long periodMilliSeconds = (milliSeconds - milliSecondsNow);
                elapsedDays = periodMilliSeconds / 1000 / 60 / 60 / 24;
            }

            if (elapsedDays > 99)
            {
                tvVisitDay.setTextSize(50);
            }

            tvVisitDay.setText(Long.toString(elapsedDays));
        }

        return row;
    }
}
