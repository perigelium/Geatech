package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.ReportsListAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ReportItem;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.SwipeDetector;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;

public class FragListReportsSent extends ListFragment
{
    private Communicator mCommunicator;
    SwipeDetector swipeDetector;
    ArrayList<ReportItem> reportItemsFilteredSent;
    ReportsListAdapter myListAdapterSent;
    ListView lv;
    Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        mCommunicator = (Communicator) getActivity();
        swipeDetector = new SwipeDetector();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.list_reports_sent, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        reportItemsFilteredSent = new ArrayList<>();

        TreeMap<Long, ReportItem> unsortedReportsSent = new TreeMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<ReportItem> reportItems = realm.where(ReportItem.class).equalTo("company_id", company_id)
                .equalTo("tech_id", selectedTech.getId())
                .findAll();
        realm.commitTransaction();

        for (ReportItem reportItem : reportItems)
        {
            String data_ora_sopralluogo = reportItem.getGeaSopralluogo().getData_ora_sopralluogo();

            boolean isReportSent;

                isReportSent = reportItem.getGea_rapporto_sopralluogo().getData_ora_invio_rapporto() != null;

                if (isReportSent)
                {
                    try
                    {
                        Date date = sdf.parse(data_ora_sopralluogo);
                        long time = date.getTime();

                        while (unsortedReportsSent.get(time) != null) // item with the same time already exists
                        {
                            time++;
                        }
                            unsortedReportsSent.put(time, reportItem);

                    } catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
                }

        }

        for (Map.Entry entry : unsortedReportsSent.entrySet())
        {
            ReportItem reportItem = (ReportItem) entry.getValue();
            {
                reportItemsFilteredSent.add(reportItem);
            }
        }

        myListAdapterSent = new ReportsListAdapter(getActivity(), R.layout.list_visits_fragment_row, reportItemsFilteredSent);
        setListAdapter(myListAdapterSent);

        if(reportItemsFilteredSent.size() == 0)
        {
            showToastMessage(getString(R.string.UseSearchButton));
        }

        lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                int id_sopralluogo = reportItemsFilteredSent.get(position).getId_sopralluogo();
                mCommunicator.showDetailedReport(id_sopralluogo);
            }
        });
        realm.close();
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
}
