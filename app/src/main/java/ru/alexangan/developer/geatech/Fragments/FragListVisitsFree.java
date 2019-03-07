package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import ru.alexangan.developer.geatech.Adapters.ListVisitsAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.GeaSopralluogo;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.SwipeDetector;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class FragListVisitsFree extends ListFragment
{
    private Communicator mCommunicator;
    SwipeDetector swipeDetector;
    boolean timeNotSetItemsOnly;
    ArrayList<VisitItem> visitItemsFiltered;
    ListVisitsAdapter myListAdapter;
    ListView lv;
    Activity activity;
    //

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

        timeNotSetItemsOnly = false;

        if (getArguments() != null)
        {
            timeNotSetItemsOnly = getArguments().getBoolean("ownVisitsOnly", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.list_visits_with_title, container, false);

        TextView tvTitleListVisits = (TextView) rootView.findViewById(R.id.tvTitleListVisits);
        tvTitleListVisits.setText(R.string.TimeNotSetVisits);

        visitItemsFiltered = new ArrayList<>();

        TreeMap<Long, VisitItem> unsortedVisits = new TreeMap<>();
        long n = 0;

        for (VisitItem visitItem : visitItems)
        //for (int i = 0; i < visitItems.size(); i++)
        {
            GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
            String data_ora_sopralluogo = geaSopralluogo.getData_ora_sopralluogo();
            int id_tecnico = visitItem.getGeaSopralluogo().getId_tecnico();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

            if ((!timeNotSetItemsOnly) || (timeNotSetItemsOnly && id_tecnico == 0))
            {
                try
                {
                    if (data_ora_sopralluogo != null)
                    {
                        Date date = sdf.parse(data_ora_sopralluogo);
                        long time = date.getTime();

                        while (unsortedVisits.get(time) != null)
                        {
                            time++;
                        }
                        unsortedVisits.put(time, visitItem);
                    } else
                    {
                        while (unsortedVisits.get(n) != null)
                        {
                            n++;
                        }
                        unsortedVisits.put(n++, visitItem);
                    }
                } catch (ParseException e)
                {
                    while (unsortedVisits.get(n) != null)
                    {
                        n++;
                    }
                    unsortedVisits.put(n++, visitItem);
                    e.printStackTrace();
                }
            }
        }

        for (Map.Entry entry : unsortedVisits.entrySet()) // add free visits
        {
            VisitItem visitItem = (VisitItem) entry.getValue();
            int id_tecnico = visitItem.getGeaSopralluogo().getId_tecnico();
            boolean ownReport = selectedTech.getId() == id_tecnico;

            if (!ownReport && id_tecnico == 0)
            {
                visitItemsFiltered.add(visitItem);
            }
        }

        myListAdapter = new ListVisitsAdapter(getActivity(), R.layout.list_visits_fragment_row, visitItemsFiltered, null);

        setListAdapter(myListAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        lv = getListView();

        lv.setOnTouchListener(swipeDetector);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                int idVisit = visitItemsFiltered.get(position).getId();
                mCommunicator.OnVisitListItemSelected(idVisit);
            }
        });
    }
}
