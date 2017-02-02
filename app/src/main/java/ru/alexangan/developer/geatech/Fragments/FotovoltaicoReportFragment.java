package ru.alexangan.developer.geatech.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmResults;
import ru.alexangan.developer.geatech.Models.CaldaiaReportModel;
import ru.alexangan.developer.geatech.Models.ReportStates;
import ru.alexangan.developer.geatech.Models.VisitItem;
import ru.alexangan.developer.geatech.Models.VisitStates;
import ru.alexangan.developer.geatech.R;

import static ru.alexangan.developer.geatech.Activities.LoginActivity.realm;
import static ru.alexangan.developer.geatech.Activities.MainActivity.visitItems;

public class FotovoltaicoReportFragment extends Fragment
{
    View rootView;
    CaldaiaReportModel caldaiaReportModel;
    private int selectedIndex;
    int idSopralluogo;
    ReportStates reportStates;
    Context context;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FotovoltaicoReportFragment()
    {
        // Required empty public constructor
    }

/*    // TODO: Rename and change types and number of parameters
    public static TermodinamicoReportFragment newInstance(String param1, String param2)
    {
        CaldaiaReportFragment fragment = new CaldaiaReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = getActivity();

        if (getArguments() != null)
        {
            selectedIndex = getArguments().getInt("selectedIndex");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView =  inflater.inflate(R.layout.fotovoltaico_report, container, false);

        realm.beginTransaction();
        VisitItem visitItem = visitItems.get(selectedIndex);
        VisitStates visitStates = visitItem.getVisitStates();
        idSopralluogo = visitStates.getIdSopralluogo();

        reportStates = realm.where(ReportStates.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        //caldaiaReportModel = realm.where(CaldaiaReportModel.class).equalTo("idSopralluogo", idSopralluogo).findFirst();
        //RealmResults<CaldaiaReportModel> caldaieModels = realm.where(CaldaiaReportModel.class).findAll();

        if (reportStates != null)
        {
/*            if (caldaiaReportModel == null)
            {
                caldaiaReportModel = new CaldaiaReportModel(caldaieModels.size());
                caldaiaReportModel.setIdSopralluogo(idSopralluogo);
                realm.copyToRealmOrUpdate(caldaiaReportModel);
            }*/
        }
        realm.commitTransaction();

        return rootView;
    }
}
