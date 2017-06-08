package ru.alexangan.developer.geatech.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ItalianMonths;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;

/**
 * Created by user on 11/21/2016.
 */

public class InWorkListVisitsAdapter extends BaseAdapter
{
    private final Realm realm;
    private Context mContext;
    private ArrayList<VisitItem> visitItems;
    private int layout_id;
    //ViewHolder holder;

    public InWorkListVisitsAdapter(Context context, int layout_id, ArrayList<VisitItem> visitItems)
    {
        //super(context, textViewResourceId, objects);
        mContext = context;
        this.visitItems = visitItems;
        this.layout_id = layout_id;
        realm = Realm.getDefaultInstance();
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(layout_id, parent, false);

/*        View row = convertView;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout_id, parent, false);

            holder = new ViewHolder();

            calendarioIcon = (ImageView) row.findViewById(R.id.calendario);
            vVisitDateView = row.findViewById(R.id.vVisitDateCell);
            tvVisitDay = (TextView)row.findViewById(R.id.tvVisitDay);
            tvVisitMonth = (TextView)row.findViewById(R.id.tvVisitMonth);
            tvVisitTime = (TextView)row.findViewById(R.id.tvVisitTime);
            ivPersonTimeSet = (ImageView) row.findViewById(R.id.ivPersonTimeSet);
            ivPersonTimeUnset = (ImageView) row.findViewById(R.id.ivPersonTimeUnset);

            clientNameTextView = (TextView) row.findViewById(R.id.tvClientName);
            serviceTypeTextView = (TextView) row.findViewById(R.id.tvVisitTOS);
            clientAddressTextView = (TextView) row.findViewById(R.id.tvClientAddress);

            row.setTag(holder);
        } else
        {
            row = convertView;

            holder = (ViewHolder) row.getTag();*/


        ImageView ivReportStatus = (ImageView) row.findViewById(R.id.ivReportStatus);
        TextView tvVisitDay = (TextView) row.findViewById(R.id.tvVisitDay);
        TextView tvVisitMonth = (TextView) row.findViewById(R.id.tvVisitMonth);
        TextView tvVisitTime = (TextView) row.findViewById(R.id.tvVisitTime);

        TextView clientNameTextView = (TextView) row.findViewById(R.id.tvClientName);
        TextView serviceTypeTextView = (TextView) row.findViewById(R.id.tvVisitTOS);
        TextView clientAddressTextView = (TextView) row.findViewById(R.id.tvClientAddress);

        TextView tvDateTimeHasSet = (TextView) row.findViewById(R.id.tvDateTimeHasSet);
        TextView tvTecnicalReportState = (TextView) row.findViewById(R.id.tvTecnicalReportState);
        TextView tvPhotosQuant = (TextView) row.findViewById(R.id.tvPhotosQuant);

        VisitItem visitItem = visitItems.get(position);
        ClientData clientData = visitItem.getClientData();
        ProductData productData = visitItem.getProductData();
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        int id_sopralluogo = geaSopralluogo.getId_sopralluogo();
        //int tech_id = geaSopralluogo.getId_tecnico();

        String productType = productData.getProductType();

        String clientName = clientData.getName();

        clientName = clientName.toLowerCase();
        String[] strArray = clientName.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray)
        {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap).append(" ");
        }

        clientNameTextView.setText(builder.toString());
        serviceTypeTextView.setText(productType);
        clientAddressTextView.setText(clientData.getAddress());

        //boolean ownReport = selectedTech.getId() == tech_id;
        TextView tvTechName = (TextView) row.findViewById(R.id.tvTechName);

        realm.beginTransaction();
        ReportItem reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", id_sopralluogo).findFirst();
        realm.commitTransaction();

        realm.beginTransaction();
        int id_rapporto_sopralluogo = reportItem != null ? reportItem.getGea_rapporto().getId_rapporto_sopralluogo() : -1;

        realm.commitTransaction();

        if (reportItem != null)
        {
            int tech_id = reportItem.getTech_id();
            String techName = reportItem.getGea_rapporto().getNome_tecnico();

            tvDateTimeHasSet.setText(reportItem.getReportStates().getGeneralInfoCompletionStateString().Value());
            tvTecnicalReportState.setText(reportItem.getReportStates().getReportCompletionStateString().Value());

            int photoAddedNumber = reportItem.getReportStates().getPhotosAddedNumber();
            String photoAddedNumberStr;

            if (photoAddedNumber == 0)
            {
                photoAddedNumberStr = reportItem.getReportStates().getPhotoAddedNumberString(photoAddedNumber).Value();
            } else
            {
                photoAddedNumberStr = photoAddedNumber + reportItem.getReportStates().getPhotoAddedNumberString(photoAddedNumber).Value();
            }

            tvPhotosQuant.setText(photoAddedNumberStr);
            String dataOraSopralluogo = reportItem.getGeaSopralluogo().getData_ora_sopralluogo();

            if (tech_id != 0)
            {
                tvTechName.setText(techName);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

                try
                {
                    calendar.setTime(sdf.parse(dataOraSopralluogo));

                } catch (ParseException e)
                {
                    e.printStackTrace();
                }

/*                if (ownReport)
                {
                    ivReportStatus.setBackgroundResource(R.drawable.dot_green);

                } else
                {
                    ivReportStatus.setBackgroundResource(R.drawable.dot_gray);
                }*/

                tvVisitDay.setVisibility(View.VISIBLE);
                tvVisitMonth.setVisibility(View.VISIBLE);

                String visitDay = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
                tvVisitDay.setText(visitDay);
                String visitMonth = ItalianMonths.numToString(calendar.get(Calendar.MONTH) + 1);
                tvVisitMonth.setText(visitMonth);

                String minuteStr = Integer.toString(calendar.get(Calendar.MINUTE));
                if (minuteStr.length() == 1)
                {
                    minuteStr = "0" + minuteStr;
                }

                String visitTime = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + minuteStr;
                tvVisitTime.setText(visitTime);
            }/* else
            {
                ivReportStatus.setBackgroundResource(R.drawable.dot_yellow);
                tvVisitDay.setText("");
                tvVisitMonth.setText("");
                tvVisitTime.setText("");
            }*/
        }


        return row;
    }

/*static class ViewHolder
{
    ImageView calendarioIcon;
    View vVisitDateView;
    TextView tvVisitDay;
    TextView tvVisitMonth;
    TextView tvVisitTime;
    ImageView ivPersonTimeSet;
    ImageView ivPersonTimeUnset;

    TextView clientNameTextView;
    TextView serviceTypeTextView;
    TextView clientAddressTextView;
}*/
}
