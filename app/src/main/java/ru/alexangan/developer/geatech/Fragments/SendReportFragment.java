package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
import ru.alexangan.developer.geatech.Models.ProductData;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.SEND_DATA_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.listVisitsIsObsolete;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.tokenStr;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class SendReportFragment extends Fragment implements View.OnClickListener, Callback
{
    View rootView;
    private Communicator mCommunicator;

    private Button btnSendReportNow;
    private int selectedIndex;

    ReportStates reportStates;

    String reportSendResponse;
    Call callSendReport, callSendImage;
    List<Call> callSendImagesList;
    Activity activity;
    NetworkUtils networkUtils;
    List<GeaImagineRapporto> imagesArray;
    private ProgressDialog requestServerDialog;
    int objectsSentSuccessfully;
    AlertDialog alert;
    int id_rapporto_sopralluogo;

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

        callSendImagesList = new ArrayList<>();
        networkUtils = new NetworkUtils();

        requestServerDialog = new ProgressDialog(activity);
        requestServerDialog.setTitle("");
        requestServerDialog.setMessage(getString(R.string.TransmittingDataPleaseWait));
        requestServerDialog.setIndeterminate(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.send_report_fragment, container, false);

        btnSendReportNow = (Button) rootView.findViewById(R.id.btnSendReport);
        btnSendReportNow.setOnClickListener(this);

        VisitItem visitItem = visitItems.get(selectedIndex);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        ProductData productData = visitItem.getProductData();
        String productType = productData.getProductType();
        //int idProductType = productData.getIdProductType();
        int idSopralluogo = geaSopralluogo.getId_sopralluogo();
        //idRapportoSopralluogo = idSopralluogo;

        //Class modelClass = ModelsMapping.assignClassModel(productType);

        realm.beginTransaction();
        reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();
        realm.commitTransaction();

        if (reportStates != null)
        {
            if (visitItem.getGeaSopralluogo().getId_sopralluogo() == reportStates.getId_sopralluogo()
                    && (reportStates.getGeneralInfoCompletionState() == ReportStates.COORDS_SET
                    && reportStates.getReportCompletionState() == ReportStates.REPORT_COMPLETED)
                    && reportStates.getPhotoAddedNumber() >= 3) //  && reportStates.getData_ora_invio_rapporto() == null
            {
                btnSendReportNow.setAlpha(1.0f);
                btnSendReportNow.setEnabled(true);
            } else
            {
                btnSendReportNow.setAlpha(.4f);
                btnSendReportNow.setEnabled(false);
            }

            int reportCompletionState = reportStates.getReportCompletionState();

            String generalInfoCompletionState = reportStates.getGeneralInfoCompletionStateString(reportStates.getGeneralInfoCompletionState()).Value();
            String strReportCompletionState = reportStates.getReportCompletionStateString(reportCompletionState).Value();

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
            tvTecnicalReportState.setText(strReportCompletionState);
        }

        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btnSendReport)
        {
            if (!NetworkUtils.isNetworkAvailable(activity))
            {
                showToastMessage(getString(R.string.CheckInternetConnection));
                return;
            }
            disableInputAndShowProgressDialog();

            objectsSentSuccessfully = 0;
            sendReportItem(selectedIndex);
        }
    }

    private void sendReportItem(int selectedIndex)
    {
        realm.beginTransaction();

        ReportItem reportItem = new ReportItem();
        Gson gson = new Gson();

        VisitItem visitItem = visitItems.get(selectedIndex);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        ProductData productData = visitItem.getProductData();
        String productType = productData.getProductType();
        //int idProductType = productData.getIdProductType();
        int idSopralluogo = geaSopralluogo.getId_sopralluogo();

        GeaSopralluogo geaSopralluogoUnmanaged = realm.copyFromRealm(geaSopralluogo);
        reportItem.setGeaSopralluogo(geaSopralluogoUnmanaged);

        ReportStates reportStates = realm.where(ReportStates.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).findFirst();
        id_rapporto_sopralluogo = reportStates.getId_rapporto_sopralluogo();

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

        //Log.d("DEBUG", str_ReportItem_json);

        callSendReport = networkUtils.sendData(this, SEND_DATA_URL_SUFFIX, tokenStr, str_ReportItem_json);
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        if (call == callSendReport)
        {
            showToastMessage(getString(R.string.SendingReportFailed));
        }

        if (call == callSendImage)
        {
            showToastMessage(getString(R.string.SendingReportFailed));
        }

        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                enableInput();
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        if (call == callSendReport)
        {
            reportSendResponse = response.body().string();

            response.body().close();

            JSONObject jsonObject;

            try
            {
                jsonObject = new JSONObject(reportSendResponse);

                if (jsonObject.has("error"))
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            enableInput();
                            alertDialog("Info", getString(R.string.OfflineModeShowLoginScreenQuestion));
                        }
                    });

                    try
                    {
                        String errorStr = jsonObject.getString("error");
                        if (errorStr.length() != 0)
                        {
                            //showToastMessage(errorStr);
                        }

                    } catch (JSONException e)
                    {
                        activity.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                enableInput();
                            }
                        });
                        e.printStackTrace();
                        return;
                    }
                } else
                {
                    for (GeaImagineRapporto geaImagineRapporto : imagesArray)
                    {
                        callSendImagesList.add(networkUtils.sendImage(this, activity, geaImagineRapporto));
                    }
                }

            } catch (JSONException e)
            {
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        enableInput();
                    }
                });
                e.printStackTrace();
                showToastMessage("JSON parse error");
            }

/*            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(activity, R.string.ReportSent + ", server ritornato: " + reportSendResponse, Toast.LENGTH_LONG).show(); //
                }
            });*/


            //mCommunicator.onSendReportReturned();
        }

        for (int i = 0; i < callSendImagesList.size(); i++)
        {
            if (call == callSendImagesList.get(i))
            {
                reportSendResponse = response.body().string();

                response.body().close();

                JSONObject jsonObject;

                try
                {
                    jsonObject = new JSONObject(reportSendResponse);

                    if (jsonObject.has("success"))
                    {

                        final String strSuccess ;
                        try
                        {
                            strSuccess = jsonObject.getString("success");

                            if (strSuccess.equals("0"))
                            {

                            }
                            else
                            {
                                objectsSentSuccessfully++;

                                if (objectsSentSuccessfully != 0 && objectsSentSuccessfully == callSendImagesList.size())
                                {
                                    activity.runOnUiThread(new Runnable()
                                    {
                                        public void run()
                                        {
                                            Calendar calendarNow = Calendar.getInstance();
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                                            String strDateTime = sdf.format(calendarNow.getTime());

                                            realm.beginTransaction();
                                            //sentVisitItems.add(visitItems.get(selectedIndex));
                                            reportStates.setData_ora_invio_rapporto(strDateTime);
                                            realm.commitTransaction();

                                            requestServerDialog.dismiss();

                                            Toast.makeText(activity, R.string.ReportSent, Toast.LENGTH_LONG).show();

                                            listVisitsIsObsolete = true;
                                            mCommunicator.onSendReportReturned(id_rapporto_sopralluogo);
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e)
                        {
                            activity.runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    enableInput();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e)
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            enableInput();
                        }
                    });
                    e.printStackTrace();
                }

                break;

                //showToastMessage("Immagine " + i + " inviato"); //, server ritorna: " + reportSendResponse
                //Log.d("DEBUG", "image " + i + ", server returned:" + reportSendResponse);
            }
        }
    }

    private void showToastMessage(final String msg)
    {
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void disableInputAndShowProgressDialog()
    {
        btnSendReportNow.setEnabled(false);
        btnSendReportNow.setAlpha(.4f);

        requestServerDialog.show();
    }

    private void enableInput()
    {
        btnSendReportNow.setEnabled(true);
        btnSendReportNow.setAlpha(1.0f);

        requestServerDialog.dismiss();
    }

    private void alertDialog(String title, String message)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("Si",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                mCommunicator.onLogoutCommand();
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                alert.dismiss();
                            }
                        });

        alert = builder.create();

        alert.show();
    }
}
