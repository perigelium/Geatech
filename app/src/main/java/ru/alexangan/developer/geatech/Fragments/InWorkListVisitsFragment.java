package ru.alexangan.developer.geatech.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.InWorkListVisitsAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.company_id;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.selectedTech;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class InWorkListVisitsFragment extends ListFragment
{
    ArrayList<Integer> visitItemsPositions;
    private Communicator mCommunicator;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
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
        View rootView = inflater.inflate(R.layout.list_visits_fragment, container, false);

        ArrayList<VisitItem> visitItemsDateTimeSet = new ArrayList<>();
        visitItemsPositions = new ArrayList<>();

        realm.beginTransaction();
        RealmResults<ReportStates> reportStatesList = realm.where(ReportStates.class).equalTo("company_id", company_id)
                .equalTo("tech_id", selectedTech.getId()).findAll();
        realm.commitTransaction();

        TreeMap<Long, VisitItem> unsortedVisits = new TreeMap<>();

        for (VisitItem visitItem : visitItems)
        {
            for(ReportStates reportStates : reportStatesList)
            {
                int photoAddedStatus = reportStates.getPhotoAddedNumber();
                int generalInfoCompletionState = reportStates.getGeneralInfoCompletionState();
                int reportCompletionState = reportStates.getReportCompletionState();
                String data_ora_sopralluogo = visitItem.getGeaSopralluogo().getData_ora_sopralluogo();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ITALIAN);

                if (visitItem.getGeaSopralluogo().getId_sopralluogo() == reportStates.getId_sopralluogo()

                        && (photoAddedStatus!=0 || generalInfoCompletionState!=0 || reportCompletionState!=0) // report data not empty
                        && !(photoAddedStatus>=3 && generalInfoCompletionState==2) // data not complete
                        && reportStates.getDataOraRaportoCompletato()==null
                        && reportStates.getData_ora_invio_rapporto()==null
                        )
                {

                    try
                    {
                        Date date = sdf.parse(data_ora_sopralluogo);
                        long time = date.getTime();
                        //Log.d("DEBUG", String.valueOf(time));
                        unsortedVisits.put(time, visitItem);
                    } catch (ParseException e)
                    {
                        e.printStackTrace();
                    }

                    //visitItemsDateTimeSet.add(visitItem);

                    break;
                }
            }
        }

        for (Map.Entry entry : unsortedVisits.entrySet())
        {
            VisitItem visitItem = (VisitItem)entry.getValue();
            visitItemsDateTimeSet.add(visitItem);

            visitItemsPositions.add(visitItem.getId());
        }


        InWorkListVisitsAdapter myListAdapter =
                new InWorkListVisitsAdapter(getActivity(), R.layout.in_work_list_visits_fragment_row, visitItemsDateTimeSet);
        setListAdapter(myListAdapter);

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        mCommunicator.OnInWorkListItemSelected(visitItemsPositions.get(position));
    }
}

/*            int optionId = randomInteger!=0 ? R.layout.list_visits_cell_datetime_set : R.layout.list_visits_cell_datetime_set;
            View C = inflater.inflate(optionId, parent, false);
            int index = parent.indexOfChild(C);
            parent.addView(C, index);*/