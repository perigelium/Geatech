package ru.alexangan.developer.geatech.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.InWorkListVisitsAdapter;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class InWorkListVisitsFragment extends ListFragment
{
    //GeneralInfoReceiver generalInfoReceiver;
    //private Communicator mCommunicator;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        Context context = getActivity();

        ArrayList<VisitItem> visitItemsDateTimeSet = new ArrayList<>();

        realm.beginTransaction();
        RealmResults<ReportStates> reportStatesList = realm.where(ReportStates.class).findAll();
        realm.commitTransaction();

        for (VisitItem visitItem : visitItems)
        {
            for(ReportStates reportStates : reportStatesList)
            {
                int photoAddedStatus = reportStates.getPhotoAddedNumber();
                int generalInfoCompletionState = reportStates.getGeneralInfoCompletionState();
                int reportCompletionState = reportStates.getReportCompletionState();

                if (visitItem.getVisitStates().getIdSopralluogo() == reportStates.getIdSopralluogo()

                        && (photoAddedStatus!=0 || generalInfoCompletionState!=0 || reportCompletionState!=0) // report data not empty
                        && !(photoAddedStatus!=0 && generalInfoCompletionState==2 && reportCompletionState==3) // report not complete
                        )
                {
                    visitItemsDateTimeSet.add(visitItem);
                    break;
                }
            }
        }

        InWorkListVisitsAdapter myListAdapter =
                new InWorkListVisitsAdapter(getActivity(), R.layout.in_work_list_visits_fragment_row, visitItemsDateTimeSet);
        setListAdapter(myListAdapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.list_visits_fragment, container, false);
    }
}

/*            int optionId = randomInteger!=0 ? R.layout.list_visits_cell_datetime_set : R.layout.list_visits_cell_datetime_set;
            View C = inflater.inflate(optionId, parent, false);
            int index = parent.indexOfChild(C);
            parent.addView(C, index);*/