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
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ItalianMonths;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.TechnicianItem;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;

public class OwnListVisitsAdapter extends BaseAdapter
{
    private final Realm realm;
    private Context mContext;
    private ArrayList<VisitItem> visitItems;
    private int layout_id;
    private boolean overdueVisit;
    //ViewHolder holder;

    public OwnListVisitsAdapter(Context context, int layout_id, ArrayList<VisitItem> visitItems)
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

        overdueVisit = false;

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
        TextView tvReportStatus = (TextView) row.findViewById(R.id.tvReportStatus);
        //View vVisitDateView = row.findViewById(R.id.vVisitDateCell);
        TextView tvVisitDay = (TextView) row.findViewById(R.id.tvVisitDay);
        TextView tvVisitMonth = (TextView) row.findViewById(R.id.tvVisitMonth);
        TextView tvVisitTime = (TextView) row.findViewById(R.id.tvVisitTime);
        //ImageView ivPersonTimeSet = (ImageView) row.findViewById(R.id.ivPersonTimeSet);
        //ImageView ivPersonTimeUnset = (ImageView) row.findViewById(R.id.ivPersonTimeUnset);

        TextView clientNameTextView = (TextView) row.findViewById(R.id.tvClientName);
        TextView serviceTypeTextView = (TextView) row.findViewById(R.id.tvVisitTOS);
        TextView clientAddressTextView = (TextView) row.findViewById(R.id.tvClientAddress);

        VisitItem visitItem = visitItems.get(position);
        ClientData clientData = visitItem.getClientData();
        ProductData productData = visitItem.getProductData();
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        String dataOraSopralluogo = geaSopralluogo.getData_ora_sopralluogo();
        //int idSopralluogo = geaSopralluogo.getId_sopralluogo();
        int tech_id = geaSopralluogo.getId_tecnico();

        String techName = "";
        TechnicianItem technicianItem = realm.where(TechnicianItem.class).equalTo("id", tech_id).findFirst();

        if (technicianItem != null)
        {
            techName = technicianItem.getFullNameTehnic();
        }

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

        boolean ownReport = selectedTech.getId() == tech_id;
        TextView tvTechName = (TextView) row.findViewById(R.id.tvTechName);
        tvTechName.setText(techName);

        Calendar calendarTodayFirstMin = Calendar.getInstance(Locale.ITALY);

        calendarTodayFirstMin.set(Calendar.HOUR_OF_DAY, 0);
        calendarTodayFirstMin.set(Calendar.MINUTE, 0);
        calendarTodayFirstMin.set(Calendar.SECOND, 0);

        long firstMilliSecondsOfToday = calendarTodayFirstMin.getTimeInMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);
        Date date = null;

        if (dataOraSopralluogo != null)
        {
            try
            {
                date = sdf.parse(dataOraSopralluogo);
                long time = date.getTime();

                if (time < firstMilliSecondsOfToday)
                {
                    overdueVisit = true;
                }
            } catch (ParseException e)
            {
                e.printStackTrace();
            }

        }
        if (tech_id != 0 && dataOraSopralluogo != null)
        {
            Calendar calendar = Calendar.getInstance();

            try
            {
                calendar.setTime(sdf.parse(dataOraSopralluogo));

            } catch (ParseException e)
            {
                e.printStackTrace();
            }

            if (ownReport)
            {
                if (overdueVisit)
                {
                    ivReportStatus.setBackgroundResource(R.drawable.red_oval_shape);
                } else
                {
                    ivReportStatus.setBackgroundResource(R.drawable.green_oval_shape);
                }

            } else
            {
                ivReportStatus.setBackgroundResource(R.drawable.gray_oval_shape);
            }

            tvVisitDay.setVisibility(View.VISIBLE);
            tvVisitMonth.setVisibility(View.VISIBLE);
/*            ivPersonTimeSet.setVisibility(View.VISIBLE);
            ivPersonTimeUnset.setVisibility(View.GONE);*/

            tvVisitDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
            tvVisitMonth.setText(ItalianMonths.numToString(calendar.get(Calendar.MONTH) + 1));

            String minuteStr = Integer.toString(calendar.get(Calendar.MINUTE));
            if (minuteStr.length() == 1)
            {
                minuteStr = "0" + minuteStr;
            }

            tvVisitTime.setText(Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + minuteStr);
        } else
        {
            ivReportStatus.setBackgroundResource(R.drawable.yellow_oval_shape);
            tvVisitDay.setText("");
            tvVisitMonth.setText("");
            tvVisitTime.setText("");
            //calendarioIcon.setVisibility(View.VISIBLE);
/*            ivPersonTimeUnset.setVisibility(View.VISIBLE);
            ivPersonTimeSet.setVisibility(View.GONE);*/
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