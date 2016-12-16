package ru.alexangan.developer.geatech.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import ru.alexangan.developer.geatech.Adapters.NotSentListVisitsAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Network.RESTdataReceiver.visitItems;

public class NotSentListVisitsFragment extends ListFragment implements View.OnClickListener
{

    private Communicator mCommunicator;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Context context = getActivity();

        ArrayList<VisitItem> visitItemsDateTimeSet = new ArrayList<>();

        for (VisitItem item : visitItems)
        {
            //if(item.getReportStatesModel().getReportCompletionState() == 2) // 2 = completed, 0 = not started
            if(item.getVisitData().getDataOraSopralluogo() != null)
            {
                visitItemsDateTimeSet.add(item);
            }
        }

        NotSentListVisitsAdapter myListAdapter =
                new NotSentListVisitsAdapter(getActivity(), R.layout.not_sent_list_visits_fragment_row, visitItemsDateTimeSet);
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
    public void onClick(View v)
    {
        if (v.getId() == R.id.btnSendReportNow)
        {
            Toast.makeText(getActivity(),"Rapporto inviato", Toast.LENGTH_LONG).show();
            //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }
    }
}

/*            int optionId = randomInteger!=0 ? R.layout.list_visits_cell_datetime_set : R.layout.list_visits_cell_datetime_set;
            View C = inflater.inflate(optionId, parent, false);
            int index = parent.indexOfChild(C);
            parent.addView(C, index);*/