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

import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ItalianMonths;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.TechnicianItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;

/**
 * Created by user on 11/21/2016.*/

public class ReportsListAdapter extends BaseAdapter
{
    private Context mContext;
    private int layout_id;
    private ArrayList<ReportStates> reportStatesItems;

    public ReportsListAdapter(Context context, int layout_id, ArrayList<ReportStates> reportStatesItems)
    {
        //super(context, textViewResourceId, objects);
        mContext = context;
        this.layout_id = layout_id;
        this.reportStatesItems = reportStatesItems;
    }

    @Override
    public int getCount()
    {
        return reportStatesItems.size();
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

        ReportStates reportStates = reportStatesItems.get(position);

        //ClientData clientData = visitItem.getClientData();
        //ProductData productData = visitItem.getProductData();
        //GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();

/*        realm.beginTransaction();

        ReportStates reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId()).findFirst();

        realm.commitTransaction();*/

        TextView tvVisitDay = (TextView)row.findViewById(R.id.tvVisitDay);
        TextView tvVisitMonth = (TextView)row.findViewById(R.id.tvVisitMonth);
        TextView tvVisitTime = (TextView)row.findViewById(R.id.tvVisitTime);

        TextView clientNameTextView = (TextView) row.findViewById(R.id.tvClientName);
        clientNameTextView.setText(reportStates.getClientName());

        TextView serviceTypeTextView = (TextView) row.findViewById(R.id.tvVisitTOS);
        serviceTypeTextView.setText(reportStates.getProductType());

        TextView clientAddressTextView = (TextView) row.findViewById(R.id.tvClientAddress);
        clientAddressTextView.setText(reportStates.getClientAddress());

        //int idSopralluogo = geaSopralluogo.getId_sopralluogo();

        //int tech_id = geaSopralluogo.getId_tecnico();

/*        String techName = "";
        TechnicianItem technicianItem = realm.where(TechnicianItem.class).equalTo("id", tech_id).findFirst();

        if(technicianItem!=null)
        {
            techName = technicianItem.getFullNameTehnic();
        }*/

        String visitDateTime = reportStates.getData_ora_invio_rapporto();

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

            tvVisitDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
            tvVisitMonth.setText(ItalianMonths.numToString(calendar.get(Calendar.MONTH)+1));

            String minuteStr = Integer.toString(calendar.get(Calendar.MINUTE));
            if (minuteStr.length() == 1)
            {
                minuteStr = "0" + minuteStr;
            }

            tvVisitTime.setText(Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + minuteStr);
        }

        return row;
    }
}
