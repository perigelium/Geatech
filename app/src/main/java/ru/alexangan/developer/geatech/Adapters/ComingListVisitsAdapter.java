package ru.alexangan.developer.geatech.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.VisitData;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

/**
 * Created by user on 11/21/2016.
 */

public class ComingListVisitsAdapter extends BaseAdapter
{
    private Context mContext;
    ArrayList<VisitItem> visitItemsDateTimeSet;
    int layout_id;
    final Calendar calendarNow;
    Calendar calendar;
    long elapsedDays;

    public ComingListVisitsAdapter(Context context, int layout_id, ArrayList<VisitItem> visitItemsDateTimeSet)
    {
        //super(context, textViewResourceId, objects);
        mContext = context;
        this.visitItemsDateTimeSet = visitItemsDateTimeSet;
        this.layout_id = layout_id;

        calendarNow = Calendar.getInstance();
        calendar = Calendar.getInstance();
    }

    @Override
    public int getCount()
    {
        return visitItemsDateTimeSet.size();
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

        VisitItem visitItem = visitItemsDateTimeSet.get(position);

        ClientData clientData = visitItem.getClientData();
        ProductData productData = visitItem.getProductData();
        VisitData visitData = visitItem.getVisitData();

        TextView tvVisitDay = (TextView)row.findViewById(R.id.tvVisitDay);
        //TextView tvVisitMonth = (TextView)row.findViewById(R.id.tvVisitMonth);

        TextView clientNameTextView = (TextView) row.findViewById(R.id.tvClientName);
        clientNameTextView.setText(clientData.getName());

        TextView serviceTypeTextView = (TextView) row.findViewById(R.id.tvTypeOfService);
        serviceTypeTextView.setText(productData.getProductType());

        TextView clientAddressTextView = (TextView) row.findViewById(R.id.tvClientAddress);
        clientAddressTextView.setText(clientData.getAddress());

        String visitDateTime = visitData.getDataOraSopralluogo();

        if(visitDateTime != null)
        {
/*            calendar.set(Calendar.YEAR, visitsList.get(position).getVisitYear());
            calendar.set(Calendar.MONTH, visitsList.get(position).getVisitMonth() - 1);
            calendar.set(Calendar.DAY_OF_MONTH, visitsList.get(position).getVisitDay());
            calendar.set(Calendar.HOUR_OF_DAY, visitsList.get(position).getVisitHour());
            calendar.set(Calendar.MINUTE, visitsList.get(position).getVisitMinute());*/

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            try
            {
                calendar.setTime(sdf.parse(visitDateTime));

            } catch (ParseException e)
            {
                e.printStackTrace();
            }

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
