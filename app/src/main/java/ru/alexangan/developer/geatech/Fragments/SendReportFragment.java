package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.GeaImagineRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;
import ru.alexangan.developer.geatech.Models.GeaRapporto;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Adapters.ModelsMapping;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.SEND_DATA_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.tokenStr;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class SendReportFragment extends Fragment implements View.OnClickListener, Callback
{
    View rootView;
    private Communicator mCommunicator;

    private Button sendReport;
    private int selectedIndex;
    int idSopralluogo;
    int idRapportoSopralluogo;

    ReportStates reportStates;
    VisitItem visitItem;
    GeaSopralluogo geaSopralluogo;

    String reportSendResponse;
    Call callSendReport, callSendImage;
    Activity activity;

    public SendReportFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mCommunicator = (Communicator) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        if (getArguments() != null)
        {
            selectedIndex = getArguments().getInt("selectedIndex");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.send_report_fragment, container, false);

        sendReport = (Button) rootView.findViewById(R.id.btnSendReport);
        sendReport.setOnClickListener(this);

        visitItem = visitItems.get(selectedIndex);
        geaSopralluogo = visitItem.getGeaSopralluogo();
        ProductData productData = visitItem.getProductData();
        String productType = productData.getProductType();
        //int idProductType = productData.getIdProductType();
        idSopralluogo = geaSopralluogo.getId_sopralluogo();
        //idRapportoSopralluogo = idSopralluogo;

        Class modelClass = ModelsMapping.assignClassModel(productType);

        realm.beginTransaction();
        reportStates = realm.where(ReportStates.class).equalTo("id_sopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

        if (reportStates != null)
        {
            if (visitItem.getGeaSopralluogo().getId_sopralluogo() == reportStates.getId_sopralluogo()
                    && (reportStates.getGeneralInfoCompletionState() == 2 && reportStates.getReportCompletionState() == 3)
                    && reportStates.getPhotoAddedNumber() >= 1) //  && reportStates.getData_ora_invio_rapporto() == null
            {
                sendReport.setAlpha(1.0f);
                sendReport.setEnabled(true);
            } else
            {
                sendReport.setAlpha(.4f);
                sendReport.setEnabled(false);
            }


            String generalInfoCompletionState = reportStates.getGeneralInfoCompletionStateString(reportStates.getGeneralInfoCompletionState()).Value();
            String reportCompletionState = reportStates.getReportCompletionStateString(reportStates.getReportCompletionState()).Value();

            int photoAddedNumber = reportStates.getPhotoAddedNumber();
            String photoAddedNumberStr;

            if (photoAddedNumber == 0)
            {
                photoAddedNumberStr = reportStates.getPhotoAddedNumberString(photoAddedNumber).Value();
            } else
            {
                photoAddedNumberStr = photoAddedNumber + reportStates.getPhotoAddedNumberString(photoAddedNumber).Value();
            }

            TextView tvPhotosPresent = (TextView) rootView.findViewById(R.id.tvPhotosQuant);
            tvPhotosPresent.setText(photoAddedNumberStr);

            TextView tvGeneralInfo = (TextView) rootView.findViewById(R.id.tvGeneralInfo);
            tvGeneralInfo.setText(generalInfoCompletionState);

            TextView tvTecnicalReportState = (TextView) rootView.findViewById(R.id.tvTecnicalReportState);
            tvTecnicalReportState.setText(reportCompletionState);
        }

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btnSendReport)
        {
            if (reportStates != null)
            {
                sendReport.setAlpha(.4f);
                sendReport.setEnabled(false);

                realm.beginTransaction();
                ReportItem reportItem = new ReportItem();
                Gson gson = new Gson();

                GeaSopralluogo geaSopralluogo = realm.where(GeaSopralluogo.class).equalTo("id_sopralluogo", idSopralluogo).findFirst();
                GeaSopralluogo geaSopralluogoUnmanaged = realm.copyFromRealm(geaSopralluogo);
                reportItem.setGeaSopralluogo(geaSopralluogoUnmanaged);


                ReportStates reportStatesUnmanaged = realm.copyFromRealm(reportStates);
                String strReportStatesUnmanaged = gson.toJson(reportStatesUnmanaged);
                GeaRapporto gea_rapporto = gson.fromJson(strReportStatesUnmanaged, GeaRapporto.class);
                reportItem.setGea_rapporto_sopralluogo(gea_rapporto);


                RealmResults geaItemsRapporto = realm.where(GeaItemRapporto.class).equalTo("id_rapporto_sopralluogo", idSopralluogo).findAll();
                List<GeaItemRapporto> listGeaItemRapporto = new ArrayList<>();

                for(Object gi : geaItemsRapporto)
                {
                    GeaItemRapporto gi_unmanaged = realm.copyFromRealm((GeaItemRapporto)gi);
                    listGeaItemRapporto.add(gi_unmanaged);
                }
                reportItem.setGea_items_rapporto_sopralluogo(listGeaItemRapporto);


                RealmResults<GeaImagineRapporto> listReportImages = realm.where(GeaImagineRapporto.class).equalTo("id_rapporto_sopralluogo", idSopralluogo).findAll();

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

            mCommunicator.onSendReportReturned();

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
                        reportStates.setDataOraRaportoInviato(strDateTime);
                        realm.commitTransaction();
                    }
                });
            }

            mCommunicator.onSendReportReturned();
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
