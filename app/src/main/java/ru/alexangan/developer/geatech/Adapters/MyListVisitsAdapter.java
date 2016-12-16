package ru.alexangan.developer.geatech.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.ItalianMonths;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.VisitData;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Network.RESTdataReceiver.visitItems;

/**
 * Created by user on 11/21/2016.
 */

public class MyListVisitsAdapter extends BaseAdapter
{
    private Context mContext;
    //ArrayList<VisitItem> visitItems = RESTdataReceiver.visitItems;
    int layout_id;

    public MyListVisitsAdapter(Context context, int layout_id)
    {
        //super(context, textViewResourceId, objects);
        mContext = context;
        //this.visitItems = visitItems;
        this.layout_id = layout_id;
    }

    @Override
    public int getCount()
    {
        return visitItems.size();
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
        View vVisitDateView = row.findViewById(R.id.vVisitDateCell);
        TextView tvVisitDay = (TextView)row.findViewById(R.id.tvVisitDay);
        TextView tvVisitMonth = (TextView)row.findViewById(R.id.tvVisitMonth);
        TextView tvVisitTime = (TextView)row.findViewById(R.id.tvVisitTime);
        ImageView ivPersonTimeSet = (ImageView) row.findViewById(R.id.ivPersonTimeSet);
        ImageView ivPersonTimeUnset = (ImageView) row.findViewById(R.id.ivPersonTimeUnset);

        TextView clientNameTextView = (TextView) row.findViewById(R.id.tvClientName);
        TextView serviceTypeTextView = (TextView) row.findViewById(R.id.tvVisitTOS);
        TextView clientAddressTextView = (TextView) row.findViewById(R.id.tvClientAddress);

        VisitItem visitItem = visitItems.get(position);
        ClientData clientData = visitItem.getClientData();
        ProductData productData = visitItem.getProductData();
        VisitData visitData = visitItem.getVisitData();

        String visitDateTime = visitData.getDataOraSopralluogo();
        if(visitDateTime == null)
        {
            visitDateTime = visitData.getDataSollecitoAppuntamento();
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        try
        {
            calendar.setTime(sdf.parse(visitDateTime));

        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        clientNameTextView.setText(clientData.getName());
        serviceTypeTextView.setText(productData.getProductType());
        clientAddressTextView.setText(clientData.getAddress());

        if(calendar.DAY_OF_MONTH != 0)
        {
            vVisitDateView.setBackgroundColor(Color.parseColor("#009922"));
            tvVisitDay.setVisibility(View.VISIBLE);
            tvVisitMonth.setVisibility(View.VISIBLE);
            ivPersonTimeSet.setVisibility(View.VISIBLE);

            tvVisitDay.setText(Integer.toString(calendar.get(calendar.DAY_OF_MONTH)));
            tvVisitMonth.setText(ItalianMonths.numToString(calendar.get(calendar.MONTH)+1));

            String minuteStr = Integer.toString(calendar.get(calendar.MINUTE));
            if (minuteStr.length() == 1)
            {
                minuteStr = "0" + minuteStr;
            }

            tvVisitTime.setText(Integer.toString(calendar.get(calendar.HOUR_OF_DAY)) + ":" + minuteStr);
        }
        else
        {
            calendarioIcon.setVisibility(View.VISIBLE);
            ivPersonTimeUnset.setVisibility(View.VISIBLE);
        }

        return row;
    }
}