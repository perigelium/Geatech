package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Adapters.ReportsSearchResultsListAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.GeaImmagineRapporto;
import ru.alexangan.developer.geatech.Models.GeaItemRapporto;
import ru.alexangan.developer.geatech.Models.GeaRapporto;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.GlobalConstants;
import ru.alexangan.developer.geatech.Models.ImgCallAttrs;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.ReportsSearchResultItem;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.JSON_to_model;
import ru.alexangan.developer.geatech.Utils.SwipeDetector;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.GET_VISITS_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.tokenStr;

public class FragReportsSearchResults extends ListFragment implements Callback
{
    private Communicator mCommunicator;
    SwipeDetector swipeDetector;
    List<ReportsSearchResultItem> reportsSearchResultItems;
    ReportsSearchResultsListAdapter myReportsSearchResultsAdapter;
    ListView lv;
    Activity activity;
    String strReportsSearchResultsJSON;
    private Handler handler;
    private Runnable runnable;
    private Call callGetReport;
    List<Call> callDownloadImagesList;
    private ProgressDialog requestServerDialog;
    AlertDialog alert;
    String image_base_url;
    List<ImgCallAttrs> imgCallAttrs;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mCommunicator = (Communicator) getActivity();
        activity = getActivity();

        handler = new Handler();

        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                showToastMessage(getString(R.string.ServerAnswerNotReceived));
                requestServerDialog.dismiss();
            }
        };

        if (getArguments() != null)
        {
            strReportsSearchResultsJSON = getArguments().getString("reportsSearchResultsJSON");
        }

        if (strReportsSearchResultsJSON != null)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(strReportsSearchResultsJSON);

                JSONArray arr_caseArray = jsonObject.getJSONArray("arr_case");
                Gson gson = new Gson();

                Type typeReportsSearchResults = new TypeToken<List<ReportsSearchResultItem>>()
                {
                }.getType();
                reportsSearchResultItems = gson.fromJson(String.valueOf(arr_caseArray), typeReportsSearchResults);

            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //mSettings.edit().putString("reportSearchLastQueryString", null).apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.list_visits_no_title, container, false);

        //tvListVisitsTodayDate = (TextView) rootView.findViewById(R.id.tvListVisitsTodayDate);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        requestServerDialog = new ProgressDialog(activity);
        requestServerDialog.setTitle("");
        requestServerDialog.setMessage(getString(R.string.DownloadingDataPleaseWait));
        requestServerDialog.setIndeterminate(true);

        if (reportsSearchResultItems != null)
        {
            myReportsSearchResultsAdapter =
                    new ReportsSearchResultsListAdapter(getActivity(), R.layout.list_search_results_repors_row, reportsSearchResultItems);
            setListAdapter(myReportsSearchResultsAdapter);

            lv = getListView();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    int id_rapporto_sopralluogo = reportsSearchResultItems.get(position).getId_rapporto_sopralluogo();

                    if (!NetworkUtils.isNetworkAvailable(activity))
                    {
                        mCommunicator.onSendReportReturned(id_rapporto_sopralluogo);
                    } else
                    {
                        int id_sopralluogo = reportsSearchResultItems.get(position).getId_sopralluogo();

                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        ReportItem reportItem = realm.where(ReportItem.class).equalTo("company_id", company_id).equalTo("tech_id", selectedTech.getId())
                                .equalTo("id_rapporto_sopralluogo", id_rapporto_sopralluogo)
                                .findFirst();
                        realm.commitTransaction();

                        if (reportItem != null)
                        {
                            mCommunicator.onSendReportReturned(id_rapporto_sopralluogo);
                        } else
                        {
                            NetworkUtils networkUtils = new NetworkUtils();
                            callGetReport = networkUtils.getData
                                    (FragReportsSearchResults.this, GET_VISITS_URL_SUFFIX, tokenStr, null, Integer.toString(id_sopralluogo), false);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();


    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        if (call == callGetReport)
        {
            showToastMessage(getString(R.string.ServerAnswerNotReceived));

            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    requestServerDialog.dismiss();
                }
            });

            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {
        if (call == callGetReport)
        {
            handler.removeCallbacks(runnable);

            final String reportSearchResultJSON = response.body().string();

            response.body().close();

            JSONObject jsonObject;

            try
            {
                jsonObject = new JSONObject(reportSearchResultJSON);
            } catch (JSONException e)
            {
                e.printStackTrace();
                return;
            }


            if (jsonObject.has("error"))
            {
                final String errorStr;

                try
                {
                    errorStr = jsonObject.getString("error");
                    if (errorStr.length() != 0)
                    {
                        showToastMessage(errorStr);
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            } else
            {
                // Parsing object
                try
                {
                    image_base_url = jsonObject.getString("image_base_url");

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                RealmList<VisitItem> visitItemsRList = JSON_to_model.getVisitTtemsList(reportSearchResultJSON);
                VisitItem visitItem = null;

                if (visitItemsRList != null && visitItemsRList.size() > 0)
                {
                    visitItem = visitItemsRList.get(0);
                }

                // Create report

                if (visitItem != null)
                {
                    GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
                    ClientData clientData = visitItem.getClientData();
                    GeaRapporto geaRapporto = visitItem.getGeaRapporto();
                    int tech_id = geaSopralluogo.getId_tecnico();
                    int idSopralluogo = geaSopralluogo.getId_sopralluogo();
                    final int id_rapporto_sopralluogo = geaRapporto.getId_rapporto_sopralluogo();
                    RealmList<GeaItemRapporto> rl_ItemsRapportoSopralluogo = visitItem.getGea_items_rapporto_sopralluogo();
                    RealmList<GeaImmagineRapporto> rl_ImaginesRapportoSopralluogo = visitItem.getGea_immagini_rapporto_sopralluogo();

                    Realm realm = Realm.getDefaultInstance();

                    realm.beginTransaction();
                    ReportItem reportItem = new ReportItem(company_id, tech_id,
                            idSopralluogo, id_rapporto_sopralluogo,
                            new ReportStates(ReportStates.GENERAL_INFO_DATETIME_AND_COORDS_SET),
                            geaSopralluogo, clientData, geaRapporto,
                            rl_ItemsRapportoSopralluogo,
                            rl_ImaginesRapportoSopralluogo);

                    realm.copyToRealm(reportItem);
                    realm.commitTransaction();

                    callDownloadImagesList = new ArrayList<>();
                    imgCallAttrs = new ArrayList<>();
                    NetworkUtils networkUtils = new NetworkUtils();

                    for (int i = 0; i < rl_ImaginesRapportoSopralluogo.size(); i++)
                    {
                        String imageName = rl_ImaginesRapportoSopralluogo.get(i).getNome_file();
                        String pathSuffix = Integer.toString(id_rapporto_sopralluogo) + "/" + imageName;

                        callDownloadImagesList.add(networkUtils.downloadURL(this, image_base_url + pathSuffix));

                        imgCallAttrs.add(new ImgCallAttrs(callDownloadImagesList.get(callDownloadImagesList.size() - 1), idSopralluogo, imageName));
                    }

                    activity.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            mCommunicator.onSendReportReturned(id_rapporto_sopralluogo);
                        }
                    });
                }
            }
        } else
        {
            for (int i = 0; i < callDownloadImagesList.size(); i++)
            {
                if (call == callDownloadImagesList.get(i))
                {
                    String reportSendResponse = response.body().string();

                    response.body().close();

                    for (int j = 0; j < callDownloadImagesList.size(); j++)
                    {
                        if (call == imgCallAttrs.get(i).getCall())
                        {
                            int id_sopralluogo = imgCallAttrs.get(i).getId_sopralluogo();
                            String fileName = imgCallAttrs.get(i).getFileName();

                            String photosFolderName = "photos" + id_sopralluogo;

                            File photosDir = new File(activity.getFilesDir(), photosFolderName);

                            if (!photosDir.exists())
                            {
                                photosDir.mkdir();
                            }

                            Writer output;
                            File file = new File(photosDir, fileName);
                            output = new BufferedWriter(new FileWriter(file));
                            output.write(reportSendResponse);
                            output.close();

                            break;
                        }
                    }

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

/*    private void showToastMessage(final String msg)
    {
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}

/*            int optionId = randomInteger!=0 ? R.layout.list_visits_cell_datetime_set : R.layout.list_visits_cell_datetime_set;
            View C = inflater.inflate(optionId, parent, false);
            int index = parent.indexOfChild(C);
            parent.addView(C, index);*/