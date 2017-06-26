package ru.alexangan.developer.geatech.Fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
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

import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class FragListVisitsReminded extends ListFragment
{
    private Communicator mCommunicator;
    SwipeDetector swipeDetector;
    ArrayList<VisitItem> visitItemsFiltered;
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

        //realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.list_visits_with_title, container, false);

        TextView  tvTitleListVisits = (TextView) rootView.findViewById(R.id.tvTitleListVisits);
        tvTitleListVisits.setText(R.string.RemindedVisits);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        visitItemsFiltered = new ArrayList<>();
        TreeMap<Long, VisitItem> unsortedVisits = new TreeMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

        for (int i = 0; i < visitItems.size(); i++)
        {
            VisitItem visitItem = visitItems.get(i);
            GeaSopralluogo geaSopralluogo = visitItem.getGeaSopralluogo();
            String data_ora_sopralluogo = geaSopralluogo.getData_ora_sopralluogo();
            String data_sollecito_appuntamento = geaSopralluogo.getData_sollecito_appuntamento();

            boolean remindedVisit = data_ora_sopralluogo == null && data_sollecito_appuntamento != null;

            if (data_sollecito_appuntamento != null)
            {
                try
                {
                    Date date = sdf.parse(data_sollecito_appuntamento);
                    long time = date.getTime();
                    //Log.d("DEBUG", String.valueOf(time));

                    while (unsortedVisits.get(time) != null) // item with the same time already exists
                    {
                        time++;
                    }

                    if (remindedVisit)
                    {
                        unsortedVisits.put(time, visitItem);
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

        for (Map.Entry entry : unsortedVisits.entrySet())
        {
            VisitItem visitItem = (VisitItem) entry.getValue();
            {
                visitItemsFiltered.add(visitItem);
            }
        }

        ListAdapter myListAdapter = new ListVisitsAdapter(getActivity(), R.layout.list_visits_fragment_row, visitItemsFiltered, null);

        setListAdapter(myListAdapter);

        lv = getListView();

        //ViewUtils.setListViewHeightBasedOnChildren(lv);

        lv.setOnTouchListener(swipeDetector);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int idVisit = visitItemsFiltered.get(position).getId();

                mCommunicator.OnVisitListItemSelected(idVisit);

/*                if (swipeDetector.swipeDetected())
                {
                    if (swipeDetector.getAction() == SwipeDetector.Action.LR)
                    {
                        mCommunicator.OnVisitListItemSwiped(idVisit, true);
                    } else if (swipeDetector.getAction() == SwipeDetector.Action.RL)
                    {
                        mCommunicator.OnVisitListItemSwiped(idVisit, false);
                    }
                } else
                {
                    mCommunicator.OnVisitListItemSelected(idVisit, false);
                }*/

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