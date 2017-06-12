package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.MyListVisitsAdapter;
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
    private Realm realm;

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
        swipeDetector = new SwipeDetector();

/*        if (getArguments() != null)
        {
            id_rapporto_sopralluogo = getArguments().getInt("id_rapporto_sopralluogo", 0);
        }*/

        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.list_reports_sent, container, false);

        //tvListVisitsTodayDate = (TextView) rootView.findViewById(R.id.tvListVisitsTodayDate);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        reportItemsFilteredSent = new ArrayList<>();

        TreeMap<Long, ReportItem> unsortedReportsSent = new TreeMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

        realm.beginTransaction();
        RealmResults<ReportItem> reportItems = realm.where(ReportItem.class).equalTo("company_id", company_id)
                .equalTo("tech_id", selectedTech.getId())
                .findAll();
        realm.commitTransaction();

        for (ReportItem reportItem : reportItems)
        //for (int i = 0; i < visitItems.size(); i++)
        {
/*            GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
            GeaRapporto geaRapporto = visitItem.getGeaRapporto();
            String data_ora_sopralluogo = geaSopralluogo.getData_ora_sopralluogo();
            int idSopralluogo = geaSopralluogo.getId_sopralluogo();
            int id_rapporto_sopralluogo = visitItem.getGeaRapporto().getId_rapporto_sopralluogo();
            int completion_percent = geaRapporto.getCompletion_percent();*/

            String data_ora_sopralluogo = reportItem.getGeaSopralluogo().getData_ora_sopralluogo();

            boolean isReportSent;

            //if (reportItem != null)
            {
/*                int generalInfoCompletionState = reportItem.getReportStates().getGeneralInfoCompletionState();
                int reportCompletionState = reportItem.getReportStates().getReportCompletionState();
                int photosAddedNumber = reportItem.getReportStates().getPhotosAddedNumber();*/

                isReportSent = reportItem.getGea_rapporto_sopralluogo().getData_ora_invio_rapporto() != null;

/*                int id_tecnico = visitItem.getGeaSopralluogo().getId_tecnico();
                boolean ownVisit = selectedTech.getId() == id_tecnico;*/

                if (isReportSent)
                {
                    try
                    {
                        Date date = sdf.parse(data_ora_sopralluogo);
                        long time = date.getTime();
                        //Log.d("DEBUG", String.valueOf(time));

                        while (unsortedReportsSent.get(time) != null) // item with the same time already exists
                        {
                            time++;
                        }

                        if (true)
                        {
                            unsortedReportsSent.put(time, reportItem);
                        }

                    } catch (ParseException e)
                    {
/*                    while(unsortedVisits.get(n) != null)
                    {
                        n++;
                    }
                    unsortedVisits.put(n++, visitItem);*/
                        e.printStackTrace();
                    }
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

        lv = getListView();

        //ViewUtils.setListViewHeightBasedOnChildren(lv);

        lv.setOnTouchListener(swipeDetector);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                //int idVisit = reportItemsFilteredSent.get(position).getId();
                int id_tecnico = reportItemsFilteredSent.get(position).getGeaSopralluogo().getId_tecnico();
                int id_rapporto_sopralluogo = reportItemsFilteredSent.get(position).getId_rapporto_sopralluogo();

                boolean ownVisit = selectedTech.getId() == id_tecnico;

                if (ownVisit)
                {
                    if (swipeDetector.swipeDetected())
                    {
                        mCommunicator.onSendReportReturned(id_rapporto_sopralluogo);

                    } else
                    {
                        mCommunicator.onSendReportReturned(id_rapporto_sopralluogo);
                    }
                }
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