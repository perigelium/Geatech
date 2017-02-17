package ru.alexangan.developer.geatech.Fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Adapters.MyListVisitsAdapter;
import ru.alexangan.developer.geatech.Interfaces.Communicator;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.R;
import ru.alexangan.developer.geatech.Utils.SwipeDetector;

import static ru.alexangan.developer.geatech.Models.GlobalConstants.realm;
import static ru.alexangan.developer.geatech.Models.GlobalConstants.visitItems;

public class ListVisitsFragment extends ListFragment
{
    private Communicator mCommunicator;
    SwipeDetector swipeDetector;
    boolean visitTimeNotSetOnly;
    ArrayList<VisitItem> visitItemsFiltered;
    MyListVisitsAdapter myListAdapter;
    ListView lv;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        Context context = getActivity();

/*        Collections.sort(visitItemsFiltered, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                VisitItem p1 = (VisitItem) o1;
                VisitItem p2 = (VisitItem) o2;
                // ## Ascending order
                return p1.getGeaSopralluogo().getData_ora_sopralluogo().compareToIgnoreCase(p2.getGeaSopralluogo().getData_ora_sopralluogo()); // To compare string values
                // return Integer.valueOf(emp1.getId()).compareTo(emp2.getId()); // To compare integer values

                // ## Descending order
                // return emp2.getFirstName().compareToIgnoreCase(emp1.getFirstName()); // To compare string values
                // return Integer.valueOf(emp2.getId()).compareTo(emp1.getId()); // To compare integer values
            }
        });*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mCommunicator = (Communicator)getActivity();
        swipeDetector = new SwipeDetector();
        visitTimeNotSetOnly = false;

        if (getArguments() != null)
        {
            visitTimeNotSetOnly = getArguments().getBoolean("visitTimeNotSetOnly");
        }

        visitItemsFiltered = new ArrayList<>();

        realm.beginTransaction();
        RealmResults<ReportStates> reportStatesList = realm.where(ReportStates.class).findAll();
        realm.commitTransaction();


        for (VisitItem visitItem : visitItems)
        {
            for(ReportStates reportStates : reportStatesList)
            {
                if (visitItem.getGeaSopralluogo().getId_sopralluogo() == reportStates.getId_sopralluogo())
                {
                    if (!visitTimeNotSetOnly || (visitTimeNotSetOnly && reportStates.getData_ora_sopralluogo() == null))
                    {
                        visitItemsFiltered.add(visitItem);
                        break;
                    }
                }
            }
        }

        myListAdapter = new MyListVisitsAdapter(getActivity(), R.layout.list_visits_fragment_row, visitItemsFiltered);
        setListAdapter(myListAdapter);
    }

/*    public void updateView(boolean filterTimeSetItems)
    {
        realm.beginTransaction();
        RealmResults<ReportStates> reportStatesList = realm.where(ReportStates.class).findAll();
        realm.commitTransaction();

        //visitItemsFiltered.clear();

        for (VisitItem visitItem : visitItems)
        {
            for(ReportStates reportStates : reportStatesList)
            {
                if (visitItem.getGeaSopralluogo().getId_sopralluogo() == reportStates.getId_sopralluogo())
                {
                    if (filterTimeSetItems && reportStates.getData_ora_sopralluogo() != null)
                    {
                        visitItemsFiltered.remove(visitItem);
                        myListAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }

        //setListAdapter(myListAdapter);

        //onResume();
    }*/

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

        lv = getListView();
        swipeDetector = new SwipeDetector();
        lv.setOnTouchListener(swipeDetector);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int idSopralluogo = visitItemsFiltered.get(position).getGeaSopralluogo().getId_sopralluogo();
                int idVisit = visitItemsFiltered.get(position).getId();
                realm.beginTransaction();
                ReportStates reportStates = realm.where(ReportStates.class)
                        .equalTo("id_sopralluogo", idSopralluogo).findFirst();
                realm.commitTransaction();

                if (swipeDetector.swipeDetected())
                {
                    if(swipeDetector.getAction() == SwipeDetector.Action.LR)
                    {
                        mCommunicator.OnListItemSwiped(idVisit, reportStates.getData_ora_sopralluogo() != null);
                    }

                    if(swipeDetector.getAction() == SwipeDetector.Action.RL)
                    {
                        mCommunicator.OnListItemSwiped(idVisit, false);
                    }
                } else
                {
                    mCommunicator.OnListItemSelected(idVisit, reportStates.getData_ora_sopralluogo() != null);
                }
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id)
            {
                int idSopralluogo = visitItemsFiltered.get(position).getGeaSopralluogo().getId_sopralluogo();
                int idVisit = visitItemsFiltered.get(position).getId();
                realm.beginTransaction();
                ReportStates reportStates = realm.where(ReportStates.class)
                        .equalTo("id_sopralluogo", idSopralluogo).findFirst();
                realm.commitTransaction();

                if (swipeDetector.swipeDetected())
                {
                    // do the onSwipe action
                } else
                {
                    if(reportStates.getData_ora_sopralluogo() != null)
                    {
                        mCommunicator.OnListItemSelected(idVisit, false);
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