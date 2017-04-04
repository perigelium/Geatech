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
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;

/**
 * Created by user on 11/21/2016.*/

public class ComingListVisitsAdapter extends BaseAdapter
{
    private Context mContext;
    ArrayList<VisitItem> visitItemsDateTimeSet;
    int layout_id;
    final Calendar calendarNow;
    Calendar calendar;
    long elapsedHours, elapsedDays;

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
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        int idSopralluogo = geaSopralluogo.getId_sopralluogo();

        realm.beginTransaction();

        ReportStates reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();

        realm.commitTransaction();

        TextView tvVisitDay = (TextView)row.findViewById(R.id.tvVisitDay);
        TextView tvVisitDaysOrHoursRemain = (TextView)row.findViewById(R.id.tvVisitDaysOrHoursRemain);

        TextView clientNameTextView = (TextView) row.findViewById(R.id.tvClientName);
        clientNameTextView.setText(clientData.getName());

        TextView serviceTypeTextView = (TextView) row.findViewById(R.id.tvTypeOfService);
        serviceTypeTextView.setText(productData.getProductType());

        TextView clientAddressTextView = (TextView) row.findViewById(R.id.tvClientAddress);
        clientAddressTextView.setText(clientData.getAddress());

        String visitDateTime = reportStates!=null ? reportStates.getData_ora_sopralluogo() : null;

        if(visitDateTime != null)
        {
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
                elapsedHours = 0;
            }
            else
            {
                long milliSeconds = calendar.getTimeInMillis();
                long milliSecondsNow = calendarNow.getTimeInMillis();
                long periodMilliSeconds = (milliSeconds - milliSecondsNow);
                elapsedHours = periodMilliSeconds / 1000 / 60 / 60;
                elapsedDays = elapsedHours / 24;
            }

            if (elapsedDays > 99)
            {
                tvVisitDay.setTextSize(50);
            }

            if(elapsedDays > 0)
            {
                tvVisitDay.setText(Long.toString(elapsedDays));
            }
            else
            {
                tvVisitDay.setText(Long.toString(elapsedHours));
                tvVisitDaysOrHoursRemain.setText("ore");
            }
        }

        return row;
    }
}
