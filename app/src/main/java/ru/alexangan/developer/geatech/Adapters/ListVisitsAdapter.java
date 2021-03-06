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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ItalianMonths;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.TechnicianItem;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;

public class ListVisitsAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<VisitItem> visitItems;
    private List<ReportItem> reportItems;
    private int layout_id;

    public ListVisitsAdapter(Context context, int layout_id, ArrayList<VisitItem> visitItems, List<ReportItem> reportItems)
    {
        mContext = context;
        this.visitItems = visitItems;
        this.reportItems = reportItems;
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(layout_id, parent, false);

        boolean overdueVisit = false;


        ImageView ivReportStatus = (ImageView) row.findViewById(R.id.ivReportStatus);
        TextView tvReportStatus = (TextView) row.findViewById(R.id.tvReportStatus);
        TextView tvVisitDay = (TextView) row.findViewById(R.id.tvVisitDay);
        TextView tvVisitMonth = (TextView) row.findViewById(R.id.tvVisitMonth);
        TextView tvVisitTime = (TextView) row.findViewById(R.id.tvVisitTime);

        TextView clientNameTextView = (TextView) row.findViewById(R.id.tvClientName);
        TextView serviceTypeTextView = (TextView) row.findViewById(R.id.tvVisitTOS);
        TextView clientAddressTextView = (TextView) row.findViewById(R.id.tvClientAddress);

        VisitItem visitItem = visitItems.get(position);
        ClientData clientData = visitItem.getClientData();
        ProductData productData = visitItem.getProductData();
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        String data_ora_sopralluogo = geaSopralluogo.getData_ora_sopralluogo();
        int id_sopralluogo = geaSopralluogo.getId_sopralluogo();
        int tech_id = geaSopralluogo.getId_tecnico();

        String data_sollecito_appuntamento = geaSopralluogo.getData_sollecito_appuntamento();
        boolean remindedVisit = data_ora_sopralluogo == null && data_sollecito_appuntamento != null;

        String data_invio_rapporto = visitItem.getGeaRapporto().getData_ora_invio_rapporto();
        String data_sollecito_rapporto = geaSopralluogo.getData_sollecito_rapporto();
        boolean remindedReport = data_invio_rapporto == null && data_sollecito_rapporto != null;

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

        if (data_ora_sopralluogo != null)
        {
            try
            {
                Date date = sdf.parse(data_ora_sopralluogo);
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
        if (tech_id != 0 && data_ora_sopralluogo != null)
        {
            Calendar calendar = Calendar.getInstance();

            try
            {
                calendar.setTime(sdf.parse(data_ora_sopralluogo));

            } catch (ParseException e)
            {
                e.printStackTrace();
            }

            if (ownReport)
            {
                if (overdueVisit || remindedReport)
                {
                    ivReportStatus.setBackgroundResource(R.drawable.red_oval_shape);
                } else
                {
                    ivReportStatus.setBackgroundResource(R.drawable.green_oval_shape);
                }

                if (reportItems != null)
                {
                    for (int i = 0; i < reportItems.size(); i++)
                    {
                        ReportItem reportItem = reportItems.get(i);

                        if (reportItem.getId_sopralluogo() == id_sopralluogo)
                        {
                            int generalInfoCompletionState = reportItem.getReportStates().getGeneralInfoCompletionState();
                            int reportCompletionState = reportItem.getReportStates().getReportCompletionState();
                            int photosAddedNumber = reportItem.getReportStates().getPhotosAddedNumber();

                            boolean reportStartedNotCompleted =
                                    (reportCompletionState > ReportStates.REPORT_NON_INITIATED || photosAddedNumber != 0)
                                            &&
                                            (! (generalInfoCompletionState == ReportStates.GENERAL_INFO_DATETIME_AND_COORDS_SET
                                                    && reportCompletionState == ReportStates.REPORT_COMPLETED
                                                    && photosAddedNumber >= ReportStates.PHOTOS_MIN_ADDED));

                            boolean reportCompleteNotSent = generalInfoCompletionState == ReportStates.GENERAL_INFO_DATETIME_AND_COORDS_SET
                                    && reportCompletionState == ReportStates.REPORT_COMPLETED
                                    && photosAddedNumber >= ReportStates.PHOTOS_MIN_ADDED
                                    && reportItem.getGea_rapporto_sopralluogo().getData_ora_invio_rapporto() == null;

                            if (reportStartedNotCompleted)
                            {
                                tvReportStatus.setVisibility(View.VISIBLE);
                                tvReportStatus.setTextColor(Color.parseColor("#ffffd100"));
                                tvReportStatus.setText(R.string.NoCompletedCompilation);
                            } else if (reportCompleteNotSent)
                            {
                                tvReportStatus.setVisibility(View.VISIBLE);
                                tvReportStatus.setTextColor(Color.parseColor("#ffff0000"));
                                tvReportStatus.setText(R.string.CompletedNotSentReport);
                            } else
                            {
                                tvReportStatus.setVisibility(View.VISIBLE);
                                tvReportStatus.setTextColor(Color.parseColor("#ff808080"));
                                tvReportStatus.setText(R.string.NotInitiatedCompilation);
                            }
                            break;
                        }
                    }
                }
            } else
            {
                ivReportStatus.setBackgroundResource(R.drawable.gray_oval_shape);
            }

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
        } else
        {
            if (remindedVisit)
            {
                ivReportStatus.setBackgroundResource(R.drawable.red_oval_shape);
            } else
            {
                ivReportStatus.setBackgroundResource(R.drawable.yellow_oval_shape);
            }

            tvVisitDay.setText("");
            tvVisitMonth.setText("");
            tvVisitTime.setText("");
        }

        return row;
    }
}
