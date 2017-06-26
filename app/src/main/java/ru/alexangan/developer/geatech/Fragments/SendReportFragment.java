package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.GeaImmagineRapporto;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.SEND_DATA_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.tokenStr;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitsListIsObsolete;

public class SendReportFragment extends Fragment implements View.OnClickListener, Callback
{
    View rootView;
    private Communicator mCommunicator;

    private int selectedVisitId;
    ReportItem reportItem;

    String reportSendResponse;
    Call callSendReport;
    List<Call> callSendImagesList;
    Activity activity;
    NetworkUtils networkUtils;
    List<GeaImmagineRapporto> imagesArray;
    private ProgressDialog requestServerDialog;
    int objectsSentSuccessfully;
    AlertDialog alert;
    int id_rapporto_sopralluogo;
    private boolean reportComplete;

    private Button btnSendReportNow;
    private ImageView ivCoordsSetCheckmark, ivReportFilledCheckmark, ivPhotosAddedCheckmark;
    private TextView tvGeneralInfo, tvTecnicalReportState, tvPhotosPresent;

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
            selectedVisitId = getArguments().getInt("selectedVisitId");
        }

        callSendImagesList = new ArrayList<>();
        networkUtils = new NetworkUtils();

        requestServerDialog = new ProgressDialog(activity);
        requestServerDialog.setTitle("");
        requestServerDialog.setMessage(getString(R.string.TransmittingDataPleaseWait));
        requestServerDialog.setIndeterminate(false);
        requestServerDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        reportComplete = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.sync_report_fragment, container, false);

        btnSendReportNow = (Button) rootView.findViewById(R.id.btnSendReport);
        btnSendReportNow.setOnClickListener(this);

        ivCoordsSetCheckmark = (ImageView) rootView.findViewById(R.id.ivCoordsSetCheckmark);
        ivReportFilledCheckmark = (ImageView) rootView.findViewById(R.id.ivReportFilledCheckmark);
        ivPhotosAddedCheckmark = (ImageView) rootView.findViewById(R.id.ivPhotosAddedCheckmark);

        tvGeneralInfo = (TextView) rootView.findViewById(R.id.tvGeneralInfo);
        tvTecnicalReportState = (TextView) rootView.findViewById(R.id.tvTecnicalReportState);
        tvPhotosPresent = (TextView) rootView.findViewById(R.id.tvPhotosQuant);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        VisitItem visitItem = visitItems.get(selectedVisitId);
        GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
        int idSopralluogo = geaSopralluogo.getId_sopralluogo();
        id_rapporto_sopralluogo = visitItem.getGeaRapporto().getId_rapporto_sopralluogo();

        //Class modelClass = ModelsMapping.assignClassModel(productType);

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                .equalTo("id_sopralluogo", idSopralluogo).equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo).findFirst();
        realm.commitTransaction();
        realm.close();

        if (reportItem != null)
        {
            int generalInfoCompletionState = reportItem.getReportStates().getGeneralInfoCompletionState();
            int reportCompletionState = reportItem.getReportStates().getReportCompletionState();
            int photosAddedNumber = reportItem.getReportStates().getPhotosAddedNumber();

            reportComplete = generalInfoCompletionState == ReportStates.GENERAL_INFO_DATETIME_AND_COORDS_SET
                    && reportCompletionState == ReportStates.REPORT_COMPLETED
                    && photosAddedNumber >= ReportStates.PHOTOS_MIN_ADDED;

            if (generalInfoCompletionState == ReportStates.GENERAL_INFO_DATETIME_AND_COORDS_SET)
            {
                ivCoordsSetCheckmark.setImageResource(R.drawable.green_filter_checkmark);
            }

            if (reportCompletionState == ReportStates.REPORT_COMPLETED)
            {
                ivReportFilledCheckmark.setImageResource(R.drawable.green_filter_checkmark);
            }

            if (photosAddedNumber >= ReportStates.PHOTOS_MIN_ADDED)
            {
                ivPhotosAddedCheckmark.setImageResource(R.drawable.green_filter_checkmark);
            }

            String strPhotosAddedNumber = photosAddedNumber == 0 ? reportItem.getReportStates().getPhotoAddedNumberString(photosAddedNumber).Value()
                    : photosAddedNumber + reportItem.getReportStates().getPhotoAddedNumberString(photosAddedNumber).Value();

            tvPhotosPresent.setText(strPhotosAddedNumber);


            tvGeneralInfo.setText(reportItem.getReportStates().getGeneralInfoCompletionStateString().Value());

            tvTecnicalReportState.setText(reportItem.getReportStates().getReportCompletionStateString().Value());
        }

        if (reportComplete)
        {
            btnSendReportNow.setBackgroundResource(R.drawable.button_green);
            btnSendReportNow.setTextColor(Color.parseColor("#ffffffff"));
        } else
        {
            btnSendReportNow.setBackgroundResource(R.drawable.button_transparent_grey_border);
            btnSendReportNow.setTextColor(Color.parseColor("#ff666666"));
        }
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btnSendReport)
        {
            if (reportComplete && reportItem != null)
            {
                if (!NetworkUtils.isNetworkAvailable(activity))
                {
                    showToastMessage(getString(R.string.CheckInternetConnection));
                    return;
                }

                if (tokenStr == null)
                {
                    alertDialog("Info", getString(R.string.OfflineModeShowLoginScreenQuestion));
                }

                disableInputAndShowProgressDialog();

                objectsSentSuccessfully = 0;

                sendReportItem();

            } else
            {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                reportItem.getReportStates().setTriedToSendReport(true);
                realm.commitTransaction();
                realm.close();

                showToastMessage("Rapporto non ancora completato.");
            }
        }
    }

    private void sendReportItem()
    {
        Gson gson = new Gson();

/*        Calendar calendarNow = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String strDateTime = sdf.format(calendarNow.getTime());
        reportItem.setData_ora_invio_rapporto(strDateTime);*/

        Realm realm = Realm.getDefaultInstance();
        ReportItem reportItemUnmanaged = realm.copyFromRealm(reportItem);
        ReportItem reportItemExRealm = realm.copyFromRealm(reportItem);

        realm.beginTransaction();
        reportItemUnmanaged.setReportStates(null);
        reportItemUnmanaged.setClientData(null);
        realm.commitTransaction();

        realm.beginTransaction();

        imagesArray = new ArrayList<>();

        for (int i = 0; i < reportItemExRealm.getGea_immagini_rapporto_sopralluogo().size(); i++)
        {
            imagesArray.add(reportItemExRealm.getGea_immagini_rapporto_sopralluogo().get(i));

/*            File file = new File(reportItemUnmanaged.getGea_immagini_rapporto_sopralluogo().get(i).getFilePath());
            long fileLength = file.length();

            Log.d("DEBUG", String.valueOf(fileLength));*/
        }
        realm.commitTransaction();
        realm.close();

        requestServerDialog.setMax(100);

        realm.beginTransaction();

        for (int i = 0; i < reportItemUnmanaged.getGea_immagini_rapporto_sopralluogo().size(); i++)
        {
            reportItemUnmanaged.getGea_immagini_rapporto_sopralluogo().get(i).setId_immagine_rapporto(0);
            reportItemUnmanaged.getGea_immagini_rapporto_sopralluogo().get(i).setFilePath(null);
        }

        realm.commitTransaction();

        String str_ReportItem_json = gson.toJson(reportItemUnmanaged);

        //Log.d("DEBUG", str_ReportItem_json);

        callSendReport = networkUtils.sendData(this, SEND_DATA_URL_SUFFIX, tokenStr, str_ReportItem_json);
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
                    enableInput();
                }
            });
            showToastMessage(getString(R.string.SendingReportFailed));
        }

        for (int i = 0; i < callSendImagesList.size(); i++)
        {
            if (call == callSendImagesList.get(i))
            {
                showToastMessage("Invio immagine " + (i + 1) + " di " + callSendImagesList.size() + " non riuscito");

                if (i == callSendImagesList.size() - 1)
                {
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            enableInput();
                        }
                    });
                }
                break;
            }
        }
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
                    try
                    {
                        String errorStr = jsonObject.getString("error");
                        if (errorStr.length() != 0 && errorStr.contains("Empty token"))
                        {
                            activity.runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    enableInput();
                                    alertDialog("Info", getString(R.string.OfflineModeShowLoginScreenQuestion));
                                }
                            });
                        } else
                        {
                            showToastMessage(errorStr);
                            //showToastMessage("Error in sent data");
                        }
                    } catch (JSONException e)
                    {
                        showToastMessage(getString(R.string.DatabaseError));

                        e.printStackTrace();
                    }

                    activity.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            enableInput();
                        }
                    });
                } else
                {
                    callSendImagesList.clear();

                    JSONArray json_arImages = (JSONArray) jsonObject.get("images");

                    ArrayList<String> alImages = new ArrayList<>();

                    if (json_arImages != null)
                    {
                        for (int i = 0; i < json_arImages.length(); i++)
                        {
                            alImages.add(json_arImages.getString(i));
                        }
                    }

                    for (int i = 0; i < imagesArray.size(); i++)
                    {
                        String imageName = imagesArray.get(i).getNome_file();

                        if (!alImages.contains(imageName))
                        {
                            callSendImagesList.add(networkUtils.sendImage(this, activity, imagesArray.get(i)));
                        } else
                        {
                            objectsSentSuccessfully++;
                        }
                    }

                    if (objectsSentSuccessfully == imagesArray.size())
                    {
                        Calendar calendarNow = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);
                        String strDateTime = sdf.format(calendarNow.getTime());

                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        reportItem.getGea_rapporto_sopralluogo().setData_ora_invio_rapporto(strDateTime);
                        realm.commitTransaction();
                        realm.close();

                        Toast.makeText(activity, R.string.ReportSent, Toast.LENGTH_SHORT).show();

                        visitsListIsObsolete = true;

                        activity.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                requestServerDialog.dismiss();
                                mCommunicator.showDetailedReport(id_rapporto_sopralluogo);
                            }
                        });
                    } else
                    {
                        showToastMessage(getString(R.string.SendingReportFailed));
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
                showToastMessage("JSON Error in report response");
            }

/*            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(activity, R.string.ReportSent + ", server ritornato: " + reportSendResponse, Toast.LENGTH_SHORT).show(); //
                }
            });*/


            //mCommunicator.showDetailedReport();
        } else
        {
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
                            final String strSuccess;
                            try
                            {
                                strSuccess = jsonObject.getString("success");

                                if (!strSuccess.equals("0"))
                                {
                                    objectsSentSuccessfully++;
                                    requestServerDialog.setProgress(objectsSentSuccessfully * 100 / imagesArray.size());

                                    //showToastMessage("immagine " + (i + 1) + " di " + callSendImagesList.size() + " inviato");

                                    if (i == callSendImagesList.size() - 1)
                                    {
                                        activity.runOnUiThread(new Runnable()
                                        {
                                            public void run()
                                            {
                                                enableInput();
                                            }
                                        });
                                    }

                                    if (objectsSentSuccessfully == imagesArray.size())
                                    {
                                        activity.runOnUiThread(new Runnable()
                                        {
                                            public void run()
                                            {
                                                Calendar calendarNow = Calendar.getInstance();
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                                                String strDateTime = sdf.format(calendarNow.getTime());

                                                if (objectsSentSuccessfully == imagesArray.size())
                                                {
                                                    Realm realm = Realm.getDefaultInstance();
                                                    realm.beginTransaction();
                                                    reportItem.getGea_rapporto_sopralluogo().setData_ora_invio_rapporto(strDateTime);
                                                    realm.commitTransaction();
                                                    realm.close();

                                                    Toast.makeText(activity, R.string.ReportSent, Toast.LENGTH_SHORT).show();

                                                    visitsListIsObsolete = true;
                                                } else
                                                {
                                                    showToastMessage(getString(R.string.SendingReportFailed));
                                                }
                                                requestServerDialog.dismiss();
                                                mCommunicator.showDetailedReport(id_rapporto_sopralluogo);
                                            }
                                        });
                                    }
                                } else
                                {
                                    showToastMessage("Invio immagine " + (i + 1) + "non riuscito, Server ritorna: " + reportSendResponse);

                                    if (i == callSendImagesList.size() - 1)
                                    {
                                        activity.runOnUiThread(new Runnable()
                                        {
                                            public void run()
                                            {
                                                enableInput();
                                            }
                                        });
                                    }
                                }
                            } catch (JSONException e)
                            {
                                showToastMessage("Immagine JSONException");
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
                        showToastMessage("Immagine JSONException");
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

        requestServerDialog.setProgress(0);
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
