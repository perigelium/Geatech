package ru.alexangan.developer.geatech.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.ReportsListAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Activities.LoginActivity.realm;
import static ru.alexangan.developer.geatech.Activities.MainActivity.visitItems;

public class ReportsListFragment extends ListFragment
{
    private Communicator mCommunicator;
    ArrayList<Integer> visitItemsPositions;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Context context = getActivity();

        ArrayList<VisitItem> visitItemsReportSent = new ArrayList<>();
        visitItemsPositions = new ArrayList<>();

        realm.beginTransaction();

        RealmResults<ReportStates> reportStatesList = realm.where(ReportStates.class).findAll();

        realm.commitTransaction();


        for (VisitItem visitItem : visitItems)
        {
            for(ReportStates reportStates : reportStatesList)
            {
                if (visitItem.getVisitStates().getIdSopralluogo() == reportStates.getIdSopralluogo()
                        && reportStates.getDataOraRaportoInviato()!=null)
                {
                    visitItemsReportSent.add(visitItem);
                    visitItemsPositions.add(visitItem.getId());
                    break;
                }
            }
        }

        ReportsListAdapter myListAdapter =
                new ReportsListAdapter(getActivity(), R.layout.reports_list_fragment_row, visitItemsReportSent);
        setListAdapter(myListAdapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mCommunicator = (Communicator)getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.list_visits_fragment, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        mCommunicator.OnListItemSelected(visitItemsPositions.get(position));
    }
}

/*            int optionId = randomInteger!=0 ? R.layout.list_visits_cell_datetime_set : R.layout.list_visits_cell_datetime_set;
            View C = inflater.inflate(optionId, parent, false);
            int index = parent.indexOfChild(C);
            parent.addView(C, index);*/