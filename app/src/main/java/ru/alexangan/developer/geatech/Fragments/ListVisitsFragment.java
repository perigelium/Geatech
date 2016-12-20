package ru.alexangan.developer.geatech.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ru.alexangan.developer.geatech.Activities.MainActivity;
import ru.alexangan.developer.geatech.Adapters.MyListVisitsAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Network.RESTdataReceiver;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.SwipeDetector;

import static ru.alexangan.developer.geatech.Activities.LoginActivity.realm;

public class ListVisitsFragment extends ListFragment
{
    private Communicator mCommunicator;
    SwipeDetector swipeDetector;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MyListVisitsAdapter myListAdapter =
                new MyListVisitsAdapter(getActivity(), R.layout.list_visits_fragment_row);
        setListAdapter(myListAdapter);

        Context context = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mCommunicator = (Communicator)getActivity();
        swipeDetector = new SwipeDetector();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView =  inflater.inflate(R.layout.list_visits_fragment, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        final ListView lv = getListView();
        final SwipeDetector swipeDetector = new SwipeDetector();
        lv.setOnTouchListener(swipeDetector);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int idSopralluogo = MainActivity.visitItems.get(position).getVisitStates().getIdSopralluogo();
                realm.beginTransaction();
                ReportStates reportStates = realm.where(ReportStates.class)
                        .equalTo("idSopralluogo", idSopralluogo).findFirst();
                realm.commitTransaction();

                if (swipeDetector.swipeDetected())
                {
                    if(swipeDetector.getAction() == SwipeDetector.Action.LR)
                    {
                        mCommunicator.OnListItemSwiped(position, reportStates.getDataOraSopralluogo() != null);
                    }
                } else
                {
                    mCommunicator.OnListItemSelected(position, reportStates.getDataOraSopralluogo() != null);
                }
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id)
            {
                int idSopralluogo = MainActivity.visitItems.get(position).getVisitStates().getIdSopralluogo();
                realm.beginTransaction();
                ReportStates reportStates = realm.where(ReportStates.class)
                        .equalTo("idSopralluogo", idSopralluogo).findFirst();
                realm.commitTransaction();

                if (swipeDetector.swipeDetected())
                {
                    // do the onSwipe action
                } else
                {
                    if(reportStates.getDataOraSopralluogo() != null)
                    {
                        mCommunicator.OnListItemSelected(position, false);
                    }
                }

                return false;
            }
        });
    }
}

/*            int optionId = randomInteger!=0 ? R.layout.list_visits_cell_datetime_set : R.layout.list_visits_cell_datetime_set;
            View C = inflater.inflate(optionId, parent, false);
            int index = parent.indexOfChild(C);
            parent.addView(C, index);*/