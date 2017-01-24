package ru.alexangan.developer.geatech.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.ItalianMonths;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.VisitStates;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Activities.LoginActivity.realm;

/**
 * Created by user on 11/21/2016.
 */

public class NotSentListVisitsAdapter extends BaseAdapter
{
    private Context mContext;
    ArrayList<VisitItem> visitItemsDateTimeSet;
    int layout_id;
    ReportStates reportStates;

    public NotSentListVisitsAdapter(Context context, int layout_id, ArrayList<VisitItem> objects)
    {
        //super(context, textViewResourceId, objects);
        mContext = context;
        visitItemsDateTimeSet = objects;
        this.layout_id = layout_id;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // return super.getView(position, convertView, parent);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(layout_id, parent, false);

        VisitItem visitItem = visitItemsDateTimeSet.get(position);

        ClientData clientData = visitItem.getClientData();
        ProductData productData = visitItem.getProductData();
        VisitStates visitStates = visitItem.getVisitStates();
        int idSopralluogo = visitStates.getIdSopralluogo();

        TextView tvVisitDay = (TextView)row.findViewById(R.id.tvVisitDay);
        TextView tvVisitMonth = (TextView)row.findViewById(R.id.tvVisitMonth);
        TextView tvVisitTime = (TextView)row.findViewById(R.id.tvVisitTime);

        TextView clientNameTextView = (TextView) row.findViewById(R.id.tvClientName);
        clientNameTextView.setText(clientData.getName());

        TextView serviceTypeTextView = (TextView) row.findViewById(R.id.tvTypeOfService);
        serviceTypeTextView.setText(productData.getProductType());

        TextView clientAddressTextView = (TextView) row.findViewById(R.id.tvClientAddress);
        clientAddressTextView.setText(clientData.getAddress());

        realm.beginTransaction();
        reportStates = realm.where(ReportStates.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

        if(reportStates != null)
        {
            TextView tvReportCompletionState = (TextView) row.findViewById(R.id.tvReportCompletionState);
            tvReportCompletionState.setText(reportStates.getDataOraRaportoCompletato());

            TextView tvNotSentReason = (TextView) row.findViewById(R.id.tvNotSentReason);
            tvNotSentReason.setText(reportStates.getSendingReportTriesStateString(reportStates.getSendingReportTriesState()).Value());

            TextView tvNextTimeTryToSendReport = (TextView) row.findViewById(R.id.tvNextTimeTryToSendReport);
            tvNextTimeTryToSendReport.setText(reportStates.getDataOraProssimoTentativo());
        }

        Button btnSendReportNow = (Button) row.findViewById(R.id.btnSendReportNow);
        btnSendReportNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(reportStates != null)
                {
                    Calendar calendarNow = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                    String strDateTime = sdf.format(calendarNow.getTime());

                    // check whether report was received on server side and than set time at report has sent
                    realm.beginTransaction();
                    reportStates.setDataOraRaportoInviato(strDateTime);
                    realm.commitTransaction();

                    reportStates = realm.copyFromRealm(reportStates);

                    Gson gson = new Gson();
                    String json = gson.toJson(reportStates);

                    Log.d("DEBUG", json);

                    //sendReport(json);

                    Toast.makeText(mContext, "Rapporto inviato", Toast.LENGTH_LONG).show();

                    visitItemsDateTimeSet.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

        String visitDateTime = reportStates.getDataOraSopralluogo();

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

            String minuteStr = Integer.toString(calendar.get(calendar.MINUTE));
            if (minuteStr.length() == 1)
            {
                minuteStr = "0" + minuteStr;
            }

            tvVisitTime.setText(Integer.toString(calendar.get(calendar.HOUR_OF_DAY)) + ":" + minuteStr);

        }

        return row;
    }
}
