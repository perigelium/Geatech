package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Adapters.CustomSpinnerAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ClientData;
import ru.alexangan.developer.geatech.Models.GlobalConstants;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.Models.ReportsSearchResultItem;
import ru.alexangan.developer.geatech.Models.SpinnerItemData;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.LIST_VISITS_MODE_ALL;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.SEARCH_VISITS_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.tokenStr;

/**
 * Created by user on 11/10/2016
 */

public class NotificationBarFragment extends Fragment implements SearchView.OnQueryTextListener, Callback
{
    private Communicator mCommunicator;
    private Activity activity;
    Spinner spVisitsFilter;
    private TextView tvWindowTitle;
    private ImageView ivLogoSmall;
    private ImageView ivVisitsListsFilter;
    int spinnerCurItem;
    private SearchView searchViewReports;
    private Handler handler;
    private Runnable runnable;
    private Call callSearchReports;
    private ProgressDialog requestServerDialog;
    AlertDialog alert;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mCommunicator = (Communicator) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.notification_bar, container, false);

        String[] listItemsArray = {"Tutti i sopralluoghi", "I miei sopralluoghi", "Sopralluoghi da fissare"};
        Integer[] icons = new Integer[]{R.drawable.transparent21px, R.drawable.transparent21px, R.drawable.transparent21px};

        tvWindowTitle = (TextView) rootView.findViewById(R.id.tvWindowTitle);
        ivLogoSmall = (ImageView) rootView.findViewById(R.id.ivLogoSmall);
        ivVisitsListsFilter = (ImageView) rootView.findViewById(R.id.ivVisitsListsFilter);
        searchViewReports = (SearchView) rootView.findViewById(R.id.searchViewReports);

        searchViewReports.setSubmitButtonEnabled(true);
        searchViewReports.setIconifiedByDefault(true);
        searchViewReports.setOnQueryTextListener(this);

        spVisitsFilter = (Spinner) rootView.findViewById(R.id.spVisitsFilter);

        ArrayList<SpinnerItemData> list = new ArrayList<>();

        for (int i = 0; i < listItemsArray.length; i++)
        {
            list.add(new SpinnerItemData(listItemsArray[i], icons[i]));
        }
        SpinnerAdapter adapter =
                new CustomSpinnerAdapter(activity, R.layout.alert_visits_filter_item_custom, R.id.tvVisitsFilterDialogItem, list);
        spVisitsFilter.setAdapter(adapter);

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

        spVisitsFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                if (spinnerCurItem == position)
                {
                    return;
                }
                mSettings.edit().putInt("listVisitsFilterMode", position).apply();

                mCommunicator.onListVisitsDisplayModeSelected(position);

                spinnerCurItem = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    @Override
    public View getView()
    {
        return super.getView();
    }

    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        //searchViewReports.setQuery("", false);
    }

    public void setView
            (int tvWindowTitleString, int ivLogoSmallVisibility,
             int ivVisitsListsFilterVisibility, int searchBarVisibility, boolean iconifySearchView)
    {
        tvWindowTitle.setText(tvWindowTitleString);
        ivLogoSmall.setVisibility(ivLogoSmallVisibility);
        ivVisitsListsFilter.setVisibility(ivVisitsListsFilterVisibility);
        searchViewReports.setVisibility(searchBarVisibility);

        if(searchViewReports.getVisibility() == View.VISIBLE && iconifySearchView)
        {
            searchViewReports.setIconified(true);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String queryString)
    {
        mSettings.edit().putString("reportSearchLastQueryString", queryString).commit();

        Realm realm = Realm.getDefaultInstance();

        String strQueryLower = queryString.toLowerCase();

        if (!NetworkUtils.isNetworkAvailable(activity))
        {
            showToastMessage(getString(R.string.OfflineModeSeekInLocalReportsOnly));

            realm.beginTransaction();
            RealmResults<ReportItem> reportItems = realm.where(ReportItem.class).equalTo("company_id", company_id)
                    .equalTo("tech_id", selectedTech.getId())
                    .findAll();
            realm.commitTransaction();

            List<ReportsSearchResultItem> reportsSearchResultItems = new ArrayList<>();

            for (ReportItem reportItem : reportItems)
            //for (int i = 0; i < visitItems.size(); i++)
            {
                boolean isReportSent;
                isReportSent = reportItem.getGea_rapporto_sopralluogo().getData_ora_invio_rapporto() != null;

                if (isReportSent)
                {
                    ClientData clientData = reportItem.getClientData();
                    String clientName = clientData.getName();
                    String clientMobile = clientData.getMobile();
                    String clientPhone = clientData.getPhone();
                    String clientAddress = clientData.getAddress();
                    String data_ora_sopralluogo = reportItem.getGeaSopralluogo().getData_ora_sopralluogo();

                    if(clientName.toLowerCase().contains(strQueryLower) || clientMobile.toLowerCase().contains(strQueryLower)
                            || clientPhone.toLowerCase().contains(strQueryLower) || clientAddress.toLowerCase().contains(strQueryLower)
                            || data_ora_sopralluogo.toLowerCase().contains(strQueryLower))
                    {
                        int id_sopralluogo = reportItem.getId_sopralluogo();
                        int id_rapporto_sopralluogo = reportItem.getId_rapporto_sopralluogo();
                        String productType = clientData.getProduct_type();


                        ReportsSearchResultItem reportsSearchResultItem = new ReportsSearchResultItem
                                (id_sopralluogo, id_rapporto_sopralluogo, clientName, clientMobile, clientPhone, clientAddress, productType, data_ora_sopralluogo);
                        reportsSearchResultItems.add(reportsSearchResultItem);
                    }
                }
            }

            Gson gson = new Gson();

            String listString = gson.toJson(reportsSearchResultItems, new TypeToken<ArrayList<ReportsSearchResultItem>>() {}.getType());

            try
            {
                JSONArray jsonArray = new JSONArray(listString);

                JSONObject jsonObject = new JSONObject();
                try
                {
                    jsonObject.put("arr_case", jsonArray);

                    mCommunicator.showReportsSearchResults(jsonObject.toString());

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            return true;
        } else if (tokenStr == null)
        {
            alertDialog("Info", getString(R.string.OfflineModeShowLoginScreenQuestion));
            return true;
        }

        NetworkUtils networkUtils = new NetworkUtils();
        callSearchReports = networkUtils.getData(this, SEARCH_VISITS_URL_SUFFIX, tokenStr, queryString, null, false);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
/*        if (TextUtils.isEmpty(newText))
        {
            //mListView.clearTextFilter();
        } else
        {
            //mListView.setFilterText(newText.toString());
        }*/
        return false;
    }

    @Override
    public void onFailure(Call call, IOException e)
    {
        if (call == callSearchReports)
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
        if (call == callSearchReports)
        {
            handler.removeCallbacks(runnable);

            final String reportsSearchResultsJSON = response.body().string();

            response.body().close();

            JSONObject jsonObject;

            try
            {
                jsonObject = new JSONObject(reportsSearchResultsJSON);
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
                GlobalConstants.lastSearchResults = reportsSearchResultsJSON;

                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        mCommunicator.showReportsSearchResults(reportsSearchResultsJSON);
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
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        });
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
