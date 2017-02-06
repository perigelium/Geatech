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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.RealmObject;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ClimaReportModel;
import ru.alexangan.developer.geatech.Models.ImageReport;
import ru.alexangan.developer.geatech.Models.Gea_rapporto_sopralluogo;
import ru.alexangan.developer.geatech.Adapters.ModelsMapping;
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.VisitStates;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Activities.LoginActivity.realm;
import static ru.alexangan.developer.geatech.Activities.MainActivity.visitItems;

public class SendReportFragment extends Fragment implements View.OnClickListener, Callback
{
    View rootView;
    private Communicator mCommunicator;

    private Button sendReport;
    private int selectedIndex;

    ReportStates reportStates;
    VisitItem visitItem;
    VisitStates visitStates;

    String reportSendResponse;
    Call callSendReport, callSendImage;
    Activity activity;
    RealmObject modelReport;
    RealmResults<ImageReport> gea_immagini;

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
        visitStates = visitItem.getVisitStates();
        ProductData productData = visitItem.getProductData();
        String productType = productData.getProductType();
        int idSopralluogo = visitStates.getIdSopralluogo();

        Class modelClass = ModelsMapping.assignClassModel(productType);

        realm.beginTransaction();
        reportStates = realm.where(ReportStates.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        modelReport = (RealmObject) realm.where(modelClass).equalTo("idSopralluogo", idSopralluogo).findFirst();
        RealmResults<ImageReport> gea_immagini = realm.where(ImageReport.class).equalTo("idSopralluogo", idSopralluogo).findAll();
        realm.commitTransaction();

        if (reportStates != null)
        {
            if (visitItem.getVisitStates().getIdSopralluogo() == reportStates.getIdSopralluogo()
                    && (reportStates.getGeneralInfoCompletionState() == 2 && reportStates.getReportCompletionState() == 3)
                    && reportStates.getPhotoAddedNumber() >= 1) //  && reportStates.getDataOraRaportoInviato() == null
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

                ReportStates reportStatesUnmanaged = realm.copyFromRealm(reportStates);
                VisitStates visitStatesUnmanaged = realm.copyFromRealm(visitStates);
                //ImageReport imageReportUnmanaged = realm.copyFromRealm(imageReport);
                Object modelReportUnmanaged = realm.copyFromRealm(modelReport);

                Gson gsonTmp = new Gson();
                String tmp = gsonTmp.toJson(reportStatesUnmanaged);
                Gea_rapporto_sopralluogo gea_rapporto_sopralluogo = gsonTmp.fromJson(tmp, Gea_rapporto_sopralluogo.class);

                Gson gson = new Gson();
                String gea_rapporto_sopralluogo_json = gson.toJson(gea_rapporto_sopralluogo);
                String visitStatesUnmanaged_json = gson.toJson(visitStatesUnmanaged);
                String modelReportUnmanaged_json = gson.toJson(modelReportUnmanaged);
                //String gea_immagineUnmanaged_json = gson.toJson(imageReportUnmanaged);

                JSONObject jsonObject = new JSONObject();
                try
                {
                    jsonObject.put("gea_rapporto_sopralluogo", gea_rapporto_sopralluogo_json);
                    jsonObject.put("gea_sopralluoghi", visitStatesUnmanaged_json);
                    jsonObject.put("gea_rapporto_modello", modelReportUnmanaged_json);
                    //jsonObject.put("gea_immagine_rapporto_sopralluogo", gea_immagineUnmanaged_json);

                } catch (JSONException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Log.d("DEBUG", String.valueOf(jsonObject));

                for (ImageReport imageReport : gea_immagini)
                {

                    NetworkUtils networkUtils = new NetworkUtils();
                    callSendReport = networkUtils.sendReport(this, String.valueOf(jsonObject));


                    callSendImage = networkUtils.sendImage(this, activity, imageReport);
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
            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(activity, "Invio rapporto fallito", Toast.LENGTH_LONG).show();
                }
            });
        }

        if (call == callSendImage)
        {
            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(activity, "Invio immagine fallito", Toast.LENGTH_LONG).show();
                }
            });
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
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(activity, "Immagine inviato, risposta non ha ricevuto", Toast.LENGTH_LONG).show();
                    }
                });

                return;
            } else
            {
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(activity, "Immagine inviato, server ritorna: " + reportSendResponse, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        if (call == callSendReport)
        {
            reportSendResponse = response.body().string();

            if (reportSendResponse == null)
            {
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(activity, "Rapporto inviato, risposta non ha ricevuto", Toast.LENGTH_LONG).show();
                    }
                });

                return;
            } else
            {
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(activity, "Rapporto inviato, server ritorna: " + reportSendResponse , Toast.LENGTH_LONG).show();

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
}
