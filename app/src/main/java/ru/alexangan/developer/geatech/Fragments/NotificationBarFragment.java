package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.alexangan.developer.geatech.Adapters.CustomSpinnerAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.SpinnerItemData;
import ru.alexangan.developer.geatech.Network.NetworkUtils;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.SEARCH_VISITS_URL_SUFFIX;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;
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

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        requestServerDialog = new ProgressDialog(activity);
        requestServerDialog.setTitle("");
        requestServerDialog.setMessage(getString(R.string.DownloadingDataPleaseWait));
        requestServerDialog.setIndeterminate(true);

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
        searchViewReports.setOnQueryTextListener(this);
        searchViewReports.setVisibility(View.VISIBLE);

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

                mCommunicator.onNotificationReportReturned(position);

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
    }

    public void setView(int tvWindowTitleString, int ivLogoSmallVisibility, int ivVisitsListsFilterVisibility, int searchBarVisibility)
    {
        tvWindowTitle.setText(tvWindowTitleString);
        ivLogoSmall.setVisibility(ivLogoSmallVisibility);
        ivVisitsListsFilter.setVisibility(ivVisitsListsFilterVisibility);
        searchViewReports.setVisibility(searchBarVisibility);
    }

    @Override
    public boolean onQueryTextSubmit(String queryString)
    {
        if (NetworkUtils.isNetworkAvailable(activity))
        {
            NetworkUtils networkUtils = new NetworkUtils();
            callSearchReports = networkUtils.getData(this, SEARCH_VISITS_URL_SUFFIX, tokenStr, queryString, false);
        }
        return false;
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
            showToastMessage(getString(R.string.ListVisitsReceiveFailed));

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

            final String visitsJSONData = response.body().string();

            Log.d("DEBUG", visitsJSONData);

            response.body().close();

            JSONObject jsonObject;

            try
            {
                jsonObject = new JSONObject(visitsJSONData);
            } catch (JSONException e)
            {
                e.printStackTrace();
                return;
            }

            {
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
                    showToastMessage(visitsJSONData);

/*                    SubProductsListAdapter adapter = new SubProductsListAdapter(getActivity(), listSubproducts);

                    ListView listView = (ListView) rootView.findViewById(R.id.listSubproducts);
                    listView.setAdapter(adapter);*/
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
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
