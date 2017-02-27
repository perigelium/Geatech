package ru.alexangan.developer.geatech.Adapters;

import android.app.Activity;
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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.GeaImagineRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;
import ru.alexangan.developer.geatech.Models.GeaRapporto;
import ru.alexangan.developer.geatech.Models.ItalianMonths;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.SEND_DATA_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.tokenStr;

/**
 * Created by user on 11/21/2016.
 */

public class NotSentListVisitsAdapter extends BaseAdapter implements Callback
{
    private Activity mContext;
    ArrayList<VisitItem> visitItemsDateTimeSet;
    int layout_id;
    ReportStates reportStates;
    Call callSendReport, callSendImage;
    Activity activity;
    String reportSendResponse;

    public NotSentListVisitsAdapter(Activity activity, int layout_id, ArrayList<VisitItem> objects)
    {
        //super(context, textViewResourceId, objects);
        this.activity = activity;
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

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(layout_id, parent, false);

        VisitItem visitItem = visitItemsDateTimeSet.get(position);

        ClientData clientData = visitItem.getClientData();
        ProductData productData = visitItem.getProductData();
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        final int idSopralluogo = geaSopralluogo.getId_sopralluogo();

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
        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

        if(reportStates != null)
        {
            TextView tvReportCompletionState = (TextView) row.findViewById(R.id.tvReportCompletionState);
            tvReportCompletionState.setText(reportStates.getDataOraRaportoCompletato());

/*            TextView tvNotSentReason = (TextView) row.findViewById(R.id.tvNotSentReason);
            tvNotSentReason.setText(reportStates.getSendingReportTriesStateString(reportStates.getSendingReportTriesState()).Value());

            TextView tvNextTimeTryToSendReport = (TextView) row.findViewById(R.id.tvNextTimeTryToSendReport);
            tvNextTimeTryToSendReport.setText(reportStates.getDataOraProssimoTentativo());*/
        }

        Button btnSendReportNow = (Button) row.findViewById(R.id.btnSendReportNow);

        btnSendReportNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(reportStates != null)
                {
                    sendReportItem(position);

                    visitItemsDateTimeSet.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

        String visitDateTime = reportStates.getData_ora_sopralluogo();

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

    private void sendReportItem(int position)
    {
        realm.beginTransaction();

        ReportItem reportItem = new ReportItem();
        Gson gson = new Gson();

        VisitItem visitItem = visitItemsDateTimeSet.get(position);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        ProductData productData = visitItem.getProductData();
        //String productType = productData.getProductType();
        //int idProductType = productData.getIdProductType();
        int idSopralluogo = geaSopralluogo.getId_sopralluogo();

        GeaSopralluogo geaSopralluogoUnmanaged = realm.copyFromRealm(geaSopralluogo);
        reportItem.setGeaSopralluogo(geaSopralluogoUnmanaged);

        ReportStates reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();

        ReportStates reportStatesUnmanaged = realm.copyFromRealm(reportStates);
        String strReportStatesUnmanaged = gson.toJson(reportStatesUnmanaged);
        GeaRapporto gea_rapporto = gson.fromJson(strReportStatesUnmanaged, GeaRapporto.class);
        reportItem.setGea_rapporto_sopralluogo(gea_rapporto);

        RealmResults geaItemsRapporto = realm.where(GeaItemRapporto.class).equalTo("company_id", company_id)
                .equalTo("tech_id", selectedTech.getId()).equalTo("id_rapporto_sopralluogo", idSopralluogo).findAll();
        List<GeaItemRapporto> listGeaItemRapporto = new ArrayList<>();

        for(Object gi : geaItemsRapporto)
        {
            GeaItemRapporto gi_unmanaged = realm.copyFromRealm((GeaItemRapporto)gi);
            listGeaItemRapporto.add(gi_unmanaged);
        }
        reportItem.setGea_items_rapporto_sopralluogo(listGeaItemRapporto);


        RealmResults<GeaImagineRapporto> listReportImages = realm.where(GeaImagineRapporto.class)
                .equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_rapporto_sopralluogo", idSopralluogo).findAll();

        List<GeaImagineRapporto> imagesArray = new ArrayList<>();

        for (GeaImagineRapporto geaImagineRapporto : listReportImages)
        {
            GeaImagineRapporto geaImagineRapportoUnmanaged = realm.copyFromRealm(geaImagineRapporto);
            geaImagineRapportoUnmanaged.setFilePath(null);
            imagesArray.add(geaImagineRapportoUnmanaged);
        }
        reportItem.setGea_immagini_rapporto_sopralluogo(imagesArray);


        realm.commitTransaction();

        String str_ReportItem_json = gson.toJson(reportItem);

        Log.d("DEBUG", str_ReportItem_json);

        NetworkUtils networkUtils = new NetworkUtils();
        callSendReport = networkUtils.sendData(this, SEND_DATA_URL_SUFFIX, tokenStr, str_ReportItem_json);

        for (GeaImagineRapporto geaImagineRapporto : listReportImages)
        {
            callSendImage = networkUtils.sendImage(this, activity, geaImagineRapporto);
        }
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        if (call == callSendReport)
        {
            showToastMessage("Invio rapporto fallito");
        }

        if (call == callSendImage)
        {
            showToastMessage("Invio immagine fallito");
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        if (call == callSendImage)
        {
            reportSendResponse = response.body().string();

            if (reportSendResponse == null)
            {
                showToastMessage("Immagine inviato, risposta non ha ricevuto");

                return;
            } else
            {
                showToastMessage("Immagine inviato, server ritorna: " + reportSendResponse);
            }
        }

        if (call == callSendReport)
        {
            reportSendResponse = response.body().string();

            if (reportSendResponse == null)
            {
                showToastMessage("Rapporto inviato, risposta non ha ricevuto");

                return;
            } else
            {
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(activity, "Rapporto inviato, server ritorna: " + reportSendResponse, Toast.LENGTH_LONG).show();

                        Calendar calendarNow = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                        String strDateTime = sdf.format(calendarNow.getTime());

                        realm.beginTransaction();
                        reportStates.setData_ora_invio_rapporto(strDateTime);
                        realm.commitTransaction();

                        Toast.makeText(mContext, "Rapporto inviato", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private void showToastMessage(final String msg)
    {
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
