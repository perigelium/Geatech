package ru.alexangan.developer.geatech.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

/**
 * Created by user on 11/21/2016.
 */

public class NotSentListVisitsAdapter extends BaseAdapter implements Callback
{
    private final ProgressDialog requestServerDialog;
    ArrayList<VisitItem> visitItemsDateTimeSet;
    int layout_id;
    ReportStates reportStates;
    Call callSendReport, callSendImage;
    Activity activity;
    String reportSendResponse;
    NetworkUtils networkUtils;
    List<GeaImagineRapporto> imagesArray;
    List<Call> callSendImagesList;
    ViewHolder mViewHolder;
    ArrayList<Boolean> alReportSent;

    public NotSentListVisitsAdapter(Activity activity, int layout_id, ArrayList<VisitItem> objects)
    {
        //super(context, textViewResourceId, objects);
        this.activity = activity;
        visitItemsDateTimeSet = objects;
        this.layout_id = layout_id;
        callSendImagesList = new ArrayList<>();
        networkUtils = new NetworkUtils();

        requestServerDialog = new ProgressDialog(activity);
        requestServerDialog.setTitle("");
        requestServerDialog.setMessage("Trasmettere dei dati, si prega di attendere un po'...");
        requestServerDialog.setIndeterminate(true);

        alReportSent = new ArrayList<>(Collections.nCopies(visitItemsDateTimeSet.size(), false));
        //alReportSent = (ArrayList<Boolean>) Arrays.asList(false);
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // return super.getView(position, convertView, parent);

        View row = convertView;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout_id, parent, false);
            mViewHolder = new ViewHolder();

            mViewHolder.tvVisitDay = (TextView) row.findViewById(R.id.tvVisitDay);
            mViewHolder.tvVisitMonth = (TextView) row.findViewById(R.id.tvVisitMonth);
            mViewHolder.tvVisitTime = (TextView) row.findViewById(R.id.tvVisitTime);

            mViewHolder.clientNameTextView = (TextView) row.findViewById(R.id.tvClientName);
            mViewHolder.serviceTypeTextView = (TextView) row.findViewById(R.id.tvTypeOfService);
            mViewHolder.clientAddressTextView = (TextView) row.findViewById(R.id.tvClientAddress);

            mViewHolder.tvReportCompletionState = (TextView) row.findViewById(R.id.tvReportCompletionState);

            mViewHolder.btnSendReportNow = (Button) row.findViewById(R.id.btnSendReportNow);

            mViewHolder.tvDataOraReportSent = (TextView) row.findViewById(R.id.tvDataOraReportSent);
            mViewHolder.tvDataOraReportSent.setText("Rapporto non è stato ancora inviato");

            row.setTag(mViewHolder);

        } else
        {
            mViewHolder = (ViewHolder) row.getTag();
        }

        VisitItem visitItem = visitItemsDateTimeSet.get(position);

        ClientData clientData = visitItem.getClientData();
        ProductData productData = visitItem.getProductData();
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        final int idSopralluogo = geaSopralluogo.getId_sopralluogo();

/*        TextView tvVisitDay = (TextView)row.findViewById(R.id.tvVisitDay);
        TextView tvVisitMonth = (TextView)row.findViewById(R.id.tvVisitMonth);
        TextView tvVisitTime = (TextView)row.findViewById(R.id.tvVisitTime);

        TextView clientNameTextView = (TextView) row.findViewById(R.id.tvClientName);*/
        mViewHolder.clientNameTextView.setText(clientData.getName());

        //TextView serviceTypeTextView = (TextView) row.findViewById(R.id.tvTypeOfService);
        mViewHolder.serviceTypeTextView.setText(productData.getProductType());

        //TextView clientAddressTextView = (TextView) row.findViewById(R.id.tvClientAddress);
        mViewHolder.clientAddressTextView.setText(clientData.getAddress());

        realm.beginTransaction();
        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

        if (reportStates != null)
        {
            mViewHolder.tvReportCompletionState.setText(reportStates.getDataOraRaportoCompletato());

/*            TextView tvNotSentReason = (TextView) row.findViewById(R.id.tvNotSentReason);
            tvNotSentReason.setText(reportStates.getSendingReportTriesStateString(reportStates.getSendingReportTriesState()).Value());

            TextView tvNextTimeTryToSendReport = (TextView) row.findViewById(R.id.tvNextTimeTryToSendReport);
            tvNextTimeTryToSendReport.setText(reportStates.getDataOraProssimoTentativo());*/
        }

        Boolean reportSent = alReportSent.get(position);

        if (reportSent)
        {
            mViewHolder.btnSendReportNow.setEnabled(false);
            mViewHolder.btnSendReportNow.setAlpha(.4f);
        }

        mViewHolder.btnSendReportNow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!NetworkUtils.isNetworkAvailable(activity))
                {
                    showToastMessage("Controlla la connessione a Internet");
                    return;
                }

                disableInputAndShowProgressDialog();

                alReportSent.set(position, true);

                notifyDataSetChanged();

                sendReportItem(position);
            }
        });

        String visitDateTime = reportStates.getData_ora_sopralluogo();

        if (visitDateTime != null)
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

            mViewHolder.tvVisitDay.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
            mViewHolder.tvVisitMonth.setText(ItalianMonths.numToString(calendar.get(Calendar.MONTH) + 1));

            String minuteStr = Integer.toString(calendar.get(calendar.MINUTE));
            if (minuteStr.length() == 1)
            {
                minuteStr = "0" + minuteStr;
            }

            mViewHolder.tvVisitTime.setText(Integer.toString(calendar.get(calendar.HOUR_OF_DAY)) + ":" + minuteStr);

        }

        return row;
    }

    private void sendReportItem(int selectedIndex)
    {
        realm.beginTransaction();

        ReportItem reportItem = new ReportItem();
        Gson gson = new Gson();

        VisitItem visitItem = visitItemsDateTimeSet.get(selectedIndex);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        ProductData productData = visitItem.getProductData();
        String productType = productData.getProductType();
        //int idProductType = productData.getIdProductType();
        int idSopralluogo = geaSopralluogo.getId_sopralluogo();

        GeaSopralluogo geaSopralluogoUnmanaged = realm.copyFromRealm(geaSopralluogo);
        reportItem.setGeaSopralluogo(geaSopralluogoUnmanaged);

        ReportStates reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();

        if (reportStates == null)
        {
            showToastMessage("Invio rapporto fallito");
            return;
        }

        int id_rapporto_sopralluogo = reportStates.getId_rapporto_sopralluogo();

        ReportStates reportStatesUnmanaged = realm.copyFromRealm(reportStates);
        String strReportStatesUnmanaged = gson.toJson(reportStatesUnmanaged);
        GeaRapporto gea_rapporto = gson.fromJson(strReportStatesUnmanaged, GeaRapporto.class);
        reportItem.setGea_rapporto_sopralluogo(gea_rapporto);

        RealmResults geaItemsRapporto = realm.where(GeaItemRapporto.class).equalTo("company_id", company_id)
                .equalTo("tech_id", selectedTech.getId()).equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findAll();
        List<GeaItemRapporto> listGeaItemRapporto = new ArrayList<>();

        for (Object gi : geaItemsRapporto)
        {
            GeaItemRapporto gi_unmanaged = realm.copyFromRealm((GeaItemRapporto) gi);
            listGeaItemRapporto.add(gi_unmanaged);
        }
        reportItem.setGea_items_rapporto_sopralluogo(listGeaItemRapporto);


        RealmResults<GeaImagineRapporto> listReportImages = realm.where(GeaImagineRapporto.class)
                .equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findAll();

        imagesArray = new ArrayList<>();

        for (GeaImagineRapporto geaImagineRapporto : listReportImages)
        {
            GeaImagineRapporto geaImagineRapportoUnmanaged = realm.copyFromRealm(geaImagineRapporto);
            //geaImagineRapportoUnmanaged.setFilePath(null);
            geaImagineRapportoUnmanaged.setId_immagine_rapporto(0);
            imagesArray.add(geaImagineRapportoUnmanaged);
        }
        reportItem.setGea_immagini_rapporto_sopralluogo(imagesArray);

        realm.commitTransaction();

        String str_ReportItem_json = gson.toJson(reportItem);

        ////Log.d("DEBUG", str_ReportItem_json);

        if (!NetworkUtils.isNetworkAvailable(activity))
        {
            showToastMessage("Connessione ad internet non presente");
            return;
        }

        callSendReport = networkUtils.sendData(this, SEND_DATA_URL_SUFFIX, tokenStr, str_ReportItem_json);
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                enableInput();
            }
        });

        if (call == callSendReport)
        {
            showToastMessage("Invio rapporto fallito");
        }

        if (call == callSendImage)
        {
            showToastMessage("Invio immagine fallito");
            ////Log.d("DEBUG", "Invio immagine fallito");
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        if (callSendImagesList != null)
        {
            for (int i = 0; i < callSendImagesList.size(); i++)
            {
                if (call == callSendImagesList.get(i))
                {
                    reportSendResponse = response.body().string();

                    showToastMessage("Immagine " + i + " inviato"); //, server ritorna: " + reportSendResponse
                    ////Log.d("DEBUG", "image " + i + ", server returned:" + reportSendResponse);
                }
            }
        }

        if (call == callSendReport)
        {
            reportSendResponse = response.body().string();


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

                    mViewHolder.tvDataOraReportSent.setText("Rapporto inviato il: " + strDateTime);
                    notifyDataSetChanged();
                }
            });

            for (GeaImagineRapporto geaImagineRapporto : imagesArray)
            {
                callSendImagesList.add(networkUtils.sendImage(this, activity, geaImagineRapporto));
            }

            //mCommunicator.onSendReportReturned();
        }

        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                requestServerDialog.dismiss();
            }
        });
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

    private void disableInputAndShowProgressDialog()
    {
        mViewHolder.btnSendReportNow.setEnabled(false);
        mViewHolder.btnSendReportNow.setAlpha(.4f);

        requestServerDialog.show();
    }

    private void enableInput()
    {
        mViewHolder.btnSendReportNow.setEnabled(true);
        mViewHolder.btnSendReportNow.setAlpha(1.0f);

        requestServerDialog.dismiss();
    }

    class ViewHolder
    {
        Button btnSendReportNow;
        TextView tvVisitDay;
        TextView tvVisitMonth;
        TextView tvVisitTime;
        TextView clientNameTextView;
        TextView serviceTypeTextView;
        TextView clientAddressTextView;
        TextView tvReportCompletionState;
        TextView tvDataOraReportSent;
    }
}
