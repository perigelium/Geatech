package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Locale;

import ru.alexangan.developer.geatech.Adapters.ReportsSearchResultsListAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ReportsSearchResultItem;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.SwipeDetector;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.mSettings;

public class FragReportsSearchResults extends ListFragment
{
    private Communicator mCommunicator;
    SwipeDetector swipeDetector;
    List<ReportsSearchResultItem> reportsSearchResultItems;
    ReportsSearchResultsListAdapter myReportsSearchResultsAdapter;
    ListView lv;
    Activity activity;
    String strReportsSearchResultsJSON;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mCommunicator = (Communicator) getActivity();

        if (getArguments() != null)
        {
            strReportsSearchResultsJSON = getArguments().getString("reportsSearchResultsJSON");
        }

        if(strReportsSearchResultsJSON != null)
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
        mSettings.edit().putString("reportSearchLastQueryString", null).apply();
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

        myReportsSearchResultsAdapter =
                new ReportsSearchResultsListAdapter(getActivity(), R.layout.list_search_results_repors_row, reportsSearchResultItems);
        setListAdapter(myReportsSearchResultsAdapter);

        lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                int id_rapporto_sopralluogo = reportsSearchResultItems.get(position).getId_rapporto_sopralluogo();
                mCommunicator.onSendReportReturned(id_rapporto_sopralluogo);
            }
        });
    }

/*    private void showToastMessage(final String msg)
    {
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }
        });
    }*/
}

/*            int optionId = randomInteger!=0 ? R.layout.list_visits_cell_datetime_set : R.layout.list_visits_cell_datetime_set;
            View C = inflater.inflate(optionId, parent, false);
            int index = parent.indexOfChild(C);
            parent.addView(C, index);*/