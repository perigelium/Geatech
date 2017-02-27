package ru.alexangan.developer.geatech.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.NotSentListVisitsAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class NotSentListVisitsFragment extends ListFragment// implements View.OnClickListener
{

    private Communicator mCommunicator;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Context context = getActivity();
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
        View rootView =  inflater.inflate(R.layout.list_visits_fragment, container, false);

        ArrayList<VisitItem> visitItemsDateTimeSet = new ArrayList<>();

        realm.beginTransaction();
        RealmResults<ReportStates> reportStatesList = realm.where(ReportStates.class).equalTo("company_id", company_id)
                .equalTo("tech_id", selectedTech.getId()).findAll();
        realm.commitTransaction();

        for (VisitItem visitItem : visitItems)
        {
            for(ReportStates reportStates : reportStatesList)
            {
                if (visitItem.getGeaSopralluogo().getId_sopralluogo() == reportStates.getId_sopralluogo()
                        && (reportStates.getGeneralInfoCompletionState()==2 && reportStates.getReportCompletionState() ==3 )
                        && reportStates.getPhotoAddedNumber() >= 2 && reportStates.getData_ora_invio_rapporto()==null)
                {
                    visitItemsDateTimeSet.add(visitItem);
                    break;
                }
            }
        }

        NotSentListVisitsAdapter myListAdapter =
                new NotSentListVisitsAdapter(getActivity(), R.layout.not_sent_list_visits_fragment_row, visitItemsDateTimeSet);
        setListAdapter(myListAdapter);

        return  rootView;
    }

/*    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btnSendReportNow)
        {

            Toast.makeText(getActivity(),"Rapporto inviato", Toast.LENGTH_LONG).show();
            //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
    }*/
}

/*            int optionId = randomInteger!=0 ? R.layout.list_visits_cell_datetime_set : R.layout.list_visits_cell_datetime_set;
            View C = inflater.inflate(optionId, parent, false);
            int index = parent.indexOfChild(C);
            parent.addView(C, index);*/