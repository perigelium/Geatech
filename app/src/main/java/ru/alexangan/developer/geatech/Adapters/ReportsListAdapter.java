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
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.TechnicianItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;

public class ReportsListAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<ReportItem> reportItems;
    private int layout_id;

    public ReportsListAdapter(Context context, int layout_id, ArrayList<ReportItem> reportItems)
    {
        mContext = context;
        this.reportItems = reportItems;
        this.layout_id = layout_id;
    }

    @Override
    public int getCount()
    {
        return reportItems.size();
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


        ImageView ivReportStatus = (ImageView) row.findViewById(R.id.ivReportStatus);
        TextView tvVisitDay = (TextView) row.findViewById(R.id.tvVisitDay);
        TextView tvVisitMonth = (TextView) row.findViewById(R.id.tvVisitMonth);
        TextView tvVisitTime = (TextView) row.findViewById(R.id.tvVisitTime);

        TextView clientNameTextView = (TextView) row.findViewById(R.id.tvClientName);
        TextView serviceTypeTextView = (TextView) row.findViewById(R.id.tvVisitTOS);
        TextView clientAddressTextView = (TextView) row.findViewById(R.id.tvClientAddress);

        ReportItem reportItem = reportItems.get(position);
        ClientData clientData = reportItem.getClientData();
        GeaSopralluogo geaSopralluogo = reportItem.getGeaSopralluogo();
        String dataOraSopralluogo = geaSopralluogo.getData_ora_sopralluogo();
        int tech_id = geaSopralluogo.getId_tecnico();

        String techName = "";
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        TechnicianItem technicianItem = realm.where(TechnicianItem.class).equalTo("id", tech_id).findFirst();
        realm.commitTransaction();
        realm.close();

        if (technicianItem != null)
        {
            techName = technicianItem.getFullNameTehnic();
        }

        if(clientData!=null)
        {
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
            clientAddressTextView.setText(clientData.getAddress());

            String productType = clientData.getProduct_type();
            serviceTypeTextView.setText(productType);
        }

        boolean ownReport = selectedTech.getId() == tech_id;
        TextView tvTechName = (TextView) row.findViewById(R.id.tvTechName);
        tvTechName.setText(techName);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

        if (ownReport && dataOraSopralluogo != null)
        {
            Calendar calendar = Calendar.getInstance();

            try
            {
                calendar.setTime(sdf.parse(dataOraSopralluogo));

            } catch (ParseException e)
            {
                e.printStackTrace();
            }

            ivReportStatus.setBackgroundResource(R.drawable.green_oval_shape);

            tvVisitDay.setVisibility(View.VISIBLE);
            tvVisitMonth.setVisibility(View.VISIBLE);

            tvVisitDay.setText(String.format(Locale.ITALIAN, "%d", calendar.get(Calendar.DAY_OF_MONTH)));
            tvVisitMonth.setText(ItalianMonths.numToString(calendar.get(Calendar.MONTH) + 1));

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
